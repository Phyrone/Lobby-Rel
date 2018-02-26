package de.phyrone.lobbyrel.player.data;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class OfflinePlayerData {
    InternalOfllineDataManager manager;

    public OfflinePlayerData(UUID uuid) {
        manager = new InternalOfllineDataManager(uuid);
    }

    public OfflinePlayerData(Player player) {
        manager = new InternalOfllineDataManager(player.getUniqueId());
    }

    public void load() {
        manager.load();
    }

    public int getNavigator() {
        return manager.get().getNavigator();
    }

    public void setNavigator(int id) {
        manager.set(manager.get().setNavigator(id));
        quickSave();
    }

    public void SetPlayerHider(int mode) {
        manager.set(manager.get().setPlayerHider(mode));
        quickSave();
    }

    public int getPlayerHider() {
        return manager.get().getPlayerHider();
    }

    public void saveToConf() {
        manager.saveAndClear();
    }

    public void quickSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                manager.save();
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());

    }

    public boolean getSound() {
        return manager.get().getSound();

    }

    public void setSound(boolean b) {
        manager.set(manager.get().setSound(b));
        quickSave();
    }

    public boolean getScoreboard() {
        return manager.get().getScoreboard();
    }

    public OfflinePlayerData setScoreboard(boolean enabled) {
        manager.set(manager.get().setScoreboard(enabled));
        quickSave();
        return this;
    }

    public Object getCustomdata(String key) {
        return manager.get().getCustomData();
    }

    public Object getCustomdata(String key, Object def) {
        return manager.get().getCustomData().getOrDefault(key, def);
    }

    public Object getCustomdata() {
        return manager.get().getCustomData();
    }

    public OfflinePlayerData setCustomData(HashMap<String, Object> customdata) {
        manager.set(manager.get().setCustomData(customdata));
        quickSave();
        return this;
    }

    public OfflinePlayerData addCustomData(String key, Object data) {
        HashMap<String, Object> dt = manager.get().getCustomData();
        dt.put(key, data);
        manager.set(manager.get().setCustomData(dt));
        return this;
    }

    public boolean getJumpPads() {
        return manager.get().isJumpPad();
    }

    public boolean getDoubleJump() {
        return manager.get().isDoubleJump();
    }

    public OfflinePlayerData setDoubleJump(boolean value) {
        manager.set(manager.get().setDoubleJump(value));
        quickSave();
        return this;
    }

    public OfflinePlayerData setJumpPad(boolean value) {
        manager.set(manager.get().setJumpPad(value));
        quickSave();
        return this;
    }
}
