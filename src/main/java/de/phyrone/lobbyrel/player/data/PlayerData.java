package de.phyrone.lobbyrel.player.data;

import de.phyrone.lobbyrel.hotbar.api.HotbarWrapper;
import de.phyrone.lobbyrel.hotbar.api.PlayerHotbar;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;
import de.phyrone.lobbyrel.player.data.internal.InternalPlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public interface PlayerData {
    boolean isBuilder();

    void setBuilder(boolean builder);

    PlayerHotbar getCurrendHotbar();

    void openHotbar(HotbarWrapper hotbar);

    void setAllowGamemodeChange(boolean b);

    boolean allowGamemodeChange();

    boolean isVisible();

    void setVisible(boolean visible);

    Player getBukkitPlayer();

    int getPlayerHider();

    void setPlayerHider(int hider);

    boolean getJumpPad();

    void setJumpPad(boolean jumpPad);

    boolean getDoubleJump();

    void setDoubleJump(boolean doubleJump);

    boolean isScoreboard();

    void setScoreboard(boolean scoreBoard);

    boolean getSound();

    void setSound(boolean sound);

    HashMap<String, Object> getCustomDatas();

    int getNavigator();

    void setNavigator(int navigator);

    List<String> getTags();

    int getMoney();

    void setMoney(int money);

    void quickSave();

    InternalOfflinePlayerData internalOfflineData();

    InternalPlayerData internalData();

}
