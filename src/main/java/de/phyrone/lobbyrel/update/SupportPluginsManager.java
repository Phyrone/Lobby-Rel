package de.phyrone.lobbyrel.update;

import de.phyrone.lobbyrel.LobbyPlugin;
import org.bukkit.Bukkit;

public class SupportPluginsManager {
    public static String cfgPath = "PluginSupport.";
    public static void check() {
        for (SupportedPlugin supportedPlugin : SupportedPlugin.values()) {
            if (Bukkit.getPluginManager().isPluginEnabled(supportedPlugin.getPluginname()))
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), supportedPlugin.getOnDetect());
        }
    }

    public enum SupportedPlugin {
        ULTRACOSMETICS("UltraCosmetics", new UltraCosmetics());
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

class UltraCosmetics implements Runnable {

    @Override
    public void run() {
        System.out.println("[Lobby-Rel] UltraCosmetics Detected!");
        System.out.println("      -> Support comming Soon :-)");
    }
}
