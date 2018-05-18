package de.phyrone.lobbyrel.scoreborad;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class ScoreboardManager {
    HashMap<Player, InternalPlayerScoreboard> scoreboards = new HashMap<>();

    public static void init() {
        for (Player player : Bukkit.getOnlinePlayers())
            clearScoreboard(player);
    }

    public static void clearScoreboard(Player player) {
        if (player.getScoreboard() != null) {
            Scoreboard board = player.getScoreboard();
            for (DisplaySlot slot : DisplaySlot.values())
                board.clearSlot(slot);
            for (Team team : board.getTeams())
                team.unregister();
            for (Objective objective : board.getObjectives())
                objective.unregister();
        }
    }
}

class InternalPlayerScoreboard {
    Scoreboard scoreboard;
    HashMap<Integer, Team> lines = new HashMap<>();

    public void registerLines(int amount) {
        for (int i = 0; i < amount; i++) {
            int score = 15 - i;

        }
    }
}
