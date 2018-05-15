package de.phyrone.lobbyrel.gui;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.lib.LobbyItem;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.material.Dye;

public class PlayerHiderBrewGUI implements InventoryProvider {
    public static void open(Player p) {
        SmartInventory.builder().id("playerhider_Brew-" + p.getUniqueId().toString())
                .title("§cHider").provider(new PlayerHiderBrewGUI()).type(InventoryType.BREWING).build()
                .open(p);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init(Player player, InventoryContents contents) {
        Dye dye = new Dye();
        dye.setColor(DyeColor.LIME);
        contents.set(0, 0, ClickableItem.of(ItemsConfig.getLobbyItem("PlayerHider.Option.ALL",
                new LobbyItem(Material.INK_SACK).setDisplayName("&aAll Visible").setData(10)
        ).getAsItemStack(player), e -> PlayerHiderManager.setPlayerHider(player, 1)));
        dye.setColor(DyeColor.PURPLE);
        contents.set(0, 1, ClickableItem.of(ItemsConfig.getLobbyItem("PlayerHider.Option.VIP",
                new LobbyItem(Material.INK_SACK).setDisplayName("&6VIP Visible").setData(14)
        ).getAsItemStack(player), e -> PlayerHiderManager.setPlayerHider(player, 2)));
        dye.setColor(DyeColor.RED);
        contents.set(0, 2, ClickableItem.of(ItemsConfig.getLobbyItem("PlayerHider.Option.NONE",
                new LobbyItem(Material.INK_SACK).setDisplayName("&cNobody Visible").setData(1)
        ).getAsItemStack(player), e -> PlayerHiderManager.setPlayerHider(player, 0)));
        contents.set(0, 3, ClickableItem.empty(getCurrendItem(PlayerHiderManager.getPlayerHider(player)).getAsItemStack(player)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void update(Player player, InventoryContents con) {
        if (con.property("LastTick", 0) < 20) {
            con.set(0, 3, ClickableItem.empty(getCurrendItem(PlayerHiderManager.getPlayerHider(player)).getAsItemStack(player)));
            con.setProperty("LastTick", 0);
        } else con.setProperty("LastTick", con.property("LastTick", 0) + 1);
    }

    public LobbyItem getCurrendItem(int hider) {
        switch (hider) {
            case 0:
                return ItemsConfig.getLobbyItem("PlayerHider.Current.NONE",
                        new LobbyItem(Material.SKULL).setDisplayName("&4NONE").setPlayerHead(true).setSkin("http://textures.minecraft.net/texture/b439a71957cc1dd4546f4132965effbe3429c889b1c6ba7212cb4b8c03540f8"));
            case 1:
                return ItemsConfig.getLobbyItem("PlayerHider.Current.ALL",
                        new LobbyItem(Material.SKULL).setDisplayName("&aALL").setPlayerHead(true).setSkin("http://textures.minecraft.net/texture/22d145c93e5eac48a661c6f27fdaff5922cf433dd627bf23eec378b9956197"));
            case 2:
                return ItemsConfig.getLobbyItem("PlayerHider.Current.VIP",
                        new LobbyItem(Material.SKULL).setDisplayName("&6VIP").setPlayerHead(true).setSkin("http://textures.minecraft.net/texture/2c4886ef362b2c823a6aa65241c5c7de71c94d8ec5822c51e96976641f53ea35"));
            default:
                return ItemsConfig.getLobbyItem("PlayerHider.Current." + String.valueOf(hider),
                        new LobbyItem(Material.WOOL).setDisplayName("&4&lUnknown Mode(" + hider + ")"));
        }
    }

}
