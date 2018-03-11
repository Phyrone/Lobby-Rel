package de.phyrone.lobbyrel.update;

import com.github.alessiop86.antiantibotcloudflare.exceptions.AntiAntibotException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class UpdateManager {
    private static boolean update = false;
    private static LobbyDependency asDependency = new LobbyDependency(49126, "Lobby-Rel");
    public static void check() {
        check(null);
    }

    private static void check(Runnable ifFinish) {
        new Thread(() -> {
            try {
                String oVersion = getOnlineVersion();
                update = !oVersion.equalsIgnoreCase(LobbyPlugin.getInstance().getDescription().getVersion());
                System.out.println("[LobbyRel] Updater -> Current-Online-Version: " + oVersion);
                if (ifFinish != null)
                    ifFinish.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "LobbyUpdateCheckTask").start();


    }

    public static void init() {
        if (Config.getBoolean("Updater.Enabled", true))
            check(() -> {
                if (Config.getBoolean("Updater.UpdateOnStart", false) && needUpdate()) {
                    updateAsync();
                }
            });


    }

    private static boolean needUpdate() {
        return update;
    }

    public static void informAdminAboutUpdates(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (Config.getBoolean("Updater.InformAdmin", true) && player.hasPermission("lobby.update") && needUpdate()) {
                    FancyMessage m = new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then(LangManager.getMessage(player, "Message.Update.Inform", "&aa new &6Update &ais available "))
                            .then(LangManager.getMessage(player, "Message.Update.Button.Message", "&8[&6Update&8]")).tooltip(LangManager.getMessage(player, "Message.Update.Button.Tooltip", "&5Click to Update"))
                            .command("/lobbysystem update");
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            m.send(player);
                        }
                    }.runTask(LobbyPlugin.getInstance());
                }
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());
    }

    public static void updateCMD(CommandSender s) {
        new BukkitRunnable() {

            @Override
            public void run() {
                Player p = null;
                if (s instanceof Player) p = (Player) s;
                if (Config.getBoolean("Updater.AllowCMD", true) && s.hasPermission("lobby.update")) {
                    if (needUpdate()) {
                        LangManager.sendMessage(p, "Message.Update.Downloading", "&6Downloading...");
                        System.out.println(LobbyPlugin.getPrefix() + " Downloading...");
                        if (update()) {
                            LangManager.sendMessage(p, "Message.Update.Download-Finish", "&5Update Finish you can Restart now!");
                            update = false;
                        } else {
                            LangManager.sendMessage(p, "Message.Update.Download-Failed", "&4&lUpdate Failed please try again later!");
                        }

                        System.out.println(LobbyPlugin.getPrefix() + " Update Downloaded...");
                    } else {
                        LangManager.sendMessage(p, "Message.Update.Alredy-Uptodate", "&c Already Uptodate!");
                    }
                } else {
                    s.sendMessage(LangManager.noPerm(p));
                }

            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());

    }

    @SuppressWarnings("resource")
    public static boolean update() {
        try {
            asDependency.download();
            return true;
        } catch (AntiAntibotException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void updateAsync() {
        new Thread("LobbyUpdateTask") {
            @Override
            public void run() {
                update();
            }
        }.start();

    }


    private static String getOnlineVersion() {
        try {
            StringBuilder in = new StringBuilder();
            Scanner inStream = new Scanner(new URL("https://api.spiget.org/v2/resources/49126/versions").openStream());
            while (inStream.hasNext()) {
                in.append(inStream.nextLine()).append(inStream.hasNext() ? "\n\r" : "");
            }
            Type listType = new TypeToken<List<OneVersion>>() {
            }.getType();
            List<OneVersion> versions = new Gson().fromJson(in.toString(), listType);
            Collections.reverse(versions);
            return versions.get(0).name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LobbyPlugin.getInstance().getDescription().getVersion();
    }

    public class OneVersion {
        public String name = "Unknown";
    }

}
