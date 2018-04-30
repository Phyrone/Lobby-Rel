package de.phyrone.lobbyrel.cmd;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface CommandAction {
	boolean onCommand(CommandSender sender, String[] args);

	ArrayList<String> onTab(CommandSender sender, String[] args);

}
