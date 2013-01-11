package org.Epixcrafted.EpixServer.threads;

import org.Epixcrafted.EpixServer.EpixServer;
import org.Epixcrafted.EpixServer.mc.world.World;

public class UpdateWorldExecutor extends Thread implements Runnable {

	private static int threadNum = 0;
	private EpixServer server;

	public UpdateWorldExecutor(EpixServer server) {
		this.server = server;
		this.setName("UpdateWorldExecutor-#" + ++threadNum);
		this.setDaemon(false);
	}
	
	@Override
	public void run() {
		update();
		try {
			finalize();
			interrupt();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		synchronized(server.getWorldList()) {
			for (World world : server.getWorldList()) world.update();
		}
	}
}
