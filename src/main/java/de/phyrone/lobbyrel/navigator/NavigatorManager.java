package de.phyrone.lobbyrel.navigator;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.gui.NavigatorGUI;
import de.phyrone.lobbyrel.hotbar.NavigatorHobar;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigatorManager {
    public static void init() {
        navs.clear();
        ItemsConfig cfg = ItemsConfig.getInstance();
        addNavigator(0, new Navigator(cfg.getItem("settings.navigator.option.gui",
                new LobbyItem(Material.WOOL).setData(3).setDisplayName("&bGUI")
        ), player -> NavigatorGUI.open(player, 0)).setName("GUI"));
        addNavigator(1, new Navigator(cfg.getItem("settings.navigator.option.hotbar",
                new LobbyItem(Material.WOOL).setData(10).setDisplayName("&bHotbar")
        ), player -> new NavigatorHobar().open(player)).setName("Hotbar"));
    }
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
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> nav.getAction().onOpen(player));
			}
		}
}
