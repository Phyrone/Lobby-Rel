package de.phyrone.lobbyrel.lobbyswitcher;

import de.dytanic.cloudnet.api.CloudAPI;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class LobbySwitcher {
    private static final String groupsPath = "LobbySwitcher.Groups";
    private static int runID = -1;
    private static LobbySwitcher instance = null;
    public ArrayList<ConfGroup> groups = new ArrayList<>();
    private String serverName = "Lobby";
    private boolean cloudNet = Bukkit.getPluginManager().isPluginEnabled("CloudNetAPI");
    private HashMap<String, ServerConData> serverCacheData = new HashMap<>();
    private ArrayList<String> bungeeServers = new ArrayList<>();

    private LobbySwitcher() {
        instance = this;
        bungeeServers.clear();
        serverCacheData.clear();
        if (Bukkit.getScheduler().isCurrentlyRunning(runID))
            Bukkit.getScheduler().cancelTask(runID);
        runID = Bukkit.getScheduler().scheduleSyncRepeatingTask(LobbyPlugin.getInstance(), this::updateServers, 0, Config.getInt("LobbySwitcher.UpdateTime", 5 * 20));
        LobbyPlugin.getInstance().sendBungeeMessage(Collections.singletonList("GetServer"));
        if (Config.getConf().contains(groupsPath)) {
            List<String> list = Config.getConf().getStringList(groupsPath);
            for (String group : list) {
                try {
                    String[] groupInfo = group.split(";");
                    groups.add(new ConfGroup(groupInfo[1], SwitchCategory.valueOf(groupInfo[0].toUpperCase())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Config.set(groupsPath, new ArrayList<>(Arrays.asList(
                    "SERVER;PremiumLobby", "STARTSWITH;Lobby", "CLOUDNET;aCloudnetGroup")));
        }
    }

    public static LobbySwitcher getInstance() {
        if (instance == null)
            return new LobbySwitcher();
        return instance;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void updateServers() {
        LobbyPlugin.getInstance().sendBungeeMessage(new ArrayList<>(Collections.singletonList("GetServers")));
    }

    public void updateBungeeServers(String[] servers) {
        bungeeServers.clear();
        serverCacheData.clear();
        for (String s : servers) {
            bungeeServers.add(s);
            LobbyPlugin.getInstance().sendBungeeMessage(Arrays.asList("PlayerCount", s));
        }

    }

    public ArrayList<String> getAllBungeeServers() {
        return bungeeServers;
    }

    public void send(Player player, String server) {
        LobbyPlugin.getInstance().sendBungeeMessage(player, new ArrayList<>(Arrays.asList("Connect", server)));
    }

    private List<String> getServersSorted(SwitchCategory category, String value) {
        List<String> ret = new ArrayList<>();
        try {
            switch (category) {
                case STARTSWITH:
                    getAllBungeeServers().stream().filter(s -> s.toLowerCase().startsWith(value.toLowerCase())).forEach(ret::add);
                    break;
                case SERVER:
                    getAllBungeeServers().stream().filter(value::equalsIgnoreCase).forEach(ret::add);
                    break;
                case CLOUDNET:
                    if (cloudNet)
                        if (value == null) {
                            CloudAPI.getInstance().getServers().iterator().forEachRemaining(serverInfo -> ret.add(serverInfo.getServiceId().getServerId()));
                        } else {
                            CloudAPI.getInstance().getServers(value).iterator().forEachRemaining(serverInfo -> ret.add(serverInfo.getServiceId().getServerId()));
                        }
                    break;
            }
            ret.sort(String::compareTo);
            ret.sort((s1, s2) -> {
                Integer int1 = null;
                Integer int2 = null;
                try {
                    int1 = Integer.parseInt(s1.replaceAll("^[0-9]*$", ""));
                } catch (NumberFormatException ignored) {
                }
                try {
                    int2 = Integer.parseInt(s2.replaceAll("^[0-9]*$", ""));
                } catch (NumberFormatException ignored) {
                }
                if (int1 == null && int2 == null) {
                    return 0;
                } else if (int1 == null) {
                    return -1;
                } else if (int2 == null) {
                    return 1;
                } else {
                    return Integer.compare(int1, int2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public List<String> getServers() {
        ArrayList<String> ret = new ArrayList<>();
        for (ConfGroup group : groups)
            ret.addAll(getServersSorted(group.getCategory(), group.getName()));
        return ret;
    }

    public void setServerData(String servername, ServerConData server) {
        serverCacheData.put(servername, server);

    }

    public ServerConData getData(String serverName) {
        return serverCacheData.getOrDefault(serverName, null);
    }

    public enum SwitchCategory {
        STARTSWITH, SERVER, CLOUDNET, NULL
    }

    public class ConfGroup {
        private String name;
        private SwitchCategory category;

        public ConfGroup(String name, SwitchCategory category) {
            this.category = category;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public SwitchCategory getCategory() {
            return category;
        }

    }

    public class ServerConData {
        private int online;

        public ServerConData(int online) {
            this.online = online;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }
    }

}
