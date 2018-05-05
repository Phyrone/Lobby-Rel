package de.phyrone.lobbyrel.hotbar.api2.util;

import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public abstract class StaticItemsHotbarWrapper extends EmptyHotbar {
    public HashMap<Integer, HotbarItem> staticItems = new HashMap<>();

    @Override
    public HashMap<Integer, HotbarItem> getItems(Player player) {
        HashMap<Integer, HotbarItem> ret = new HashMap<>();
        HashMap<Integer, HotbarItem> dynItems = getDynamicItems(player);
        int max = getMaxSlot(dynItems) < 9 ? 9 : getMaxSlot(dynItems);
        /* Dynamic Items Slot */
        int totlalslot = 0;
        /* Switched Slot */
        int currend = 0;
        while (totlalslot < max) {
            /* Run a Hotbar */
            for (int slot = 0; slot < 9; slot++) {
                if (staticItems.containsKey(slot)) {
                    ret.put(currend, staticItems.get(slot));
                } else {
                    if (dynItems.containsKey(totlalslot))
                        ret.put(currend, dynItems.get(totlalslot));
                    totlalslot++;
                }
                currend++;
            }
        }
        return ret;
    }

    public void setStaticItem(int slot, HotbarItem item) {
        if (slot < 0 || slot > 8) {
            System.out.println("Lobby-Rel[Debug] Error while set static item (wrong slot)(slot: " + String.valueOf(slot) + ")");
            return;
        }
        staticItems.put(slot, item);
    }

    public HashMap<Integer, HotbarItem> getStaticItems() {
        return staticItems;
    }

    public abstract HashMap<Integer, HotbarItem> getDynamicItems(Player player);

    public int getMaxSlot(HashMap<Integer, HotbarItem> items) {
        if (items.isEmpty())
            return 0;
        return Collections.max(items.keySet(), Comparator.comparingInt(i -> i));
    }
}
