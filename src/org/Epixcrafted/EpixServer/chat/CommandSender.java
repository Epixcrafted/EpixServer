package org.Epixcrafted.EpixServer.chat;

import org.Epixcrafted.EpixServer.engine.IServer;

public interface CommandSender {

	public String getName();
	
	public IServer getServer();
	
	public void sendMessage(String message);
}
