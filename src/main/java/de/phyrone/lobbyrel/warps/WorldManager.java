package de.phyrone.lobbyrel.warps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import de.phyrone.lobbyrel.config.Config;

public class WorldManager {
	static boolean enabled = false;
	static List<String> worlds = new ArrayList<>();
	public static void init() {
		enabled = Config.getBoolean("MultiWorld.Enabled", false);
		FileConfiguration cfg = Config.getConf();
		if(cfg.contains("MultiWorld.Lobbys"))
			worlds = cfg.getStringList("MultiWorld.Lobbys");
		else
			cfg.set("MultiWorld.Lobbys", new ArrayList<String>(Arrays.asList("world,lobby")));
	}public static boolean isLobby(String world) {
		if(enabled)
			return worlds.contains(world);
		else
			return true;
	}public static boolean isLobby(World world) {
		return isLobby(world.getName());
	}

}
