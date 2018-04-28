package de.phyrone.lobbyrel.storage;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyLoadStoragesEvent;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class StorageManager {
    public final static JsonStorage defaultStorage = new JsonStorage();
    public static HashMap<String, OfflinePlayerStorage> storageTeamplates = new HashMap<String, OfflinePlayerStorage>();
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

    public static void removeSorage(String tag) {
        storageTeamplates.remove(tag.toLowerCase());
    }

    public static void init() {
        if (init) {
            disable();
        }
        init = true;
        LobbyLoadStoragesEvent event = new LobbyLoadStoragesEvent(Config.getString("Storage.Type", "json").toLowerCase());
        Bukkit.getPluginManager().callEvent(event);
        storage = event.getStorage();
        storageTeamplates.getOrDefault(storage, defaultStorage).init();
    }

    public static void disable() {
        init = false;
        storageTeamplates.getOrDefault(storage, defaultStorage).disable();
    }

    public static InternalOfflinePlayerData loadPlayerData(UUID uuid) {

        InternalOfflinePlayerData dt = null;
        try {
            System.out.println("Load OFFLINEDATA - " + uuid.toString());
            dt = storageTeamplates.getOrDefault(storage, defaultStorage).load(uuid);
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
            System.out.println("Save OFFLINEDATA - " + uuid.toString());
            storageTeamplates.getOrDefault(storage, defaultStorage).save(uuid, data);
        } catch (Exception e) {
            if (LobbyPlugin.getDebug())
                e.printStackTrace();
        }
    }

}
