package de.phyrone.lobbyrel.storage;
/*
 *   Copyright © 2018 by Phyrone  *
 *   Creation: 19.06.2018 by Phyrone
 */


import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MongoDB extends OfflinePlayerStorage {
    private MongoClient client;
    private MongoCollection<Document> table;

    @Override
    public void init() {
        List<String> hosts = Config.getStringList("Storage.MongoDB.Hosts",
                new ArrayList<>(Collections.singletonList("localhost:27017")));
        String dbName = Config.getString("Storage.MongoDB.Database", "lobbyrel");
        String tableName = Config.getString("Storage.MongoDB.Table", "playerdata");
        List<ServerAddress> dbServers = new ArrayList<>();
        for (String host : hosts) {
            try {
                if (host.contains(":")) {
                    String[] cont = host.split(":");
                    dbServers.add(new ServerAddress(cont[0], Integer.valueOf(cont[1])));
                } else
                    dbServers.add(new ServerAddress(host));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        client = new MongoClient(dbServers);

        MongoDatabase db = client.getDatabase(dbName);
        table = db.getCollection(tableName);

    }

    private Document getPlayerDoc(UUID uuid) {
        return new Document("_id", uuid.toString());
    }

    @Override
    public void save(UUID uuid, InternalOfflinePlayerData data) {
        /* Remove Old Format */
        if (table.find(new Document("user", uuid.toString())).iterator().hasNext())
            table.deleteMany(new Document("user", uuid.toString()));

        if (table.find(new Document("_id", uuid.toString())).iterator().hasNext()) {
            table.replaceOne(getPlayerDoc(uuid), getPlayerDoc(uuid).append("data", data.toPrettyJsonString()));
        } else {
            table.insertOne(getPlayerDoc(uuid).append("data", data.toPrettyJsonString()));
        }
    }

    @Override
    public InternalOfflinePlayerData load(UUID uuid) {
        {
            /* Load Old Format */
            for (Document document : table.find(new Document("user", uuid.toString()))) {
                return InternalOfflinePlayerData.fromJson(document.getString("user"));
            }
        }
        {
            for (Document document : table.find(getPlayerDoc(uuid))) {
                return InternalOfflinePlayerData.fromJson(document.getString("data"));
            }
        }
        return new InternalOfflinePlayerData();
    }

    @Override
    public void disable() {
        client.close();
    }
}

