package org.Epixcrafted.EpixServer.engine.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class RegionFileCache {
	
	private final int MAX_CACHE_SIZE = 256;

	private final Map<File, Reference<RegionFile>> cache = new HashMap<File, Reference<RegionFile>>();

	public RegionFile getRegionFile(File basePath, int chunkX, int chunkZ) throws IOException {
		File regionDir = new File(basePath, "region");
		File file = new File(regionDir, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");

		Reference<RegionFile> ref = cache.get(file);

		if (ref != null && ref.get() != null) {
			return ref.get();
		}

		if (!regionDir.exists()) {
			regionDir.mkdirs();
		}

		if (cache.size() >= MAX_CACHE_SIZE) {
			clear();
		}

		RegionFile reg = new RegionFile(file);
		cache.put(file, new SoftReference<RegionFile>(reg));
		return reg;
	}

	public void clear() throws IOException {
		for (Reference<RegionFile> ref : cache.values()) {
			if (ref.get() != null) {
				ref.get().close();
			}
		}
		cache.clear();
	}

	public int getSizeDelta(File basePath, int chunkX, int chunkZ) throws IOException {
		RegionFile r = getRegionFile(basePath, chunkX, chunkZ);
		return r.getSizeDelta();
	}

	public DataInputStream getChunkDataInputStream(File basePath, int chunkX, int chunkZ) throws IOException {
		RegionFile r = getRegionFile(basePath, chunkX, chunkZ);
		return r.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
	}

	public DataOutputStream getChunkDataOutputStream(File basePath, int chunkX, int chunkZ) throws IOException {
		RegionFile r = getRegionFile(basePath, chunkX, chunkZ);
		return r.getChunkDataOutputStream(chunkX & 31, chunkZ & 31);
	}

}
