package de.phyrone.lobbyrel.storage;

import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;

import java.util.UUID;

public abstract class OfflinePlayerStorage {
	public void init() {
	}
	public void disable() {
	}

    abstract public void save(UUID uuid, InternalOfflinePlayerData data) throws Exception;

    abstract public InternalOfflinePlayerData load(UUID uuid) throws Exception;
}
