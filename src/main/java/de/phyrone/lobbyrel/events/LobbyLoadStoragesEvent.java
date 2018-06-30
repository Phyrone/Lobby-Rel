package de.phyrone.lobbyrel.events;

import de.phyrone.lobbyrel.storage.OfflinePlayerStorage;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

public abstract class LobbyLoadStoragesEvent extends Event {
    private static HandlerList HandlerList = new HandlerList();
    String storage;

    public LobbyLoadStoragesEvent(String storage) {
        this.storage = storage;
    }

    public static HandlerList getHandlerList() {
        return HandlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return HandlerList;
    }

    public abstract void addStorage(String name, OfflinePlayerStorage storage);

    public abstract void removeStorage(String name);

    public abstract HashMap<String, OfflinePlayerStorage> getAlreadyLoadedStorages();

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

}
