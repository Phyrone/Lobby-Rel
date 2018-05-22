package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.lib.LobbyItem;
import de.phyrone.lobbyrel.player.lang.LangManager;
import de.phyrone.lobbyrel.player.settings.SettingsManager;
import de.phyrone.lobbyrel.player.settings.SettingsModule;
import fr.minuskube.inv.*;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class SettingsGUI implements InventoryProvider {
    private static final int ipp = 5;
    int site;
    List<SettingsModule> modules = SettingsManager.getModulesAsList();
    int additor;
    ItemStack fillItem;

    public SettingsGUI(int site) {
        this.site = site;
        additor = site * ipp;
    }

    public static void open(final Player player, final int site) {
        SmartInventory.builder()
                .closeable(true)
                .type(InventoryType.CHEST)
                .id("settings-" + player.getUniqueId().toString() + "-" + String.valueOf(site))
                .title(LangManager.getMessage(player, "GUI.Settings.Title", "&8Settings"))
                .provider(new SettingsGUI(site))
                .listener(new InventoryListener<>(InventoryClickEvent.class,
                        inventoryClickEvent ->
                                InventoryManager.getInstance().getContents((Player) inventoryClickEvent
                                        .getWhoClicked()).get().setProperty("tick", 2)))
                .size(SettingsManager.getModulesAsList().size() > ipp ? ipp + 1 : SettingsManager.getModulesAsList().size(), 9)
                .build().open(player);

    }

    @Override
    public void init(Player player, InventoryContents con) {
        fillItem = ItemsConfig.getLobbyItem("settings.fillItem",
                new LobbyItem(Material.STAINED_GLASS_PANE).setData(15).setDisplayName(" ")
        ).getAsItemStack(player);
        setModules(player, con);

        if (site > 0) {
            con.set(ipp, 2, ClickableItem.of(
                    ItemsConfig.getLobbyItem("settings.previous",
                            new LobbyItem(Material.ARROW).setDisplayName("&6Previous")
                    ).getAsItemStack(player)
                    , e -> open(player, site - 1)));
        }
        if ((modules.size() - additor) > ipp) {
            con.set(ipp, 6, ClickableItem.of(ItemsConfig.getLobbyItem("settings.next",
                    new LobbyItem(Material.ARROW).setDisplayName("&6Next")
            ).getAsItemStack(player), e -> {
                try {
                    open(player, site + 1);
                } catch (Exception e1) {
                    player.closeInventory();
                    player.sendTitle("§4§lError", "");
                    e1.printStackTrace();
                }
            }));
        }

        if (modules.size() > ipp) {
            con.set(ipp, 4, ClickableItem.empty(ItemsConfig.getLobbyItem("settings.current",
                    new LobbyItem(Material.DIAMOND).setDisplayName("&2Page %current%")).setAmount(site + 1)
                    .getAsItemStack(player, (input, player1) -> LobbyItem.DEFAULT_PLACEHOLDER
                            .replace(input, player1).replace("%current%", String.valueOf(site + 1)))));
        }
    }

    @Override
    public void update(Player player, InventoryContents con) {
        int tick = con.property("tick", 0);
        if (tick < 1) {
            setModules(player, con);
            con.setProperty("tick", 10);
        } else con.setProperty("tick", tick - 1);

    }

    public void setModules(Player player, InventoryContents con) {
        if (LobbyPlugin.getDebug()) {
            System.out.println("Additor: " + additor);
            System.out.println("Site: " + site);
        }
        for (int i = 0; i < ipp; i++) {
            SettingsModule module;
            try {
                module = modules.get(additor + i);
                setModule(player, con, i, module);
            } catch (Exception e) {
                break;
            }
            setModule(player, con, i, module);
        }
    }

    public void setModule(Player player, InventoryContents con, int i, SettingsModule module) {
        con.set(i, 0, ClickableItem.empty(module.getIcon(player)));
        con.set(i, 1, ClickableItem.empty(module.getCurrent(player)));
        con.set(i, 2, ClickableItem.empty(fillItem));
        final int size = module.getOptions(player).size();
        /*if (size > 6) {

        } else*/
        {
            int i2 = 0;
            for (ClickableItem opt : module.getOptions(player)) {
                if (i2 > 6) break;
                con.set(i, 8 - size + 1 + i2, opt);
                i2++;
            }
        }
    }

}

class MoreSettingsGUI implements InventoryProvider {

    public MoreSettingsGUI(Player player) {

    }

    public void open() {

    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
