package de.phyrone.lobbyrel.gui.api;

import de.phyrone.lobbyrel.lib.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class AnvilInputInterface implements InputGUI.InputInterface {
    public HashMap<Player, Integer> xps = new HashMap<>();

    public AnvilInputInterface() {
    }

    @Override
    public void build(InputGUI.InputHandler inputHandler, Player player) {
        xps.put(player, player.getTotalExperience());
        player.setLevel(1000);
        SmartInventory.builder().id("InPutGUI-" + player.getUniqueId().toString())
                .size(0, 3).provider(new InvListner(inputHandler)).type(InventoryType.ANVIL)
                .listener(new InventoryListener<>(InventoryCloseEvent.class, inventoryCloseEvent -> {
                    Player closer = ((Player) inventoryCloseEvent.getPlayer());
                    closer.setTotalExperience(xps.getOrDefault(closer, 0));
                })).listener(new InventoryListener<>(InventoryClickEvent.class, inventoryClickEvent -> {
            if (inventoryClickEvent.getRawSlot() == 2 && inventoryClickEvent.isLeftClick()) {
                inputHandler.onAccept((Player) inventoryClickEvent.getWhoClicked(),
                        inventoryClickEvent.getClickedInventory().getItem(2).getItemMeta().getDisplayName());
                ((Player) inventoryClickEvent).closeInventory();
            }
            inventoryClickEvent.setCancelled(true);

        })).build().open(player);
    }
}

class InvListner implements InventoryProvider {
    InputGUI.InputHandler handler;

    public InvListner(InputGUI.InputHandler inputHandler) {
        this.handler = inputHandler;
    }

    @Override
    public void init(Player player, InventoryContents inv) {
        setDenyItem(player, inv);
        System.out.println("INIT ANVIL");
        inv.set(0, 1, ClickableItem.of(new ItemBuilder(Material.BARRIER).displayname("&4Reset").build(), inventoryClickEvent -> {
            setDenyItem(player, inv);
        }));
    }

    @Override
    public void update(Player player, InventoryContents inv) {
        System.out.println("UPDATE ANVIL");
    }

    public void setDenyItem(Player player, InventoryContents inv) {
        ItemStack item = new ItemBuilder(Material.PAPER).displayname(handler.onBuild()).build();
        inv.set(0, 0, ClickableItem.of(item, inventoryClickEvent -> {
            inv.inventory().close((Player) inventoryClickEvent.getWhoClicked());
            handler.onDeny((Player) inventoryClickEvent.getWhoClicked());
        }));
        player.getOpenInventory().setItem(0, item);

    }
}
