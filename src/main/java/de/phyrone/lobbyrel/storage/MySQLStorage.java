package de.phyrone.lobbyrel.storage;

import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.sql.MySQL;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySQLStorage extends OfflinePlayerStorage{
	static MySQL sql = null;
	static String table;
	@Override
	public void init() {
		System.out.println("[Lobby-Rel] MySQL Conecting...");
		sql = new MySQL(
				Config.getString("Storage.MySQL.Host","localhost"), 
				String.valueOf(Config.getInt("Storage.MySQL.Port", 3306)), 
				Config.getString("Storage.MySQL.Database", "Lobby-Rel"), 
				Config.getString("Storage.MySQL.User", "root"), 
				Config.getString("Storage.MySQL.Password","MySecretPassword"));
		try {
			sql.openConnection();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		table = Config.getString("Storage.MySQL.Table","LobbyPlayerData");
		try {
            sql.getConnection().createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + table + " (UUID UUID , DATA JSON)");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			sql.openConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}@Override
	public void disable() {
		if(sql != null)
		try {
			if(sql.checkConnection())sql.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[Lobby-Rel] MySQL Closed");
	}
	@Override
	public void save(UUID uuid, InternalOfflinePlayerData data) {
		PreparedStatement ps;
		if(userExist(uuid)) {
			try {
				ps = sql.getConnection().prepareStatement("UPDATE "+table+" SET DATA = ? WHERE UUID = ?");
				ps.setString(2, uuid.toString());
				ps.setString(1, data.toJsonString());
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}else {
			try {
				ps = sql.getConnection().prepareStatement("INSERT INTO "+table+" (UUID,DATA) VALUES (?,?)");
				ps.setString(1, uuid.toString());
				ps.setString(2, data.toJsonString());
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}

	@Override
	public InternalOfflinePlayerData load(UUID uuid) {
		if(!userExist(uuid))return null;
		try {
			PreparedStatement ps = sql.getConnection().prepareStatement("SELECT * FROM "+table+" WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				String ret = rs.getString("DATA");
				rs.close();
				return InternalOfflinePlayerData.fromJson(ret);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return new InternalOfflinePlayerData();
	}
	private boolean userExist(UUID uuid) {
		 try {
			  PreparedStatement ps = sql.getConnection().prepareStatement("SELECT UUID FROM "+table+" WHERE UUID = ?");
			  ps.setString(1, uuid.toString());
			  ResultSet rs = ps.executeQuery();
			  boolean ret = rs.next();
			  ps.close();
			  rs.close();
			  return ret;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
}
