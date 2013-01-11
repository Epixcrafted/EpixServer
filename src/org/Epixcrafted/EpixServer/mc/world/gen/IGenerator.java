package org.Epixcrafted.EpixServer.mc.world.gen;

import org.Epixcrafted.EpixServer.mc.world.World;

public interface IGenerator {
	
	public void generate();
	
	public World getWorld();
	
	public void setWorld(World world);

}
