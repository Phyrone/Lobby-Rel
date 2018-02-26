package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.entity.Player;

public interface HotbarAction {
	void onOpen(Player player);
	void onClose(Player player);
	void onSwitchSite(Player player,int from,int to);
}
