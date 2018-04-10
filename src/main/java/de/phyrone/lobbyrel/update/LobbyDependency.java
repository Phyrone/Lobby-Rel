package de.phyrone.lobbyrel.update;


import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.LobbyPlugin;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class LobbyDependency {
    private int id;
    private String pluginname;
    private File pluginFile;
    private boolean pluginEnabler = true;
    private String customURL = null;

    public LobbyDependency(int id, String pluginname) {
        this.id = id;
        this.pluginname = pluginname;
        pluginFile = new File("plugins/", pluginname + ".jar");
    }

    public LobbyDependency setCustomURL(String customURL) {
        this.customURL = customURL;
        return this;
    }

    private ResourcesInfo getInfo() throws IOException {
        URLConnection con = new URL("https://api.spiget.org/v2/resources/" + String.valueOf(id)).openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        return new GsonBuilder().create().fromJson(IOUtils.toString(con.getInputStream(), "UTF-8"), ResourcesInfo.class);

    }

    public boolean isAlreadyThere() {
        if (!Bukkit.getPluginManager().isPluginEnabled(LobbyPlugin.getInstance().getDescription().getName()))
            try {
                System.out.println("[Lobby-Rel]PrePluginDetector Found:");
                for (File file : new File("plugins/IGNORE").getParentFile().listFiles())
                    if (file.getName().toLowerCase().endsWith(".jar")) {
                        String name = ZipReader.getPluginName(file);
                        if (LobbyPlugin.getDebug())
                            System.out.println("- " + name);
                        if (name.equalsIgnoreCase(pluginname))
                            return true;
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }

        return Bukkit.getPluginManager().isPluginEnabled(pluginname);
    }

    public void check() {
        if (!isAlreadyThere()) {
            try {
                downloadAndEnable();
            } catch (IOException e) {
                System.err.println("[LobbyRel] Downloading " + pluginname + " failed! (Plugin could not be saved!");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("[LobbyRel] Download,Loading or Enabling " + pluginname + "Failed! (Unknown Error!)");
                e.printStackTrace();
            }
        }
    }

    public LobbyDependency setPluginEnabler(boolean pluginEnabler) {
        this.pluginEnabler = pluginEnabler;
        return this;
    }

    private void downloadAndEnable() throws IOException {
        download();
        PluginManager pm = Bukkit.getPluginManager();
        try {
            pm.loadPlugin(pluginFile);
        } catch (Exception e) {
        }

        try {
            if (pluginEnabler)
                pm.enablePlugin(pm.getPlugin(pluginname));
        } catch (NullPointerException e) {
            throw new NullPointerException("[Plugin-Enable] Plugin not Found");
        }


    }

    public void download() throws IOException {
        System.out.println("[LobbyRel] Downloading " + pluginname + "...");
        InputStream in;
        URL url = new URL(getURL());
        URLConnection hc = url.openConnection();
        hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        in = hc.getInputStream();
        ReadableByteChannel rbc = Channels.newChannel(in);
        FileOutputStream out = new FileOutputStream(pluginFile.getPath());
        out.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        System.out.println("[LobbyRel] " + pluginname + " downloaded and saved!");
    }


    private String getURL() throws IOException {
        if (customURL != null)
            return customURL;
        ResourcesInfo info = getInfo();
        if (!info.external)
            return "https://api.spiget.org/v2/resources/" + String.valueOf(id) + "/download";
        else
            return "https://www.spigotmc.org/" + info.file.url;
    }
}

class ResourcesInfo {
    public boolean external = false;

    public String name = "null";
    public ResourcesFileInfo file = new ResourcesFileInfo();

}

class ResourcesFileInfo {
    public String url = "";
}

class ZipReader {

    public static String getPluginName(File file) {
        try {
            return readYml(file).getString("name", "Unknown");
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

    public static YamlConfiguration readYml(File file) throws Exception {
        ZipFile zipFile = new ZipFile(file);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().equalsIgnoreCase("plugin.yml"))
                continue;
            InputStream stream = zipFile.getInputStream(entry);
            return YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
        }
        throw new FileNotFoundException();
    }
}
