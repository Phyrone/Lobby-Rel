package de.phyrone.lobbyrel.hotbar.api;

import de.phyrone.lobbyrel.hotbar.api2.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HotbarItemAction {
    public interface Update {
        ItemStack onGetItem(Player player);
    }
    public interface Click {
        void onClick(Player player, Boolean rightClick);
    }

    public interface Interact {
        void onInteract(Player player, PlayerHotbar.Dispatch dispatch);
    }

}
