package de.phyrone.lobbyrel.hotbar;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.WarpConf;
import de.phyrone.lobbyrel.hotbar.api.Hotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarAction;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api.HotbarItemAction;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.player.effect.EffectPlayer;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;

public class NavigatorHobar {
	Hotbar navHotbar = new Hotbar();
	public void open(Player player) {
		int i = 0;
		navHotbar.setAction(new HotbarAction() {
			
			@Override
			public void onSwitchSite(Player player, int from, int to) {
				
			}
			
			@Override
			public void onOpen(Player player) {
				new EffectPlayer(player).playSound(Sound.CLICK, 2, 1);
				
			}
			
			@Override
			public void onClose(Player player) {
				new EffectPlayer(player).playSound(Sound.CLICK, 1, 0);
			}
		});
		for(String warpname: WarpManager.getWarps().keySet()) {
			try {
				Warp warp = WarpManager.getWarps().get(warpname);
				final String w2 = warpname;
				navHotbar.setItem(i, new HotbarItem(warp.getItemStack(player)).setAction(new HotbarItemAction() {
					
					@Override
					public ItemStack onSelect(Player player) {
						return null;
					}
					
					@Override
					public void onClick(PlayerInteractEvent event, Boolean rightClick) {
						Teleporter.teleport(player, w2);
						MainHotbar.open(event.getPlayer());
					}
				}));
			}catch (Exception e) {
				System.out.println("Error: Irtems: "+warpname);
				e.printStackTrace();
			}i++;
		}
		navHotbar.setStaticItem(0, new HotbarItem(new ItemBuilder(Material.SLIME_BALL).displayname("&7Back").build()).setAction(new HotbarItemAction() {
			
			@Override
			public ItemStack onSelect(Player player) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void onClick(PlayerInteractEvent event, Boolean rightClick) {
				if(rightClick)MainHotbar.open(event.getPlayer());
				
			}
		}));
		navHotbar.setStaticItem(1, new HotbarItem(WarpConf.getInstance().Spawn.getItemStack(player)).setAction(new HotbarItemAction() {
			
			@Override
			public ItemStack onSelect(Player player) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void onClick(PlayerInteractEvent event, Boolean rightClick) {
				Teleporter.toSpawn(event.getPlayer());
				MainHotbar.open(event.getPlayer());
			}
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
