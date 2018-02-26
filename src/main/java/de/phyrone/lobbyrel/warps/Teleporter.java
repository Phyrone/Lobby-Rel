package de.phyrone.lobbyrel.warps;

import org.bukkit.entity.Player;

import de.phyrone.lobbyrel.player.effect.EffectPlayer;

public class Teleporter {
	public static void teleport(Player player,String warpname) {
		player.teleport(WarpManager.getLocation(warpname));
		new EffectPlayer(player).teleportEffect();
	}public static void toSpawn(Player player) {
		player.teleport(WarpManager.getSpawn().getLocation());
		new EffectPlayer(player).teleportEffect();
	}

}
