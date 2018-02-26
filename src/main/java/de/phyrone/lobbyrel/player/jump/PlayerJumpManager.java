package de.phyrone.lobbyrel.player.jump;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.OfflinePlayerData;
import de.phyrone.lobbyrel.player.effect.EffectPlayer;

public class PlayerJumpManager {
	static ArrayList<Player> jumpedplayers = new ArrayList<>();
	static int task = -1;
	@SuppressWarnings("deprecation")
	public static void setup() {
		jumpedplayers.clear();
		BukkitScheduler sch = Bukkit.getScheduler();
		if(sch.isCurrentlyRunning(task))sch.cancelTask(task);
		task = sch.scheduleAsyncRepeatingTask(LobbyPlugin.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(Player p:jumpedplayers) {
					Bukkit.getScheduler().runTask(LobbyPlugin.getInstance(), new Runnable() {
						
						@Override
						public void run() {
							if(p.isOnGround()) {
								jumpedplayers.remove(p);
								p.setAllowFlight(new OfflinePlayerData(p).getDoubleJump());
							}
						}
					});

				}
			}
		}, 5, 5);
		
	}
	public static void doubleJump(Player p) {
			Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					if(!jumpedplayers.contains(p)) {
						Vector vector = p.getLocation().getDirection();
						Vector vec = new Vector(vector.getX()*2, 1, vector.getZ()*2);
						p.setVelocity(vec);
						jumpedplayers.add(p);
						new EffectPlayer(p).dJump();
						new BukkitRunnable() {
							
							@Override
							public void run() {
								p.setAllowFlight(false);
							}
						}.runTask(LobbyPlugin.getInstance());
					}

				}
			});
	}
	public static void jumpPadJump(Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				Vector vector = p.getLocation().getDirection();
				Vector vec = new Vector(vector.getX()*2, 1, vector.getZ()*2);
				p.setVelocity(vec);
				new EffectPlayer(p).JumpPad();
			}
		});
	}

}
