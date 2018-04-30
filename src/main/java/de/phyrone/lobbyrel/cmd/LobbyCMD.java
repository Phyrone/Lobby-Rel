package de.phyrone.lobbyrel.cmd;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.cmd.help.HelpManager;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.lang.LangManager;
import de.phyrone.lobbyrel.update.UpdateManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class LobbyCMD implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length == 0) {
                useHelpMessage(sender, label);
            } else {
                boolean isPlayer = sender instanceof Player;
                if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (sender.hasPermission("lobby.reload")) {
                        LobbyPlugin.getInstance().reload();
                        LangManager.sendMessage(sender, "CMD.Reloaded.Chat", "&cReloaded!");
                        if (isPlayer)
                            Tools.sendTitle((Player) sender, new FancyMessage(LangManager.getMessage(sender, "CMD.Reloaded.Title", "&5Reloaded")), 20, 20, 20);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("update")) {
                    UpdateManager.updateCMD(sender);

                } else if (args[0].equalsIgnoreCase("save")) {
                    if (sender.hasPermission("lobby.reload")) {
                        LobbyPlugin.getInstance().saveConfig();
                        new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then("§csaving Config...").send(sender);
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    if (args.length == 1) {
                        new HelpManager(sender).showHelp(label, 1);
                    } else if (args.length == 2) {
                        try {
                            new HelpManager(sender).showHelp(label, Integer.parseInt(args[1]));
                        } catch (Exception e) {
                            useHelpMessage(sender, label);
                        }
                    } else useHelpMessage(sender, label);
                } else {
                    if (!CommandManager.runCommand(sender, args)) {
                        useHelpMessage(sender, label);

                    }
                    return true;
                }
            }

        return true;
    }

    private void useHelpMessage(CommandSender sender, String label) {
        new FancyMessage(LobbyPlugin.getPrefix()).then(LangManager.getMessage(sender, "CMD.UnknownCMD.Message", " &cuse &7/%lobbycmd% help")
                .replace("%lobbycmd%", label)).command("/" + label + " help").tooltip(LangManager.getMessage(sender, "UnknownCMD.Tooltip", "&aRun Command")).send(sender);
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
