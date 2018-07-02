package de.phyrone.lobbyrel.player.data.internal;

import de.phyrone.lobbyrel.hotbar.api.PlayerHotbar;

import java.util.HashMap;

public class InternalPlayerData {
    public HashMap<String, Object> customdatas = new HashMap<>();
    //Variablen
    public boolean builder = false;
    public boolean allowGamemodeChange = false;
    public boolean visible = true;
    public PlayerHotbar.MutiPageDisplay hotbarMutiPageDisplay = PlayerHotbar.MutiPageDisplay.XP;

}
