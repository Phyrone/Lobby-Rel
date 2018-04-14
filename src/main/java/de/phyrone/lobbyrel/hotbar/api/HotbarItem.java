package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarItem {
    ItemStack item;
    HotbarItemAction.Select select = null;
    HotbarItemAction.Click click = null;
	public HotbarItem setItem(ItemStack item) {
		this.item = item;
		return this;
	}

    public HotbarItem setClick(HotbarItemAction.Click click) {
        this.click = click;
		return this;
	}

    public HotbarItem setSelect(HotbarItemAction.Select select) {
        this.select = select;
        return this;
    }

    public HotbarItemAction.Click getClick() {
        return click;
    }

    public HotbarItemAction.Select getSelect() {
        return select;
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
        if (select == null) {
			ret = item;
		}else {
            ItemStack actiont = select.onSelect(player);
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
        if (click != null) click.onClick(event, lc);
	}

}
