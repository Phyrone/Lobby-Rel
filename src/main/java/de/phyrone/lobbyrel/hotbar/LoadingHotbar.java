package de.phyrone.lobbyrel.hotbar;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hotbar.api.Hotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api.HotbarItemAction;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadingHotbar {
	Player p;
	static Hotbar hb;
	static boolean enabled;
	static int time;
	static boolean fullHotbar;
    static LobbyItem item;
	public static void init() {
		enabled = Config.getBoolean("LoadingHotbar.Enabled", true);
		time = Config.getInt("LoadingHotbar.LoadingTime", 5);
		fullHotbar = Config.getBoolean("LoadingHotbar.FullHotbar", true);
        item = ItemsConfig.getInstance().getItem("LoadingItem", new LobbyItem().setMaterial(Material.STAINED_GLASS_PANE)
                .setData((byte) 14).setDisplayName("&4&lLoading"), true);
		hb = new Hotbar();
			for(int i = 0; 9 > i;i++) {
				final int slot = i;
				hb.setStaticItem(i, new HotbarItem(new ItemStack(Material.AIR)).setAction(new HotbarItemAction() {
					
					@Override
					public ItemStack onSelect(Player player) {
						String message = LangManager.getMessage(player, "Hotbar.Loading.ItemName", "&c&lLoading");
						if(fullHotbar) {
                            return item.getAsItemStack(player);
						}else {
							ItemStack mainItem = MainHotbar.getHotbar().getItem(slot, 0).getItem(player);
							if(!(mainItem.getType() == Material.AIR)) {
                                return item.getAsItemStack(player);
							}else return null;
						}
					}
					
					@Override
					public void onClick(PlayerInteractEvent event, Boolean rightClick) {
					}
				}));
			}

	}
	public LoadingHotbar(Player player) {
		this.p = player;
		
	}
	public void open() {
		try {
			if(enabled) {
				hb.open(p, 0);
				new BukkitRunnable() {
					
					@Override
					public void run() {
						MainHotbar.open(p);
					}
				}.runTaskLaterAsynchronously(LobbyPlugin.getInstance(), time);
			}else
				MainHotbar.open(p);
		}catch (Exception e) {
			System.out.println("[Lobby-Rel] Error: LoadingAnimationHotbar");
			e.printStackTrace();
		}

		
	}

}
