package org.Epixcrafted.EpixServer.engine;

import org.Epixcrafted.EpixServer.Main;
import org.Epixcrafted.EpixServer.chat.Colour;
import org.Epixcrafted.EpixServer.engine.player.Player;
import org.Epixcrafted.EpixServer.engine.player.Session;
import org.Epixcrafted.EpixServer.mc.entity.metadata.EntityMetadata;
import org.Epixcrafted.EpixServer.protocol.Packet20NamedEntity;
import org.Epixcrafted.EpixServer.protocol.Packet29DestroyEntity;
import org.Epixcrafted.EpixServer.protocol.Packet31Move;
import org.Epixcrafted.EpixServer.protocol.Packet3Chat;

public class PlayerActionLogger {

	/**
	 * Should be fired after receiving Packet2Handshake
	 * @param session
	 */
	public static void playerPreLogin(Session session) {
		Main.getServer().getLogger().info(session.getPlayer().getName() + " ["+ session.getAddress().getAddress().getHostAddress() + ":" + session.getAddress().getPort() + "] is trying to login...");
	}
	
	/**
	 * Should be fired after receiving Packet204Settings and after sending map chunks
	 * @param session
	 */
	public static void playerLogin(Session session) {
		Main.getServer().getLogger().info(session.getPlayer().getName() + " logged in. EID: " + session.getPlayer().getEntityId() + "; Location: x=" +session.getPlayer().getX() + ", y=" + session.getPlayer().getY() + ", z=" + session.getPlayer().getZ() + ";");
		for (Session s : Main.getServer().getSessionList()) {
			s.send(new Packet3Chat(Colour.YELLOW + session.getPlayer().getName() + " joined the game."));
			if (s != session) s.send(new Packet20NamedEntity(session.getPlayer().getEntityId(), session.getPlayer().getName(), (int)session.getPlayer().getX(), (int)session.getPlayer().getY(), (int)session.getPlayer().getZ(), (byte)0, (byte)0, (short)0, new EntityMetadata()));
		}
	}
	
	/**
	 * Should be fired after receiving Packet11Pos, Packet12Look, Packet13PosLook
	 * @param session, old player, isPacket11Pos, isPacket13
	 */
	public static void playerMove(Session session, Player old, boolean isPacket11, boolean isAll) {
		if (isAll) {
			byte x = (byte) (-((int)old.getX() - (int)session.getPlayer().getX()) * 32);
			byte y = (byte) (-((int)old.getY() - (int)session.getPlayer().getY()) * 32);
			byte z = (byte) (-((int)old.getZ() - (int)session.getPlayer().getZ()) * 32);
			for (Session s : Main.getServer().getSessionList()) {
				if (s != session) s.send(new Packet31Move(session.getPlayer().getEntityId(), x, y, z));
			}
		} else if (isPacket11) {
			byte x = (byte) (-((int)old.getX() - (int)session.getPlayer().getX()) * 32);
			byte y = (byte) (-((int)old.getY() - (int)session.getPlayer().getY()) * 32);
			byte z = (byte) (-((int)old.getZ() - (int)session.getPlayer().getZ()) * 32);
			for (Session s : Main.getServer().getSessionList()) {
				if (s != session) s.send(new Packet31Move(session.getPlayer().getEntityId(), x, y, z));
			}
		} else {
			//not currently supported
		}
	}
	
	/**
	 * Should be fired after receiving Packet3Chat
	 * @param session
	 * @param message
	 */
	public static void playerChat(Session session, String message) {
		Main.getServer().getLogger().info("<" + session.getPlayer().getName() + "> " + message);
		String formattedMessage = "<" + session.getPlayer().getName() + "> " + message;
		for (Session s : Main.getServer().getSessionList()) {
				s.send(new Packet3Chat(formattedMessage));
		}
	}
	
	/**
	 * Should be fired after receiving Packet3Chat if message starts with "/"
	 * @param session
	 * @param command
	 */
	public static void playerCommand(Session session, String command) {
		Main.getServer().getLogger().info(session.getPlayer().getName() + " issued server command: " + command);
	}
	
	/**
	 * Should be fired after receiving Packet255Disconnect
	 * @param player
	 */
	public static void playerDisconnect(Session session) {
		Main.getServer().getLogger().info(session.getPlayer().getName() + " lost connection. (safe quit)");
		for (Session s : Main.getServer().getSessionList()) {
				if (!s.getPlayer().equals(session.getPlayer())) s.send(new Packet29DestroyEntity((byte)1, new int[] { s.getPlayer().getEntityId() }));
				if (!s.getPlayer().equals(session.getPlayer())) s.send(new Packet3Chat(Colour.YELLOW + session.getPlayer().getName() + " left the game."));
		}
	}
	
	/**
	 * Should be fired on special occasions, such as receiving invalid packets or kicking player
	 * @param player
	 */
	public static void playerKick(Session session) {
		Main.getServer().getLogger().info(session.getPlayer().getName() + " lost connection. (kick)");
		for (Session s : Main.getServer().getSessionList()) {
			if (!s.getPlayer().equals(session.getPlayer())) s.send(new Packet29DestroyEntity((byte)1, new int[] { s.getPlayer().getEntityId() }));
			if (!s.getPlayer().equals(session.getPlayer())) s.send(new Packet3Chat(Colour.YELLOW + session.getPlayer().getName() + " was kicked from the game."));
		}
	}
}
