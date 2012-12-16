package org.Epixcrafted.EpixServer.chat.commands;

import org.Epixcrafted.EpixServer.EpixServer;
import org.Epixcrafted.EpixServer.chat.CommandSender;

public class CommandList implements Command {

	@Override
	public String getCommandName() {
		return "list";
	}
	
	@Override
	public String[] getAliases() {
		return new String[] {"l"};
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		sender.sendMessage("There are " + EpixServer.onlinePlayers + "/" + EpixServer.maxPlayers + " players online:"); //TODO use non-static field
		return true;
	}

}
