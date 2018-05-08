package de.phyrone.lobbyrel.hotbar.api.util;

import org.bukkit.entity.Player;

public interface HotbarActions {
    void onOpen(Player player);

    void onClose(Player player);

    void onPageChange();
}
