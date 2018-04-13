package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItem;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ItemsConfig {

    // Hier schreibst du deine Attribute hin

    // DON'T TOUCH THE FOLLOWING CODE
    private static ItemsConfig instance;
    public ArrayList<CustomItem> CustomItems = new ArrayList<CustomItem>(Arrays.asList(
            new CustomItem(3, new ItemBuilder(Material.CHEST).displayname("&5CustomItem").build()),
            new CustomItem(5).setItem(new LobbyItem(
                    new ItemBuilder(Material.SKULL_ITEM).displayname("&5PlayerHead").build())
                    .setSkin("%player%").setPlayerHead(true))
    ));
    public HashMap<String, LobbyItem> Items = new HashMap<>();

    public ItemsConfig() {

    }

    public static ItemsConfig getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);

        // no config file found
        if (instance == null) {
            instance = fromDefaults();
        }
    }

    public static void load(String file) {
        load(new File(file));
    }

    private static ItemsConfig fromDefaults() {
        ItemsConfig config = new ItemsConfig();
        return config;
    }

    private static ItemsConfig fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(configFile)));
            return gson.fromJson(reader, ItemsConfig.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static void write(File file, String jsonConfig) {
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

    public static LobbyItem getLobbyItem(String name, ItemStack noFound) {
        return getInstance().getItem(name, noFound);
    }

    public static LobbyItem getLobbyItem(String name) {
        return getInstance().getItem(name);
    }

    public static LobbyItem getLobbyItem(String name, LobbyItem noFound, boolean saveNotFound) {
        return getInstance().getItem(name, noFound, saveNotFound);
    }

    public static LobbyItem getLobbyItem(String name, LobbyItem noFound) {
        return getInstance().getItem(name, noFound);
    }

    public LobbyItem getItem(String name, LobbyItem noFound) {
        return getItem(name, noFound, true);
    }

    public LobbyItem getItem(String name, LobbyItem noFound, boolean saveNotFound) {
        ItemsConfig instance = getInstance();
        if (saveNotFound && !instance.Items.containsKey(name)) {
            getInstance().Items.put(name, noFound);
            CustomItemsManager.save();
        }
        return instance.Items.getOrDefault(name, noFound);


    }
    public LobbyItem getItem(String name) {
        return getItem(name,
                new LobbyItem().setMaterial(Material.BARRIER).setDisplayName("Item not Found")
                        .setLore("", "You can edit it in the \"Items.json\"", ""));
    }

    public LobbyItem getItem(String name, ItemStack noFound) {
        return getItem(name, new LobbyItem(noFound));
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonConfig = StringEscapeUtils.unescapeJava(gson.toJson(this));
            FileWriter writer;
            write(file, jsonConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}