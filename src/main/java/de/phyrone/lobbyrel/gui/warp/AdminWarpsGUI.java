package de.phyrone.lobbyrel.gui.warp;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.gui.AdminMainGui;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class AdminWarpsGUI implements InventoryProvider {
    private static final SmartInventory inv = SmartInventory.builder().size(3, 9).provider(new AdminWarpsGUI()).manager(LobbyPlugin.getInstance().getInventoryManager()).build();

    public static void open(Player player) {
        inv.open(player);
    }

    @Override
    public void init(Player p, InventoryContents con) {
        con.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
        Pagination pagi = con.pagination();
        HashMap<String, Warp> warplist = WarpManager.getWarpsUnsorted();
        ClickableItem[] warps = new ClickableItem[warplist.keySet().size()];
        int i = 0;
        for (String w : warplist.keySet()) {
            Warp warp = warplist.get(w);
            warps[i] = ClickableItem.of(warp.getItemStack(p), e -> {
                AdminWarpGUI.open(p, w);
            });
            i++;
        }
        pagi.setItems(warps);
        pagi.setItemsPerPage(7);

        pagi.addToIterator(con.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
        con.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Next").build(),
                e -> inv.open(p, pagi.next().getPage())));
        con.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Prev").build(),
                e -> inv.open(p, pagi.previous().getPage())));
        con.set(2, 8, ClickableItem.of(new ItemBuilder(Material.BARRIER).displayname("&cBack").build(), e -> {
            AdminMainGui.open((Player) e.getWhoClicked());
        }));
    }

    @Override
    public void update(Player p, InventoryContents con) {

    }

}
