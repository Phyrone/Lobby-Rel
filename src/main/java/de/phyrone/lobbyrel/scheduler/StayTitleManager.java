package de.phyrone.lobbyrel.scheduler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StayTitleManager implements Listener {

    private List<Player> schedPlayers = new ArrayList<>();
    private HashMap<Player, String> titleText = new HashMap<>();


    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (schedPlayers.contains(player))
            schedPlayers.remove(player);
        if (titleText.containsKey(player))
            titleText.remove(player);
    }
}
