package de.phyrone.lobbyrel.storage;

import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;

import java.util.UUID;

public class NoStorage extends OfflinePlayerStorage{

	@Override
    public void save(UUID uuid, InternalOfflinePlayerData data) {
    }

	@Override
	public InternalOfflinePlayerData load(UUID uuid) {
		return null;
	}

}
