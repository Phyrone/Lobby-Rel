package de.phyrone.lobbyrel.cmd;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public interface CommandAction {
	boolean onCommand(Player player,String[] args);
	ArrayList<String> onTab(Player player,String[] args);

}
