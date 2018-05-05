package de.phyrone.lobbyrel.hotbar.api2.util;

import de.phyrone.lobbyrel.hotbar.api2.HotbarManager;
import de.phyrone.lobbyrel.hotbar.api2.HotbarWrapper;
import org.bukkit.entity.Player;

public abstract class EmptyHotbar implements HotbarWrapper {
    public void open(Player player) {
        HotbarManager.setHotbar(player, this);
    }
}
