package org.Epixcrafted.EpixServer.mc.world;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.Epixcrafted.EpixServer.mc.Location;
import org.Epixcrafted.EpixServer.mc.entity.Entity;
import org.Epixcrafted.EpixServer.mc.material.block.Block;

public class World {
	
	private String name;
	
	private List<Entity> entityList;
	protected Set<Chunk> activeChunkSet;

	private int time;
	
	public World(String name) {
		this.name = name;
		entityList = Collections.emptyList();
		activeChunkSet = Collections.emptySet();
	}
	
	public String getWorldName() {
		return name;
	}
	
	public Location getSpawnLocation() {
		return new Location(0, 10, 0);
	}
	
	public void setSpawnLocation(Location location) {
		//TODO
	}
	
	public Chunk getChunkAt(int x, int z) {
		x = x >> 4; 
		z = z >> 4;
		for (Chunk chunk : activeChunkSet) {
			if (chunk.x == x && chunk.z == z) return chunk; 
		}
		return null;
	}
	
	public Block getBlockAt(int x, int y, int z) {
		//return getChunkAt(x, z).getBlockId(x, y, z);
		return null; //TODO
	}
	
	public int getBlockIdAt(int x, int y, int z) {
		return getChunkAt(x, z).getBlockId(x, y, z);
	}
	
	public void setBlockAt(int x, int y, int z, int blockId) {
		if (!isChunkLoaded(x,z)) return;
		getChunkAt(x, z).setBlockAt(x, y, z, blockId);
	}
	
	public boolean isChunkLoaded(int x, int z) {
		return getChunkAt(x, z) != null ? true : false;
	}
	
	public Chunk[] getLoadedChunks() {
		return (Chunk[]) activeChunkSet.toArray();
	}
	
	public void loadChunk(int x, int y) {
		//TODO
	}
	
	public void unloadChunk(int x, int y) {
		//TODO
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time > 24000 ? 24000 : time;
	}
	
	public Biome getBiome(int x, int z) {
		return null;//TODO
	}
	
	public void setBiome(int x, int y, Biome biome) {
		//TODO
	}
	
	public List<Entity> getEntities() {
		return this.entityList;
	}

	public void save() {
		//TODO
	}
	
	public long getSeed() {
		return 0; //TODO
	}

	public void update() {
		setTime(getTime() + 1);
	}
}
