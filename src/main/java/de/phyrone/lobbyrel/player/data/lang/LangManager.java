package de.phyrone.lobbyrel.player.data.lang;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.json.FancyMessage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LangManager
{
  static Boolean init = Boolean.valueOf(false);
  static String defaultLang = null;
  static File langFolder = null;
  static HashMap<String, LangConf> langs = new HashMap<String, LangConf>();
  
  public static void init()
  {
	langs.clear();
    String filename = Config.getString("Language.Folder", new StringBuilder("plugins/").append(LobbyPlugin.getInstance().getName()).append("/Lang/").toString()) + "IGNORE_THIS";
    langFolder = new File(filename);
    if (!langFolder.isDirectory()) {
      langFolder = langFolder.getParentFile();
    }
    langFolder.mkdirs();
    defaultLang = Config.getString("Language.Default", "en_US");
    if (LangConf.getLangs().size() == 0)
    {
      File df = new File(langFolder.getPath(), defaultLang + ".yml");
      try
      {
        df.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      for (String l : LangConf.getLangs())
      {
        System.out.println("Reading Language " + l + "...");
        LangConf cfg = new LangConf(l);
        cfg.load();
        langs.put(l, cfg);
      }
    }
    init = Boolean.valueOf(true);
  }
  public static void sendMessage(Player player, String messagePath, String defaultMessage) {
	  new BukkitRunnable() {
		
		@Override
		public void run() {
			String m = getMessage(player, messagePath, defaultMessage);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then(m).send(player);
				}
			}.runTask(LobbyPlugin.getInstance());
		}
	}.runTaskAsynchronously(LobbyPlugin.getInstance());
  }
  public static String getMessage(Player player, String messagePath, String defaultMessage)
  {
    if (!init.booleanValue()) {
      init();
    }
    String lang = getLangOrDefault(player);
    if (!langs.containsKey(defaultLang)) {
      langs.put(defaultLang, new LangConf(defaultLang));
    }
    LangConf langconf = langs.getOrDefault(lang, langs.getOrDefault(defaultLang, new LangConf(defaultLang)));
    String ret = null;
    if (langconf.conf.contains(messagePath))
    {
      ret = langconf.conf.getString(messagePath);
    }
    else
    {
      ret = defaultMessage;
      langconf.conf.set(messagePath, defaultMessage);
      langconf.save();
      langs.put(langconf.lang, langconf);
    }
    return ret.replace("&", "§");
  }
  
  private static String getLangOrDefault(Player p)
  {
    if (p == null) {
      return defaultLang;
    }
    if (!(p instanceof Player)) {
      return defaultLang;
    }
    try {
		return p.spigot().getLocale();
	} catch (Exception e) {
		return getLanguage(p);
	}
    
  }
  
  public static String getLanguage(Player p)
  {
    try
    {
      Object obj = getMethod("getHandle", p.getClass()).invoke(p, null);
      Field f = obj.getClass().getDeclaredField("locale");
      f.setAccessible(true);
      return (String)f.get(obj);
    }
    catch (Exception e) {}
    return defaultLang;
  }
  
  private static Method getMethod(String n, Class<?> c)
  {
    Method[] arrayOfMethod;
    int j = (arrayOfMethod = c.getDeclaredMethods()).length;
    for (int i = 0; i < j; i++)
    {
      Method m = arrayOfMethod[i];
      if (m.getName().equals(n)) {
        return m;
      }
    }
    return null;
  }
  public static String noPerm(Player p) {
	  return LangManager.getMessage(p,"NoPermission","&cThe computer say no!");
  }
}
