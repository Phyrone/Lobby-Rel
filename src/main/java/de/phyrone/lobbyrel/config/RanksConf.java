package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.groups.DisplayGroup;
import de.phyrone.lobbyrel.lib.Tools;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RanksConf {
    private static final File configFile = new File("plugins/Lobby-Rel", "GroupManager.json");
    private static RanksConf instance;
    public ArrayList<DisplayGroup> Ranks = new ArrayList<>(Arrays.asList(
            new DisplayGroup("Admin", '4'),
            new DisplayGroup("Moderator", '1'),
            new DisplayGroup("VIP", '6')
    ));
    public DisplayGroup DefaultGroup = new DisplayGroup("Player", '7');

    public static RanksConf getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load() {
        instance = fromFile();
        if (instance == null) {
            instance = fromDefaults();
        }
    }


    private static RanksConf fromDefaults() {
        return new RanksConf();
    }

    private static RanksConf fromFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));

            return gson.fromJson(reader, RanksConf.class);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public void toFileAsync() {
        new Thread(() -> toFile()).start();
    }

    public void toFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = StringEscapeUtils.unescapeJava(gson.toJson(this));
        Tools.saveJson(jsonConfig, configFile);
    }



    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}