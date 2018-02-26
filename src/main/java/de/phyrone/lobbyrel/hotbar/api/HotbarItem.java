package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarItem {
	ItemStack item = new ItemStack(Material.AIR);
	HotbarItemAction action = null;
	public HotbarItem setItem(ItemStack item) {
		this.item = item;
		return this;
	}
	public HotbarItemAction getAction() {
		return action;
	}
	public HotbarItem setAction(HotbarItemAction action) {
		this.action = action;
		return this;
	}
	public HotbarItem(ItemStack item) {
		if(item == null) {
			this.item = new ItemStack(Material.AIR);
		}else {
			this.item = item;
		}
	}
	public ItemStack getItem(Player player) {
		ItemStack ret;
		if(action == null) {
			ret = item;
		}else {
			ItemStack actiont = action.onSelect(player);
			if(actiont == null) {
				ret = item;
			}else{
				ret = actiont;
			}
		}return Hotbar.setMeta(ret);
	}
	public ItemStack getItem() {
		return getItem(null);
	}public void click(PlayerInteractEvent event) {
		Boolean lc = event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
		if(action != null)action.onClick(event,lc );
	}

}
