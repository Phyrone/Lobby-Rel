package de.phyrone.lobbyrel.hotbar.api;

import de.phyrone.lobbyrel.hotbar.api.util.Hotbar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HotbarItem {
    private ItemStack item = new ItemStack(Material.AIR);
    private HotbarItemAction.Update update = null;
    private HotbarItemAction.Interact interact = null;
    private HotbarItemAction.Click click = null;

    @Deprecated
    public HotbarItem(ItemStack item) {
        if (item == null) {
            this.item = new ItemStack(Material.AIR);
        } else {
            this.item = item;
        }
    }

    public HotbarItem(HotbarItemAction.Update update) {
        this.update = update;
    }

    public HotbarItem setItemUpdater(HotbarItemAction.Update select) {
        this.update = select;
        return this;
    }

    public HotbarItemAction.Click getClick() {
        return click;
    }

    public HotbarItem setClick(HotbarItemAction.Click click) {
        this.click = click;
        return this;
    }

    public HotbarItemAction.Interact getInteract() {
        return interact;
    }

    public HotbarItem setInteract(HotbarItemAction.Interact interact) {
        this.interact = interact;
        return this;
    }

    public ItemStack getItem(Player player) {
        ItemStack ret;
        if (update == null) {
            ret = item;
        } else {
            ItemStack actiont = update.onGetItem(player);
            if (actiont == null) {
                ret = item;
            } else {
                ret = actiont;
            }
        }
        return Hotbar.setMeta(ret);
    }

    public ItemStack getItem() {
        return getItem(null);
    }

    public HotbarItem setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    void interact(Player player, PlayerHotbar.Dispatch dispatch) {
        try {
            if (interact != null) interact.onInteract(player, dispatch);
            switch (dispatch.getType()) {
                case CLICK:
                case HIT:
                    if (click != null) click.onClick(player, dispatch.getType() == PlayerHotbar.DispachType.CLICK);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
