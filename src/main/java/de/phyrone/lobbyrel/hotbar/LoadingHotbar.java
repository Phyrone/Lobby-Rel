package de.phyrone.lobbyrel.hotbar;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api2.util.PreparedHotbar;
import de.phyrone.lobbyrel.lib.LobbyItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LoadingHotbar {
    static PreparedHotbar hb;
    static boolean enabled;
    static int time;
    static boolean fullHotbar;
    static LobbyItem item;
    Player p;

    public LoadingHotbar(Player player) {
        this.p = player;

    }

    public static void init() {
        enabled = Config.getBoolean("LoadingHotbar.Enabled", true);
        time = Config.getInt("LoadingHotbar.LoadingTime", 5);
        item = ItemsConfig.getInstance().getItem("LoadingItem", new LobbyItem().setMaterial(Material.STAINED_GLASS_PANE)
                .setData((byte) 14).setDisplayName("&4&lLoading"), true);
        hb = new PreparedHotbar(nu -> {
            HashMap<Integer, HotbarItem> items = new HashMap<>();
            for (int i = 0; 9 > i; i++) {
                items.put(i, new HotbarItem(new ItemStack(Material.BARRIER)).setItemUpdater(player ->
                        item.getAsItemStack(player)));
            }
            return items;
        });

    }

    public void open() {
        try {
            if (enabled) {
                hb.open(p);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        MainHotbar.open(p);
                    }
                }.runTaskLaterAsynchronously(LobbyPlugin.getInstance(), time);
            } else
                MainHotbar.open(p);
        } catch (Exception e) {
            System.out.println("[Lobby-Rel] Error: LoadingAnimationHotbar");
            e.printStackTrace();
        }


    }

}
