package de.phyrone.lobbyrel.storage;

import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;

import java.util.HashMap;
import java.util.UUID;

public class CacheOnlyStorage extends OfflinePlayerStorage {
    private HashMap<UUID, InternalOfflinePlayerData> cache = new HashMap<>();

    @Override
    public void save(UUID uuid, InternalOfflinePlayerData data) {
        cache.put(uuid, data);
    }

    @Override
    public InternalOfflinePlayerData load(UUID uuid) {
        return cache.getOrDefault(uuid, new InternalOfflinePlayerData());
    }

    @Override
    public void disable() {

    }
}
