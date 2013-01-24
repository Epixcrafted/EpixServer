package org.Epixcrafted.EpixServer.mc.material.block;

public class BlockList {
	
	private static Block[] blocks = new Block[4096];
	
	private static void add(EpixBlock block) {
		blocks[block.getId()] = block;
	}
	
	public static Block get(int id) {
		return blocks[id];
	}
	
	static {
		add(new BlockStone(1));
	}
	
	private BlockList() {
		//cannot initialize
	}
}
