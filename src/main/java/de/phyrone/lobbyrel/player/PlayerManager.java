package de.phyrone.lobbyrel.player;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.events.LobbyResetPlayerEvent;
import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.hotbar.LoadingHotbar;
import de.phyrone.lobbyrel.hotbar.customitems.ExternalItemsManager;
import de.phyrone.lobbyrel.player.data.OfflinePlayerData;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.player.data.offline.InternalOfllineDataManager;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import de.phyrone.lobbyrel.update.UpdateManager;
import de.phyrone.lobbyrel.warps.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {
    static File file = new File("plugins/Lobby-Rel/data/players.json");
    static HashMap<UUID, PlayerData> datas = new HashMap<UUID, PlayerData>();
    static ArrayList<ResetHandler> handlers = new ArrayList<>();

    public static void resetHandlers() {
        handlers.clear();
    }

    public static PlayerData getPlayerData(Player player) {
        return datas.getOrDefault(player.getUniqueId(), new PlayerData(player));
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return datas.getOrDefault(uuid, new PlayerData(uuid));
    }

    public static void setPlayerData(UUID uuid, PlayerData playerdata) {
        datas.put(uuid, playerdata);
    }

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
        new BukkitRunnable() {

            @Override
            public void run() {
                ExternalItemsManager.checkExternal(player);
                new LoadingHotbar(player).open();
            }
        }.runTaskLaterAsynchronously(LobbyPlugin.getInstance(), 3);
        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    player.setAllowFlight(new OfflinePlayerData(player).getDoubleJump());
                    player.setFlying(false);
                } catch (Exception e) {
                    player.sendMessage("§8[§6Lobby§8] §4§lError: §cDoubleJump Disabled!");
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
            }
        }.runTaskLater(LobbyPlugin.getInstance(), 10);
        Bukkit.getScheduler().runTaskLater(LobbyPlugin.getInstance(), new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    getPlayerData(player).setAllowGamemodeChange(true).save();
                    player.setGameMode(GameMode.ADVENTURE);
                    getPlayerData(player).setAllowGamemodeChange(false).save();
                } catch (Exception e) {
                    System.err.println("Error : Set Gamemode");
                    e.printStackTrace();
                }
            }
        }, 3);
        UpdateManager.informAdminAboutUpdates(player);

    }

    public static void resetPlayerAndData(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                resetPlayerData(player);
                resetPlayer(player);
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());
    }

    public static void resetPlayerData(Player player) {
        new InternalOfllineDataManager(player.getUniqueId()).load();
        datas.put(player.getUniqueId(), new PlayerData(player));
    }

    public static boolean isBuilder(Player player) {
        return getPlayerData(player).isBuilder();
    }

    public interface ResetHandler {
        void onReset(Player player);
    }
}
