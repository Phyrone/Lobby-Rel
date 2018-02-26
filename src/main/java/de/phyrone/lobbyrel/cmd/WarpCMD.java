package de.phyrone.lobbyrel.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.WarpManager;

public class WarpCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player ) {			
			Player p = (Player)sender;
			switch (cmd.getName().toLowerCase()) {
			case "setwarp":
				if(p.hasPermission("lobby.admin.setwarp")) {
					if(args.length == 1) {
						if(Config.getBoolean("Warps.SetOneBlockUp", true)){
							WarpManager.setWarpLocation(args[0], p.getEyeLocation(),true);
						}else {
							WarpManager.setWarpLocation(args[0], p.getLocation(),true);
						}

						p.sendMessage(LobbyPlugin.getPrefix() + LangManager.getMessage(p, "CMD.Warp.Set", " &cwarp was set"));
					}else {
						p.sendMessage(LobbyPlugin.getPrefix() + " §6/setwarp <Displayname>");
					}
				}else {
					p.sendMessage(LangManager.noPerm(p));
				}

				break;
			case "setspawn":
				if(p.hasPermission("lobby.admin.setspawn")) {
					if(args.length == 0) {
						if(Config.getBoolean("Warps.SetOneBlockUp", true)){
							WarpManager.setSpawnLocation(p.getEyeLocation(),true);
						}else {
							WarpManager.setSpawnLocation( p.getLocation(),true);
						}
					}p.sendMessage(LobbyPlugin.getPrefix()+LangManager.getMessage(p,"CMD.Warp.SpawnSet", " &cspawn was set"));
				}else {
					p.sendMessage(LangManager.noPerm(p));
				}
				break;
			case "spawn":
				Teleporter.toSpawn(p);
				break;
			default:
				
				break;
			}
		}else {
			sender.sendMessage("Player only!");
		}
		return false;
	}
	

}
