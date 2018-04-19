package de.phyrone.lobbyrel.player.lang;

import de.phyrone.lobbyrel.lib.Utf8YamlConfiguration;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LangConf
{
  String lang;
  File file;
  public Utf8YamlConfiguration conf = new Utf8YamlConfiguration();
  
  public LangConf(String lang)
  {
    this.lang = lang;
    this.file = new File(LangManager.langFolder, lang + ".yml");
  }
  
  public static ArrayList<String> getLangs()
  {
    ArrayList<String> ret = new ArrayList<String>();
    File files = LangManager.langFolder;
    File[] arrayOfFile;
    int j = (arrayOfFile = files.listFiles()).length;
    for (int i = 0; i < j; i++)
    {
      File f = arrayOfFile[i];
      String name = f.getName();
      if (name.endsWith(".yml")) {
        name = name.substring(0, 5);
      }
      ret.add(name);
    }
    return ret;
  }
  
  public void load()
  {
    try
    {
      this.conf.load(this.file);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (InvalidConfigurationException e)
    {
      e.printStackTrace();
    }
  }
  
  public void save()
  {
    try
    {
      this.conf.save(this.file);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
