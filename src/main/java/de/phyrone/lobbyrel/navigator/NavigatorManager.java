package de.phyrone.lobbyrel.navigator;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.data.lang.LangManager;

public class NavigatorManager {
	static HashMap<Integer, Navigator> navs = new HashMap<>();
	//Navigator
		public static void addNavigator(int id,Navigator navigator) {
			if(navigator == null) {
				return;
			}
			navs.put(id, navigator);
		}public static void removeNavigator(int id) {
			navs.remove(id);
		}public static Navigator getNavigator(int id) {
			return navs.getOrDefault(id, null);
		}public static ArrayList<Navigator> getNavigators() {
			return new ArrayList<>(navs.values());
		}
		public static void openNavigator(Player player) {
			PlayerData pd = PlayerManager.getPlayerData(player);
			Navigator nav = navs.getOrDefault(pd.getNavigator(), null);
			if(nav == null) {
				player.sendMessage(LangManager.getMessage(player,"Error.noNavigator", LobbyPlugin.getPrefix()+" &cNavigator nicht gefunden!"));
			}else {
				Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						nav.getAction().onOpen(player);
					}
				});
				
			}
		}
}
