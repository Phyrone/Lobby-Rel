package de.phyrone.lobbyrel.groups;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.RanksConf;
import de.phyrone.lobbyrel.events.LobbyResetPlayerEvent;
import de.phyrone.lobbyrel.lib.RandomString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class GroupManager implements Listener {
    private static boolean tabEnabled = false;
    private static boolean chatEnabled = false;
    private static boolean customTab = false;
    private static BukkitTask updater;

    public static void init() {
        RanksConf.load();
        RanksConf.getInstance().toFileAsync();
        tabEnabled = Config.getBoolean("GroupManager.TabList", true);
        customTab = Config.getBoolean("GroupManager.CustomTabList", false);
        chatEnabled = Config.getBoolean("GroupManager.Chat", true);
        if (updater != null && Bukkit.getScheduler().isCurrentlyRunning(updater.getTaskId())) {
            updater.cancel();
        }
        if (Config.getBoolean("GroupManager.UpdateScheduler.Enabled", false))
            updater = Bukkit.getScheduler().runTaskTimerAsynchronously(LobbyPlugin.getInstance(), () ->
                    Bukkit.getOnlinePlayers().iterator().forEachRemaining((Consumer<Player>)
                            GroupManager::updateTablistIfEnabled), 0, Config.getInt("GroupManager.UpdateScheduler.Seconds", 30) * 20);
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

    public static List<Player> getPlayers(Player viewer) {
        List<Player> ret = getPlayers();
        ret.iterator().forEachRemaining(player -> {
            if (!viewer.canSee(player))
                ret.remove(player);
        });
        return ret;
    }

    private static String getChat(Player player, String message) {
        return ChatColor.translateAlternateColorCodes('&',
                GroupManager.getGroup(player).ChatLayout
                        .replaceAll("(?i)%player%", player.getDisplayName()))
                .replaceAll("(?i)%message%", player.hasPermission("lobby.chat.colo") ?
                        ChatColor.translateAlternateColorCodes('&', message) : message);
    }

    public static void updateTablistIfEnabled(Player player) {
        if (tabEnabled) setTablist(player);
    }

    private static void setTablist(Player viewer) {
        try {
            setScoreboard(viewer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getTabString(Player player) {
        return ChatColor.translateAlternateColorCodes('&', GroupManager.getGroup(player).TabLayout.replaceAll("(?i)%player%", player.getDisplayName()));
    }

    private static boolean hasScoreboard(Player player) {
        return player.getScoreboard() != null;
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
