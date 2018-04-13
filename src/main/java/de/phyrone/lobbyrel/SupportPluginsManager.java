package de.phyrone.lobbyrel;

import org.bukkit.Bukkit;

public class SupportPluginsManager {
    public static void check() {
        for (SupportedPlugin supportedPlugin : SupportedPlugin.values()) {
            if (Bukkit.getPluginManager().isPluginEnabled(supportedPlugin.getPluginname()))
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), supportedPlugin.getOnDetect());
        }
    }

    public enum SupportedPlugin {
        ULTRACOSMETICS("UltraCosmetics", new Ultracometics());
        String pluginname;
        Runnable onDetect;

        SupportedPlugin(String pluginname, Runnable onDetect) {
            this.pluginname = pluginname;
            this.onDetect = onDetect;
        }

        public Runnable getOnDetect() {
            return onDetect;
        }

        public String getPluginname() {
            return pluginname;
        }
    }
}

class Ultracometics implements Runnable {
    @Override
    public void run() {

    }
}
