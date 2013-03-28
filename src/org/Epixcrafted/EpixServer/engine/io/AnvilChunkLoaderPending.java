package org.Epixcrafted.EpixServer.engine.io;

import org.Epixcrafted.EpixServer.tools.nbt.NBTTagCompound;

public class AnvilChunkLoaderPending {
	
	public final ChunkCoordIntPair chunkCoordinate;
	public final NBTTagCompound nbtTags;

	public AnvilChunkLoaderPending(ChunkCoordIntPair par1ChunkCoordIntPair,
			NBTTagCompound par2NBTTagCompound) {
		this.chunkCoordinate = par1ChunkCoordIntPair;
		this.nbtTags = par2NBTTagCompound;
	}

}
