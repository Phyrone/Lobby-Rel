package de.phyrone.lobbyrel.player.data;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.phyrone.lobbyrel.hotbar.api.Hotbar;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.PlayerState;
import de.phyrone.lobbyrel.warps.WorldManager;

public class PlayerData {
	//Variablen
	UUID uuid;
	PlayerState state = PlayerState.INLOBBY;
	Hotbar currendHotbar = null;
	Boolean allowGamemodeChange = false;
	Boolean visible = true;
	


	//Functions
	public Boolean isVisible() {
		return visible;
	}
	public PlayerData setVisible(Boolean visible) {
		this.visible = visible;
		return this;
	}
	
	public PlayerData setAllowGamemodeChange(Boolean allowGamemodeChange) {
		this.allowGamemodeChange = allowGamemodeChange;
		return this;
	}
	public PlayerData(Player player) {
		this.uuid = player.getUniqueId();
	}public PlayerData(UUID uuid){
		this.uuid = uuid;
	}
	public Hotbar getCurrendHotbar() {
		return currendHotbar;
	}
	public PlayerData setCurrendHotbar(Hotbar currendHotbar) {
		this.currendHotbar = currendHotbar;
		return this;
	}
	public HashMap<String, Object> customdatas = new HashMap<>();
	public PlayerData save() {
		PlayerManager.setPlayerData(uuid, this);
		return this;
	}
	public Boolean isBuilder() {
		//return isBuilder;
		return state == PlayerState.BUILDER;
	}
	public PlayerData setBuilder(Boolean isBuilder,World world) {
		return setBuilder(isBuilder, world.getName());
	}
	public PlayerData setBuilder(Boolean isBuilder,String world) {
		//this.isBuilder = isBuilder;
		if(isBuilder)
			setState(PlayerState.BUILDER);
		else if(WorldManager.isLobby(world))
			setState(PlayerState.INLOBBY);
		else
			setState(PlayerState.OUTLOBBY);
		return this;
	}
	public boolean isInLobby() {
		return state == PlayerState.INLOBBY;
	}
	public boolean getAllowGamemodeChange() {
		return allowGamemodeChange;
	}public PlayerState getState() {
		return state;
	}
	public PlayerData setState(PlayerState state) {
		this.state = state;
		return this;
	}
	public int getNavigator() {
		return new OfflinePlayerData(uuid).getNavigator();
	}public PlayerData setNavigator(int id) {
		new OfflinePlayerData(uuid).setNavigator(id);
		return this;
	}public int getHider() {
		return new OfflinePlayerData(uuid).getPlayerHider();
	}public PlayerData setHider(int mode) {
		new OfflinePlayerData(uuid).SetPlayerHider(mode);
		return this;
	}public OfflinePlayerData getOfflineData() {
		return new OfflinePlayerData(uuid);
	}

}
