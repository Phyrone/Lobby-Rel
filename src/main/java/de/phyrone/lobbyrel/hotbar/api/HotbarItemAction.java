package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarItemAction {
    public interface Select {
        ItemStack onSelect(Player player);
    }

    public interface Click {
        void onClick(PlayerInteractEvent event, Boolean rightClick);
    }

}
