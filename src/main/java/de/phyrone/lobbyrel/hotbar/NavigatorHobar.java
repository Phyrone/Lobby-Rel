package de.phyrone.lobbyrel.hotbar;

import de.phyrone.lobbyrel.config.WarpConf;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api2.HotbarManager;
import de.phyrone.lobbyrel.hotbar.api2.PlayerHotbar;
import de.phyrone.lobbyrel.hotbar.api2.util.StaticItemsHotbarWrapper;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.Sounds;
import de.phyrone.lobbyrel.player.effect.EffectPlayer;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class NavigatorHobar extends StaticItemsHotbarWrapper {
    private static NavigatorHobar instance = new NavigatorHobar();

    public NavigatorHobar() {


        setStaticItem(0, new HotbarItem(new ItemBuilder(Material.SLIME_BALL).displayname("&7Back").build()).setClick((player, rightClick) -> {
            if (rightClick) {
                MainHotbar.open(player);
                new EffectPlayer(player).playSound(Sounds.CLICK, 1, 0);
            }

        }));
        setStaticItem(1, new HotbarItem(new ItemStack(Material.AIR)).setClick((player, rightClick) -> {
            Teleporter.toSpawn(player);
            MainHotbar.open(player);
        }).setItemUpdater(player -> WarpConf.getInstance().Spawn.getItemStack(player)));
        setStaticItem(8, new HotbarItem(new ItemStack(Material.AIR)));
    }

    public void open(Player player) {
        PlayerHotbar hotbar = HotbarManager.getHotbar(player);
        new EffectPlayer(player).playSound(Sounds.CLICK, 2, 1);
        try {
            HotbarManager.setHotbar(player, instance);
        } catch (Exception e) {
            System.out.println("Error: Open");
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<Integer, HotbarItem> getDynamicItems(Player un) {
        HashMap<Integer, HotbarItem> items = new HashMap<>();
        int i = 0;
        for (WarpManager.WarpPack warpset : WarpManager.getWarps()) {
            try {
                final String w2 = warpset.getName();
                final Warp warp = warpset.getWarp();
                items.put(i, new HotbarItem(warp.getItemStack(un)).setClick((clicker, rightClick) -> {
                    Teleporter.teleport(clicker, w2);
                    new EffectPlayer(clicker).playSound(Sounds.CLICK, 1, 0);
                    MainHotbar.open(clicker);
                }));
            } catch (Exception e) {
                System.out.println("Error: Items: " + warpset.getName());
                e.printStackTrace();
            }
            i++;
        }
        return items;
    }

}
