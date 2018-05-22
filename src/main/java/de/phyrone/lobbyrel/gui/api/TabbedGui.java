package de.phyrone.lobbyrel.gui.api;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabbedGui {
    List<TabWrapper> tabs = new ArrayList<>();

    public interface TabWrapper extends InventoryProvider{
        ItemStack getTabItem(Player player);
    }

    public class Tab implements TabWrapper {
        ItemStack tabitem;
        HashMap<Integer, ClickableItem> items = new HashMap<>();

        public Tab(ItemStack tabitem) {
            this.tabitem = tabitem;
        }

        @Override
        public ItemStack getTabItem(Player player) {
            return tabitem;
        }

        @Override
        public void init(Player player, InventoryContents inventoryContents) {

        }

        @Override
        public void update(Player player, InventoryContents inventoryContents) {

        }
    }public abstract class DynamicTab implements TabWrapper {

        @Override
        public ItemStack getTabItem(Player player) {
            return null;
        }

        @Override
        public void init(Player player, InventoryContents inventoryContents) {

        }
    }

    public void getTab(int index){

    }
}

class PlayerTabbedGui implements InventoryProvider {
    private Player player;
    TabbedGui gui;

    public PlayerTabbedGui(Player player,TabbedGui gui)
    {
        this.gui = gui;
        this.player = player;
        SmartInventory.builder().id("TABBED-"+player.getUniqueId().toString()).size(6,9);
    }

    @Override
    public void init(Player p, InventoryContents con) {
        con.setProperty("TAB",0);
    }

    @Override
    public void update(Player p, InventoryContents con) {

    }
}
