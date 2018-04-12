package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.player.settings.SettingsManager;
import de.phyrone.lobbyrel.player.settings.SettingsModule;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.SmartInvsPlugin;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;


public class SettingsGUI implements InventoryProvider {
    public static void open(Player player, int site) {
        SmartInventory.builder()
                .closeable(true)
                .type(InventoryType.CHEST)
                .id("settings-" + player.getUniqueId().toString())
                .title(LangManager.getMessage(player, "GUI.Settings.Title", "&8Settings"))
                .provider(new SettingsGUI())
                .listener(new InventoryListener<>(InventoryClickEvent.class,
                        inventoryClickEvent -> SmartInvsPlugin.manager().getContents((Player) inventoryClickEvent.getWhoClicked())))
                .size(5, 9)
                .build().open(player, site);
    }

    @Override
    public void init(Player player, InventoryContents con) {
        int site = con.pagination().getPage();
        int additor = site * 5;
        List<SettingsModule> modules = SettingsManager.getModulesAsList();
        if (site > 0) {
            con.set(3, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).build(), e -> {
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> open(player, site - 1));
            }));
        }
        if ((additor - modules.size()) > 5) {
            con.set(5, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).build(), e -> {
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> open(player, site + 1));
            }));
        }
        for (int i = 0; i < 5; i++) {
            SettingsModule module;
            try {
                module = modules.get(additor + i);
            } catch (Exception e) {
                module = new SettingsModule(new ItemStack(Material.AIR));
            }
            con.set(i, 0, ClickableItem.empty(module.getIcon(player)));
            con.set(i, 1, ClickableItem.empty(module.getCurrent(player)));
            int size = module.getOptions(player).size();
            if (size > 6) {

            } else {
                int i2 = 0;
                for (ClickableItem opt : module.getOptions(player)) {
                    con.set(i, 8 - size + 1 + i2, opt);
                    i2++;
                }
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents con) {
        int tick = con.property("tick", 0);
        if (tick > 10) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    int additor = con.pagination().getPage() * 5;
                    List<SettingsModule> modules = SettingsManager.getModulesAsList();
                    for (int i = 0; i < 5; i++) {
                        SettingsModule module;
                        try {
                            module = modules.get(additor + i);
                        } catch (Exception e) {
                            module = new SettingsModule(new ItemStack(Material.AIR));
                        }
                        con.set(i, 1, ClickableItem.empty(module.getCurrent(player)));
                    }
                }
            }.runTaskAsynchronously(LobbyPlugin.getInstance());
            con.setProperty("tick", 0);
        } else
            con.setProperty("tick", tick++);

    }

}
