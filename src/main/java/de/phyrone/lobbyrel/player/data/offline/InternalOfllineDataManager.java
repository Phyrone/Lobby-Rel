package de.phyrone.lobbyrel.player.data.offline;

import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyLoadStoragesEvent;
import de.phyrone.lobbyrel.storage.JsonStorage;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class InternalOfllineDataManager {
	static boolean init = false;
	public final static JsonStorage defaultStorage = new JsonStorage();
	static String storage = "json";
	public static String getStorage() {
		return storage;
	}
	public static HashMap<String, OfflinePlayerStorage> storageTeamplates = new HashMap<String, OfflinePlayerStorage>();
	private static HashMap<UUID, InternalOfflinePlayerData> datas = new HashMap<UUID, InternalOfflinePlayerData>();
	public static void addSorage(String tag,OfflinePlayerStorage storage) {
		storageTeamplates.put(tag.toLowerCase(), storage);
	}public static void removeSorage(String tag) {
		storageTeamplates.remove(tag.toLowerCase());
	}
	public static void clearStorages() {
		storageTeamplates.clear();
	}
	public static void init() {
		if(init) {
			disable();
		}
		init = true;
		LobbyLoadStoragesEvent event = new LobbyLoadStoragesEvent(Config.getString("Storage.Type", "json").toLowerCase());
		Bukkit.getPluginManager().callEvent(event);
		storage = event.getStorage();
		storageTeamplates.getOrDefault(storage,defaultStorage).init();
	}public static void disable() {
		init = false;
		datas.clear();
		storageTeamplates.getOrDefault(storage,defaultStorage).disable();
	}

    UUID uuid;
	public InternalOfllineDataManager(UUID uuid) {
		this.uuid = uuid;
	}
	public InternalOfflinePlayerData get() {
		if(!datas.containsKey(uuid))load();
		return datas.get(uuid);
	}public InternalOfllineDataManager set(InternalOfflinePlayerData data) {
		datas.put(uuid, data);
		return this;
	}public InternalOfllineDataManager save() {
		storageTeamplates.getOrDefault(storage, defaultStorage).save(uuid, get());
		return this;
	}public InternalOfllineDataManager saveAndClear() {
		storageTeamplates.getOrDefault(storage, defaultStorage).save(uuid, get());
		datas.remove(uuid);
		return this;
	}public InternalOfflinePlayerData load() {
		InternalOfflinePlayerData dt = storageTeamplates.getOrDefault(storage, defaultStorage).load(uuid);
		if(dt == null) {
			dt = new InternalOfflinePlayerData();
		}
		datas.put(uuid, dt);
		return dt;
	}

}
