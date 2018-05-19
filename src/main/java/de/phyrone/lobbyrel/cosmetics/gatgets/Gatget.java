package de.phyrone.lobbyrel.cosmetics.gatgets;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.cosmetics.Cosmetic;
import de.phyrone.lobbyrel.lib.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Gatget extends Cosmetic {

    String defaultDisplayname = "a Gadget";
    Action action = player -> 0;
    private ItemGetter getter = player ->
            ItemsConfig.getLobbyItem(
                    "Gadget." + name,
                    new LobbyItem(Material.BARRIER).setDisplayName("&6" + name)
            ).getAsItemStack(player);
    public ItemStack getItem(Player player) {
        return getter.onGet(player);
    }

    interface Action {
        /**
         * @param player who uses the Gadget
         * @return the Cooldown of the Gatget
         */
        int onClick(Player player);
    }

    interface ItemGetter {
        /**
         * @param player the viewer (Permitted)
         * @return Dynamic GadgetItem
         */
        ItemStack onGet(Player player);
    }
}
