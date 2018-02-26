package de.phyrone.lobbyrel.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LobbyResetPlayerEvent extends Event{
	Player p;
	public LobbyResetPlayerEvent(Player player) {
		p = player;
	}
	public Player getPlayer() {return p;}
	private static HandlerList HandlerList = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return HandlerList;
	}public static HandlerList getHandlerList() {
        return HandlerList;
    }

}
