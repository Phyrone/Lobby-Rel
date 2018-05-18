package de.phyrone.lobbyrel.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class ServerProp {
    static Location getSpawnLocation() {
		File file = new File("server.properties");
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
			Location loc = Bukkit.getWorld(prop.getProperty("level-name")).getSpawnLocation();
			loc.setPitch(0);
			loc.setYaw(0);
			return loc;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Bukkit.getWorlds().get(0).getSpawnLocation();
	}

}
