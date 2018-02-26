package de.phyrone.lobbyrel.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

import de.phyrone.lobbyrel.LobbyPlugin;

public class MySQLConf {
	//static MySQL sql = new MySQL(Config.getString("MySQL.host", "localhost"), Config.getString("MySQL.database", "database"), Config.getString("MySQL.user", "user"), Config.getString("MySQL.password", "passwd"));
	static Connection con;
	static Plugin plugin = LobbyPlugin.getInstance();
	/*public static void conect() {
		sql = new MySQL(Config.getString("MySQL.host", "localhost"), Config.getString("MySQL.database", "database"), Config.getString("MySQL.user", "user"), Config.getString("MySQL.password", "passwd"));
		con = sql.getConnection();
	}*/
    public static void connect(){
        if(!isConnected()){
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + Config.getString("MySQL.host", "localhost") + ":" + Config.getInt("MySQL.port", 3306) + "/" + Config.getString("MySQL.database", "database") + "?autoReconnect=true", Config.getString("MySQL.user", "root"), Config.getString("MySQL.password", "password"));
            } catch (SQLException ex) {
            	System.out.println("SQL Conection Failed: "+ex.getMessage());
            }
        }else{
            return;
        }
    }

    public static void disconnect(){
        if(isConnected()){
            try {
                con.close();
            } catch (SQLException ex) {
            	System.out.println("SQL Error: "+ex.getMessage());
            }
        }else{
            return;
        }
    }
public static void update(String qry){
        if(isConnected()){
            try {
                PreparedStatement ps = con.prepareStatement(qry);

                ps.executeUpdate();
            } catch (SQLException ex) {
            	
            }

        }
    }

    public static ResultSet getResult(String qry){
        if(isConnected()){
            try {
                PreparedStatement ps = con.prepareStatement(qry);

                return ps.executeQuery();
            } catch (SQLException ex) {
                
            }
        }
        return null;
    }
    public static boolean isUserInDatabase(String uuid, String dataTable){
        if(isConnected()){
            ResultSet rs = getResult("SELECT * FROM " + dataTable + " WHERE UUID = '" + uuid + "'");

            try {
                if(rs.next()){
                        return rs.getString("UUID") != null;
                    }
            } catch (SQLException ex) {
            	
            }
        }
        return false;
    }private static boolean isConnected(){
        if(con != null){
            return true;
        } 
        return false;
    }
}
