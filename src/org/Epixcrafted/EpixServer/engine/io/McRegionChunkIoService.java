package org.Epixcrafted.EpixServer.engine.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.Epixcrafted.EpixServer.mc.world.ExtendedBlockStorage;
import org.Epixcrafted.EpixServer.mc.world.Chunk;
import org.Epixcrafted.EpixServer.mc.world.NibbleArray;
import org.Epixcrafted.EpixServer.tools.nbt.NBTTagCompound;
import org.Epixcrafted.EpixServer.tools.nbt.NBTTagList;
import org.Epixcrafted.EpixServer.mc.world.World;

public class McRegionChunkIoService implements ChunkIoService, IThreadedFileIO {

	private File dir;
	private Set<ChunkCoordIntPair> pendingAnvilChunksCoordinates = new HashSet<ChunkCoordIntPair>();
	private List<AnvilChunkLoaderPending> chunksToRemove = new ArrayList<AnvilChunkLoaderPending>();
	private Object syncLockObject = new Object();

	private RegionFileCache cache = new RegionFileCache();

	public McRegionChunkIoService() {
		this(new File("world"));
	}

	public McRegionChunkIoService(File dir) {
		this.dir = dir;
	}

	/**
	 * Loads the specified(XZ) chunk into the specified world.
	 */
	public Chunk read(World world, int x, int z) throws IOException {
		NBTTagCompound var4 = null;
		ChunkCoordIntPair var5 = new ChunkCoordIntPair(x, z);
		synchronized (this.syncLockObject) {
			if (this.pendingAnvilChunksCoordinates.contains(var5)) {
				Iterator<AnvilChunkLoaderPending> var7 = this.chunksToRemove
						.iterator();

				while (var7.hasNext()) {
					AnvilChunkLoaderPending var8 = (AnvilChunkLoaderPending) var7
							.next();

					if (var8.chunkCoordinate.equals(var5)) {
						var4 = var8.nbtTags;
						break;
					}
				}
			}
		}

		if (var4 == null) {
			DataInputStream var11 = cache.getChunkDataInputStream(dir, x, z);

			if (var11 == null) {
				return null;
			}

			var4 = CompressedStreamTools.read(var11);
		}

		return checkedReadChunkFromNBT(world, x, z, var4);
	}

	private Chunk checkedReadChunkFromNBT(World par1World, int par2, int par3,
			NBTTagCompound par4NBTTagCompound) {
		if (!par4NBTTagCompound.hasKey("Level")) {
			System.out.println("Chunk file at " + par2 + "," + par3
					+ " is missing level data, skipping");
			return null;
		} else if (!par4NBTTagCompound.getCompoundTag("Level").hasKey(
				"Sections")) {
			System.out.println("Chunk file at " + par2 + "," + par3
					+ " is missing block data, skipping");
			return null;
		} else {
			Chunk var5 = this.readChunkFromNBT(par1World,
					par4NBTTagCompound.getCompoundTag("Level"));

			if (var5.getX() != par2 || var5.getZ() != par3) {
				System.out.println("Chunk file at " + par2 + "," + par3
						+ " is in the wrong location; relocating. (Expected "
						+ par2 + ", " + par3 + ", got " + var5.getX() + ", "
						+ var5.getZ() + ")");
				par4NBTTagCompound.setInteger("xPos", par2);
				par4NBTTagCompound.setInteger("zPos", par3);
				var5 = this.readChunkFromNBT(par1World,
						par4NBTTagCompound.getCompoundTag("Level"));
			}

			return var5;
		}
	}

	private Chunk readChunkFromNBT(World par1World,
			NBTTagCompound par2NBTTagCompound) {
		int var3 = par2NBTTagCompound.getInteger("xPos");
		int var4 = par2NBTTagCompound.getInteger("zPos");
		Chunk var5 = new Chunk(par1World, var3, var4);
		var5.heightMap = par2NBTTagCompound.getIntArray("HeightMap");
		NBTTagList var6 = par2NBTTagCompound.getTagList("Sections");
		byte var7 = 16;
		ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];

		for (int var9 = 0; var9 < var6.tagCount(); ++var9) {
			NBTTagCompound var10 = (NBTTagCompound) var6.tagAt(var9);
			byte var11 = var10.getByte("Y");
			ExtendedBlockStorage var12 = new ExtendedBlockStorage(var11 << 4);
			var12.setBlockLSBArray(var10.getByteArray("Blocks"));

			if (var10.hasKey("Add")) {
				var12.setBlockMSBArray(new NibbleArray(var10
						.getByteArray("Add"), 4));
			}

			var12.setBlockMetadataArray(new NibbleArray(var10
					.getByteArray("Data"), 4));
			var12.setSkylightArray(new NibbleArray(var10
					.getByteArray("SkyLight"), 4));
			var12.setBlocklightArray(new NibbleArray(var10
					.getByteArray("BlockLight"), 4));
			var8[var11] = var12;
		}

