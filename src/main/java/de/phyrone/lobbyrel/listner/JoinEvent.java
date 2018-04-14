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

public class JoinEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e) {
        try {
            e.setJoinMessage(null);
        } catch (Exception e1) {
            System.out.println("JoinEvent");
            e1.printStackTrace();
        }
        try {
            PlayerManager.resetPlayerAndData(e.getPlayer());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), () -> Teleporter.toSpawn(e.getPlayer()), 3);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        }
        try {
            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
                if (e.getPlayer().hasPermission("lobby.admin.ideodcheck.cnlobby") && IdeodPreventer.cloudnet_lobby)
                    LangManager.sendMessage(e.getPlayer(), "Message.Ideod.Cloudnet-Lobby",
                            "&4&lYou must set Cloudnet to Groupmode: &6Lobby");
            });
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void onDisconect(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> new InternalOfllineDataManager(e.getPlayer().getUniqueId()).saveAndClear());

    }

}
