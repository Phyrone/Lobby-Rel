package de.phyrone.lobbyrel.lib;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum Color {
	WHITE(ChatColor.WHITE,DyeColor.WHITE),
	BLACK(ChatColor.BLACK,DyeColor.BLACK),
	GREEN(ChatColor.GREEN, DyeColor.GREEN),
	RED(ChatColor.RED,DyeColor.RED),
	ORANGE(ChatColor.GOLD,DyeColor.ORANGE),
	GREY(ChatColor.GRAY,DyeColor.GRAY);
	
	private ChatColor chat;
	private DyeColor dye;
	
	Color(ChatColor chat, DyeColor dye) {
		this.chat = chat;
		this.dye = dye;
	}
	public DyeColor getDye(){
		return dye;
	}public ChatColor getChatColor() {
		return chat;
	}
	

}
	