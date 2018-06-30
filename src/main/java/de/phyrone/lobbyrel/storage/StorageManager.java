package de.phyrone.lobbyrel.storage;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyLoadStoragesEvent;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class StorageManager {
    public final static JsonStorage DEFAULT_STORAGE = new JsonStorage();
    private static HashMap<String, OfflinePlayerStorage> storageTeamplates = new HashMap<>();
    static boolean init = false;
    static String storage = "json";

    public static String getStorage() {
        return storage.toUpperCase();
    }

    public static void clearStorages() {
        storageTeamplates.clear();
    }

    public static void addSorage(String tag, OfflinePlayerStorage storage) {
        storageTeamplates.put(tag.toLowerCase(), storage);
    }

    public static void removeStorage(String tag) {
        storageTeamplates.remove(tag.toLowerCase());
    }

    public static void init() {
        if (init) {
            disable();
        }
        init = true;
        LobbyLoadStoragesEvent event = new LobbyLoadStoragesEvent(Config.getString("Storage.Type", "json").toLowerCase()) {
            @Override
            public void addStorage(String name, OfflinePlayerStorage storage) {
                StorageManager.addSorage(name, storage);
            }

            @Override
            public void removeStorage(String name) {
                StorageManager.removeStorage(name);
            }

            @Override
            public HashMap<String, OfflinePlayerStorage> getAlreadyLoadedStorages() {
                return storageTeamplates;
            }
        };
        Bukkit.getPluginManager().callEvent(event);
        storage = event.getStorage();
        try {
            storageTeamplates.getOrDefault(storage, DEFAULT_STORAGE).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void disable() {
        init = false;
        try {
            storageTeamplates.getOrDefault(storage, DEFAULT_STORAGE).disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InternalOfflinePlayerData loadPlayerData(UUID uuid) {

        InternalOfflinePlayerData dt = null;
        try {
            if (LobbyPlugin.getDebug())
                System.out.println("Load OFFLINEDATA - " + uuid.toString());
            dt = storageTeamplates.getOrDefault(storage, DEFAULT_STORAGE).load(uuid);
        } catch (Exception e) {
            if (LobbyPlugin.getDebug())
                e.printStackTrace();
        }
        if (dt == null) {
            dt = new InternalOfflinePlayerData();
        }
        return dt;
    }

    public static void save(UUID uuid, InternalOfflinePlayerData data) {
        try {
            if (LobbyPlugin.getDebug())
                System.out.println("Save OFFLINEDATA - " + uuid.toString());
            storageTeamplates.getOrDefault(storage, DEFAULT_STORAGE).save(uuid, data);
        } catch (Exception e) {
            if (LobbyPlugin.getDebug())
                e.printStackTrace();
        }
    }

}
