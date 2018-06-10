package de.phyrone.lobbyrel;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api.HotbarManager;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.HashMap;

public class SupportPluginsManager {
    public static String cfgPath = "PluginSupport.";
    static HashMap<SupportedPlugin, Boolean> enbledList = new HashMap<>();

    public static void check() {
        for (SupportedPlugin supportedPlugin : SupportedPlugin.values()) {
            if (Config.getBoolean("PluginSuport." + supportedPlugin.pluginname + ".Enabled", true)
                    && Bukkit.getPluginManager().isPluginEnabled(supportedPlugin.getPluginname())) {
                if (supportedPlugin.getOnDetect() != null)
                    Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), supportedPlugin.getOnDetect());
                enbledList.put(supportedPlugin, true);
            }
        }
    }

    public enum SupportedPlugin {
        ULTRACOSMETICS("UltraCosmetics", new UltraCosmeticsSupport()),
        BETTERNICK("BetterNick", new BetterNickSupport()),
        VAULT("Vault", new VaultSupport());
        String pluginname;
        Runnable onDetect;

        SupportedPlugin(String pluginname, Runnable onDetect) {
            this.pluginname = pluginname;
            this.onDetect = onDetect;
        }

        public boolean isEnabled() {
            return enbledList.getOrDefault(this, false);
        }

        public Runnable getOnDetect() {
            return onDetect;
        }

        public String getPluginname() {
            return pluginname;
        }
    }
}

class VaultSupport implements Runnable {

    @Override
    public void run() {

    }
}

class UltraCosmeticsSupport implements Runnable {

    @Override
    public void run() {
        System.out.println("[Lobby-Rel] UltraCosmetics Detected!");
        System.out.println("      -> Support comming Soon :-)");
    }
}


class BetterNickSupport implements Runnable {
    BetterNickAPI api;
    int slot = 10;

    @Override
    public void run() {
        if (true) {
            ItemsConfig cfg = ItemsConfig.getInstance();
            api = BetterNick.getApi();
            slot = Config.getInt("NickSystem.Item.Slot", 10);
            LobbyItem noPermItem = cfg.getItem("hotbar.nick:noPermission",
                    new LobbyItem(Material.AIR)
            );
            LobbyItem nickOffItem = cfg.getItem("hotbar.nick:off",
                    new LobbyItem(Material.NAME_TAG).setDisplayName("&4AutoNick OFF")
            );
            LobbyItem nickOnItem = cfg.getItem("hotbar.nick:on",
                    new LobbyItem(Material.NAME_TAG).setGlow(true).setDisplayName("&aAutoNick ON")
            );
            MainHotbar.addItem(slot, new HotbarItem(new ItemBuilder(Material.BEDROCK).displayname("&4&lERROR").build())
                    .setItemUpdater(player -> {
                        try {
                            System.out.println(api.hasPlayerAutoNick(player));
                            if (!player.hasPermission("lobby.nick.item"))
                                return noPermItem.getAsItemStack(player);
                            else {
                                if (api.hasPlayerAutoNick(player)) {
                                    return nickOnItem.getAsItemStack(player);
                                } else {
                                    return nickOffItem.getAsItemStack(player);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error Get Nick-Item");
                            e.printStackTrace();
                            return null;
                        }
                    }).setClick((player, rightClick) -> {
                        if (rightClick && player.hasPermission("lobby.nick.item")) {
                            api.setPlayerAutoNick(player, !api.hasPlayerAutoNick(player));
                            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(),
                                    () -> HotbarManager.getHotbar(player).update());
                        }

                    }));
            PlayerManager.addHandler(player -> {
                if (!player.hasPermission("lobby.nick.item")) {
                    api.setPlayerAutoNick(player, false);
                }
            });
        }

    }
}
