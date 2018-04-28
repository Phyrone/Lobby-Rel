package de.phyrone.lobbyrel.cmd;

import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.gui.AdminMainGui;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.lang.LangManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandManager {
    static public ArrayList<CommandAction> Commands = new ArrayList<>();

    public static Boolean runCommand(Player p, String[] args) {
        for (CommandAction cmd : Commands) {
            if (cmd.onCommand(p, args)) return true;
        }
        return false;

    }

    public static void init() {
        Commands.clear();

        Commands.add(new CommandAction() {

            @Override
            public ArrayList<String> onTab(Player player, String[] args) {
                if (args.length == 1) {
                    return new ArrayList<>(Arrays.asList("admingui", "help", "reload", "update", "save", "debug"));
                }
                return null;
            }

            @Override
            public boolean onCommand(Player player, String[] args) {
                if (args[0].equalsIgnoreCase("admingui")) {
                    if (player.hasPermission("lobby.admin")) AdminMainGui.open(player);
                    else
                        new FancyMessage(LobbyPlugin.getPrefix()).then(LangManager.getMessage(player, "NoPermision", "&cder Computer sagt Nein!")).tooltip("§5lobby.admin");
                    return true;
                } else if (args[0].equalsIgnoreCase("debug")) {
                    if (player.hasPermission("lobby.debug")) {
                        if (args.length == 3 && args[1].equalsIgnoreCase("playerdata") && Bukkit.getPlayer(args[2]) != null) {
                            Player target = Bukkit.getPlayer(args[2]);
                            player.sendMessage("Playerdata:");
                            player.sendMessage(
                                    new GsonBuilder().setPrettyPrinting().create().toJson(PlayerManager.getInternalPlayerData(target.getUniqueId())));
                            player.sendMessage("Offlinedata:");
                            player.sendMessage(
                                    new GsonBuilder().setPrettyPrinting().create().toJson(PlayerManager.getInternalOfflinePlayerData(target.getUniqueId())));
                            return true;
                        } else if (args.length > 3 && args[1].equalsIgnoreCase("sendaction")) {
                            Player target = Bukkit.getPlayer(args[2]);
                            String msg = "";
                            for (int i = 3; i < args.length; i++)
                                msg += " " + args[i];
                            Tools.sendActionbar(target, msg);
                            return true;
                        } else if (args.length > 3 && args[1].equalsIgnoreCase("sendtitle")) {
                            Player target = Bukkit.getPlayer(args[2]);
                            String msg = "";
                            for (int i = 3; i < args.length; i++)
                                msg += " " + args[i];
                            Tools.sendTitle(target, new FancyMessage(msg), 1, 40, 1);
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    public static ArrayList<String> runTab(Player p, String[] args) {
        ArrayList<String> ret = new ArrayList<>();
        for (CommandAction cmd : Commands) {
            ArrayList<String> subcomplete = cmd.onTab(p, args);
            if (subcomplete != null) {
                for (String comp : subcomplete)
                    ret.add(comp);
            }
        }

        if (!ret.isEmpty()) {
            ArrayList<String> rr = new ArrayList<>();
            try {
                for (String r : ret)
                    if (r.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                        rr.add(r);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return rr;
        } else {
            return null;
        }

    }

}
