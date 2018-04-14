package de.phyrone.lobbyrel.hotbar;

import de.phyrone.lobbyrel.config.WarpConf;
import de.phyrone.lobbyrel.hotbar.api.Hotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.Sounds;
import de.phyrone.lobbyrel.player.effect.EffectPlayer;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NavigatorHobar {
	Hotbar navHotbar = new Hotbar();
	public void open(Player player) {
		int i = 0;
        navHotbar.setOpenListner(player12 -> new EffectPlayer(player12).playSound(Sounds.CLICK, 2, 1));
        navHotbar.setCloseListner(player1 -> new EffectPlayer(player1).playSound(Sounds.CLICK, 1, 0));
		for(String warpname: WarpManager.getWarps().keySet()) {
			try {
				Warp warp = WarpManager.getWarps().get(warpname);
				final String w2 = warpname;
                navHotbar.setItem(i, new HotbarItem(warp.getItemStack(player)).setClick((event, rightClick) -> {
                    Teleporter.teleport(player, w2);
                    MainHotbar.open(event.getPlayer());
				}));
			}catch (Exception e) {
				System.out.println("Error: Irtems: "+warpname);
				e.printStackTrace();
			}i++;
		}
        navHotbar.setStaticItem(0, new HotbarItem(new ItemBuilder(Material.SLIME_BALL).displayname("&7Back").build()).setClick((event, rightClick) -> {
            if (rightClick) MainHotbar.open(event.getPlayer());

		}));
        navHotbar.setStaticItem(1, new HotbarItem(WarpConf.getInstance().Spawn.getItemStack(player)).setClick((event, rightClick) -> {
            Teleporter.toSpawn(event.getPlayer());
            MainHotbar.open(event.getPlayer());
		}));
		navHotbar.setStaticItem(8, new HotbarItem(new ItemStack(Material.AIR)));
		try {
			navHotbar.open(player, 0);
		}catch (Exception e) {
			System.out.println("Error: Open");
			e.printStackTrace();
		}
	}
}
