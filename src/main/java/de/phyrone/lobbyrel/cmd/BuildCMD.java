package de.phyrone.lobbyrel.cmd;

import de.phyrone.lobbyrel.config.Config;
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
                        LangManager.sendMessage(p, "CMD.Build.Disabled", "&c&lYou can't build anymore.");
                    } else {
                        pd.setAllowGamemodeChange(true);
                        p.setGameMode(GameMode.CREATIVE);
                        pd.setBuilder(true);
                        pd.setAllowGamemodeChange(false);
                        p.getInventory().clear();
                        p.setMaxHealth(20);
                        p.setHealth(20);
                        LangManager.sendMessage(p, "CMD.Build.Enabled", "&a&lYou can build now.");
                    }

                }
            } else {

            }
        }
        return false;
    }

}
