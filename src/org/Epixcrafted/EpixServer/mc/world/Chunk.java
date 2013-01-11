package org.Epixcrafted.EpixServer.mc.world;

import org.Epixcrafted.EpixServer.mc.entity.Entity;

public class Chunk {

	public boolean deferRender; //пока не особо разбрался зачем, но по сути связано с обновлением блоков
	
	public World world;
	public final int x;
	public final int z;

    /**
     * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a
     * logical segment of 16x16x16 blocks, stacked vertically.
     */
	private ExtendedBlockStorage[] storageArrays;

    /**
     * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each column belongs.
     */
	private byte[] blockBiomeArray;
	
	public Chunk(World world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
		this.storageArrays = new ExtendedBlockStorage[16];
		this.deferRender = false;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getBlockId(int x, int y, int z) {
		if (y >> 4 >= storageArrays.length) return 0;
        ExtendedBlockStorage storage = storageArrays[y >> 4];
        return storage != null ? storage.getExtBlockID(x, y & 15, z) : 0;
	}
	
	public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }
	
	public void setBiomeArray(byte[] biome) {
		this.blockBiomeArray = biome;
	}
	
	public Entity[] getEntities() {
		return new Entity[0];
	}
	
	public ExtendedBlockStorage[] getBlockStorageArray()
    {
        return this.storageArrays;
    }
	
	public void setBlockAt(int x, int y, int z, int blockId) {
		int id = getBlockId(x, y, z);
		if (id == blockId) return;
		ExtendedBlockStorage storage = storageArrays[y >> 4];
		if (storage == null) {
			if (blockId == 0) return;
			storage = storageArrays[y >> 4] = new ExtendedBlockStorage(y >> 4 << 4);
		}
		storage.setExtBlockID(x, y & 15, z, blockId);
	}
}
