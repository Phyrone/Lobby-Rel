package de.phyrone.lobbyrel.player.data;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class OfflinePlayerData {
    InternalOfllineDataManager manager;
    UUID uuid;

    public OfflinePlayerData(UUID uuid) {
        manager = new InternalOfllineDataManager(uuid);
        this.uuid = uuid;
    }

    public OfflinePlayerData(Player player) {
        manager = new InternalOfllineDataManager(player.getUniqueId());
        this.uuid = player.getUniqueId();
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
        ScoreboardManager.update(Bukkit.getPlayer(uuid));
        return this;
    }

    public Object getCustomdata(String key) {
        return manager.get().getCustomData();
    }

    public Object getCustomObject(String key, Object def) {
        return manager.get().getCustomData().getOrDefault(key, def);
    }

    public <T> T getCustomdata(String key, T def) {
        if (!manager.get().getCustomData().containsKey(key)) {
            addCustomData(key, def);
            return def;
        }
        Object data = manager.get().getCustomData().get(key);
        try {
            return (T) data;
        } catch (Exception e) {
            return def;
        }
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
