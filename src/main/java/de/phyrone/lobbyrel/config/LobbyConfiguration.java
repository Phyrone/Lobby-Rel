package de.phyrone.lobbyrel.config;

import de.phyrone.lobbyrel.lib.RandomString;
import de.phyrone.lobbyrel.lib.Utf8YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LobbyConfiguration {
    public Utf8YamlConfiguration conf = new Utf8YamlConfiguration();
    private File file;

    public LobbyConfiguration(File file) {
        if (file.isDirectory())
            file = new File(file.getPath(), new RandomString(16).nextString() + ".yml");
        this.file = file;
    }

    public void load() {
        try {
            loadDefault();
            if (file.exists())
                conf.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getStringList(String path, List<String> byDefault) {
        if (conf.contains(path))
            return conf.getStringList(path);
        else {
            conf.set(path, byDefault);
            saveAsync();
            return byDefault;
        }

    }

    private void loadDefault() {
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

    public void saveAsync() {
        new Thread(() -> saveSync(), "SaveConfigTask").start();
    }

    public synchronized void saveSync() {
        try {
            conf.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getInt(String path) {
        return conf.getInt(path);
    }

    public String getString(String path) {
        return conf.getString(path);
    }

    public Boolean getBoolean(String path) {
        return conf.getBoolean(path);
    }

    public Object get(String path) {
        return conf.get(path);
    }

    public void set(String path, Object value) {
        conf.set(path, value);
    }

    public Object get(String path, Object byDyfault) {
        if (!conf.contains(path)) {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
        return conf.get(path);
    }

    public int getInt(String path, int byDyfault) {
        if (conf.contains(path)) {
            return conf.getInt(path, byDyfault);
        } else {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
    }

    public boolean getBoolean(String path, boolean byDyfault) {
        if (conf.contains(path)) {
            return conf.getBoolean(path, byDyfault);
        } else {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
    }

    public String getString(String path, String byDyfault) {
        if (conf.contains(path)) {
            return conf.getString(path, byDyfault);
        } else {
            conf.set(path, byDyfault);
            saveAsync();
            return byDyfault;
        }
    }

    public FileConfiguration getConf() {
        return conf;
    }

    public void setConf(Utf8YamlConfiguration conf) {
        this.conf = conf;
    }

}
