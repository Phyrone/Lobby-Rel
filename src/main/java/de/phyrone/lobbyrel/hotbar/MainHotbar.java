package de.phyrone.lobbyrel.hotbar;

import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api.HotbarManager;
import de.phyrone.lobbyrel.hotbar.api.HotbarWrapper;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class MainHotbar implements HotbarWrapper {
    private static HashMap<Integer, HotbarItem> items = new HashMap<>();

    public static void setup() {
        items.clear();
        if (Config.getBoolean("Items.Navigator.Enabled", true)) {
            addItem(Config.getInt("Items.Navigator.Slot", 0), new HotbarItem(player -> ItemsConfig.getInstance().getItem("Hotbar.Navigator", new ItemBuilder(Material.COMPASS).displayname("&l&6Navigator").build()).getAsItemStack(player)).setClick((event, rightClick) -> {
                if (rightClick) NavigatorManager.openNavigator(event.getPlayer());
            }));
        }
        if (Config.getBoolean("Items.PlayerHider.Enabled", true)) {
            addItem(Config.getInt("Items.PlayerHider.Slot", 1), new HotbarItem(player -> ItemsConfig.getInstance().getItem("PlayerHider", new ItemBuilder(Material.BLAZE_ROD).displayname("&6PlayerHider").build()).getAsItemStack(player)));
        }
        if (Config.getBoolean("Items.Settings.Enabled", true)) {

            addItem(Config.getInt("Items.Settings.Slot", 8),
                    new HotbarItem(player -> ItemsConfig.getInstance().getItem("Settings", new LobbyItem(Material.SKULL_ITEM).setDisplayName("&6Settings").setPlayerHead(true).setSkin("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzZGNmYjRkYTEwMTc3MjY0OTY4YjQ0OWU3MjRhZGViZWUzYmMzM2I3MmJhZTg1ODQyYjRhYWI5YmQ5YzRkYiJ9fX0=")).getAsItemStack(player)));
        }
        if (Config.getBoolean("Items.Switcher.Enabled", true)) {
            addItem(Config.getInt("Items.Switcher.Slot", 7), new HotbarItem(player -> ItemsConfig.getInstance().getItem("LobbySwitcher", new ItemBuilder(Material.WATCH).displayname("&6Lobbys").build()).getAsItemStack(player)));
        }
    }

    public static void open(Player player) {
        HotbarManager.setHotbar(player, new MainHotbar());
    }

    public static void addItem(int slot, HotbarItem item) {
        items.put(slot, item);
    }


    @Nonnull
    @Override
    public HashMap<Integer, HotbarItem> getItems(@Nullable Player player) {
        return items;
    }
}
