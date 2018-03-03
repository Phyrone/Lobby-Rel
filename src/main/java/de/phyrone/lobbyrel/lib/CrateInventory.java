package de.phyrone.lobbyrel.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Zombie_Striker.
 * https://bukkit.org/threads/csgo-crates-create-inventorys
 * -the-cycle-through-items.431949
 */

public class CrateInventory {

    private Inventory inv;
    private Plugin plugin;
    private ItemStack[] contents;

    private InventoryClickHandler helper;

    private int itemIndex = 0;

    public CrateInventory(int size, ItemStack[] contents, String name,
                          Plugin main) {
        inv = Bukkit.createInventory(null, size, name);
        this.plugin = main;
        this.contents = contents;
        helper = new InventoryClickHandler(main, name);
        shuffle();
    }

    /**
     * Shuffles the existing inventory.
     * <p>
     * Note that this does not shuffle the order of the items. This only shuffles the starting index.
     */
    public void shuffle() {
        int startingIndex = ThreadLocalRandom.current().nextInt(
                this.contents.length);
        for (int index = 0; index < startingIndex; index++) {
            for (int itemstacks = 0; itemstacks < inv.getSize(); itemstacks++) {
                inv.setItem(itemstacks, contents[(itemstacks + itemIndex)
                        % contents.length]);
            }
            itemIndex++;
        }
    }

    /**
     * Returns the inventory click manager. There should be no reason why you
     * need it unless you want to unregister the class (meaning players can pick
     * up items)
     *
     * @return
     */
    public InventoryClickHandler getInventoryClickManager() {
        return helper;
    }

    /**
     * Returns the contents of the crate
     *
     * @return The contents of the crate
     */
    public ItemStack[] getContents() {
        return this.contents;
    }

    /**
     * Sets the contents for the crate.
     *
     * @param contents
     */
    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    /**
     * Returns the inventory instance
     *
     * @return The inventory instance.
     */
    public Inventory getInventory() {
        return inv;
    }

    /**
     * This spins the inventory. Call this to play the animation and give a
     * random item.
     *
     * @param seconds  the amount of second you want this to spin.
     * @param reciever player who should recieve the item.
     */
    public void spin(final double seconds, final Player reciever) {
        reciever.openInventory(this.inv);
        new BukkitRunnable() {
            double delay = 0;
            int ticks = 0;
            boolean done = false;

            public void run() {
                if (done)
                    return;
                ticks++;
                delay += 1 / (20 * seconds);
                if (ticks > delay * 10) {
                    ticks = 0;

                    for (int itemstacks = 0; itemstacks < inv.getSize(); itemstacks++)
                        inv.setItem(itemstacks,
                                contents[(itemstacks + itemIndex)
                                        % contents.length]);

                    itemIndex++;

                    if (delay >= 1) {
                        done = true;
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                reciever.getInventory().addItem(
                                        contents[(4 + itemIndex - 1)
                                                % contents.length]);
                                reciever.closeInventory();
                                cancel();
                            }
                        }.runTaskLater(plugin, 50);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}

class InventoryClickHandler implements Listener {
    private String name = null;

    public InventoryClickHandler(Plugin plugin, String inventoryName) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.name = inventoryName;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals(name)) {
            e.setCancelled(true);
        }
    }

    public void changeName(String name) {
        this.name = name;
    }
}