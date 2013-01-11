package org.Epixcrafted.EpixServer.mc.material.block;

import java.util.ArrayList;

public class BlockList {
	
	private static ArrayList<Block> list;
	
	private static void add(EpixBlock block) {
		list.set(block.getId(), block);
	}
	
	public static Block get(int id) {
		return list.get(id);
	}
	
	static {
		add(new BlockStone(1));
	}
	
	private BlockList() {
		//cannot initialize
	}
}
