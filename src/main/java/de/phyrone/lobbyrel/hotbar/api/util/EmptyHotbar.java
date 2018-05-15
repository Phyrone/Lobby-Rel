package de.phyrone.lobbyrel.hotbar.api.util;

import de.phyrone.lobbyrel.hotbar.api.HotbarManager;
import de.phyrone.lobbyrel.hotbar.api.HotbarWrapper;
import org.bukkit.entity.Player;

public abstract class EmptyHotbar implements HotbarWrapper {
    public void open(Player player) {
        HotbarManager.setHotbar(player, this);
    }
}
