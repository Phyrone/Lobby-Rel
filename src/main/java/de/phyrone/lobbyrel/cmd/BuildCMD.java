package de.phyrone.lobbyrel.cmd;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.lang.LangManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class BuildCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission(Config.getString("BuildPermission", "lobby.build"))) {
                if (args.length == 0) {
                    PlayerData pd = PlayerManager.getPlayerData(p);
                    if (pd.isBuilder()) {
                        pd.setAllowGamemodeChange(true);
                        p.setGameMode(GameMode.ADVENTURE);
                        pd.setBuilder(false);
                        pd.setAllowGamemodeChange(false);
                        PlayerManager.resetPlayer(p);
                        new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then(LangManager.getMessage(p, "CMD.Build.Disabled", "&a&lBuilemode Disabled")).send(p);
                    } else {
                        pd.setAllowGamemodeChange(true);
                        p.setGameMode(GameMode.CREATIVE);
                        pd.setBuilder(true);
                        pd.setAllowGamemodeChange(false);
                        p.getInventory().clear();
                        p.setMaxHealth(20);
                        p.setHealth(20);
                        new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then(LangManager.getMessage(p, "CMD.Build.Enabled", "&a&lBuilemode Enabled")).send(p);
                    }

                }
            } else {

            }
        }
        return false;
    }

}
