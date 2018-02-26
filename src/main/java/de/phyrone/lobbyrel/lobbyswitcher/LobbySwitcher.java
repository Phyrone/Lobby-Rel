package de.phyrone.lobbyrel.lobbyswitcher;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LobbySwitcher {
	public static final String groupsPath = "LobbySwitcher.Groups";
	private String serverName = "Lobby";
	private boolean cloudNet = Bukkit.getPluginManager().isPluginEnabled("CloudNetAPI");
	static int runID = -1;
	private static LobbySwitcher instance = null;
	private HashMap<String, ServerConData> serverCacheData= new HashMap<>();
	private ArrayList<String> bungeeServers = new ArrayList<>();
	public ArrayList<ConfGroup> groups = new ArrayList<>();
	public LobbySwitcher() {
		instance = this;
		bungeeServers.clear();
		serverCacheData.clear();
		if(Bukkit.getScheduler().isCurrentlyRunning(runID))
			Bukkit.getScheduler().cancelTask(runID);
		runID = Bukkit.getScheduler().scheduleSyncRepeatingTask(LobbyPlugin.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				updateServers();
			}
		}, 0, Config.getInt("LobbySwitcher.UpdateTime",5*20));
		LobbyPlugin.getInstance().sendBungeeMessage(Arrays.asList("GetServer"));
		if(Config.getConf().contains(groupsPath)) {
			List<String> list = Config.getConf().getStringList(groupsPath);
			for(String group:list) {
				try {
					String[] groupInfo = group.split(";");
					groups.add(new ConfGroup(groupInfo[1],SwitchCategory.valueOf(groupInfo[0].toUpperCase())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else {
			Config.set(groupsPath, new ArrayList<String>(Arrays.asList(
					"SERVER;PremiumLobby", "STARSTWITH;Lobby", "CLOUDNET;aCloudnetGroup")));
		}
	}public static LobbySwitcher getInstance() {
		if(instance == null)
			return new LobbySwitcher();
		return instance;
	}public void setServerName(String serverName) {
		this.serverName = serverName;
	}public String getServerName() {
		return serverName;
	}
	public void updateServers() {
		LobbyPlugin.getInstance().sendBungeeMessage(new ArrayList<>(Arrays.asList("GetServers")));
	}public void updateBungeeServers(String[] servers) {
		bungeeServers.clear();
		serverCacheData.clear();
		for(String s:servers) {
			bungeeServers.add(s);
			LobbyPlugin.getInstance().sendBungeeMessage(Arrays.asList("PlayerCount",s));
		}
			
	}public ArrayList<String> getAllBungeeServers() {
		return bungeeServers;
	}public void send(Player player,String server) {
		LobbyPlugin.getInstance().sendBungeeMessage(player, new ArrayList<>(Arrays.asList("Connect",server)));
	}public List<String> getServersSorted(SwitchCategory category,String value) {
		List<String> ret = new ArrayList<String>();
		try {
			switch (category) {
			case STARSTWITH:
				for(String s: bungeeServers)
					if(s.toLowerCase().startsWith(value.toLowerCase()))
						ret.add(s);
				break;
			case SERVER:
				for(String s:bungeeServers)
					if(s.equalsIgnoreCase(value))
						ret.add(s);
				break;
			case CLOUDNET:
				if(cloudNet)
					if(value ==null) {
						for(ServerInfo s:CloudAPI.getInstance().getServers())
							if(s != null)
							ret.add(s.getServiceId().getServerId());
					}else {
						for(ServerInfo s:CloudAPI.getInstance().getServers(value))
							if(s != null)
							ret.add(s.getServiceId().getServerId());
					}
				break;
			}java.util.Collections.sort(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}public List<String> getServers() {
		ArrayList<String> ret = new ArrayList<String>();
		for(ConfGroup group:groups)
			ret.addAll(getServersSorted(group.getCategory(), group.getName()));
		return ret;
	}public void setServerData(String servername,ServerConData server) {
		serverCacheData.put(servername, server);
		
	}public ServerConData getData(String serverName) {
		return serverCacheData.getOrDefault(serverName, null);
	}
	public enum SwitchCategory{
		STARSTWITH,SERVER,CLOUDNET,NULL
	}public class ConfGroup{
		private String name;
		private SwitchCategory category;
		public ConfGroup(@Nonnull String name,@Nonnull SwitchCategory category) {
			this.category = category;
			this.name = name;
		}public String getName() {
			return name;
		}public SwitchCategory getCategory() {
			return category;
		}

	}public class ServerConData{
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
