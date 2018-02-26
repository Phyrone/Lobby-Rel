package de.phyrone.lobbyrel.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.phyrone.lobbyrel.hotbar.api.Hotbar;

public class HotbarSwitchEvent extends Event {
	
	private HandlerList HandlerList = new HandlerList();
	
	private Player player;
	
	private Hotbar hotbarFrom;
	
	private Hotbar hotbarTo;
	
	private boolean canceld;
	
	@Override
	public HandlerList getHandlers() {
		return HandlerList;
	}
	
	public HotbarSwitchEvent(Player player, Hotbar hotbarFrom, Hotbar hotbarTo) {
		this.player = player;
		this.hotbarFrom = hotbarFrom;
		this.hotbarTo = hotbarTo;
		this.canceld = false;
	}

	public HandlerList getHandlerList() {
		return HandlerList;
	}

	public void setHandlerList(HandlerList handlerList) {
		HandlerList = handlerList;
	}

	public Hotbar getHotbarFrom() {
		return hotbarFrom;
	}

	public void setHotbarFrom(Hotbar hotbarFrom) {
		this.hotbarFrom = hotbarFrom;
	}

	public Hotbar getHotbarTo() {
		return hotbarTo;
	}

	public void setHotbarTo(Hotbar hotbarTo) {
		this.hotbarTo = hotbarTo;
	}

	public boolean isCanceld() {
		return canceld;
	}

	public void setCanceld(boolean canceld) {
		this.canceld = canceld;
	}

	public Player getPlayer() {
		return player;
	}
	
}
