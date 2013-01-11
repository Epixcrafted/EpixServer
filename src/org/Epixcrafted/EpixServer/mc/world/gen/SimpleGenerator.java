package org.Epixcrafted.EpixServer.mc.world.gen;

import org.Epixcrafted.EpixServer.mc.world.World;

public class SimpleGenerator implements IGenerator {
	
	private World world;
	
	public SimpleGenerator(World world) {
		this.world = world;
	}

	@Override
	public void generate() {
		for (int x = 0; x < 256; x++) {
			for (int z = 0; z < 256; x++) {
				world.setBlockAt(x, 1, z, 1);
			}
		}
	}
	
	@Override
	public World getWorld() {
		return world;
	}
	
	@Override
	public void setWorld(World world) {
		this.world = world;
	}

}
