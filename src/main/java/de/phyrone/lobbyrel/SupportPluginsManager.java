package de.phyrone.lobbyrel;

import de.phyrone.lobbyrel.config.Config;
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
        PAF("FriendsAPIForPartyAndFriends", new PartyAndFriends()),
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

class PartyAndFriends implements Runnable {
    @Override
    public void run() {
        if (Config.getBoolean(SupportPluginsManager.cfgPath + "PartyAndFriends.Enabled", true)) {
            String addCMD = Config.getString("PartyAndFriends.CMD.addFriend", "/friend add %friend%");
            String menueCMD = Config.getString("PartyAndFriends.CMD.addFriend", "/friendsgui");


        }
    }
}

class UltraCosmetics implements Runnable {

    @Override
    public void run() {

    }
}
