package de.phyrone.lobbyrel.player.data.internal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.config.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InternalOfflinePlayerData {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Gson PRETTYGSON = new GsonBuilder().setPrettyPrinting().create();
    //Data
    public int Navigator = Config.getInt("PlayerSettings.Default.Navigator", 0);
    public int PlayerHider = Config.getInt("PlayerSettings.Default.PlayerHider", 1);
    public int Money = 0;
    public Boolean Sound = true;
    public Boolean Scoreboard = true;
    public boolean JumpPad = true;
    public boolean DoubleJump = true;
    public HashMap<String, Object> CustomData = new HashMap<>();
    public ArrayList<String> Tags = new ArrayList<>();

    public static InternalOfflinePlayerData fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, InternalOfflinePlayerData.class);
    }

    public static InternalOfflinePlayerData fromFile(UUID uuid, File file) throws FileNotFoundException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file)));
        return gson.fromJson(reader, InternalOfflinePlayerData.class);

    }

    public String toJsonString() {
        return GSON.toJson(this);
    }

    public String toPrettyJsonString() {
        return PRETTYGSON.toJson(this);
    }


    public InternalOfflinePlayerData toFile(File file) throws IOException {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String jsonConfig = PRETTYGSON.toJson(this);
        FileWriter writer;
        writer = new FileWriter(file);
        writer.write(jsonConfig);
        writer.flush();
        writer.close();

        return this;
    }
}
