package de.phyrone.lobbyrel.groups;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.RanksConf;
import de.phyrone.lobbyrel.lib.tablist.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GroupManager implements Listener {
    private static boolean tabEnabled = false;
    private static boolean chatEnabled = false;

    public static void init() {
        RanksConf.load();
        RanksConf.getInstance().toFileAsync();
        if (tabEnabled)
            for (Player player : Bukkit.getOnlinePlayers())
                PlayerList.getPlayerList(player).resetTablist();
        tabEnabled = Config.getBoolean("GroupManager.TabList", true);
        chatEnabled = Config.getBoolean("GroupManager.TabList", true);

    }

    private static List<DisplayGroup> getGroups() {
        return RanksConf.getInstance().Ranks;
    }

    private static DisplayGroup getGroup(Player player) {
        for (DisplayGroup group : RanksConf.getInstance().Ranks)
            if (player.hasPermission(group.Permission)) return group;
        return RanksConf.getInstance().DefaultGroup;
    }

    public static List<Player> getPlayers() {
        List<Player> ret = new ArrayList<>(Bukkit.getOnlinePlayers());
        ret.sort(Comparator.comparing(Player::getDisplayName));
        ret.sort((o1, o2) -> {
            for (DisplayGroup group : getGroups()) {
                if (o1.hasPermission(group.Permission) && !o2.hasPermission(group.Permission))
                    return 1;
                else if (!o1.hasPermission(group.Permission) && o2.hasPermission(group.Permission))
                    return -1;
                else return 0;
            }
            return 0;
        });
        return ret;
    }

    private static String getChat(Player player, String message) {
        return ChatColor.translateAlternateColorCodes('&',
                GroupManager.getGroup(player).ChatLayout
                        .replace("%player%", player.getDisplayName()))
                .replace("%message%", player.hasPermission("lobby.chat.colo") ?
                        ChatColor.translateAlternateColorCodes('&', message) : message);
    }

    private static void setTablist(Player viewer) {
        PlayerList list = PlayerList.getPlayerList(viewer);
        list.clearAll();
        int i = 0;
        for (Player player : GroupManager.getPlayers()) {
            //list.updateSlot(i,ChatColor.translateAlternateColorCodes('&', GroupManager.getGroup(player).TabLayout.replace("%player%", player.getDisplayName())),true);
            list.addExistingPlayer(i, ChatColor.translateAlternateColorCodes('&', GroupManager.getGroup(player).TabLayout.replace("%player%", player.getDisplayName())), player);
            i++;
        }
    }

    @EventHandler
    public static void onChat(AsyncPlayerChatEvent event) {
        if (chatEnabled) {
            Player player = event.getPlayer();
            event.setFormat(getChat(player, event.getMessage()));
        }
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        if (tabEnabled) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) setTablist(player);
            }, 10);
            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> setTablist(event.getPlayer()));
        }
    }
}
