package de.phyrone.lobbyrel.player.lang;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class LangManager {
    static boolean init = false;
    static String defaultLang = null;
    static File langFolder = null;
    static HashMap<String, LangConf> langs = new HashMap<String, LangConf>();

    public static void init() {
        try {

        } catch (Exception e) {
        }
        ;
        langs.clear();
        String filename = Config.getString("Language.Folder", new StringBuilder("plugins/").append(LobbyPlugin.getInstance().getName()).append("/Lang/").toString()) + "IGNORE_THIS";
        langFolder = new File(filename);
        if (!langFolder.isDirectory()) {
            langFolder = langFolder.getParentFile();
        }
        langFolder.mkdirs();
        defaultLang = Config.getString("Language.Default", "en_US");
        if (LangConf.getLangs().size() == 0) {
            getDefaultFiles();
            File df = new File(langFolder.getPath(), defaultLang + ".yml");
            try {
                df.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (String l : LangConf.getLangs()) {
                System.out.println("Reading Language " + l + "...");
                LangConf cfg = new LangConf(l);
                cfg.load();
                langs.put(l, cfg);
            }
        }
        init = true;
    }

    private static void getDefaultFiles() {
        String path = langFolder.getPath();
        /* EN */
        LobbyPlugin.copyResource("en_lang.yml", new File(path, "en_US"));
        /* DE */
        LobbyPlugin.copyResource("de_lang.yml", new File(path, "de_DE"));
    }

    public static void sendMessage(CommandSender sender, String messagePath, String defaultMessage) {
        Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
            String m = getMessage(sender, messagePath, defaultMessage);
            new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then(m).send(sender);
        });
    }

    public static String getMessage(CommandSender sender, String messagePath, String defaultMessage) {
        if (init) {
            init();
        }
        String lang = getLangOrDefault(sender);
        LangConf langconf = langs.getOrDefault(lang, langs.getOrDefault(defaultLang, new LangConf(defaultLang)));
        String ret;
        if (langconf.conf.contains(messagePath)) {
            ret = langconf.conf.getString(messagePath);
        } else {
            ret = defaultMessage;
            langconf.conf.set(messagePath, defaultMessage);
            langconf.save();
            langs.put(langconf.lang, langconf);
        }
        return ret.replace("&", "§");
    }

    public static Future<String> getMessageAsync(CommandSender sender, String messagePath, String defaultMessage) {
        FutureTask<String> future = new FutureTask<>(() -> getMessage(sender, messagePath, defaultMessage));
        new Thread(future, "GetMessageTask-" + UUID.randomUUID()).start();
        return future;
    }

    private static String getLangOrDefault(CommandSender sender) {
        if (sender == null) {
            return defaultLang;
        }
        if (sender instanceof Player) {
            Player p = (Player) sender;
            try {
                return p.spigot().getLocale();
            } catch (Exception e) {
                return getLanguage(p);
            }
        } else {
            return defaultLang;
        }

    }

    public static String getLanguage(Player p) {
        try {
            Object obj = getMethod("getHandle", p.getClass()).invoke(p, null);
            Field f = obj.getClass().getDeclaredField("locale");
            f.setAccessible(true);
            return (String) f.get(obj);
        } catch (Exception e) {
        }
        return defaultLang;
    }

    private static Method getMethod(String n, Class<?> c) {
        Method[] arrayOfMethod;
        int j = (arrayOfMethod = c.getDeclaredMethods()).length;
        for (int i = 0; i < j; i++) {
            Method m = arrayOfMethod[i];
            if (m.getName().equals(n)) {
                return m;
            }
        }
        return null;
    }

    public static String noPerm(CommandSender sender) {
        return LangManager.getMessage(sender, "NoPermission", "&cThe computer say no!");
    }

    public static void sendNoPerms(CommandSender sender) {
        sendMessage(sender, "NoPermission", "&cThe computer say no!");
    }
}
