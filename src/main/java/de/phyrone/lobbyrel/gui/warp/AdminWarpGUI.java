package de.phyrone.lobbyrel.gui.warp;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.lib.AnvilGUI;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.lib.AnvilGUI.AnvilClickEvent;
import de.phyrone.lobbyrel.lib.AnvilGUI.AnvilSlot;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class AdminWarpGUI implements InventoryProvider {
    final String name;

    public AdminWarpGUI(String name) {
        this.name = name;
    }

    public static void open(Player player, String warpname) {
        warpname = warpname.toUpperCase();
        Warp warp = WarpManager.getWarps().getOrDefault(warpname, null);
        if (warp == null) {
            player.sendMessage(LobbyPlugin.getPrefix() + " §4Error: Warp dosn't exist!");
            return;
        }
        final SmartInventory inv = SmartInventory.builder().type(InventoryType.HOPPER).provider(new AdminWarpGUI(warpname))
                .title("§8Warp: §5" + warpname).id("warpgui_" + warpname).build();
        inv.open(player);

    }

    @SuppressWarnings("deprecation")
    @Override
    public void init(Player opner, InventoryContents con) {
        final Warp warp = WarpManager.getWarp(name);
        //con.fill(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
        //Back
        con.set(0, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).displayname("&cBack").build(), e -> {
            AdminWarpsGUI.open((Player) e.getWhoClicked());
        }));
        //Set Displayname
        con.set(0, 0, ClickableItem.of(new ItemBuilder(Material.ARMOR_STAND).lore("&8Displayname: &6" + warp.getWarpItem().getDisplayName()).displayname("&5&lset DisplayName").build(), e -> {
            final Player clicker = (Player) e.getWhoClicked();
            clicker.closeInventory();
            AnvilGUI gui = new AnvilGUI(clicker, new AnvilGUI.AnvilClickEventHandler() {
                @Override
                public void onAnvilClick(AnvilClickEvent e) {
                    if (e.getSlot() == AnvilSlot.OUTPUT) {
                        Warp warp2 = warp;
                        warp2.setDisplayname(e.getName());
                        WarpManager.setWarp(name, warp2);
                        WarpManager.saveToConf();
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {

                            @Override
                            public void run() {
                                AdminWarpGUI.open(clicker, name);
                            }
                        }, 1);
                        e.setWillClose(true);
                        e.setWillDestroy(true);

                    } else {
                        e.setWillClose(false);
                        e.setWillDestroy(false);
                    }
                }
            });
            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).replaceAndSymbol(false).displayname(warp.getWarpItem().getDisplayName().replace("§", "&")).build());
            gui.open();

        }));
        //SetItem
        con.set(0, 2, ClickableItem.of(new ItemBuilder(warp.getWarpItem().getMaterial()).displayname("&c&lset Item").build(), e -> {
            final Player clicker = (Player) e.getWhoClicked();
            clicker.closeInventory();
            AnvilGUI gui = new AnvilGUI(clicker, new AnvilGUI.AnvilClickEventHandler() {
                @Override
                public void onAnvilClick(AnvilClickEvent e) {
                    if (e.getSlot() == AnvilSlot.OUTPUT) {
                        Warp warp2 = warp;
                        Material mat = Tools.parseToMaterial(e.getName());
                        if (mat != null) {
                            warp2.getWarpItem().setMaterial(mat);
                            WarpManager.setWarp(name, warp2);
                            WarpManager.saveToConf();
                            Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {

                                @Override
                                public void run() {
                                    AdminWarpGUI.open(clicker, name);
                                }

                            }, 1);
                            e.setWillClose(true);
                            e.setWillDestroy(true);
                        } else {
                            e.setWillDestroy(false);
                            e.setWillClose(false);
                            clicker.sendMessage("&cItem not Found");
                        }


                    } else {
                        e.setWillClose(false);
                        e.setWillDestroy(false);
                    }
                }
            });
            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).replaceAndSymbol(false).displayname(warp.getWarpItem().getMaterial().name()).build());
            gui.open();

        }));
    }

    @Override
    public void update(Player p, InventoryContents con) {
        // TODO Auto-generated method stub

    }

}
