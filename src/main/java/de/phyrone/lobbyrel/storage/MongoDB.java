package de.phyrone.lobbyrel.storage;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MongoDB extends OfflinePlayerStorage {
    MongoClient client;
    MongoDatabase db;
    MongoCollection<Document> table;

    @Override
    public void init() {
        List<String> hosts = Config.getStringList("Storage.MongoDB.Hosts",
                new ArrayList<>(Arrays.asList("localhost:27017")));
        String dbName = Config.getString("Storage.MongoDB.Database", "lobbyrel");
        String tableName = Config.getString("Storage.MongoDB.Table", "lobbyrel");
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

        db = client.getDatabase(dbName);
        table = db.getCollection(tableName);

    }

    @Override
    public void save(UUID uuid, InternalOfflinePlayerData data) {
        if (table.find(new Document("user", uuid.toString())).iterator().hasNext())
            table.deleteMany(new Document("user", uuid.toString()));
        table.insertOne(new Document("user", uuid.toString()).append("data", data.toPrettyJsonString()));
    }

    @Override
    public InternalOfflinePlayerData load(UUID uuid) {
        MongoCursor<Document> ret = table.find(new Document("user", uuid.toString())).iterator();
        while (ret.hasNext()) {
            return InternalOfflinePlayerData.fromJson(ret.next().getString("data"));
        }
        return new InternalOfflinePlayerData();
    }

    @Override
    public void disable() {

        client.close();
    }
}
