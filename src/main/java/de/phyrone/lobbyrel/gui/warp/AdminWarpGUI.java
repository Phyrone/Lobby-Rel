package de.phyrone.lobbyrel.gui.warp;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.gui.api.InputGUI;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class AdminWarpGUI implements InventoryProvider {
    final String name;

    public AdminWarpGUI(String name) {
        this.name = name;
    }

    public static void open(Player player, String warpname) {
        warpname = warpname.toUpperCase();
        Warp warp = WarpManager.getWarpsUnsorted().getOrDefault(warpname, null);
        if (warp == null) {
            player.sendMessage(LobbyPlugin.getPrefix() + " §4Error: Warp dosn't exist!");
            return;
        }
        SmartInventory.builder()
                .type(InventoryType.HOPPER)
                .provider(new AdminWarpGUI(warpname))
                .title("§8Warp: §5" + warpname)
                .id("warpgui_" + warpname)
                .manager(LobbyPlugin.getInstance().getInventoryManager())
                .build().open(player);

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
            new InputGUI(InputGUI.InputType.DEFAULT).setHandler(new InputGUI.InputHandler() {
                @Override
                public void onAccept(Player player, String input) {
                    Warp warp2 = warp;
                    warp2.setDisplayname(input);
                    WarpManager.setWarp(name, warp2);
                    WarpManager.saveToConf();
                    player.closeInventory();
                    AdminWarpGUI.open(clicker, name);
                }

                @Override
                public void onDeny(Player player) {
                    player.closeInventory();
                    AdminWarpGUI.open(clicker, name);
                }
            }).open(clicker);

        }));
        //SetItem
        con.set(0, 2, ClickableItem.of(new ItemBuilder(warp.getWarpItem().getMaterialAsMaterial()).displayname("&c&lset Item").build(), e -> {
            final Player clicker = (Player) e.getWhoClicked();

            new InputGUI(InputGUI.InputType.DEFAULT).setHandler(
                    new InputGUI.InputHandler() {
                        @Override
                        public void onAccept(Player player, String input) {
                            Warp warp2 = warp;
                            Material mat = Tools.parseToMaterial(input);
                            if (mat != null) {
                                warp2.getWarpItem().setMaterial(mat);
                                WarpManager.setWarp(name, warp2);
                                WarpManager.saveToConf();
                                player.closeInventory();
                                AdminWarpGUI.open(clicker, name);
                            }

                        }

                        @Override
                        public void onDeny(Player player) {
                            player.closeInventory();
                            AdminWarpGUI.open(clicker, name);
                        }
                    }
            ).open(clicker);


        }));

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
