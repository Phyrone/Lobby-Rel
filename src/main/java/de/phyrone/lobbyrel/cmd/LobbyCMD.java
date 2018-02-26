package de.phyrone.lobbyrel.cmd;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.cmd.help.HelpManager;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.update.UpdateManager;

public class LobbyCMD implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                useHelpMessage(p, label);
            } else {
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (p.hasPermission("lobby.reload")) {
                        LobbyPlugin.getInstance().reload();
                        LangManager.sendMessage(p, "CMD.Reloaded.Chat", "&cReloaded!");
                        Tools.sendTitle(p, new FancyMessage(LangManager.getMessage(p, "CMD.Reloaded.Title", "&5Reloaded")), 20, 20, 20);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    UpdateManager.updateCMD(sender);

                } else if (args[0].equalsIgnoreCase("save")) {
                    if (p.hasPermission("lobby.reload")) {
                        LobbyPlugin.getInstance().saveConfig();
                        new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then("§csaving Config...").send(p);
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 1) {
                        new HelpManager(p).showHelp(label, 1);
                    } else if (args.length == 2) {
                        try {
                            new HelpManager(p).showHelp(label, Integer.parseInt(args[1]));
                        } catch (Exception e) {
                            useHelpMessage(p, label);
                        }
                    } else useHelpMessage(p, label);
                } else {
                    if (!CommandManager.runCommand(p, args)) {
                        useHelpMessage(p, label);

                    }
                    return true;
                }
            }
        } else {
            sender.sendMessage("[Lobby-Rel] PlayerOnly");
        }
        return true;
    }

    private void useHelpMessage(CommandSender s, String label) {
        Player p = null;
        if (s instanceof Player) p = (Player) s;
        new FancyMessage(LobbyPlugin.getPrefix()).then(LangManager.getMessage(p, "CMD.UnknownCMD.Message", " &cuse &7/%lobbycmd% help")
                .replace("%lobbycmd%", label)).command("/" + label + " help").tooltip(LangManager.getMessage(p, "UnknownCMD.Tooltip", "&aRun Command")).send(p);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            return CommandManager.runTab(p, args);
        }

        return null;
    }
}
