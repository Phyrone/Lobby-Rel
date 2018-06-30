package de.phyrone.lobbyrel;
/*
 *   Copyright © 2018 by Phyrone  *
 *   Creation: 23.06.2018 by Phyrone
 */

import de.phyrone.addonloader.AddonDownloader;
import de.phyrone.addonloader.repository.web.JsonWebRepository;
import de.phyrone.lobbyrel.config.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyAddonManager {
    Plugin plugin;
    Logger logger = LobbyPlugin.getInstance().getLogger();
    @Getter
    AddonDownloader addonDownloader;

    public LobbyAddonManager(Plugin plugin) {
        this.plugin = plugin;
        try {
            addonDownloader = new AddonDownloader(new File(LobbyPlugin.getPlugin(LobbyPlugin.class).getDataFolder().getPath() + "/Addons/"));
            addonDownloader.getLocalDir().mkdirs();
            Config.getStringList("Addons.Repositorys", Collections.singletonList("https://static.phyrone.de/repo/lobby-rel/storages.json")).iterator().forEachRemaining(url -> {
                try {
                    addonDownloader.addRepository(new JsonWebRepository(new URL(url)));
                } catch (MalformedURLException e) {
                    logger.log(Level.WARNING, "Failed to Load Repository \"" + url + "\"", e);
                    e.printStackTrace();
                }
            });
            addonDownloader.setAddonInstanceLoader(downloadedAddon -> {
                Plugin loadPlugin = Bukkit.getPluginManager().loadPlugin(downloadedAddon.getFile());
                Bukkit.getPluginManager().enablePlugin(loadPlugin);
                return loadPlugin;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        addonDownloader.updateRepositories();
        try {
            addonDownloader.updateLocalFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
