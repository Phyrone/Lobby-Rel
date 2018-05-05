package de.phyrone.lobbyrel.events;

import de.phyrone.lobbyrel.hotbar.api2.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HotbarSwitchEvent extends Event implements Cancellable {

    private HandlerList HandlerList = new HandlerList();

    private Player player;

    private PlayerHotbar hotbar;

    private boolean canceld;

    public HotbarSwitchEvent(Player player, PlayerHotbar newHotbar) {
        this.player = player;
        this.hotbar = newHotbar;
        this.canceld = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HandlerList;
    }

    public HandlerList getHandlerList() {
        return HandlerList;
    }

    public void setHandlerList(HandlerList handlerList) {
        HandlerList = handlerList;
    }

    public PlayerHotbar getHotbar() {
        return hotbar;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return canceld;
    }

    @Override
    public void setCancelled(boolean canceld) {
        this.canceld = canceld;
    }
}
