package de.phyrone.lobbyrel.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.player.data.offline.InternalOfflinePlayerData;
import de.phyrone.lobbyrel.player.data.offline.OfflinePlayerStorage;
import org.bson.Document;

import java.util.Arrays;
import java.util.UUID;

public class MongoDB extends OfflinePlayerStorage {
    MongoClient client;
    MongoDatabase db;
    MongoCollection<Document> table;

    @Override
    public void init() {
        String host = Config.getString("Storage.MongoDB.Host", "localhost");
        int port = Config.getInt("Storage.MongoDB.Port", 27017);
        String dbName = Config.getString("Storage.MongoDB.Database", "lobbyrel");
        String tableName = Config.getString("Storage.MongoDB.Table", "lobbyrel");
        if (Config.getBoolean("Storage.MongoDB.Auth.Enabled", false)) {

            char[] passwd;
            MongoCredential credital = MongoCredential.createCredential(Config.getString("Storage.MongoDB.Auth.Username")
                    , dbName, Config.getString("Storage.MongoDB.Password", "myPasswd").toCharArray());

            client = new MongoClient(new ServerAddress(host, port), Arrays.asList(credital));
        } else client = new MongoClient(new ServerAddress(host, port));

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
