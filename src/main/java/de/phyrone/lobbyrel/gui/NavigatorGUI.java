package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.player.lang.LangManager;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class NavigatorGUI implements InventoryProvider {
    static final int IPP = 6;
    ItemStack nowarpItem;
    ItemPositsion[] posiotions;
    int lastpage;

    public NavigatorGUI() {
        posiotions = ItemPositsion.getPositions();
        lastpage = WarpManager.getWarpsUnsorted().size() == 0 ? 1 : (int) Math.ceil((double) WarpManager.getWarpsUnsorted().size() / (double) IPP);
    }

    public static void open(Player p) {
        open(p, 0);
    }

    public static void open(Player p, int page) {
        SmartInventory.builder()
                .title(LangManager.getMessage(p, "GUI.Navigator.Title", "&6&lNavigator")).id("navigator-" + p.getUniqueId().toString())
                .size(Config.getInt("Gui.Navigator.Size", 5), 9)
                .provider(new NavigatorGUI())
                .manager(LobbyPlugin.getInstance().getInventoryManager())
                .build().open(p, page);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        nowarpItem = ItemsConfig.getInstance().getItem("GUI.Navigator.NoWarp",
                new LobbyItem(Material.BARRIER).setDisplayName("&cWarp not set")
        ).getAsItemStack(player);
        contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getDyeData())).displayname(" ").build()));
        contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
        Pagination pagination = contents.pagination();
        List<WarpManager.WarpPack> pwarps = WarpManager.getWarps();
        ClickableItem[] items = new ClickableItem[pwarps.size()];
        int i = 0;
        for (WarpManager.WarpPack warppack : pwarps) {
            final String name = warppack.getName();
            Warp warp = warppack.getWarp();
            items[i] = ClickableItem.of(warp.getWarpItem().getAsItemStack(player), e ->
                    Teleporter.teleport((Player) e.getWhoClicked(), name));
            i++;
        }
        pagination.setItems(items);
        pagination.setItemsPerPage(6);
        ClickableItem[] pi = pagination.getPageItems();
        contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.MAGMA_CREAM).displayname(LangManager.getMessage(player, "GUI.Navigator.Spawn", "&6Spawn")).build(),
                e -> Teleporter.toSpawn((Player) e.getWhoClicked())));
        if (pagination.isLast() || pagination.getPage() == lastpage - 1) {
        } else {
            contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname(LangManager.getMessage(player, "GUI.Navigator.Next", "&6Next")).build(),
                    e -> NavigatorGUI.open(player, pagination.next().getPage())));
        }
        if (!pagination.isFirst()) {
            contents.set(0, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname(LangManager.getMessage(player, "GUI.Navigator.Previous", "&6Previous")).build(),
                    e -> NavigatorGUI.open(player, pagination.previous().getPage())));
        }
        if (0 == lastpage - 1) {

        } else {
            contents.set(2, 8, ClickableItem.empty(new ItemBuilder(Material.PAPER).displayname(LangManager.getMessage(player, "GUI.Navigator.Page", "&7Page")).amount(pagination.getPage() + 1).build()));
        }
        /* WarpItems */
        for (i = 0; i < IPP; i++) {
            ItemPositsion pos = posiotions[i];
            if (pos.isEnabled()) {
                if (pi[i] == null) {
                    contents.set(pos.getY(), pos.getX(), ClickableItem.empty(nowarpItem));
                } else {
                    contents.set(pos.getY(), pos.getX(), pi[i]);
                }
            }
        }

    }

    @Override
    public void update(Player player, InventoryContents contents) {


    }

}

class ItemPositsion {
    private int x;
    private int y;
    private boolean enabled;

    public ItemPositsion(int x, int y, boolean enabled) {
        this.x = x;
        this.y = y;
        this.enabled = enabled;
    }

    public static ItemPositsion[] getPositions() {
        ItemPositsion[] positions = new ItemPositsion[NavigatorGUI.IPP];
        for (int i = 0; i < NavigatorGUI.IPP; i++) {
            String prepath = "Gui.Navigator.Position.Item-" + String.valueOf(i);
            positions[i] = new ItemPositsion(
                    Config.getInt(prepath + ".X", getDefaultX(i)),
                    Config.getInt(prepath + ".Y", getDefaultY(i)),
                    Config.getBoolean(prepath + ".enabled", true)
            );
        }
        return positions;
    }

    private static int getDefaultY(int id) {
        switch (id) {
            case 1:
            case 5:
                return 1;
            case 2:
            case 4:
                return 3;
            case 3:
                return 4;
            case 0:
                return 0;
            default:
                return -1;
        }
    }

    private static int getDefaultX(int id) {
        switch (id) {
            case 0:
            case 3:
                return 4;
            case 1:
            case 2:
                return 2;
            case 4:
            case 5:
                return 6;
            default:
                return -1;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
