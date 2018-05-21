package de.phyrone.lobbyrel.groups;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.RanksConf;
import de.phyrone.lobbyrel.events.LobbyResetPlayerEvent;
import de.phyrone.lobbyrel.lib.RandomString;
import de.phyrone.lobbyrel.lib.tablist.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GroupManager implements Listener {
    private static boolean tabEnabled = false;
    private static boolean chatEnabled = false;
    private static boolean customTab = false;

    public static void init() {
        RanksConf.load();
        RanksConf.getInstance().toFileAsync();
        if (tabEnabled)
            for (Player player : Bukkit.getOnlinePlayers())
                PlayerList.getPlayerList(player).resetTablist();
        tabEnabled = Config.getBoolean("GroupManager.TabList", true);
        customTab = Config.getBoolean("GroupManager.CustomTabList", true);
        chatEnabled = Config.getBoolean("GroupManager.Chat", true);
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

    public static void updateTablistIfEnabled(Player player) {
        if (tabEnabled) setTablist(player);
    }

    private static void setTablist(Player viewer) {
        if (customTab)
            try {
                PlayerList list = PlayerList.getPlayerList(viewer);
                list.clearAll();
                int i = 0;
                final List<Player> players = GroupManager.getPlayers();
                for (Player player : players) {
                    //list.updateSlot(i,ChatColor.translateAlternateColorCodes('&', GroupManager.getGroup(player).TabLayout.replace("%player%", player.getDisplayName())),true);
                    if (viewer.canSee(player))
                        try {
                            list.addExistingPlayer(i, ChatColor.translateAlternateColorCodes('&', GroupManager.getGroup(player).TabLayout.replace("%player%", player.getDisplayName())), player);
                            i++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            setScoreboard(viewer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean hasScoreboard(Player player) {
        if (player.getScoreboard() != null) {
            return true;
        } else {
            return false;
        }
    }

    private static void setScoreboard(Player viewer) {
        Scoreboard sb = hasScoreboard(viewer) ? viewer.getScoreboard() : Bukkit.getScoreboardManager().getNewScoreboard();
        for (Team team : sb.getTeams())
            if (team.getName().endsWith("LBT"))
                team.unregister();
        String uniqueTag = new RandomString(5).nextString();
        int i = 0;
        /* Groups */
        HashMap<DisplayGroup, Team> teams = new HashMap<>();

        for (DisplayGroup group : getGroups()) {
            try {
                Team team = sb.registerNewTeam(String.valueOf(i) + uniqueTag + "LBT");
                team.setPrefix(group.getPrefix());
                team.setSuffix(group.getSuffix());
                team.setDisplayName("Group-" + String.valueOf(i));
                teams.put(group, team);
            } catch (IllegalArgumentException | IllegalStateException e) {
                e.printStackTrace();
            }
            i++;
        }/* Default Group */
        Team default_Team;
        {
            DisplayGroup default_Group = RanksConf.getInstance().DefaultGroup;
            default_Team = sb.registerNewTeam(String.valueOf(i) + uniqueTag + "LBT");
            default_Team.setPrefix(default_Group.getPrefix());
            default_Team.setSuffix(default_Group.getSuffix());
            default_Team.setDisplayName("Group-Default");
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            Team team = teams.getOrDefault(getGroup(player), default_Team);
            team.addPlayer(player);
        }
        if (!hasScoreboard(viewer))
            try {
                viewer.setScoreboard(sb);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.err.println("Lobby-Rel[ERROR] setTablist-Scoreboard");
                e.printStackTrace();
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
    public static void onJoin(LobbyResetPlayerEvent event) {
        if (tabEnabled) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), () -> {
                for (Player player : Bukkit.getOnlinePlayers()) setTablist(player);
            }, 10);
            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> setTablist(event.getPlayer()));
        }
    }
}
