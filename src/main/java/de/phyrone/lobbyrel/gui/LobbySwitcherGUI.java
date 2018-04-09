package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher.ServerConData;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbySwitcherGUI implements InventoryProvider {
    public static final ClickableItem borderitem = ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build());
    String loreTemplate;

    public LobbySwitcherGUI(Player player) {
        loreTemplate = LangManager.getMessage(player, "GUI.LobbySwitcher.ServerItem.Lore", "Online: &c%online%\nServer: &6%server%");
    }

    public static void open(Player player) {
        open(player, 0);
    }

    public static void open(Player player, int site) {
        SmartInventory.builder().provider(new LobbySwitcherGUI(player)).id("LS-" + player.getUniqueId().toString())
                .size(3, 9).title(LangManager.getMessage(player, "GUI.LobbySwitcher.Title", "&8Lobbys"))
                .build().open(player, site);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void init(Player player, InventoryContents con) {
        try {
            update(con);

            con.pagination().setItemsPerPage(7);

            con.fillBorders(borderitem);
            con.pagination().addToIterator(con.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
            for (int i = 0; i < 8; i++) {
                if (!con.get(1, i).isPresent())
                    con.set(1, i, ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getDyeData())).displayname(" ").build()));
            }
            if (!con.pagination().isFirst())
                con.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Previous").build(),
                        e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().previous().getPage())));
            else con.set(2, 3, borderitem);

            if (!con.pagination().isLast())
                con.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Next").build(),
                        e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().next().getPage())));
            else con.set(2, 5, borderitem);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public void update(Player player, InventoryContents con) {
        Integer lastupdate = con.property("lastUpdate", 0);

        if (lastupdate > 20) {
            try {
                update(con);
                con.pagination().addToIterator(con.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
                for (int i = 0; i < 8; i++) {
                    if (!con.get(1, i).isPresent())
                        con.set(1, i, ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getDyeData())).displayname(" ").build()));
                }
                if (!con.pagination().isFirst())
                    con.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Previous").build(),
                            e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().previous().getPage())));
                if (!con.pagination().isLast())
                    con.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Next").build(),
                            e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().next().getPage())));
            } catch (Exception e) {
                // TODO: handle exception
            }
            con.setProperty("lastUpdate", 0);

        } else {
            con.setProperty("lastUpdate", lastupdate + 1);

        }

    }

    private void update(InventoryContents con) {
        try {
            List<String> servers = LobbySwitcher.getInstance().getServers();
            ClickableItem[] items = new ClickableItem[servers.size()];
            int i = 0;
            for (String server : servers) {
                ItemBuilder builder;
                ServerConData data = LobbySwitcher.getInstance().getData(server);
                if (server.equalsIgnoreCase(LobbySwitcher.getInstance().getServerName()))
                    builder = new ItemBuilder(Material.DIAMOND).displayname("&b" + server);
                else
                    builder = new ItemBuilder(Material.EMERALD).displayname("&a" + server);
                builder.amount(data.getOnline());
                builder.lore(loreTemplate.replace("%online%", String.valueOf(data.getOnline())).replace("%server%", server).split("\n"));
                items[i] = ClickableItem.of(builder.build(), (e) -> {
                    LobbySwitcher.getInstance().send(((Player) e.getWhoClicked()), server);
                });
                i++;
            }
            con.pagination().setItems(items);
        } catch (Exception e2) {
            System.err.println("Error Loading Servers to GUI");
            e2.printStackTrace();
        }

    }
}
