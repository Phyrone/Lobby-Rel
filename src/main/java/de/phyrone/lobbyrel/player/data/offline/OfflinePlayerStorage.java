package de.phyrone.lobbyrel.player.data.offline;

import java.util.UUID;

public abstract class OfflinePlayerStorage {
	public void init() {
	}
	public void disable() {
	}
	abstract public void save(UUID uuid,InternalOfflinePlayerData data);
	abstract public InternalOfflinePlayerData load(UUID uuid);
}
