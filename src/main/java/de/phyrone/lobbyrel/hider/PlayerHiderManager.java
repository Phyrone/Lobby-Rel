package de.phyrone.lobbyrel.hider;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.effect.EffectPlayer;

public class PlayerHiderManager {
	static HashMap<Integer, HiderModule> modules = new HashMap<>();
	public static void init() {
		modules.put(2, new HiderModule().setAction("lobby.team"));
	}
	public static void setHider(Player player,int hider) {
		PlayerData pd = PlayerManager.getPlayerData(player);
		if(hider == pd.getHider()) {
			new EffectPlayer(player).blocked();
		}else {
			pd.setHider(hider);
			new EffectPlayer(player).changeHider();
			update(player);
		}
	}public static int getHider(Player player) {
		return PlayerManager.getPlayerData(player).getHider();
	}public static Boolean isSelected(Player player,int hider) {
		return getHider(player)==hider;
	}
	public static void update(Player player) {
		Runnable t = new Runnable() {
			
			@Override
			public void run() {
				PlayerData pd = PlayerManager.getPlayerData(player);
				int hider = pd.getHider();
				for(Player p:Bukkit.getOnlinePlayers()) {
					if(hider == -1) {
						
					}else if(hider == 0) {
						new BukkitRunnable() {
							
							@Override
							public void run() {
								player.hidePlayer(p);
							}
						}.runTask(LobbyPlugin.getInstance());
						
					}else if(hider == 1) {
						if(PlayerManager.getPlayerData(p).isVisible()) {
							new BukkitRunnable() {
								
								@Override
								public void run() {
									player.showPlayer(p);
								}
							}.runTask(LobbyPlugin.getInstance());
						}else {
							new BukkitRunnable() {
								
								@Override
								public void run() {
									player.hidePlayer(p);
								}
							}.runTask(LobbyPlugin.getInstance());
							
						}
					}else {
						Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(PlayerManager.getPlayerData(p).isVisible()) {
									if(modules.containsKey(hider)) {
										boolean visible = modules.get(hider).getAction().onCheck(player, p);
										new BukkitRunnable() {
											
											@Override
											public void run() {
												if(visible) {
													player.showPlayer(p);
												}else {
													player.hidePlayer(p);
												}
											}
										}.runTask(LobbyPlugin.getInstance());
									}else {
										new BukkitRunnable() {
											
											@Override
											public void run() {
												player.showPlayer(p);
											}
										}.runTask(LobbyPlugin.getInstance());
										pd.setHider(1);
									}
								}else {
									new BukkitRunnable() {
										
										@Override
										public void run() {
											player.hidePlayer(p);
										}
									}.runTask(LobbyPlugin.getInstance());
								}
							}
						});
		
					}
				}
			}
		};
		Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), t);

	}public static void updateAll() {
		for(Player p:Bukkit.getOnlinePlayers()) {
			update(p);
		}
	}
	public static void updateForOthers(Player p) {
		Runnable t = new Runnable() {
			
			@Override
			public void run() {

				for(Player player:Bukkit.getOnlinePlayers()) {
					PlayerData pd = PlayerManager.getPlayerData(player);
					int hider = pd.getHider();
					if(hider == -1) {
						
					}else if(hider == 0) {
						player.hidePlayer(p);
					}else if(hider == 1) {
						if(PlayerManager.getPlayerData(p).isVisible()) {
							player.showPlayer(p);
						}else {
							player.hidePlayer(p);
						}
					}else {
						Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								if(PlayerManager.getPlayerData(p).isVisible()) {
									if(modules.containsKey(hider)) {
										modules.get(hider).getAction().onCheck(player, p);
									}else {
										new BukkitRunnable() {
											
											@Override
											public void run() {
												player.showPlayer(p);
											}
										}.runTask(LobbyPlugin.getInstance());
										pd.setHider(1);
									}
								}else {
									new BukkitRunnable() {
										
										@Override
										public void run() {
											player.hidePlayer(p);
										}
									}.runTask(LobbyPlugin.getInstance());
								}
							}
						});
		
					}
				}
			}
		};
		Bukkit.getScheduler().scheduleSyncDelayedTask(LobbyPlugin.getInstance(), t);
	}

	
}
