package de.phyrone.lobbyrel.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ServerProp {
	public static Location getSpawnLocation(){
		File file = new File("server.properties");
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
			Location loc = Bukkit.getWorld(prop.getProperty("level-name")).getSpawnLocation();
			loc.setPitch(0);
			loc.setYaw(0);
			return loc;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
