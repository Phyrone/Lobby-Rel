package de.phyrone.lobbyrel.storage;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.database.Database;
import de.dytanic.cloudnet.lib.utility.document.Document;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.player.data.offline.InternalOfflinePlayerData;
import de.phyrone.lobbyrel.player.data.offline.OfflinePlayerStorage;

import java.util.UUID;

public class CloudNetDB extends OfflinePlayerStorage {
    Database db;

    @Override
    public void init() {
        db = CloudAPI.getInstance().getDatabaseManager().getDatabase(Config.getString("Storage.Cloudnet.Datbase"));

    }

    @Override
    public void disable() {

    }

    @Override
    public void save(UUID uuid, InternalOfflinePlayerData data) {
        db.insertAsync(new Document(uuid.toString()).append("data", data.toJsonString()));
    }

    @Override
    public InternalOfflinePlayerData load(UUID uuid) {
        if (db.contains(uuid.toString()))
            return InternalOfflinePlayerData.fromJson(db.load(uuid.toString()).getString("data"));
        else
            return new InternalOfflinePlayerData();
    }
}
