package de.phyrone.lobbyrel.player;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.server.ServerGroup;
import de.dytanic.cloudnet.lib.server.ServerGroupMode;
import de.phyrone.lobbyrel.config.Config;
import org.bukkit.Bukkit;

public class IdeodPreventer {
    public static boolean cloudnet_lobby = false;

    public static void init() {
        if (Config.getBoolean("CheckForIdeods.CloudNet-Lobby", true) && Bukkit.getPluginManager().isPluginEnabled("CloudnetAPI")) {
            ServerGroup group = CloudAPI.getInstance().getServerGroup(
                    CloudAPI.getInstance().getServerInfo(
                            CloudAPI.getInstance().getServerId()).getServiceId().getGroup());
            cloudnet_lobby = group.getGroupMode() == ServerGroupMode.DYNAMIC || group.getGroupMode() == ServerGroupMode.STATIC;


        }


    }
}
