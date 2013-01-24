package org.Epixcrafted.EpixServer.engine;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.Epixcrafted.EpixServer.EpixServer;
import org.Epixcrafted.EpixServer.tools.MD5;

public class LoginExecutor {

	private EpixServer server;
	public String group = null;
	public String color = null;
	
	public LoginExecutor(EpixServer server) {
		this.server = server;
	}
	
	public boolean check(String login, String password) {
		String pass = MD5.hash(password+server.getMySQLSalt());
		ResultSet result = server.getMySQL().query("SELECT * FROM server_users WHERE login='"+login+"' AND password='"+pass+"'");
		boolean checked = false;
		try {
			if(result.last()) {
				checked = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!checked) {
			return false;
		}
		
		boolean checkedGroup = true;
		ResultSet resultGroup = null;
		try {
			group = result.getString("status");
			resultGroup = server.getMySQL().query("SELECT * FROM server_groups WHERE server_groups.group='"+group+"'");
			if(!resultGroup.last()) {
				checkedGroup = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			checkedGroup = false;
		}
		if(!checkedGroup) {
			return false;
		}
		
		try {
			color = resultGroup.getString("chat");
		} catch (Exception e) {
            e.printStackTrace();
		}
		return true;
	}
}
