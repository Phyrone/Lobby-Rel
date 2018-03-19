package de.phyrone.lobbyrel.listner;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.jump.PlayerJumpManager;

public class OtherEvents implements Listener {
	@EventHandler(ignoreCancelled = false)
	public void onJumppad(PlayerInteractEvent e) {
		if(e.getAction() == Action.PHYSICAL) {
			Player p = e.getPlayer();
			if(!PlayerManager.getPlayerData(p).isBuilder() && PlayerManager.getPlayerData(p).getOfflineData().getJumpPads()) {
				if(e.getClickedBlock().getType() == Material.GOLD_PLATE 
						||e.getClickedBlock().getType() == Material.IRON_PLATE) {
					PlayerJumpManager.jumpPadJump(p);
				}
			}
		}
	}@EventHandler
	public void onJump(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if(!PlayerManager.getPlayerData(p).isBuilder()) {
			e.setCancelled(true);
			PlayerJumpManager.doubleJump(p);
		}
	}
}
