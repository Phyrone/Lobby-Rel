package de.phyrone.lobbyrel.events;

import de.phyrone.lobbyrel.storage.OfflinePlayerStorage;
import de.phyrone.lobbyrel.storage.StorageManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

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
        StorageManager.addSorage(name, storage);
    }public void removeStorage(String name) {
        StorageManager.removeSorage(name);
    }public HashMap<String, OfflinePlayerStorage> getAlreadyLoadedStorages() {
        return StorageManager.storageTeamplates;
    }
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}

}
