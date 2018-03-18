package de.phyrone.lobbyrel.listner;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.IdeodPreventer;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.warps.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinEvent implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        PlayerManager.resetPlayerAndData(e.getPlayer());
        Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), new Runnable() {

            @Override
            public void run() {
                Teleporter.toSpawn(e.getPlayer());
            }
        }, 3);
        new BukkitRunnable() {
            @Override
            public void run() {
                //Felix F. and Kansy | Bjarne 칸시
                if (e.getPlayer().hasPermission("lobby.admin.ideodcheck.cnlobby") && IdeodPreventer.cloudnet_lobby) {
                    LangManager.sendMessage(e.getPlayer(), "Message.Ideod.Cloudnet-Lobby",
                            "&4&lYou must set Cloudnet to Groupmode: &6Lobby");

                }
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());

    }

    @EventHandler
    public void onDisconect(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {

            @Override
            public void run() {
                new InternalOfllineDataManager(e.getPlayer().getUniqueId()).saveAndClear();
            }
        });

    }

}
