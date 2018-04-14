package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class NineUpListner implements Listener {
    @EventHandler
    public void noSwitch(PlayerSwapHandItemsEvent e) {
        if (!PlayerManager.getPlayerData(e.getPlayer()).isBuilder())
            e.setCancelled(true);
    }
}
