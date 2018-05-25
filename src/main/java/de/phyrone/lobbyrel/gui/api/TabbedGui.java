package de.phyrone.lobbyrel.gui.api;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class TabbedGui {
    List<Tab> tabs = new ArrayList<>();

    public TabbedGui(Tab... tabs) {
        this.tabs.addAll(Arrays.asList(tabs));
    }

    public List<Tab> getTabs() {
        return tabs;
    }

    public Tab[] getTabsAsArray() {
        return (Tab[]) tabs.toArray();
    }

    class Tab {
        private ItemStack tabItem;

        public Tab(ItemStack tabItem) {
            this.tabItem = tabItem;
        }
    }

}
