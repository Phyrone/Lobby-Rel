package de.phyrone.lobbyrel;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.phyrone.lobbyrel.cmd.BuildCMD;
import de.phyrone.lobbyrel.cmd.LobbyCMD;
import de.phyrone.lobbyrel.cmd.WarpCMD;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyReloadEvent;
import de.phyrone.lobbyrel.groups.GroupManager;
import de.phyrone.lobbyrel.hotbar.api.HotbarManager;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.Metrics;
import de.phyrone.lobbyrel.lib.TpsMeter;
import de.phyrone.lobbyrel.lib.protokoll.TinyProtocol;
import de.phyrone.lobbyrel.listner.*;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.scheduler.StayActionManager;
import de.phyrone.lobbyrel.scheduler.StayTitleManager;
import de.phyrone.lobbyrel.storage.StorageManager;
import de.phyrone.lobbyrel.update.LobbyDependency;
import de.phyrone.lobbyrel.warps.WarpManager;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

public class LobbyPlugin extends JavaPlugin implements PluginMessageListener {
    static Logger logger;
    private static LobbyPlugin instance;
    private static double tps = -1D;
    private static Boolean debug = true;
    boolean mc18 = Bukkit.getServer().getClass().getPackage().getName().contains("1_8");
    PacketManipulator packetManipulator;

    public LobbyPlugin() {
        instance = this;
    }

    public static InputStreamReader getResouceFile(String name) {
        try {
            return new InputStreamReader(instance.getResource(name), "UTF8");
        } catch (UnsupportedEncodingException e) {
            return new InputStreamReader(instance.getResource(name));
        }
    }

    public static void copyResource(String in, File out) {
        try {
            if (debug)
                System.out.println("[Lobby-Rel] copy \"" + in + "\" to " + out.getPath());
            IOUtils.copy(getResouceFile(in),
                    new OutputStreamWriter(new FileOutputStream(out)));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void loadConf() {
        try {
            Config.load();
            WarpManager.loadFromConf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TinyProtocol getProtocol() {
        return getInstance().getPacketManipulator().getProtocol();
    }

    public static Boolean getDebug() {
        return debug;
    }

    /**
     * @return bukkit Plugin
     */
    public static LobbyPlugin getInstance() {
        return instance;
    }

    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

    public static String getPrefix() {
        return Config.getString("Prefix", "&8[&6Lobby&8-&cRel&8]").replaceAll("&", "§");
    }

    /**
     * @return the Lobby logger from Bukkit
     */
    public static Logger getLobbyLogger() {
        return logger;
    }

    private double getTPS() {
        return tps;
    }

    @Override
    public void onDisable() {
        try {
            super.onDisable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerManager.getPlayerData(p).quickSave();
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

    /**
     * Saves the Main, Warps and ItemsConfig
     */
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

    @Override
    public void onEnable() {
        try {
            super.onEnable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger = getLogger();
        /*if (!System.getProperty("java.version").startsWith("1.8.")) {
            System.err.println("[Lobby-Rel] Please use Java 1.8");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }*/
        Bukkit.getConsoleSender().sendMessage("§c\n" +
                "  _          _     _             ____      _ \n" +
                " | |    ___ | |__ | |__  _   _  |  _ \\ ___| |\n" +
                " | |   / _ \\| '_ \\| '_ \\| | | | | |_) / _ \\ |\n" +
                " | |__| (_) | |_) | |_) | |_| | |  _ <  __/ |\n" +
                " |_____\\___/|_.__/|_.__/ \\__, | |_| \\_\\___|_|\n" +
                "                         |___/               \n" +
                "   §1Version: §5" + this.getDescription().getVersion() + (debug ? " §8[§4Debug Mode§8]" : "") + "\n" +
                "   §1Author: §5Phyrone" + "\n" +
                "   §1Minecraft: §5" + Bukkit.getBukkitVersion() + " " + (!mc18 && debug ? "§41.9 or Newer Detected!" : "") + "\n" +
                "   §1Bukkit: §5" + Bukkit.getVersion() + "\n");


        instance = this;
        initProtokoll();
        Bukkit.getConsoleSender().sendMessage("[Lobby-Rel] §6Loading Library's if needed...");


        try {
            new LobbyDependency(42835, "SmartInvs").check();
            new LobbyDependency(0, "EffectLib").setCustomURL("https://media.forgecdn.net/files/2489/826/EffectLib-5.5.jar").check();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        loadConf();
        PluginManager pm = Bukkit.getPluginManager();
        /* BungeeMode */
        try {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Player Join */
        pm.registerEvents(new JoinEvent(), this);
        /* Like no Damage or NoBeak */
        pm.registerEvents(new LobbyGuard(), this);
        if (!mc18) pm.registerEvents(new NineUpListner(), this);
        /* Like Reload or Load Storage's */
        pm.registerEvents(new OwnEventsListner(), this);
        pm.registerEvents(new OtherEvents(), this);
        /* Scheduler */
        pm.registerEvents(new StayTitleManager(), this);
        pm.registerEvents(new StayActionManager(), this);
        /* Hotbar */
        pm.registerEvents(new HotbarManager(), this);
        /* GroupManager */
        pm.registerEvents(new GroupManager(), this);
        /* Commands */
        PluginCommand lobbyCMD = Bukkit.getPluginCommand("lobby");
        lobbyCMD.setExecutor(new LobbyCMD());
        lobbyCMD.setTabCompleter(new LobbyCMD());
        Bukkit.getPluginCommand("build").setExecutor(new BuildCMD());
        Bukkit.getPluginCommand("setwarp").setExecutor(new WarpCMD());
        Bukkit.getPluginCommand("setspawn").setExecutor(new WarpCMD());
        Bukkit.getPluginCommand("spawn").setExecutor(new WarpCMD());
        /* Metrics */
        Metrics m = new Metrics(instance);
        m.addCustomChart(new Metrics.SimplePie("storage_type", StorageManager::getStorage));
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
            return n.format(getTPS()) + "TPS";
        }));
        /* TPS */
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            tps = TpsMeter.getTPS(20 * 10);
            System.out.println();
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
        getLogger().fine("§aEnabled");
    }

    @Override
    public void onLoad() {
        System.out.println("[Lobby-Rel] Loading Plugin");
        try {
            debug = getDescription().getVersion().endsWith("-DEV") ||
                    YamlConfiguration.loadConfiguration(
                            new InputStreamReader(getResource("plugin.yml"))).getBoolean("debug", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            super.onLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void initProtokoll() {
        packetManipulator = new PacketManipulator(this);

    }

    public PacketManipulator getPacketManipulator() {
        return packetManipulator;
    }
}
