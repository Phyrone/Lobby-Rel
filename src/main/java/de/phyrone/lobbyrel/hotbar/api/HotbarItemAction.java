package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface HotbarItemAction {
	ItemStack onSelect(Player player);
	void onClick(PlayerInteractEvent event,Boolean rightClick);

}
