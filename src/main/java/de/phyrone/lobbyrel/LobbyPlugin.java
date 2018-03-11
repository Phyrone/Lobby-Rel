package de.phyrone.lobbyrel;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.phyrone.lobbyrel.cmd.BuildCMD;
import de.phyrone.lobbyrel.cmd.LobbyCMD;
import de.phyrone.lobbyrel.cmd.WarpCMD;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyReloadEvent;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.Metrics;
import de.phyrone.lobbyrel.lib.TpsMeter;
import de.phyrone.lobbyrel.listner.*;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.player.IdeodPreventer;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.OfflinePlayerData;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.update.LobbyDependency;
import de.phyrone.lobbyrel.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LobbyPlugin extends JavaPlugin implements PluginMessageListener {
    final static File licfile = new File("plugins/Lobby-Rel", "License.json");
    private static LobbyPlugin instance;
    private static double tps = -1D;

    public double getTPS() {
        return tps;
    }

    public LobbyPlugin() {
        instance = this;
    }

    private static void loadConf() {
        try {
            Config.load();
            WarpManager.loadFromConf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LobbyPlugin getInstance() {
        return instance;
    }

    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

    public static String getPrefix() {
        return Config.getString("Prefix", "&8[&6Lobby&8-&cRel&8]").replaceAll("&", "§");
    }

    @Override
    public void onDisable() {
        try {
            WarpManager.saveToConf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        InternalOfllineDataManager.disable();
        for (Player p : Bukkit.getOnlinePlayers()) {
            new OfflinePlayerData(p).saveToConf();
        }
        Bukkit.getConsoleSender().sendMessage("[Lobby-Rel] §cZzz...");
    }

    public void reload() {
        loadConf();
        Bukkit.getPluginManager().callEvent(new LobbyReloadEvent(instance));
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerManager.resetPlayerAndData(p);
        }
    }

    @SuppressWarnings({"resource", "deprecation"})
    @Override
    public void onEnable() {
        if (!System.getProperty("java.version").startsWith("1.8.")) {
            System.err.println("[Lobby-Rel] Please use Java 1.8");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Bukkit.getServer().getClass().getPackage().getName().contains("1_8")) {
            System.err.println("[Lobby-Rel] I recomend to use Spigot 1.8.8 ");
        }
        System.out.println("\n" +
                "  _          _     _             ____      _ \n" +
                " | |    ___ | |__ | |__  _   _  |  _ \\ ___| |\n" +
                " | |   / _ \\| '_ \\| '_ \\| | | | | |_) / _ \\ |\n" +
                " | |__| (_) | |_) | |_) | |_| | |  _ <  __/ |\n" +
                " |_____\\___/|_.__/|_.__/ \\__, | |_| \\_\\___|_|\n" +
                "                         |___/               \n" +
                "Version: " + this.getDescription().getVersion() + " by Phyrone");

        instance = this;
        Bukkit.getConsoleSender().sendMessage("[Lobby-Rel] §6Loading Library's if needed...");
        new LobbyDependency(42835, "SmartInvs").check();
        new LobbyDependency(0, "EffectLib")
                .setCustomURL("https://dev.bukkit.org/projects/effectlib/files/lates").check();
        new LobbyDependency(1997, "ProtocolLib").check();

        /*if (!Bukkit.getPluginManager().isPluginEnabled("SmartInvs")) {
            URL website;
            System.out.println("Downloading SmartInvs...");
            try {

                website = new URL("https://api.spiget.org/v2/resources/42835/download");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("plugins/SmartInvs.jar");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                System.out.println("SmartInvs.jar saved!");
                System.out.println("Enable SmartInvs...");
                Bukkit.getPluginManager().loadPlugin(new File("plugins/", "SmartInvs.jar"));
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("SmartInvs"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("EffectLib")) {
            URL website;
            System.out.println("Downloading EffectLib...");
            try {
                website = new URL("https://dev.bukkit.org/projects/effectlib/files/latest");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream("plugins/EffectLib.jar");
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                System.out.println("EffectLib.jar saved!");
                System.out.println("Enable EffectLib...");
                Bukkit.getPluginManager().loadPlugin(new File("plugins/", "EffectLib.jar"));
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("EffectLib"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        loadConf();
        PluginManager pm = Bukkit.getPluginManager();
        //BungeeMode
        try {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Events
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new LobbyGuard(), this);
        pm.registerEvents(new HotbarEvents(), this);
        pm.registerEvents(new OwnEventsListner(), this);
        pm.registerEvents(new OtherEvents(), this);
        //Commands
        PluginCommand lbcmd = Bukkit.getPluginCommand("lobby");
        lbcmd.setExecutor(new LobbyCMD());
        lbcmd.setTabCompleter(new LobbyCMD());
        Bukkit.getPluginCommand("build").setExecutor(new BuildCMD());
        Bukkit.getPluginCommand("setwarp").setExecutor(new WarpCMD());
        Bukkit.getPluginCommand("setspawn").setExecutor(new WarpCMD());
        Bukkit.getPluginCommand("spawn").setExecutor(new WarpCMD());
        // Metrics
        Metrics m = new Metrics(instance);
        m.addCustomChart(new Metrics.SimplePie("storage_type", () -> InternalOfllineDataManager.getStorage().toUpperCase()));
        m.addCustomChart(new Metrics.AdvancedPie("players_use_navigator", () -> {
            HashMap<String, Integer> ret = new HashMap<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                String r;
                switch (PlayerManager.getPlayerData(p).getNavigator()) {
                    case 0:
                        r = "GUI";
                        break;
                    case 1:
                        r = "Hotbar";
                        break;
                    default:
                        r = "Other";
                        break;
                }
                ret.put(r, ret.getOrDefault(r, 0) + 1);
            }
            return ret;
        }));
        m.addCustomChart(new Metrics.SimplePie("tps", () -> {
            NumberFormat n = NumberFormat.getInstance();
            n.setMaximumFractionDigits(2); // max. 2 stellen hinter komma
            return n.format(tps) + "TPS";
        }));
        //TPS
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            tps = TpsMeter.getTPS(20 * 10);
            System.out.println("");
        }, 20 * 60, 20 * 60);
        //Other
        reload();
        //Players
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerManager.resetPlayerAndData(player);
        }
        try {
            IdeodPreventer.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bukkit.getConsoleSender().sendMessage("§8[§5Lobby-Rel§8] §aEnabled");


    }

    @Override
    public void saveConfig() {
        BukkitRunnable run = new BukkitRunnable() {

            @Override
            public void run() {
                Config.saveSync();
                CustomItemsManager.save();
                WarpManager.saveToConf();
                System.out.println("Lobby-Rel] Config saved!");
            }
        };
        if (Bukkit.getPluginManager().isPluginEnabled(this)) {
            run.runTaskAsynchronously(this);
        } else {
            run.run();
        }


    }

    //Bungee Mesaging
    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] message) {
        try {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subchannel = in.readUTF();
            switch (subchannel) {
                case "GetServers":
                    LobbySwitcher.getInstance().updateBungeeServers(in.readUTF().split(", "));
                    break;
                case "PlayerCount":
                    LobbySwitcher.getInstance().setServerData(in.readUTF(), LobbySwitcher.getInstance().new ServerConData(in.readInt()));
                    break;
                case "GetServer":
                    LobbySwitcher.getInstance().setServerName(in.readUTF());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void sendBungeeMessage(Collection<String> content) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String c : content)
            out.writeUTF(c);
        this.getServer().sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public void sendBungeeMessage(Player player, ArrayList<String> content) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String c : content)
            out.writeUTF(c);
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public void sendPlayer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

}
