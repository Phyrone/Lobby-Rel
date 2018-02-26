package de.phyrone.lobbyrel.gui.customitems;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.gui.AdminMainGui;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItem;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AdminCustomItemsGUI implements InventoryProvider {
	
	public static void open(Player player,int site) {
		SmartInventory inv = SmartInventory.builder()
				.id("ItemsGUI-"+player.getUniqueId()).size(3, 9).provider(new AdminCustomItemsGUI()).build();
		inv.open(player,site);
	}

	@Override
	public void init(Player p, InventoryContents con) {
		con.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
		Pagination pagi = con.pagination();
		ArrayList<CustomItem> itemlist = ItemsConfig.getInstance().CustomItems;
		ClickableItem[] items = new ClickableItem[itemlist.size()];
		int i = 0;
		for(CustomItem ci:itemlist) {
			items[i] = ClickableItem.of(ci.getItem().getAsItemStack(p), e ->{
				AdminCustomItemGUI.open((Player) e.getWhoClicked(), ci);
			});
			i++;
		}
	    pagi.setItems(items);
	    pagi.setItemsPerPage(7);

	    pagi.addToIterator(con.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
	    con.set(2, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Next").build(),
	            e -> open((Player) e.getWhoClicked(), pagi.next().getPage())));
	    con.set(2, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("&7Prev").build(),
	            e -> open((Player) e.getWhoClicked(), pagi.previous().getPage())));
	    con.set(2, 8, ClickableItem.of(new ItemBuilder(Material.BARRIER).displayname("&cBack").build(), e -> {
	    	AdminMainGui.open((Player) e.getWhoClicked());
	    }));
	}

	@Override
	public void update(Player p, InventoryContents con) {
		
	}

}
