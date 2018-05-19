package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.LobbyPlugin;
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
    private static final File CONFIGFILE = new File("plugins/Lobby-Rel", "Items.json");
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

    public static void load() {
        try {
            CONFIGFILE.getParentFile().mkdirs();
            if (!CONFIGFILE.exists())
                LobbyPlugin.copyResource("items.json", CONFIGFILE);
            instance = fromFile();

            // no config file found
            if (instance == null) {
                instance = fromDefaults();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static ItemsConfig fromDefaults() {
        ItemsConfig config = new ItemsConfig();
        return config;
    }

    private static ItemsConfig fromFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(CONFIGFILE)));
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
        if (saveNotFound && !Items.containsKey(name)) {
            getInstance().Items.put(name, noFound);
            CustomItemsManager.save();
        }
        return Items.getOrDefault(name, noFound);


    }
    public LobbyItem getItem(String name) {
        return getItem(name,
                new LobbyItem().setMaterial(Material.BARRIER).setDisplayName("Item not Found")
                        .setLore("", "You can edit me in the \"Items.json\"", ""));
    }

    public LobbyItem getItem(String name, ItemStack noFound) {
        return getItem(name, new LobbyItem(noFound));
    }


    public void toFile() {
        try {
            CONFIGFILE.getParentFile().mkdirs();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonConfig = StringEscapeUtils.unescapeJava(gson.toJson(this));
            FileWriter writer;
            write(CONFIGFILE, jsonConfig);
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