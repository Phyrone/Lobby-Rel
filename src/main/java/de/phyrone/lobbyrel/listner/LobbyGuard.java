package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.WarpManager;
import de.phyrone.lobbyrel.warps.WorldManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.PlayerInventory;


public class LobbyGuard implements Listener {
	public static Boolean disableBreakCheck = false;
	public static Boolean disableGameModeCheck = false;
	public static Boolean disableInteractCheck = false;
	public static Boolean disableInvClickCheck = false;
	public static Boolean disableDamageCheck = false;
	public static Boolean disableHungerCheck = false;
	public static Boolean disableDropCheck = false;
	public static Boolean disablePickupCheck = false;
	@EventHandler
	public void noMob(EntitySpawnEvent e) {
		
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		Player p = e.getPlayer();
		PlayerData playerdata = PlayerManager.getPlayerData(p);
		if(playerdata.isInLobby()&&!disableBreakCheck){
			p.sendMessage(LobbyPlugin.getPrefix()+LangManager.getMessage(p,"Guard.NoBreak", " &4Du darfst hier nicht abbauen!"));
			e.setCancelled(true);
		}
	} @EventHandler
	public void onGm(PlayerGameModeChangeEvent e){
		Player p = e.getPlayer();
		PlayerData pd = PlayerManager.getPlayerData(p);
		if(!pd.isInLobby()||pd.getAllowGamemodeChange()||disableGameModeCheck){
			
		}else{
			e.setCancelled(true);
		}
	}@EventHandler
	public void noDamage(EntityDamageEvent e){
		Entity entity = e.getEntity();
		if(!(entity instanceof Player)){
			return;
		}Player p = (Player)entity;
		PlayerData pd = PlayerManager.getPlayerData(p);
		if(pd.isInLobby()&&!disableDamageCheck){
			e.setCancelled(true);
			if(e.getCause() == DamageCause.FIRE_TICK || e.getCause() == DamageCause.FIRE) {
				p.setFireTicks(0);
			}
		}
	}@EventHandler
	public void noHunger(FoodLevelChangeEvent e){
		if(PlayerManager.getPlayerData((Player)e.getEntity()).isInLobby()){
			if(e.getFoodLevel() != 20){
				e.setFoodLevel(20);
			}else{
				e.setCancelled(true);
			}
		}
	}@EventHandler
	public void noDrop(PlayerDropItemEvent e){
		if(PlayerManager.getPlayerData(e.getPlayer()).isInLobby()&&!disableDropCheck){
			e.setCancelled(true);
		}
	}@EventHandler
	public void noPickup(PlayerPickupItemEvent e){
		if(PlayerManager.getPlayerData(e.getPlayer()).isInLobby()){
			e.setCancelled(true);
		}
	}@EventHandler
	public void noXP(PlayerExpChangeEvent e){
		if(!PlayerManager.isBuilder(e.getPlayer())){
			
		}
	}@EventHandler
    public void noSwitch(PlayerSwapHandItemsEvent e) {
        if (!PlayerManager.getPlayerData(e.getPlayer()).isBuilder())
            e.setCancelled(true);
    }

    @EventHandler
    public void noMoveItems(InventoryClickEvent e){
        try {
            if(((e.getClickedInventory() instanceof PlayerInventory)|| (e.getClickedInventory().getType() == InventoryType.CRAFTING)|| (e.getClickedInventory().getType() == InventoryType.PLAYER))&&!disableInvClickCheck){
                if(PlayerManager.getPlayerData((Player)e.getWhoClicked()).isInLobby()){
                    e.setCancelled(true);
                }
            }}catch (Exception e2) {}
    }@EventHandler
	public void noWeather(WeatherChangeEvent e){
		if(WorldManager.isLobby(e.getWorld()) && e.toWeatherState()){
			e.setCancelled(true);
		}
	}@EventHandler
	public void noXp(PlayerExpChangeEvent e) {
		if(PlayerManager.getPlayerData(e.getPlayer()).isInLobby()) {
			e.setAmount(0);
		}
	}@EventHandler
	public void onVoid(PlayerMoveEvent e) {
		if(e.getTo().getBlockY() < 0) {
		if(PlayerManager.getPlayerData(e.getPlayer()).isInLobby()) {
				Teleporter.toSpawn(e.getPlayer());
			}
		}
	}
	@EventHandler
	public void noAch(PlayerAchievementAwardedEvent e) {
		if(PlayerManager.getPlayerData(e.getPlayer()).isInLobby()) {
			e.setCancelled(true);
		}
	}@EventHandler
	public void onMobSpawn(CreatureSpawnEvent  e) {
		if(e.getSpawnReason() == SpawnReason.NATURAL) {
			e.setCancelled(true);
		}
	}@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(PlayerManager.getPlayerData(p).isInLobby()) {
			PlayerManager.resetPlayer(p);
			e.setRespawnLocation(WarpManager.getSpawn().getLocation());
		}
	}

}
