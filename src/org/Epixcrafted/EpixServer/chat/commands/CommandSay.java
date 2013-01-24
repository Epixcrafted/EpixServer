package org.Epixcrafted.EpixServer.chat.commands;

import org.Epixcrafted.EpixServer.chat.Colour;
import org.Epixcrafted.EpixServer.chat.CommandSender;

public class CommandSay implements Command {

	@Override
	public String getCommandName() {
		return "say";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		String message = "";
		for (String arg : args) {
			message += " " + arg;
		}
		sender.sendMessage(Colour.PINK + "[Server]" + Colour.RESET + message);
		return true;
	}

}
