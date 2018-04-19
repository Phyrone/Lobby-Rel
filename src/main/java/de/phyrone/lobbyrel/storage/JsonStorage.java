package de.phyrone.lobbyrel.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;

import java.io.*;
import java.util.UUID;

public class JsonStorage extends OfflinePlayerStorage{
	static String path;
	@Override
	public void init() {
		path = Config.getString("Storage.FilePath",  
				LobbyPlugin.getInstance().getDataFolder().getPath()+File.separator+"players");
	}
	@Override
	public void save(UUID uuid, InternalOfflinePlayerData data) {
    	File file = new File(Config.getString("Configs.Playerdata",path),uuid.toString()+".json");
    	if(!file.getParentFile().exists()) {
    		file.getParentFile().mkdirs();
    	}
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(data);
        FileWriter writer;
        ItemsConfig.write(file, jsonConfig);

    }

	@Override
	public InternalOfflinePlayerData load(UUID uuid) {
		File file = new File(Config.getString("Configs.Playerdata",path),uuid.toString()+".json");
        try { 
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); 
            BufferedReader reader = new BufferedReader(new InputStreamReader( 
            		new FileInputStream(file))); 
            return gson.fromJson(reader, InternalOfflinePlayerData.class); 
        } catch (FileNotFoundException e) { 
            return null; 
        }
	}

}
