package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.SupportPluginsManager;
import de.phyrone.lobbyrel.cmd.CommandManager;
import de.phyrone.lobbyrel.cmd.help.HelpManager;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyLoadStoragesEvent;
import de.phyrone.lobbyrel.events.LobbyReloadEvent;
import de.phyrone.lobbyrel.groups.GroupManager;
import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.hotbar.LoadingHotbar;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.jump.PlayerJumpManager;
import de.phyrone.lobbyrel.player.lang.LangManager;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import de.phyrone.lobbyrel.player.settings.SettingsManager;
import de.phyrone.lobbyrel.playertime.TimeManager;
import de.phyrone.lobbyrel.storage.*;
import de.phyrone.lobbyrel.update.UpdateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class OwnEventsListner implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onReload(LobbyReloadEvent event) {
        try {
            LobbyPlugin.getInstance().getDataFolder().mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            event.getPlugin().getLobbyAddonManager().loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LobbyPlugin.loadConf();
        MainHotbar.setup();
        CommandManager.init();
        ScoreboardManager.init();
        SettingsManager.setup();
        CustomItemsManager.loadConfig();
        CustomItemsManager.init();
        TimeManager.init();
        PlayerManager.resetHandlers();
        PlayerHiderManager.init();
        PlayerJumpManager.setup();
        LangManager.init();
        LoadingHotbar.init();
        UpdateManager.init();
        HelpManager.init();
        StorageManager.init();
        NavigatorManager.init();
        GroupManager.init();
        SupportPluginsManager.check();
        if (Config.getBoolean("Items.Switcher.Enabled", true)) {
            LobbySwitcher.getInstance().updateServers();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLateLoad(LobbyReloadEvent e) {
        System.gc();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStorLoad(LobbyLoadStoragesEvent e) {
        e.addStorage("json", StorageManager.DEFAULT_STORAGE);
        e.addStorage("none", new NoStorage());
        e.addStorage("mysql", new MySQLStorage());
        e.addStorage("cloudnet", new CloudNetDB());
        e.addStorage("mongodb", new MongoDB());
        e.addStorage("cache", new CacheOnlyStorage());
    }

}