		var5.setBlocks(var8);

		return var5;
	}

	private void writeChunkToNBT(Chunk par1Chunk, World par2World,
			NBTTagCompound par3NBTTagCompound) {
		par3NBTTagCompound.setInteger("xPos", par1Chunk.getX());
		par3NBTTagCompound.setInteger("zPos", par1Chunk.getZ());
		par3NBTTagCompound.setIntArray("HeightMap", par1Chunk.heightMap);
		ExtendedBlockStorage[] var4 = par1Chunk.getBlocks();
		NBTTagList var5 = new NBTTagList("Sections");
		ExtendedBlockStorage[] var6 = var4;
		int var7 = var4.length;
		NBTTagCompound var10;

		for (int var8 = 0; var8 < var7; ++var8) {
			ExtendedBlockStorage var9 = var6[var8];

			if (var9 != null) {
				var10 = new NBTTagCompound();
				var10.setByte("Y", (byte) (var9.getYLocation() >> 4 & 255));
				var10.setByteArray("Blocks", var9.getBlockLSBArray());

				if (var9.getBlockMSBArray() != null) {
					var10.setByteArray("Add", var9.getBlockMSBArray().data);
				}

				var10.setByteArray("Data", var9.getMetadataArray().data);
				var10.setByteArray("SkyLight", var9.getSkylightArray().data);
				var10.setByteArray("BlockLight", var9.getBlocklightArray().data);
				var5.appendTag(var10);
			}
		}

		par3NBTTagCompound.setTag("Sections", var5);
	}

	public void write(Chunk chunk) throws IOException {
		try {
			NBTTagCompound var3 = new NBTTagCompound();
			NBTTagCompound var4 = new NBTTagCompound();
			var3.setTag("Level", var4);
			this.writeChunkToNBT(chunk, chunk.getWorld(), var4);
			this.func_75824_a(chunk.getChunkCoordIntPair(), var3);
		} catch (Exception var5) {
			var5.printStackTrace();
		}
	}

	private void func_75824_a(ChunkCoordIntPair par1ChunkCoordIntPair,
			NBTTagCompound par2NBTTagCompound) {

		synchronized (syncLockObject) {
			if (this.pendingAnvilChunksCoordinates
					.contains(par1ChunkCoordIntPair)) {
				for (int var4 = 0; var4 < this.chunksToRemove.size(); ++var4) {
					if (((AnvilChunkLoaderPending) this.chunksToRemove
							.get(var4)).chunkCoordinate
							.equals(par1ChunkCoordIntPair)) {
						chunksToRemove.set(var4, new AnvilChunkLoaderPending(
								par1ChunkCoordIntPair, par2NBTTagCompound));
						return;
					}
				}
			}

			this.chunksToRemove.add(new AnvilChunkLoaderPending(
					par1ChunkCoordIntPair, par2NBTTagCompound));
			this.pendingAnvilChunksCoordinates.add(par1ChunkCoordIntPair);
			ThreadedFileIOBase.threadedIOInstance.queueIO(this);
		}
	}

	public boolean writeNextIO() {
		AnvilChunkLoaderPending var1 = null;

		synchronized (this.syncLockObject) {
			if (this.chunksToRemove.isEmpty()) {
				return false;
			}

			var1 = (AnvilChunkLoaderPending) this.chunksToRemove.remove(0);
			this.pendingAnvilChunksCoordinates.remove(var1.chunkCoordinate);
		}

		if (var1 != null) {
			try {
				this.writeChunkNBTTags(var1);
			} catch (Exception var4) {
				var4.printStackTrace();
			}
		}

		return true;
	}

	private void writeChunkNBTTags(
			AnvilChunkLoaderPending par1AnvilChunkLoaderPending)
			throws IOException {
		DataOutputStream var2 = cache.getChunkDataOutputStream(dir,
				par1AnvilChunkLoaderPending.chunkCoordinate.chunkXPos,
				par1AnvilChunkLoaderPending.chunkCoordinate.chunkZPos);
		CompressedStreamTools.write(par1AnvilChunkLoaderPending.nbtTags, var2);
		var2.close();
	}
	
}
