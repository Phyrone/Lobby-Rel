package de.phyrone.lobbyrel.update;


import com.github.alessiop86.antiantibotcloudflare.AntiAntiBotCloudFlare;
import com.github.alessiop86.antiantibotcloudflare.ApacheHttpAntiAntibotCloudFlareFactory;
import com.github.alessiop86.antiantibotcloudflare.exceptions.AntiAntibotException;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public final class LobbyDependency {
    private int id;
    private String pluginname;
    private boolean cloudFlare = true;
    private File pluginFile;
    private String customURL = null;
    private AntiAntiBotCloudFlare client = new ApacheHttpAntiAntibotCloudFlareFactory().createInstance();

    public LobbyDependency(int id, String pluginname) {
        this.id = id;
        this.pluginname = pluginname;
        pluginFile = new File("plugins/", pluginname + ".jar");
    }

    public LobbyDependency setCloudFlare(boolean cloudFlare) {
        this.cloudFlare = cloudFlare;
        return this;
    }

    public LobbyDependency setCustomURL(String customURL) {
        this.customURL = customURL;
        return this;
    }

    private ResourcesInfo getInfo() throws AntiAntibotException {
        return new GsonBuilder().create().fromJson(client.getUrl("https://api.spiget.org/v2/resources/" + String.valueOf(id))
                , ResourcesInfo.class);

    }

    public void check() {
        if (!Bukkit.getPluginManager().isPluginEnabled(pluginname)) {
            try {
                downloadAndEnable();
            } catch (AntiAntibotException e) {
                System.err.println("[LobbyRel] Downloading " + pluginname + " failed! (Plugin could not be downloaded)");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("[LobbyRel] Downloading " + pluginname + " failed! (Plugin could not be saved!");
                e.printStackTrace();
            } catch (InvalidDescriptionException e) {
                System.out.println("[LobbyRel] Plugin " + pluginname + " cloud not be loaded! (Wrong plugin.yml)");
                e.printStackTrace();
            } catch (InvalidPluginException e) {
                System.out.println("[LobbyRel] Plugin " + pluginname + "could not be loaded! (Wrong class!)");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("[LobbyRel] Download,Loading or Enabling " + pluginname + "Failed! (Unknown Error!)");
                e.printStackTrace();
            }
        }
    }

    private void downloadAndEnable() throws IOException, AntiAntibotException, InvalidDescriptionException, InvalidPluginException {
        download();
        PluginManager pm = Bukkit.getPluginManager();
        pm.loadPlugin(pluginFile);
        try {
            pm.enablePlugin(pm.getPlugin(pluginname));
        } catch (NullPointerException e) {
            throw new NullPointerException("[Plugin-Enable] Plugin not Found");
        }


    }

    public void download() throws AntiAntibotException, IOException {
        System.out.println("[LobbyRel] Downloading " + pluginname + "...");
        InputStream in;
        if (needBypass()) {
            in = getWithBypass(getURL());
        } else {
            URL url = new URL(getURL());
            URLConnection hc = url.openConnection();
            //hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            in = hc.getInputStream();
        }
        ReadableByteChannel rbc = Channels.newChannel(in);
        FileOutputStream out = new FileOutputStream(pluginFile.getPath());
        out.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        System.out.println("[LobbyRel] " + pluginname + " downloaded and saved!");
    }

    public InputStream getWithBypass(String url) throws AntiAntibotException {
        return new ByteArrayInputStream(client.getByteArrayFromUrl(url));
    }

    boolean needBypass() throws AntiAntibotException {
        if (customURL != null)
            return cloudFlare;
        else return getInfo().external;
    }

    private String getURL() throws AntiAntibotException {
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
