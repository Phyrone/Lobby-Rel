package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.cmd.CommandManager;
import de.phyrone.lobbyrel.cmd.help.HelpManager;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyLoadStoragesEvent;
import de.phyrone.lobbyrel.events.LobbyReloadEvent;
import de.phyrone.lobbyrel.gui.NavigatorGUI;
import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.hotbar.LoadingHotbar;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.NavigatorHobar;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.Color;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.navigator.Navigator;
import de.phyrone.lobbyrel.navigator.NavigatorAction;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.player.jump.PlayerJumpManager;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import de.phyrone.lobbyrel.player.settings.SettingsManager;
import de.phyrone.lobbyrel.playertime.TimeManager;
import de.phyrone.lobbyrel.storage.*;
import de.phyrone.lobbyrel.update.UpdateManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OwnEventsListner implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onReload(LobbyReloadEvent e) {
		MainHotbar.setup();
		CommandManager.init();
		ScoreboardManager.init();
		SettingsManager.setup();
		CustomItemsManager.loadConfig();
		CustomItemsManager.init();
		TimeManager.init();
		PlayerHiderManager.init();
		PlayerJumpManager.setup();
		LangManager.init();
		LoadingHotbar.init();
		UpdateManager.init();
		HelpManager.init();
		InternalOfllineDataManager.init();
		InternalOfllineDataManager.addSorage("json", InternalOfllineDataManager.defaultStorage);
		NavigatorManager.addNavigator(0, new Navigator(new NavigatorAction() {
			
			@Override
			public void onOpen(Player player) {
				NavigatorGUI.open(player, 0);
			}
		}).setColor(Color.RED).setName("GUI"));
		NavigatorManager.addNavigator(1, new Navigator(new NavigatorAction() {
			
			@Override
			public void onOpen(Player player) {	
				new NavigatorHobar().open(player);
			}
		}).setColor(Color.GREY).setName("Hotbar"));
		if(Config.getBoolean("Items.Swticher.Enabled",true)) {
			new LobbySwitcher().updateServers();
		}


    }

    @EventHandler(priority = EventPriority.HIGHEST)
	public void onStorLoad(LobbyLoadStoragesEvent e) {
		e.addStorage("none", new NoStorage());
		e.addStorage("mysql", new MySQLStorage());
		e.addStorage("mongodb", new MongoDB());
		e.addStorage("cloudnet", new CloudNetDB());
        e.addStorage("cache", new CacheOnlyStorage());
	}

}
