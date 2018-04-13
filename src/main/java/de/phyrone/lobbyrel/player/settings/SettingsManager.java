package de.phyrone.lobbyrel.player.settings;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.navigator.Navigator;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.OfflinePlayerData;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsManager {
    static List<SettingsModule> modules = new ArrayList<>();

    public static void addModule(SettingsModule module) {
        modules.add(module);
    }

    public static void addModules(SettingsModule... modules) {
        SettingsManager.modules.addAll(Arrays.asList(modules));
    }

    public static List<SettingsModule> getModulesAsList() {
        return modules;
    }

    public static void setup() {
        modules.clear();
        ItemsConfig cfg = ItemsConfig.getInstance();
        addModules(
                //Navigator
                new SettingsModule(new SettingsModuleAction() {
                    @Override
                    public List<ClickableItem> getOptions(Player player) {
                        ArrayList<ClickableItem> ret = new ArrayList<>();
                        int i = 0;
                        for (Navigator nav : NavigatorManager.getNavigators()) {
                            final int i2 = i;
                            ret.add(ClickableItem.of(nav.getItem().getAsItemStack(player)
                                    , e -> PlayerManager.getPlayerData((Player) e.getWhoClicked()).setNavigator(i2)));
                            i++;
                        }
                        return ret;
                    }

                    @Override
                    public ItemStack getIcon(Player player) {
                        return cfg.getItem("settings.navigator.icon",
                                new LobbyItem(Material.COMPASS).setDisplayName("&bNavigator")
                        ).getAsItemStack(player);
                    }

                    @Override
                    public ItemStack getCurrent(Player player) {
                        return NavigatorManager.getNavigator(new OfflinePlayerData(player).getNavigator()).getItem()
                                .getAsItemStack(player);
                    }
                }),
                //Sound
                new SettingsModule(new SettingsModuleAction() {
                    @Override
                    public List<ClickableItem> getOptions(Player player) {
                        return new ArrayList<>(Arrays.asList(
                                ClickableItem.of(cfg.getItem("settings.sound.option:on",
                                        new LobbyItem(Material.WOOL).setData(5).setDisplayName("&4Sound ON")
                                ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setSound(true)),
                                ClickableItem.of(cfg.getItem("settings.sound.option:off",
                                        new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4Sound OFF")
                                ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setSound(false))
                        ));
                    }

                    @Override
                    public ItemStack getIcon(Player player) {
                        return cfg.getItem("settings.sound.icon",
                                new LobbyItem(Material.JUKEBOX).setDisplayName("&bSound")
                        ).getAsItemStack(player);
                    }

                    @Override
                    public ItemStack getCurrent(Player player) {
                        return new OfflinePlayerData(player).getSound() ?
                                cfg.getItem("settings.sound.current:on",
                                        new LobbyItem(Material.WOOL).setData(5).setDisplayName("&aSound On")
                                ).getAsItemStack(player) :
                                cfg.getItem("settings.sound.current:off",
                                        new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4Sound Off")
                                ).getAsItemStack(player);
                    }
                }),
                //JumpPad
                new SettingsModule(new SettingsModuleAction() {
                    @Override
                    public List<ClickableItem> getOptions(Player player) {
                        return new ArrayList<>(Arrays.asList(
                                ClickableItem.of(cfg.getItem("settings.jumppad.option:on",
                                        new LobbyItem(Material.WOOL).setData(5).setDisplayName("&4JumpPad ON")
                                ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setJumpPad(true)),
                                ClickableItem.of(cfg.getItem("settings.jumppad.option:off",
                                        new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4JumpPad OFF")
                                ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setJumpPad(false))
                        ));
                    }

                    @Override
                    public ItemStack getIcon(Player player) {
                        return cfg.getItem("settings.jumppad.icon",
                                new LobbyItem(Material.IRON_PLATE).setDisplayName("&bJumpPad")
                        ).getAsItemStack(player);
                    }

                    @Override
                    public ItemStack getCurrent(Player player) {
                        return new OfflinePlayerData(player).getJumpPads() ?
                                cfg.getItem("settings.jumppad.current:on",
                                        new LobbyItem(Material.WOOL).setData(5).setDisplayName("&aJumppad On")
                                ).getAsItemStack(player) :
                                cfg.getItem("settings.jumppad.current:off",
                                        new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4Jumppad Off")
                                ).getAsItemStack(player);
                    }
                }),
                //DoubleJump
                new SettingsModule(new SettingsModuleAction() {
                    @Override
                    public List<ClickableItem> getOptions(Player player) {
                        return new ArrayList<>(Arrays.asList(
                                ClickableItem.of(cfg.getItem("settings.doublejump.option:on",
                                        new LobbyItem(Material.WOOL).setData(5).setDisplayName("&aDoubleJump ON")
                                ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setDoubleJump(true)),
                                ClickableItem.of(cfg.getItem("settings.doublejump.option:off",
                                        new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4DoubleJump OFF")
                                ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setDoubleJump(false))
                        ));
                    }

                    @Override
                    public ItemStack getIcon(Player player) {
                        return cfg.getItem("settings.doublejump.icon",
                                new LobbyItem(Material.IRON_BOOTS).setGlow(true).setDisplayName("&bDoubleJump")
                        ).getAsItemStack(player);
                    }

                    @Override
                    public ItemStack getCurrent(Player player) {
                        return new OfflinePlayerData(player).getDoubleJump() ?
                                cfg.getItem("settings.doublejump.current:on",
                                        new LobbyItem(Material.WOOL).setData(5).setDisplayName("&aDoubleJump ON")
                                ).getAsItemStack(player) :
                                cfg.getItem("settings.doublejump.current:off",
                                        new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4DoubleJump OFF")
                                ).getAsItemStack(player);
                    }
                })
        );
        for (int i = 0; i < 200; i++) {
            int finalI = i + 1;
            addModule(new SettingsModule(new LobbyItem(Material.NOTE_BLOCK)
                    .setAmount(finalI).getAsItemStack()).setAction(new SettingsModuleAction() {
                @Override
                public List<ClickableItem> getOptions(Player player) {
                    return null;
                }

                @Override
                public ItemStack getIcon(Player player) {
                    return null;
                }

                @Override
                public ItemStack getCurrent(Player player) {
                    return null;
                }
            }));
        }
    }

}
