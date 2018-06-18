package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.gui.customitems.AdminCustomItemsGUI;
import de.phyrone.lobbyrel.gui.warp.AdminWarpsGUI;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;

public class AdminMainGui implements InventoryProvider {
    static ArrayList<ClickableItem> items = new ArrayList<>();

    public static void open(Player player) {
        SmartInventory.builder().title("§cEinstallungen")
                .id("settings_main-" + player.getUniqueId().toString())
                .closeable(true)
                .provider(new AdminMainGui())
                .type(InventoryType.HOPPER)
                .manager(LobbyPlugin.getInstance().getInventoryManager())
                .build().open(player);
    }

    @Override
    public void init(Player p, InventoryContents con) {
        //con.fill(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
        con.set(0, 0, ClickableItem.of(new ItemBuilder(Material.SUGAR).displayname("&6Warps").build(), e -> {
            AdminWarpsGUI.open((Player) e.getWhoClicked());
        }));
        con.set(0, 2, ClickableItem.of(new ItemBuilder(Material.ENCHANTMENT_TABLE).displayname("&7CustomItems").build(), e -> {
            AdminCustomItemsGUI.open((Player) e.getWhoClicked(), 0);
        }));
    }

    @Override
    public void update(Player p, InventoryContents con) {

    }

}
