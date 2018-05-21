package de.phyrone.lobbyrel.gui.api;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabbedGui {
    List<TabWrapper> tabs = new ArrayList<>();

    public interface TabWrapper {
        ClickableItem getItem(Player player, int slot);

        ItemStack getTabItem(Player player);
    }

    public class Tab {
        ItemStack tabitem;
        HashMap<Integer, ClickableItem> items = new HashMap<>();

        public Tab(ItemStack tabitem) {
            this.tabitem = tabitem;
        }
    }
}

class PlayerTabbedGui implements InventoryProvider {
    private Player player;

    public PlayerTabbedGui(Player player) {
        this.player = player;
    }

    @Override
    public void init(Player p, InventoryContents inventoryContents) {

    }

    @Override
    public void update(Player p, InventoryContents inventoryContents) {

    }
}
