package de.phyrone.lobbyrel.player.settings.time;


import org.bukkit.entity.Player;

public class TimeManager {
    private Player player;

    public TimeManager(Player player) {
        this.player = player;
    }

    public void setRelativeTime(int time) {
        player.setPlayerTime(time, false);
    }

    public void resetTime() {
        player.resetPlayerTime();
    }
}
