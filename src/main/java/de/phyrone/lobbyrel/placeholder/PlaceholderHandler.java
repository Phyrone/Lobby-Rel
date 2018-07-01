package de.phyrone.lobbyrel.placeholder;
/*
 *   Copyright © 2018 by Phyrone  *
 *   Creation: 30.06.2018 by Phyrone
 */

import org.bukkit.entity.Player;

public interface PlaceholderHandler {
    String onCall(Player player, String[] args);
}
