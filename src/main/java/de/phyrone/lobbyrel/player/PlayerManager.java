package de.phyrone.lobbyrel.player;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyResetPlayerEvent;
import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.hotbar.LoadingHotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarManager;
import de.phyrone.lobbyrel.hotbar.api.HotbarWrapper;
import de.phyrone.lobbyrel.hotbar.api.PlayerHotbar;
import de.phyrone.lobbyrel.hotbar.customitems.ExternalItemsManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.data.internal.InternalOfflinePlayerData;
import de.phyrone.lobbyrel.player.data.internal.InternalPlayerData;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import de.phyrone.lobbyrel.storage.StorageManager;
import de.phyrone.lobbyrel.update.UpdateManager;
import de.phyrone.lobbyrel.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    static File file = new File("plugins/Lobby-Rel/data/players.json");
    static HashMap<UUID, InternalPlayerData> internalDatas = new HashMap<>();
    static ArrayList<ResetHandler> handlers = new ArrayList<>();
    private static HashMap<UUID, InternalOfflinePlayerData> internalOfflineDatasCache = new HashMap<>();
    private static HashMap<UUID, PlayerData> dataCache = new HashMap<>();

    public static void resetHandlers() {
        handlers.clear();
    }

    /**
     * @param player Player
     * @return the lobbymeta of the Player(like builder settings and more)
     */
    public static PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId());
    }

    /**
     * @param uuid UUID of the Player
     * @return the lobbymeta of the Player(like builder settings and more)
     */
    public static PlayerData getPlayerData(UUID uuid) {
        try {
            if (!dataCache.containsKey(uuid))
                dataCache.put(uuid, newPlayerData(uuid));
            else dataCache.computeIfAbsent(uuid, PlayerManager::newPlayerData);
            return dataCache.getOrDefault(uuid, newPlayerData(uuid));
        } catch (Exception e) {
            System.err.println("Error Load PlayerDataAPI");
            e.printStackTrace();
            return null;
        }
    }

    public static InternalPlayerData getInternalPlayerData(UUID uuid) {
        if (!internalDatas.containsKey(uuid))
            internalDatas.put(uuid, new InternalPlayerData());
        return internalDatas.getOrDefault(uuid, new InternalPlayerData());
    }

    /**
     * NOT Recomend to use!!!
     *
     * @param uuid of the player
     * @return the direct playerdata (difficult to change)
     */
    public static InternalOfflinePlayerData getInternalOfflinePlayerData(UUID uuid) {
        if (!internalOfflineDatasCache.containsKey(uuid))
            internalOfflineDatasCache.put(uuid, StorageManager.loadPlayerData(uuid));
        return internalOfflineDatasCache.get(uuid);
    }

    /**
     * @param handler here you can set a Reset-Handler as an alternative to the Reset-Event
     */
    public static void addHandler(ResetHandler handler) {
        handlers.add(handler);
    }

    public static void removeHandler(ResetHandler handler) {
        handlers.remove(handler);
    }

    public static void resetPlayer(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new LobbyResetPlayerEvent(player));
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ResetHandler handler : handlers)
                    try {
                        handler.onReset(player);
                    } catch (Exception e) {
                    }
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());
        new BukkitRunnable() {

            @Override
            public void run() {

                int maxh = Config.getInt("maxLife", 6);
                int food = Config.getInt("maxFood", 20);
                if (food > 20) {
                    food = 20;
                }
                player.setMaxHealth(maxh);
                player.setHealth(maxh);
                player.setFoodLevel(20);
                player.setTotalExperience(0);
                player.setLevel(0);
                player.setExp(0);
                player.setCompassTarget(WarpManager.getSpawn().getLocation());
                PlayerHiderManager.updateForOthers(player);
                PlayerHiderManager.update(player);
                ScoreboardManager.update(player);

            }
        }.runTaskLaterAsynchronously(LobbyPlugin.getInstance(), 1);
        Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), () -> {
            ExternalItemsManager.checkExternal(player);
            new LoadingHotbar(player).open();
        }, 3);

        Bukkit.getScheduler().runTaskLater(LobbyPlugin.getInstance(), () -> {
            try {
                player.setAllowFlight(getPlayerData(player).getDoubleJump());
                player.setFlying(false);
            } catch (Exception e) {
                player.sendMessage("§8[§6Lobby§8] §4§lError: §cDoubleJump Disabled!");
                LobbyPlugin.getLobbyLogger().warning("Error Loading DoubleJump");
                e.printStackTrace();
                player.setAllowFlight(false);
                player.setFlying(false);
                getPlayerData(player).setDoubleJump(false);
            }
        }, 10);

        Bukkit.getScheduler().runTaskLater(LobbyPlugin.getInstance(), () -> {
            try {
                getPlayerData(player).setAllowGamemodeChange(true);
                player.setGameMode(GameMode.ADVENTURE);
                getPlayerData(player).setAllowGamemodeChange(false);
            } catch (Exception e) {
                System.err.println("Error : Set Gamemode");
                e.printStackTrace();
            }
        }, 3);
        UpdateManager.informAdminAboutUpdates(player);

    }

    public static void resetPlayerAndData(Player player) {
        resetPlayerData(player);
        new Thread(() -> resetPlayer(player), "ResetPlayerThread-" + player.getUniqueId()).start();
    }

    public static void resetPlayerData(Player player) {
        try {
            dataCache.remove(player.getUniqueId());
            internalDatas.remove(player.getUniqueId());
            deleteDataFromCache(player.getUniqueId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOfflineData(UUID uuid) {
        if (internalOfflineDatasCache.containsKey(uuid))
            StorageManager.save(uuid, internalOfflineDatasCache.get(uuid));
    }


    public static void deleteDataFromCache(UUID uuid) {
        internalOfflineDatasCache.remove(uuid);
    }


    public static void saveAndClear(UUID uuid) {
        saveOfflineData(uuid);
        deleteDataFromCache(uuid);
    }

    /**
     * @param uuid of the Player
     * @return new Player Data API
     */
    private static PlayerData newPlayerData(UUID uuid) {
        return new PlayerData() {
            @Override
            public int getPlayerHider() {
                return getInternalOfflinePlayerData(uuid).PlayerHider;
            }

            @Override
            public void setPlayerHider(int hider) {
                getInternalOfflinePlayerData(uuid).PlayerHider = hider;
                quickSave();
            }

            @Override
            public boolean getJumpPad() {
                return getInternalOfflinePlayerData(uuid).JumpPad;
            }

            @Override
            public void setJumpPad(boolean jumpPad) {
                getInternalOfflinePlayerData(uuid).JumpPad = jumpPad;
                quickSave();
            }

            @Override
            public boolean getDoubleJump() {
                return getInternalOfflinePlayerData(uuid).DoubleJump;
            }

            @Override
            public void setDoubleJump(boolean doubleJump) {
                getInternalOfflinePlayerData(uuid).DoubleJump = doubleJump;
                quickSave();
                Bukkit.getScheduler().runTaskLater(LobbyPlugin.getInstance(), () -> {
                    if (Bukkit.getPlayer(uuid) != null) {
                        Player player = Bukkit.getPlayer(uuid);
                        player.setAllowFlight(doubleJump);
                        player.setFlying(false);
                    }
                }, 3);
            }

            @Override
            public boolean isScoreboard() {
                return getInternalOfflinePlayerData(uuid).Scoreboard;

            }

            @Override
            public void setScoreboard(boolean scoreBoard) {
                getInternalOfflinePlayerData(uuid).Scoreboard = scoreBoard;
                quickSave();
                if (Bukkit.getPlayer(uuid) != null)
                    Bukkit.getScheduler().runTaskLaterAsynchronously(LobbyPlugin.getInstance(), () ->
                            ScoreboardManager.update(Bukkit.getPlayer(uuid)), 5);
            }

            @Override
            public boolean getSound() {
                return getInternalOfflinePlayerData(uuid).Sound;
            }

            @Override
            public void setSound(boolean sound) {
                getInternalOfflinePlayerData(uuid).Sound = sound;
                quickSave();
            }

            @Override
            public HashMap<String, Object> getCustomDatas() {
                return getInternalOfflinePlayerData(uuid).CustomData;
            }

            @Override
            public int getNavigator() {
                return getInternalOfflinePlayerData(uuid).Navigator;
            }

            @Override
            public void setNavigator(int navigator) {
                getInternalOfflinePlayerData(uuid).Navigator = navigator;
                quickSave();
            }

            @Override
            public List<String> getTags() {
                return getInternalOfflinePlayerData(uuid).Tags;
            }

            @Override
            public void quickSave() {
                Runnable run = () -> StorageManager.save(uuid, getInternalOfflinePlayerData(uuid));
                if (Bukkit.getPluginManager().isPluginEnabled(LobbyPlugin.getInstance()))
                    Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), run);
                else run.run();
            }

            @Override
            public boolean isBuilder() {
                return getInternalPlayerData(uuid).builder;
            }

            @Override
            public void setBuilder(boolean builder) {
                getInternalPlayerData(uuid).builder = builder;
            }

            @Override
            public PlayerHotbar getCurrendHotbar() {
                return HotbarManager.getHotbar(getBukkitPlayer());
            }

            @Override
            public void openHotbar(HotbarWrapper hotbar) {
                HotbarManager.setHotbar(getBukkitPlayer(), hotbar);
            }


            @Override
            public void setAllowGamemodeChange(boolean allowGamemodeChange) {
                getInternalPlayerData(uuid).allowGamemodeChange = allowGamemodeChange;
            }

            @Override
            public boolean allowGamemodeChange() {
                return getInternalPlayerData(uuid).allowGamemodeChange;
            }

            @Override
            public boolean isVisible() {
                return getInternalPlayerData(uuid).visible;
            }

            @Override
            public void setVisible(boolean visible) {
                getInternalPlayerData(uuid).visible = visible;
                quickSave();
            }

            @Override
            public Player getBukkitPlayer() {
                return Bukkit.getPlayer(uuid);
            }
        };
    }

    /**
     *
     */
    public interface ResetHandler {
        void onReset(Player player);
    }
}
