package de.phyrone.lobbyrel.warps;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.WarpConf;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.util.HashMap;

public class WarpManager {
	private static final File cfg = new File("plugins/Lobby-Rel", "Warps.json");
	public static void setWarpLocation(String name,Location loc,boolean saveconfig) {
		Warp warp = WarpConf.getInstance().Warps.getOrDefault(name, new Warp().setWarpItem(new LobbyItem(
				new ItemBuilder(Material.STONE).displayname(name).build()
				)));
		warp.X = loc.getX();
		warp.Z = loc.getZ();
		warp.Y = loc.getY();
		warp.Yaw = loc.getYaw();
		warp.Pitch = loc.getPitch();
		warp.World = loc.getWorld().getName();
		WarpConf.getInstance().Warps.put(name.toUpperCase(), warp);
		if(saveconfig)
			saveToConf();
		
	}public static Location getLocation(String warpid) {
		Warp warp = WarpConf.getInstance().Warps.getOrDefault(warpid.toUpperCase(), null);
		if(warp != null) {
			return new Location(Bukkit.getWorld(warp.World), warp.X, warp.Y, warp.Z,warp.Yaw,warp.Pitch);
		}
		return null;
	}@SuppressWarnings("deprecation")
	
	public static void saveToConf() {
		if(Bukkit.getPluginManager().isPluginEnabled(LobbyPlugin.getInstance())) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					try {
						WarpConf.getInstance().toFile(cfg);
					}catch (Exception e) {
						System.err.println("[Lobby] Error Save Config");
						e.printStackTrace();
					}
				}
			});
		}else {
			try {
				WarpConf.getInstance().toFile(cfg);
			}catch (Exception e) {
				System.err.println("[Lobby] Error Save Config");
				e.printStackTrace();
			}
		}


			

	}public static void loadFromConf() {
		if(cfg.exists()) {
			WarpConf.load(cfg);
		}else {
			try {
				System.out.println("Create: warps.json");
				cfg.getParentFile().mkdirs();
				cfg.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			WarpConf.load(cfg);
		}
		for(String warpname: WarpConf.getInstance().Warps.keySet()) {
			if(!isAllUpper(warpname))
			try {
				Warp wr = WarpConf.getInstance().Warps.get(warpname);
				WarpConf.getInstance().Warps.remove(warpname,wr);
				WarpConf.getInstance().Warps.put(warpname.toUpperCase(), wr);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		
	}public static void setSpawnLocation(Location location,boolean saveconfig) {
		WarpConf.getInstance().Spawn.setLocation(location);
		if(saveconfig)
			saveToConf();
	}
	public static HashMap<String, Warp> getWarps(){
		return WarpConf.getInstance().Warps;
	}
	public static Warp getSpawn() {
		return WarpConf.getInstance().Spawn;
	}public static Warp getWarp(String name) {
		return WarpConf.getInstance().Warps.getOrDefault(name, null);
	}public static void setWarp(String name,Warp warp) {
		WarpConf.getInstance().Warps.put(name, warp);
	}private static boolean isAllUpper(String s) {
	    for(char c : s.toCharArray()) {
	        if(Character.isLetter(c) && Character.isLowerCase(c)) {
	            return false;
	         }
	     }
	     return true;
	 }

}
