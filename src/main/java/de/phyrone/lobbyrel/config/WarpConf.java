package de.phyrone.lobbyrel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.hotbar.LobbyItem;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.warps.Warp;
import org.bukkit.Material;

import java.io.*;
import java.util.HashMap;

public class WarpConf {
		  public HashMap<String, Warp> Warps;
		  public Warp Spawn;
		  private static WarpConf instance;

		//Variablen
		  public WarpConf()
		  {
			  Warps = new HashMap<>();
			  Spawn =  new Warp(ServerProp.getSpawnLocation()).setWarpItem(new LobbyItem(
					  new ItemBuilder(Material.MAGMA_CREAM).displayname("&6Spawn").build()));
		  }
		  
		  public static WarpConf getInstance()
		  {
		    if (instance == null) {
		      instance = fromDefaults();
		    }
		    return instance;
		  }
		  
		  public static void load(File file)
		  {
		    instance = fromFile(file);
		    if (instance == null) {
		      instance = fromDefaults();
		    }
		  }
		  
		  public static void load(String file)
		  {
		    load(new File(file));
		  }
		  
		  private static WarpConf fromDefaults()
		  {
		    WarpConf config = new WarpConf();
		    return config;
		  }
		  
		  public void toFile(String file)
		  {
		    toFile(new File(file));
		  }
		  
		  public void toFile(File file)
		  {
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    String jsonConfig = gson.toJson(this);
		    try
		    {
		      FileWriter writer = new FileWriter(file);
		      writer.write(jsonConfig);
		      writer.flush();
		      writer.close();
		    }
		    catch (IOException e)
		    {
		      e.printStackTrace();
		    }
		  }
		  
		  private static WarpConf fromFile(File configFile)
		  {
		    try
		    {
		      Gson gson = new GsonBuilder().setPrettyPrinting().create();
		      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));

                return gson.fromJson(reader, WarpConf.class);
		    }
		    catch (FileNotFoundException e) {}
		    return null;
		  }
		  
		  public String toString()
		  {
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    return gson.toJson(this);
		  }
		
}
