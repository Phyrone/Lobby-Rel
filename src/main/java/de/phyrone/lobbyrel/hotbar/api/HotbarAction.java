package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.entity.Player;

public class HotbarAction {
    public interface OpenListner {
        void onOpen(Player player);
    }

    public interface CloseListner {
        void onClose(Player player);
    }

    public interface SwitchListner {
        void onSwitchSite(Player player, int from, int to);
    }


}
