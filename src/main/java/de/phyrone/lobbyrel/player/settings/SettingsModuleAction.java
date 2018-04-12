package de.phyrone.lobbyrel.player.settings;

import fr.minuskube.inv.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public interface SettingsModuleAction {
	ArrayList<ClickableItem> getOptions(Player player);
	ItemStack getIcon(Player player);

	ItemStack getCurrent(Player player);

}
