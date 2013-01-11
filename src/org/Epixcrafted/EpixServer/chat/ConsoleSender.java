package org.Epixcrafted.EpixServer.chat;

import org.Epixcrafted.EpixServer.engine.IServer;

public class ConsoleSender implements CommandSender {
	
	private static ConsoleSender instance;
	
	private IServer server;
	
	public ConsoleSender(IServer server) {
		if (instance != null) {
			throw new RuntimeException();
		}
		instance = this;
		this.server = server;
	}

	@Override
	public String getName() {
		return "CONSOLE";
	}
	
	@Override
	public IServer getServer() {
		return server;
	}

	@Override
	public void sendMessage(String message) {
        server.getLogger().info(cutColourCodes(message));
	}
	
	public String cutColourCodes(String string) {
		for (Colour c : Colour.values()) {
			string = string.replace(c.toString(), "");
		}
		return string;
	}

}
