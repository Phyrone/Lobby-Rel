package de.phyrone.lobbyrel.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.phyrone.lobbyrel.LobbyPlugin;

public class LobbyReloadEvent extends Event{
	
	private static HandlerList HandlerList = new HandlerList();
	
	
	
	@Override
	public HandlerList getHandlers() {
		return HandlerList;
	}public static HandlerList getHandlerList() {
        return HandlerList;
    }
	LobbyPlugin plugin;
	public LobbyReloadEvent(LobbyPlugin plugin) {
		this.plugin = plugin;
	}
	public LobbyPlugin getPlugin() {
		return plugin;
	}
	
}
