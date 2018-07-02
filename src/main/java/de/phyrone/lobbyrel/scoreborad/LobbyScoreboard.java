package de.phyrone.lobbyrel.scoreborad;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.Callable;

public interface LobbyScoreboard {
    void setSize(int lines);

    void setLine(int line, String content) throws IndexOutOfBoundsException;

    void setLine(int line, Callable<String> callable) throws IndexOutOfBoundsException;

    void setEnabled(boolean enabled);

    class Internal {
        Player player;
        Scoreboard scoreboard;

        public Internal(Player player) {
            this.player = player;

        }

        public void registerBoard() {

        }
    }
}
