package org.Epixcrafted.EpixServer.engine.io;

import java.io.IOException;

import org.Epixcrafted.EpixServer.mc.world.Chunk;
import org.Epixcrafted.EpixServer.mc.world.World;

public interface ChunkIoService {

	public Chunk read(World world, int x, int z) throws IOException;

	public void write(Chunk chunk) throws IOException;

}
