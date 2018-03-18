package de.phyrone.lobbyrel.gui.customitems;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.gui.api.InputGUI;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItem;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.Tools;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class AdminCustomItemGUI implements InventoryProvider {
    private CustomItem item;

    AdminCustomItemGUI(CustomItem item) {
        this.item = item;
    }

    public static void open(Player player, CustomItem item) {
        final SmartInventory inv = SmartInventory.builder()
                .provider(new AdminCustomItemGUI(item)).title("§5" + item.getItem().getDisplayName())
                .id("ItemGUI-" + player.getUniqueId().toString())
                .type(InventoryType.HOPPER).build();
        inv.open(player);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init(Player opner, InventoryContents con) {
        CustomItem ci = item;

        //on.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
        con.set(0, 4, ClickableItem.of(new ItemBuilder(Material.BARRIER).displayname("&cBack").build(), e -> {
            AdminCustomItemsGUI.open((Player) e.getWhoClicked(), 0);
        }));
        con.set(0, 0, ClickableItem.of(new ItemBuilder(Material.NAME_TAG).displayname("&6Displayname").build(), e -> {
            Player p = (Player) e.getWhoClicked();
            if (ci != null) {
                p.closeInventory();
                Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {

                    @Override
                    public void run() {
                        new InputGUI(InputGUI.InputType.DEFAULT).setHandler(new InputGUI.InputHandler() {
                            @Override
                            public void onAccept(Player player, String input) {
                                AdminCustomItemGUI.open(p, ci);
                                ci.getItem().setDisplayName(input);
                                CustomItemsManager.updateItem(item, ci);
                            }

                            @Override
                            public void onDeny(Player player) {
                                AdminCustomItemGUI.open(p, ci);
                            }
                        }).open(p);

                    }
                });
            } else {
                AdminCustomItemsGUI.open(p, 0);
            }
        }));
        con.set(0, 2, ClickableItem.of(new ItemBuilder(Material.APPLE).displayname("&7Slot").build(), e -> {
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
                new InputGUI(InputGUI.InputType.DEFAULT).setHandler(new InputGUI.InputHandler() {
                    @Override
                    public void onAccept(Player player, String input) {
                        try {
                            int slot = Integer.parseInt(input);
                            CustomItemsManager.updateItem(item, ci.setSlot(slot));

                        } catch (Exception ex) {
                        }
                        AdminCustomItemGUI.open(p, ci);
                    }

                    @Override
                    public void onDeny(Player player) {
                        AdminCustomItemGUI.open(p, ci);
                    }
                }).open(p);


            });
        }));
        //etitem
        con.set(0, 3, ClickableItem.of(new ItemBuilder(Material.ANVIL).displayname("&6Set ItemMaterial").build(), e -> {
            Player p = (Player) e.getWhoClicked();
            p.closeInventory();
            Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {

                @Override
                public void run() {
                    new InputGUI(InputGUI.InputType.DEFAULT).setHandler(new InputGUI.InputHandler() {
                        @Override
                        public void onAccept(Player player, String input) {
                            Material m = Tools.parseToMaterial(input);
                            if (m != null) {
                                Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {

                                    @Override
                                    public void run() {
                                        AdminCustomItemGUI.open(p, ci);
                                        ci.getItem().setMaterial(m);
                                        CustomItemsManager.updateItem(item, ci);

                                    }
                                }, 1);
                            }
                        }

                        @Override
                        public void onDeny(Player player) {
                            AdminCustomItemGUI.open(p, ci);
                        }
                    }).open(p);
                }
            });
        }));
    }

    @Override
    public void update(Player p, InventoryContents con) {
        // TODO Auto-generated method stub

    }

}
