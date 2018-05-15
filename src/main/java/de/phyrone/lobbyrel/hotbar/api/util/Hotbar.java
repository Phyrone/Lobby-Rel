package de.phyrone.lobbyrel.hotbar.api.util;

import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api.HotbarManager;
import de.phyrone.lobbyrel.hotbar.api.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
public class Hotbar extends StaticItemsHotbarWrapper {
    final HashMap<Integer, HotbarItem> items = new HashMap<>();

    public static ItemStack setMeta(ItemStack item) {
        return item;

    }

    public final void open(Player player, int site) {
        HotbarManager.setHotbar(player, this);

    }
    public void setItem(int slot, HotbarItem item) {
        items.put(slot, item);
    }


    public int getSite(Player player) {
        return HotbarManager.getHotbar(player).getCurrendPage();
    }

    public void nextSite(Player player) {
        HotbarManager.getHotbar(player).changePage(PlayerHotbar.PageDirection.FORWARD);
    }

    public void prevSite(Player player) {
        HotbarManager.getHotbar(player).changePage(PlayerHotbar.PageDirection.BACKWARD);
    }


    public HashMap<Integer, HotbarItem> getItems() {
        return items;
    }


    @Override
    public HashMap<Integer, HotbarItem> getDynamicItems(Player player) {
        return items;
    }
}
