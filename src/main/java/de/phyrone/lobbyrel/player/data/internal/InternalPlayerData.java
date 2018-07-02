package de.phyrone.lobbyrel.player.data.internal;

import de.phyrone.lobbyrel.hotbar.api.PlayerHotbar;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class InternalPlayerData {
    public InternalPlayerData(Player player) {

    }
    public HashMap<String, Object> customdatas = new HashMap<>();
    //Variablen
    public boolean builder = false;
    public boolean allowGamemodeChange = false;
    public boolean visible = true;
    public PlayerHotbar.MutiPageDisplay hotbarMutiPageDisplay = PlayerHotbar.MutiPageDisplay.XP;

}
