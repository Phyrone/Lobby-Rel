package de.phyrone.lobbyrel.player.settings;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.minuskube.inv.ClickableItem;

public interface SettingsModuleAction {
	ArrayList<ClickableItem> getOptions(Player player);
	ItemStack getIcon(Player player);
	ItemStack getCurrend(Player player);

}
