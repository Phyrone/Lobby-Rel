package de.phyrone.lobbyrel.update;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.alessiop86.antiantibotcloudflare.ApacheHttpAntiAntibotCloudFlareFactory;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.player.data.lang.LangManager;

public class UpdateManager {
	private static boolean update = false;
	public static void check() {
		try {
			update = !new ApacheHttpAntiAntibotCloudFlareFactory().createInstance()
			.getUrl("https://servercontent.phyrone.de/lobby/updater/v1/")
			.equalsIgnoreCase(LobbyPlugin.getInstance().getDescription().getVersion());
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void init() {
		if(Config.getBoolean("Updater.Enabled", true))
		check();
		if(Config.getBoolean("Updater.UpdateOnStart", false)&&needUpdate()) {
			updateAsync();
		}
		
	}public static boolean needUpdate() {
		return update;
	}public static void informAdminAboutUpdates(Player player) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(Config.getBoolean("Updater.InformAdmin", true) && player.hasPermission("lobby.update") && needUpdate()) {
					FancyMessage m = new FancyMessage(LobbyPlugin.getPrefix()).then(" ").then(LangManager.getMessage(player, "Message.Update.Inform", "&aa new &6Update &ais available "))
							.then(LangManager.getMessage(player, "Message.Update.Button.Message", "&8[&6Update&8]")).tooltip(LangManager.getMessage(player, "Message.Update.Button.Tooltip", "&5Click to Update"))
							.command("/lobbysystem update");
					new BukkitRunnable() {
						
						@Override
						public void run() {
							m.send(player);
						}
					}.runTask(LobbyPlugin.getInstance());
				}	
			}
		}.runTaskAsynchronously(LobbyPlugin.getInstance());
	}public static void updateCMD(CommandSender s) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Player p = null;
				if(s instanceof Player) p = (Player) s;
				if(Config.getBoolean("Updater.AllowCMD", true)&&s.hasPermission("lobby.update")) {
					if(needUpdate()) {
						LangManager.sendMessage(p, "Message.Update.Downloading", "&6Downloading...");
						System.out.println(LobbyPlugin.getPrefix()+" Downloading...");
						if(update()) {
							LangManager.sendMessage(p, "Message.Update.Download-Finish", "&5Update Finish you can Restart now!");
							update = false;
						}else {
							LangManager.sendMessage(p, "Message.Update.Download-Failed", "&4&lUpdate Failed please try again later!");
						}
						
						System.out.println(LobbyPlugin.getPrefix()+" Update Downloaded...");
					}else {
						LangManager.sendMessage(p, "Message.Update.Alredy-Uptodate", "&c Already Uptodate!");
					}
				}else {
					s.sendMessage(LangManager.noPerm(p));
				}

			}
		}.runTaskAsynchronously(LobbyPlugin.getInstance());

	}@SuppressWarnings("resource")
	public static boolean update() {
		File file = new File("plugins/"+LobbyPlugin.getInstance().getDescription().getName().toString()+".jar");
		try {
			URL website = new URL("https://api.spiget.org/v2/resources/49126/download");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(file.getPath().toString());
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			return true;
			} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}public static void updateAsync() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				update();
				
			}
		}, "UpdateTask").start();
	}

}
