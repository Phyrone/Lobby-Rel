package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.SupportPluginsManager;
import de.phyrone.lobbyrel.cmd.CommandManager;
import de.phyrone.lobbyrel.cmd.help.HelpManager;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyLoadStoragesEvent;
import de.phyrone.lobbyrel.events.LobbyReloadEvent;
import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.hotbar.LoadingHotbar;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.player.jump.PlayerJumpManager;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import de.phyrone.lobbyrel.player.settings.SettingsManager;
import de.phyrone.lobbyrel.playertime.TimeManager;
import de.phyrone.lobbyrel.storage.*;
import de.phyrone.lobbyrel.update.UpdateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class OwnEventsListner implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onReload(LobbyReloadEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                LobbyPlugin.getInstance().getDataFolder().mkdirs();
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());
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
        NavigatorManager.init();
		InternalOfllineDataManager.addSorage("json", InternalOfllineDataManager.defaultStorage);
        SupportPluginsManager.check();
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
