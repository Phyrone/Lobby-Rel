package de.phyrone.lobbyrel.update;

import com.github.alessiop86.antiantibotcloudflare.AntiAntiBotCloudFlare;
import com.github.alessiop86.antiantibotcloudflare.ApacheHttpAntiAntibotCloudFlareFactory;
import com.github.alessiop86.antiantibotcloudflare.exceptions.AntiAntibotException;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class LobbyDependency {
    private int id;
    private String pluginname;

    private String customURL = null;
    private AntiAntiBotCloudFlare client = new ApacheHttpAntiAntibotCloudFlareFactory().createInstance();
    private File pluginFile = new File("plugin/", pluginname + ".jar");

    public LobbyDependency(int id, String pluginname) {
        this.id = id;
        this.pluginname = pluginname;
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
        IOUtils.copy(new ByteArrayInputStream(client.getByteArrayFromUrl(getURL())),
                new FileOutputStream(pluginFile.getAbsolutePath()));
        System.out.println("[LobbyRel] " + pluginname + " downloaded and saved!");
    }

    private String getURL() throws AntiAntibotException {
        if (customURL != null)
            return customURL;
        ResourcesInfo info = getInfo();
        if (info.external)
            return "https://api.spiget.org/v2/resources/" + String.valueOf(id) + "/download";
        else
            return info.file.url;
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
