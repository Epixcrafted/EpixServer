package org.Epixcrafted.EpixServer.engine;

import java.util.logging.Logger;

public interface IServer {

	public void startServer();
	
	public void shutdownServer();
	
	public String getMinecraftVersion();
	
	public int getOnlinePlayerCount();
	
	public String[] getOnlinePlayers();
	
	public int getMaximumPlayers();

	public Logger getLogger();

}
