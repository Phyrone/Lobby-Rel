package de.phyrone.lobbyrel.update;

import de.domedd.betternick.BetterNick;
import de.domedd.betternick.api.betternickapi.BetterNickAPI;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class SupportPluginsManager {
    public static String cfgPath = "PluginSupport.";

    public static void check() {
        for (SupportedPlugin supportedPlugin : SupportedPlugin.values()) {
            if (Bukkit.getPluginManager().isPluginEnabled(supportedPlugin.getPluginname()))
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), supportedPlugin.getOnDetect());
        }
    }

    public enum SupportedPlugin {
        ULTRACOSMETICS("UltraCosmetics", new UltraCosmeticsSupport()),
        BETTERNICK("BetterNick", new BetterNickSupport());
        String pluginname;
        Runnable onDetect;

        SupportedPlugin(String pluginname, Runnable onDetect) {
            this.pluginname = pluginname;
            this.onDetect = onDetect;
        }

        public Runnable getOnDetect() {
            return onDetect;
        }

        public String getPluginname() {
            return pluginname;
        }
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
        if (Config.getBoolean("NickSystem.Enabled", true)) {
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
                    .setSelect(player -> {
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
                            System.err.println("Error Get Nickitem");
                            e.printStackTrace();
                            return null;
                        }
                    }).setClick((event, rightClick) -> {
                        if (rightClick && event.getPlayer().hasPermission("lobby.nick.item")) {
                            api.setPlayerAutoNick(event.getPlayer(), !api.hasPlayerAutoNick(event.getPlayer()));
                            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(),
                                    () -> MainHotbar.hotbar.updateItem(event.getPlayer())
                            );
                            /*event.getPlayer().getInventory().setItem(event.getPlayer().getInventory().getHeldItemSlot(), api.hasPlayerAutoNick(
                                                                      event.getPlayer()) ? nickOffItem.getAsItemStack(event.getPlayer()) : nickOnItem.getAsItemStack(event.getPlayer())*/


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
