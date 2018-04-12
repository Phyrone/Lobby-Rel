package de.phyrone.lobbyrel.player.settings;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.navigator.Navigator;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.OfflinePlayerData;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.DyeColor;
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
        final ItemsConfig cfg = ItemsConfig.getInstance();

        addModule(new SettingsModule().setAction(new SettingsModuleAction() {

            @Override
            public ArrayList<ClickableItem> getOptions(Player player) {
                ArrayList<ClickableItem> ret = new ArrayList<ClickableItem>();
                int i = 0;
                for (Navigator nav : NavigatorManager.getNavigators()) {
                    final int i2 = i;
                    ret.add(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, nav.getColor().getDye().getDyeData())).displayname(nav.getColor().getChatColor() + nav.getName()).build()
                            , e -> PlayerManager.getPlayerData((Player) e.getWhoClicked()).setNavigator(i2)));
                    i++;
                }
                return ret;
            }

            @Override
            public ItemStack getIcon(Player player) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ItemStack getCurrent(Player player) {
                Navigator nav = NavigatorManager.getNavigator(PlayerManager.getPlayerData(player).getNavigator());
                return new ItemBuilder(new ItemStack(Material.WOOL, 1, nav.getColor().getDye().getDyeData())).displayname("§8Now: " + nav.getColor().getChatColor() + nav.getName()).build();
            }
        }));

        addModule(new SettingsModule().setOptions(
                new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getDyeData())).displayname("&aSound ON").build(), e -> {
                    new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setSound(true);
                }), ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.RED.getDyeData())).displayname("&cSound OFF").build(), e -> {
                    new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setSound(false);
                })))).setAction(new SettingsModuleAction() {

            @Override
            public ArrayList<ClickableItem> getOptions(Player player) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ItemStack getIcon(Player player) {
                return null;
            }

            @Override
            public ItemStack getCurrent(Player player) {
                boolean sound = new OfflinePlayerData(player).getSound();
                if (sound) {
                    return cfg.getItem("settings.sound.current:on",
                            new LobbyItem().setMaterial(Material.WOOL).setData((byte) 5).setDisplayName("&aSound ON")).getAsItemStack(player);


                } else {
                    return cfg.getItem("settings.sound.current:off",
                            new LobbyItem().setMaterial(Material.WOOL).setData((byte) 14).setDisplayName("&4Sound OFF")).getAsItemStack(player);
                }
            }
        }));
        addModule(new SettingsModule(new ItemBuilder(Material.IRON_PLATE).displayname("&6JumpPad").build()).setAction(new SettingsModuleAction() {

            @Override
            public ArrayList<ClickableItem> getOptions(Player player) {
                // TODO Auto-generated method stub
                return new ArrayList<>(Arrays.asList(
                        ClickableItem.of(cfg.getItem("settings.jumpPad.options:on",
                                new LobbyItem(Material.WOOL).setData(5).setDisplayName("&aJumpPad ON")
                        ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setJumpPad(true)),
                        ClickableItem.of(cfg.getItem("settings.jumpPad.options:off",
                                new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4JumpPad OFF")
                        ).getAsItemStack(player), e -> new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setJumpPad(false))
                ));
            }

            @Override
            public ItemStack getIcon(Player player) {
                return cfg.getItem("settings.jumpPad.icon",
                        new LobbyItem(Material.IRON_PLATE).setDisplayName("&6JumpPad")
                ).getAsItemStack(player);
            }

            @Override
            public ItemStack getCurrent(Player player) {
                boolean sb = new OfflinePlayerData(player).getJumpPads();
                if (sb) {
                    return cfg.getItem("settings.jumpPad.current:on",
                            new LobbyItem(Material.WOOL).setData(5).setDisplayName("&aJumpPad ON")
                    ).getAsItemStack(player);
                } else {
                    return cfg.getItem("settings.jumpPad.current:off",
                            new LobbyItem(Material.WOOL).setData(14).setDisplayName("&4JumpPad OFF")
                    ).getAsItemStack(player);
                }
            }
        }));
        addModule(new SettingsModule(new ItemBuilder(Material.IRON_BOOTS).glow().displayname("&6DoubleJump").build()).setOptions(
                new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getDyeData())).displayname("&aDoubleJump ON").build(), e -> {
                            new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setDoubleJump(true);
                            ((Player) e.getWhoClicked()).setAllowFlight(true);
                        }),
                        ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.RED.getDyeData())).displayname("&cDoubleJump OFF").build(), e -> {
                            new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setDoubleJump(false);
                            ((Player) e.getWhoClicked()).setAllowFlight(false);
                        })))
        ).setAction(new SettingsModuleAction() {

            @Override
            public ArrayList<ClickableItem> getOptions(Player player) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ItemStack getIcon(Player player) {
                return cfg.getItem("settings.doubleJump.icon",
                        new LobbyItem(Material.IRON_BOOTS).setDisplayName("&6DoubleJump")).getAsItemStack(player);
            }

            @Override
            public ItemStack getCurrent(Player player) {
                boolean sb = new OfflinePlayerData(player).getDoubleJump();

                if (sb) {
                    return cfg.getItem("settings.doubleJump.currend").getAsItemStack(player);
                } else {
                    return new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.RED.getDyeData())).displayname("&cOff").build();
                }
            }
        }));
        if (ScoreboardManager.isEnabled())
            addModule(new SettingsModule(new ItemBuilder(Material.NETHER_STAR).displayname("&6Scoreboard").build()).setOptions(
                    new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getDyeData())).displayname("&aScoreboard ON").build(), e -> {
                        new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setScoreboard(true);
                        ScoreboardManager.update((Player) e.getWhoClicked());
                    }), ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.RED.getDyeData())).displayname("&cScoreboard OFF").build(), e -> {
                        new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setScoreboard(false);
                        ScoreboardManager.update((Player) e.getWhoClicked());
                    })))).setAction(new SettingsModuleAction() {

                @Override
                public ArrayList<ClickableItem> getOptions(Player player) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public ItemStack getIcon(Player player) {
                    return null;
                }

                @Override
                public ItemStack getCurrent(Player player) {
                    boolean sb = new OfflinePlayerData(player).getScoreboard();
                    if (sb) {
                        return new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getDyeData())).displayname("&aON").build();
                    } else {
                        return new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.RED.getDyeData())).displayname("&cOff").build();
                    }
                }
            }));
    }

}
