package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItem;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ItemsConfig {

    // Hier schreibst du deine Attribute hin
	public HashMap<String, LobbyItem> Items = new HashMap<String, LobbyItem>();
	public ArrayList<CustomItem> CustomItems = new ArrayList<CustomItem>(Arrays.asList(
			new CustomItem(3, new ItemBuilder(Material.CHEST).displayname("&5CustomItem").build()),
			new CustomItem(5).setItem(new LobbyItem(
					new ItemBuilder(Material.SKULL_ITEM).displayname("&5PlayerHead").build())
					.setSkin("%player%").setPlayerHead(true))
			));
	public ItemsConfig(){
		
	}public LobbyItem getItem(String name,LobbyItem noFound,boolean saveNotFound) {
		if(saveNotFound && !Items.containsKey(name)) {
			Items.put(name, noFound);
			CustomItemsManager.save();
		}
		return Items.getOrDefault(name, noFound);
		
			
	}public LobbyItem getItem(String name,LobbyItem noFound) {
		return getItem(name, noFound, true);
	}public LobbyItem getItem(String name) {
		return getItem(name, 
				new LobbyItem().setMaterial(Material.BARRIER).setDisplayName("Item not Found")
				.setLore("You can be edited","in \"Items.json\""));
	}public LobbyItem getItem(String name,ItemStack noFound) {
		return getItem(name, new LobbyItem(noFound));
	}
    // DON'T TOUCH THE FOLLOWING CODE
    private static ItemsConfig instance;

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

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
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

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}