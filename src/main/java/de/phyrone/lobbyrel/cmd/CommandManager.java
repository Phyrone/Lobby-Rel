package de.phyrone.lobbyrel.cmd;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.Player;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.gui.AdminMainGui;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.data.lang.LangManager;

public class CommandManager {
	static public ArrayList<CommandAction> Commands = new ArrayList<>();
	
	public static Boolean runCommand(Player p, String[] args) {
		for(CommandAction cmd:Commands) {
			if(cmd.onCommand(p, args))return true;
		}return false;
		
	}public static void init() {
		Commands.clear();
		
		Commands.add(new CommandAction() {
			
			@Override
			public ArrayList<String> onTab(Player player, String[] args) {
				if(args.length == 1) {
					return new ArrayList<>(Arrays.asList("admingui","help","reload","update","save"));
				}
				return null;
			}
			
			@Override
			public boolean onCommand(Player player, String[] args) {
				if(args[0].equalsIgnoreCase("admingui")) {
					if(player.hasPermission("lobby.admin")) AdminMainGui.open(player);
					else new FancyMessage(LobbyPlugin.getPrefix()).then(LangManager.getMessage(player,"NoPermision", "&cder Computer sagt Nein!")).tooltip("§5lobby.admin");
					return true;
				}else if(args[0].equalsIgnoreCase("debug")) {
					if(player.hasPermission("lobby.debug")) {
						
					}
				}
				return false;
			}
		});
	}
	public static ArrayList<String> runTab(Player p, String[] args) {
		ArrayList<String> ret = new ArrayList<String>();
		for(CommandAction cmd : Commands) {
			ArrayList<String> subcomplete = cmd.onTab(p, args);
			if(subcomplete != null) {
				for(String comp : subcomplete) 
					ret.add(comp);
			}
		}
		
		if(!ret.isEmpty()) {
			ArrayList<String> rr = new ArrayList<String>();
			try {
				for(String r : ret) if(r.toLowerCase().startsWith(args[args.length-1].toLowerCase()))
					rr.add(r);
			}catch (Exception e) {
				e.printStackTrace();
			}

			
			
			return rr;
		}else {
		return null;
		}
		
	}

}
