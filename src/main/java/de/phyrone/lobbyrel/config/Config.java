package de.phyrone.lobbyrel.config;

import de.phyrone.lobbyrel.lib.Utf8YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    public static Utf8YamlConfiguration conf = new Utf8YamlConfiguration();
    private static File file = new File("plugins/Lobby-Rel", "config.yml");

    static {
        conf.options().copyDefaults(true);
    }

    public static void load() {
        try {
            loadDefault();
            if (file.exists())
                conf.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadDefault() {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void saveAsync() {
        new Thread(() -> saveSync(), "SaveConfigTask").start();
    }

    public synchronized static void saveSync() {
        try {
            conf.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int getInt(String path) {
        return conf.getInt(path);
    }

    public static String getString(String path) {
        return conf.getString(path);
    }

    public static Boolean getBoolean(String path) {
        return conf.getBoolean(path);
    }

    public static Object get(String path) {
        return conf.get(path);
    }

    public static void set(String path, Object value) {
        conf.set(path, value);
    }

    public static Object get(String path, Object byDyfault) {
        if (!conf.contains(path)) {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
        return conf.get(path);
    }

    public static int getInt(String path, int byDyfault) {
        if (conf.contains(path)) {
            return conf.getInt(path, byDyfault);
        } else {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
    }

    public static boolean getBoolean(String path, boolean byDyfault) {
        if (conf.contains(path)) {
            return conf.getBoolean(path, byDyfault);
        } else {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
    }

    public static String getString(String path, String byDyfault) {
        if (conf.contains(path)) {
            return conf.getString(path, byDyfault);
        } else {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
    }

    public static FileConfiguration getConf() {
        return conf;
    }

    public static void setConf(Utf8YamlConfiguration conf) {
        Config.conf = conf;
    }

}
