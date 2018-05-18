package de.phyrone.lobbyrel.player.scoreboard;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.groups.GroupManager;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.lang.LangManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tade.quickboard.api.QuickBoardAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreboardManager {
    static Listener listner = null;
    static boolean enabled = false;
    static boolean external = false;
    static List<String> lines;
    static List<String> title;
    static String qickboardConf;
    static int updateTime = 10;

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        ScoreboardManager.enabled = enabled;
    }

    public static void init() {
        enabled = Config.getBoolean("Scoreboard.Enabled", false);
		/*private void respawnPlayer(final Player p) {
        new BukkitRunnable() {
            public void run() {
                final PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
                ((CraftPlayer)p).getHandle().playerConnection.a(packet);
            }
        }.runTaskLater((Plugin)Main.getInstance(), 2L);
    }*/
        new Thread(() -> {
            if (enabled) {
                if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    //6245
                    URL website;
                    System.out.println("Downloading PlaceholderAPI...");
                    try {
                        ///https://api.spiget.org/v2/resources/42835/versions/173186/download
                        //https://api.spiget.org/v2/resources/42835/download
                        website = new URL("https://api.spiget.org/v2/resources/6245/download");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("plugins/PlaceholderAPI.jar");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        System.out.println("PlaceholderAPI.jar saved!");

                        Bukkit.getPluginManager().loadPlugin(new File("plugins/", "PlaceholderAPI.jar"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!Bukkit.getPluginManager().isPluginEnabled("QuickBoard")) {
                    //15057
                    URL website;
                    System.out.println("Downloading QuickBoard...");
                    try {
                        ///https://api.spiget.org/v2/resources/42835/versions/173186/download
                        //https://api.spiget.org/v2/resources/42835/download
                        website = new URL("https://api.spiget.org/v2/resources/15057/download");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("plugins/QuickBoard.jar");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        System.out.println("QuickBoard.jar saved!");
                        System.out.println("Loading QuickBoard...");
                        Bukkit.getPluginManager().loadPlugin(new File("plugins/", "QuickBoard.jar"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                external = Config.getBoolean("Scoreboard.External.useQickBardConfig", false);
                qickboardConf = Config.getString("Scoreboard.External.Config", "default.yml");
                if (qickboardConf.toLowerCase().endsWith(".yml")) {
                    qickboardConf = qickboardConf.substring(0, qickboardConf.length() - 4);
                }
                updateTime = Config.getInt("Scoreboard.UpdateTime", 10);
                if (Config.conf.contains("Scoreboard.Title"))
                    title = Config.conf.getStringList("Scoreboard.Title");
                else {
                    title = new ArrayList<>(Arrays.asList("&6Lobby-Rel"));
                    Config.conf.set("Scoreboard.Title", title);
                    Config.saveAsync();
                }
                if (Config.conf.contains("Scoreboard.Lines"))
                    lines = Config.conf.getStringList("Scoreboard.Lines");
                else {
                    lines = new ArrayList<>(Arrays.asList("Line1", "Line2", "Line3"));
                    Config.conf.set("Scoreboard.Lines", lines);
                    Config.saveAsync();
                }
                try {
                    System.out.println("Enable PlaceholderAPI...");

                    Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("PlaceholderAPI"));
                    Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("Enable QuickBoard...");
                            Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("QuickBoard"));
                        }
                    }, 20);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, "InitScoreboardThread").start();
    }

    public static void update(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                PlayerData pd = PlayerManager.getPlayerData(player);
                if (enabled && pd.isScoreboard()) {
                    try {
                        if (external) {
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    QuickBoardAPI.createBoard(player, qickboardConf.replace("%lang%",
                                            LangManager.getMessage(player, "Scoreboard.ConfigPath", "en")));
                                }
                            }.runTask(LobbyPlugin.getInstance());

                        } else {
                            try {
                                List<String> ll = PlaceholderAPI.setPlaceholders(player, lines);
                                List<String> tt = PlaceholderAPI.setPlaceholders(player, title);
                                setBoard(player, ll, tt);
                            } catch (Exception e) {
                                System.out.println("PlaceHolderapi nicht Gefunden");
                                List<String> tt = title;
                                List<String> ll = lines;
                                setBoard(player, ll, tt);
                            }

                        }
                    } catch (Exception e) {
                        System.out.println("Error: ADD Scoreboard");
                        e.printStackTrace();
                    }
                } else if (enabled) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                QuickBoardAPI.removeBoard(player);
                            } catch (Exception e) {
                                System.out.println("Error: Remove Scoreboard");
                                e.printStackTrace();
                            }
                        }
                    }.runTask(LobbyPlugin.getInstance());
                }
            }
        }.runTaskLaterAsynchronously(LobbyPlugin.getInstance(), 1);
        Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), () -> GroupManager.updateTablistIfEnabled(player), 3);
    }

    private static void setBoard(Player player, List<String> lines, List<String> title) {
        new BukkitRunnable() {

            @Override
            public void run() {
                QuickBoardAPI.createBoard(player, lines, title, updateTime, updateTime);
            }
        }.runTask(LobbyPlugin.getInstance());

    }
}
