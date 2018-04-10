package de.phyrone.lobbyrel.player.settings;

import fr.minuskube.inv.ClickableItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class SettingsModule {
	ArrayList<ClickableItem> options = new ArrayList<>();
	ItemStack icon;
	SettingsModuleAction action = new SettingsModuleAction() {
		
		@Override
		public ArrayList<ClickableItem> getOptions(Player player) {
			return null;
		}
		
		@Override
		public ItemStack getIcon(Player player) {
			return null;
		}
		
		@Override
		public ItemStack getCurrend(Player player) {
			return null;
		}
	};
	public SettingsModule(ItemStack item) {
		this.icon = item;
	}
	public ArrayList<ClickableItem> getOptions(Player player) {
		if(player != null) {
			ArrayList<ClickableItem> x = action.getOptions(player);
			if(x != null) {
				return x;
			}
		}
		return options;
	}
	public SettingsModule setOptions(ArrayList<ClickableItem> options) {
		this.options = options;
		return this;
	}
	public ItemStack getIcon(Player player) {
		if(player != null) {
			ItemStack x = action.getIcon(player);
			if(x != null) {
				return x;
			}
		}
		return icon;
	}public ItemStack getCurrend(Player player) {
		if(player != null) {
			ItemStack x = action.getCurrend(player);
			if(x != null) {
				return x;
			}
		}
		return icon;
	}
	public SettingsModuleAction getAction() {
		return action;
	}
	public SettingsModule setAction(SettingsModuleAction action) {
		this.action = action;
		return this;
	}


}
