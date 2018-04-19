package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.api.Hotbar;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.PlayerInventory;

public class HotbarEvents implements Listener {
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		PlayerData pd = PlayerManager.getPlayerData(e.getPlayer());
		if(pd.isBuilder()) {
			return;
		}
		e.setCancelled(true);
		if(e.getAction() == Action.PHYSICAL){
			return;
		}
		Hotbar hb = pd.getCurrendHotbar();
		PlayerInventory inv = e.getPlayer().getInventory();
		if(hb == null) {
			return;
		}hb.getItem(inv.getHeldItemSlot(), MainHotbar.getHotbar().getSite(e.getPlayer())).click(e);
	}@EventHandler
	public void onSwitch(PlayerItemHeldEvent e) {
		int from = e.getPreviousSlot();
		int to = e.getNewSlot();
		Player p = e.getPlayer();
		PlayerData pd = PlayerManager.getPlayerData(p);
		if(pd.isBuilder()) {
			return;
		}else {
			Hotbar hb = pd.getCurrendHotbar();
			if(hb == null) {
				return;
			}else {
				boolean next = (from == 8 && to == 0) || (from == 7 && to == 0) || (from == 8 && to == 1);
				boolean prev = (from == 0 && to == 8) || (from == 1 && to == 8) || (from == 0 && to == 7);
				if(next) {
					hb.nextSite(p);
				}else if(prev) {
					hb.prevSite(p);
				}
			}
		}
	}

}
