package de.phyrone.lobbyrel.hotbar.api.util;

import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public interface HotbarWrapper {
    /**
     * @param player who get a new hotbar (can be NULL)
     * @return get items ,who will set in the hotbar
     */
    @Nonnull
    HashMap<Integer, HotbarItem> getItems(@Nullable Player player);

}
