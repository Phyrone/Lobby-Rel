package de.phyrone.lobbyrel.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.player.data.offline.InternalOfflinePlayerData;
import de.phyrone.lobbyrel.player.data.offline.OfflinePlayerStorage;

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
        try { 
            writer = new FileWriter(file); 
            writer.write(jsonConfig); 
            writer.flush(); 
            writer.close(); 

        } catch (IOException e) { 
            e.printStackTrace(); 
        }
		
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
