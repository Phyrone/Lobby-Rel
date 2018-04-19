package de.phyrone.lobbyrel.player.data.internal;

import de.phyrone.lobbyrel.hotbar.api.Hotbar;

import java.util.HashMap;

public class InternalPlayerData {
    public HashMap<String, Object> customdatas = new HashMap<>();
    //Variablen
    public boolean builder = false;
    public Hotbar currendHotbar = null;
    public boolean allowGamemodeChange = false;
    public boolean visible = true;

}
