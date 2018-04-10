package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.lib.StringReplacer;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher;
import de.phyrone.lobbyrel.lobbyswitcher.LobbySwitcher.ServerConData;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LobbySwitcherGUI implements InventoryProvider {
    public LobbyItem noServerItem;
    public LobbyItem otherServerItem;
    public LobbyItem myServerItem;
    public ClickableItem border;
    public LobbySwitcherGUI(Player player) {
        ItemsConfig cfg = ItemsConfig.getInstance();
        noServerItem = cfg.getItem("lobbyswitcher:noServer",
                new LobbyItem().setMaterial(Material.STAINED_GLASS_PANE).setData((byte) 15).setDisplayName(" "));
        myServerItem = cfg.getItem("lobbyswitcher:currendServer", new LobbyItem().setMaterial(Material.DIAMOND)
                .setDisplayName("%server%").setLore("Online: &4%online%").setGlow(true));
        otherServerItem = cfg.getItem("lobbyswitcher:otherServer", new LobbyItem().setMaterial(Material.EMERALD)
                .setDisplayName("%server%").setLore("Online: &4%online%"));
        border = ClickableItem.empty(cfg.getItem("lobbyswitcher:background", new LobbyItem().setMaterial(Material.STAINED_GLASS_PANE)
                .setDisplayName(" ")).getAsItemStack(player));
    }

    public static void open(Player player) {
        open(player, 0);
    }

    public static void open(Player player, int site) {
        SmartInventory.builder().provider(new LobbySwitcherGUI(player)).id("LS-" + player.getUniqueId().toString())
                .size(3, 9).title(LangManager.getMessage(player, "GUI.LobbySwitcher.Title", "&8Lobbys"))
                .build().open(player, site);
    }


    @Override
    public void init(Player player, InventoryContents con) {
        try {
            update(con, player);

            con.pagination().setItemsPerPage(7);
            con.fillBorders(border);
            con.pagination().addToIterator(con.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
            ItemStack noserverItemstack = noServerItem.getAsItemStack(player);
            for (int i = 0; i < 8; i++) {
                if (!con.get(1, i).isPresent())
                    con.set(1, i, ClickableItem.empty(noserverItemstack));
            }
            if (!con.pagination().isFirst())
                con.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Previous").build(),
                        e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().previous().getPage())));
            else con.set(2, 3, border);

            if (!con.pagination().isLast())
                con.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Next").build(),
                        e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().next().getPage())));
            else con.set(2, 5, border);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    @Override
    public void update(Player player, InventoryContents con) {
        Integer lastupdate = con.property("lastUpdate", 0);

        if (lastupdate > 20) {
            try {
                update(con, player);
                con.pagination().addToIterator(con.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
                ItemStack noserverItemstack = noServerItem.getAsItemStack(player);
                for (int i = 0; i < 8; i++) {
                    if (!con.get(1, i).isPresent())
                        con.set(1, i, ClickableItem.empty(noserverItemstack));
                }
                con.fillBorders(border);
                if (!con.pagination().isFirst())
                    con.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Previous").build(),
                            e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().previous().getPage())));
                if (!con.pagination().isLast())
                    con.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Next").build(),
                            e -> LobbySwitcherGUI.open((Player) e.getWhoClicked(), con.pagination().next().getPage())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            con.setProperty("lastUpdate", 0);

        } else {
            con.setProperty("lastUpdate", lastupdate + 1);

        }

    }

    private void update(InventoryContents con, Player player) {
        try {
            List<String> servers = LobbySwitcher.getInstance().getServers();
            ClickableItem[] items = new ClickableItem[servers.size()];
            int i = 0;
            for (String server : servers) {
                ServerConData data = LobbySwitcher.getInstance().getData(server);
                StringReplacer placeholder = (input, player1) -> LobbyItem.DEFAULT_PLACEHOLDER.replace(input, player1).replace("%server%", server).replace("%online%", String.valueOf(data.getOnline()));
                ItemStack item;
                if (server.equalsIgnoreCase(LobbySwitcher.getInstance().getServerName()))
                    item = myServerItem.getAsItemStack(player, placeholder);
                else
                    item = otherServerItem.getAsItemStack(player, placeholder);
                item.setAmount(data.getOnline());
                items[i] = ClickableItem.of(item, (e) -> {
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
