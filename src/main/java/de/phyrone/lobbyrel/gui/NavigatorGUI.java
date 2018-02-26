package de.phyrone.lobbyrel.gui;

import java.util.HashMap;
import java.util.function.Consumer;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import de.phyrone.lobbyrel.warps.Teleporter;
import de.phyrone.lobbyrel.warps.Warp;
import de.phyrone.lobbyrel.warps.WarpManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;

public class NavigatorGUI implements InventoryProvider {
	ItemStack nowarpItem;
	public static void open(Player p) {
		open(p, 0);
	}
	public static void open(Player p,int page) {
		SmartInventory.builder().title(LangManager.getMessage(p, "GUI.Navigator.Title", "&6&lNavigator")).id("navigator-"+p.getUniqueId().toString())
		.size(5, 9).provider(new NavigatorGUI()).build()
		.open(p, page);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init(Player player, InventoryContents contents) {
		nowarpItem = new ItemBuilder(Material.BARRIER).displayname(LangManager.getMessage(player, "GUI.Navigator.NoWarp", "&cWarp not set")).build();
		contents.fill(ClickableItem.empty(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE,1,DyeColor.GRAY.getData())).displayname(" ").build()));
		contents.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
	    Pagination pagination = contents.pagination();
	    HashMap<String, Warp> pwarps = WarpManager.getWarps();
	    ClickableItem[] items = new ClickableItem[pwarps.size()];
	    int i = 0;
	    for(String warpname: pwarps.keySet()) {
	    	final String fw = warpname;
	    	Warp warp = pwarps.get(warpname);
	    	items[i] = ClickableItem.of(warp.getWarpItem().getAsItemStack(player), e->{
	    		Teleporter.teleport((Player) e.getWhoClicked(), fw);
	    	});
	    	i++;
	    }
		pagination.setItems(items);
	    pagination.setItemsPerPage(6);
	    ClickableItem[] pi = pagination.getPageItems();
	    contents.set(2, 4, ClickableItem.of(new ItemBuilder(Material.MAGMA_CREAM).displayname(LangManager.getMessage(player, "GUI.Navigator.Spawn", "&6Spawn")).build(), new Consumer<InventoryClickEvent>() {
			
			@Override
			public void accept(InventoryClickEvent e) {
				Teleporter.toSpawn((Player) e.getWhoClicked());
			}
		}));
	    if(pagination.isLast() || pagination.getPage() == Config.getInt("LastNavigatorPage",1)-1) {}else{
		    contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname(LangManager.getMessage(player, "GUI.Navigator.Next", "&6Next")).build(),
		            e -> NavigatorGUI.open(player, pagination.next().getPage())));
	    }if(!pagination.isFirst()) {
		    contents.set(0, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname(LangManager.getMessage(player,"GUI.Navigator.Previous","&6Previous")).build(),
		            e -> NavigatorGUI.open(player, pagination.previous().getPage())));
		    }
	    if(0 == Config.getInt("LastNavigatorPage",1)-1) {
	    	
	    }else {
	    	contents.set(2, 8, ClickableItem.empty(new ItemBuilder(Material.PAPER).displayname(LangManager.getMessage(player, "GUI.Navigator.Page", "&7Page")).amount(pagination.getPage()+1).build()));
	    }
	    //WarpItems
	    if(pi[0] == null) {
		    contents.set(0, 4, ClickableItem.empty(nowarpItem));
	    }else {
	    	contents.set(0, 4, pi[0]);
	    }if(pi[1] == null) {
	    	contents.set(1, 2, ClickableItem.empty(nowarpItem));
	    }else {
	    	contents.set(1, 2, pi[1]);
	    }if(pi[2] == null) {
	    	contents.set(3, 2, ClickableItem.empty(nowarpItem));
	    }else {
	    	contents.set(3, 2, pi[2]);
	    }if(pi[3] == null) {
	    	contents.set(4, 4, ClickableItem.empty(nowarpItem));
	    }else {
	    	contents.set(4, 4, pi[3]);
	    }if(pi[4] == null) {
	    	contents.set(3, 6, ClickableItem.empty(nowarpItem));
	    }else {
	    	contents.set(3, 6, pi[4]);
	    }if(pi[5] == null) {
	    	contents.set(1, 6, ClickableItem.empty(nowarpItem));
	    }else {
	    	contents.set(1, 6, pi[5]);
	    }
	    
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		
		
	}

}
