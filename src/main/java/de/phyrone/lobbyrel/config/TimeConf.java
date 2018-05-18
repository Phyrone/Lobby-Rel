package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.lib.Tools;

import java.io.*;
import java.util.HashMap;


public class TimeConf {
    private static TimeConf instance;
    //Variablen
    public HashMap<Integer, TimeProfile> RealTimeProfiles = new HashMap<>();

    public TimeConf() {

    }

    private static TimeConf fromDefaults() {
        TimeConf config = new TimeConf();
        return config;
    }

    public static TimeConf getInstance() {
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

    public class TimeProfile {
        public int Ticks = 0;
    }

    private static TimeConf fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));

            return gson.fromJson(reader, TimeConf.class);
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
        Tools.saveJson(jsonConfig, file);
    }

    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
