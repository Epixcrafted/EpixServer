package org.Epixcrafted.EpixServer.threads;

import org.Epixcrafted.EpixServer.EpixServer;

public class UpdateEntityExecutor extends Thread implements Runnable {
	
	private static int threadNum = 0;
	private EpixServer server;

	public UpdateEntityExecutor(EpixServer server) {
		this.server = server;
		this.setName("UpdateEntityExecutor-#" + ++threadNum);
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
		synchronized(server.getSessionListClass()) {
			server.getSessionListClass().update();
		}
	}
}
