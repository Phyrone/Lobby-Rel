package de.phyrone.lobbyrel.hotbar.api2.util;

import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api2.HotbarWrapper;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class PreparedHotbar extends EmptyHotbar {
    private HashMap<Integer, HotbarItem> prepraredItems;

    public PreparedHotbar(@Nonnull HotbarWrapper hotbarToPrepare) {
        Validate.notNull(hotbarToPrepare);
        prepraredItems = hotbarToPrepare.getItems(null);
        Validate.notNull(prepraredItems);
    }

    @Override
    public HashMap<Integer, HotbarItem> getItems(@Nullable Player player) {
        return prepraredItems;
    }
}
