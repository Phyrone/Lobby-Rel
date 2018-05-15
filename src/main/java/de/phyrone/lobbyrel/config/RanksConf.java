package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;

public class RanksConf {
    private static RanksConf instance;
    ArrayList<PlayerRank> ranks = new ArrayList<>();

    public static RanksConf getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);
        if (instance == null) {
            instance = fromDefaults();
        }
    }

    public static void load(String file) {
        load(new File(file));
    }

    private static RanksConf fromDefaults() {
        RanksConf config = new RanksConf();
        return config;
    }

    private static RanksConf fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));

            return gson.fromJson(reader, RanksConf.class);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public class PlayerRank {
        String permission;

    }

}