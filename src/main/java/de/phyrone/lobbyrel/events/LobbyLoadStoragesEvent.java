package de.phyrone.lobbyrel.events;

import java.util.HashMap;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.player.data.offline.OfflinePlayerStorage;

public class LobbyLoadStoragesEvent extends Event{
	private static HandlerList HandlerList = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return HandlerList;
	}public static HandlerList getHandlerList() {
        return HandlerList;
    }
	String storage;
	public LobbyLoadStoragesEvent(String storage){
    	this.storage = storage;
    }public void addStorage(String name,OfflinePlayerStorage storage) {
		InternalOfllineDataManager.addSorage(name, storage);
    }public void removeStorage(String name) {
    	InternalOfllineDataManager.removeSorage(name);
    }public HashMap<String, OfflinePlayerStorage> getAlreadyLoadedStorages() {
    	return InternalOfllineDataManager.storageTeamplates;
    }
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}

}
