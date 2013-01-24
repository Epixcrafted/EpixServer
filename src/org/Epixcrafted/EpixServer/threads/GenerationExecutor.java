package org.Epixcrafted.EpixServer.threads;

import org.Epixcrafted.EpixServer.mc.world.gen.IGenerator;

public class GenerationExecutor extends Thread implements Runnable {
	
	private IGenerator generator;
	
	public GenerationExecutor(IGenerator generator) {
		super("Generation Executor (w=" + generator.getWorld().getWorldName() + ")");
		this.generator = generator;
	}
	
	@Override
	public void run() {
		generator.getWorld().getServer().getLogger().info("Generating world \"" + generator.getWorld().getWorldName() + "\".");
		generator.generate();
		generator.getWorld().getServer().getLogger().info("World \"" + generator.getWorld().getWorldName() + "\" has generated.");
	}

}
