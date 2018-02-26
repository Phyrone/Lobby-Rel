package de.phyrone.lobbyrel.storage;

import java.util.UUID;

import de.phyrone.lobbyrel.player.data.offline.InternalOfflinePlayerData;
import de.phyrone.lobbyrel.player.data.offline.OfflinePlayerStorage;

public class NoStorage extends OfflinePlayerStorage{

	@Override
	public void save(UUID uuid, InternalOfflinePlayerData data) {
		
	}

	@Override
	public InternalOfflinePlayerData load(UUID uuid) {
		return null;
	}

}
