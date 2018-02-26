package de.phyrone.lobbyrel.gui.customitems;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItem;
import de.phyrone.lobbyrel.hotbar.customitems.CustomItemsManager;
import de.phyrone.lobbyrel.lib.AnvilGUI;
import de.phyrone.lobbyrel.lib.AnvilGUI.AnvilClickEvent;
import de.phyrone.lobbyrel.lib.AnvilGUI.AnvilSlot;
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
	
	public static void open(Player player,CustomItem item) {
		final SmartInventory inv = SmartInventory.builder()
				.provider(new AdminCustomItemGUI(item)).title("§5"+item.getItem().getDisplayName())
				.id("ItemGUI-"+player.getUniqueId().toString())
				.type(InventoryType.HOPPER).build();
		inv.open(player);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init(Player opner, InventoryContents con) {
		CustomItem ci = item;
	
		//on.fillBorders(ClickableItem.empty(new ItemBuilder(Material.STAINED_GLASS_PANE).displayname(" ").build()));
		con.set(0,4, ClickableItem.of(new ItemBuilder(Material.BARRIER).displayname("&cBack").build(), e->{
			AdminCustomItemsGUI.open((Player) e.getWhoClicked(),0);
		}));
		con.set(0, 0, ClickableItem.of(new ItemBuilder(Material.NAME_TAG).displayname("&6Displayname").build(), e->{
			Player p = (Player)e.getWhoClicked();
			if(ci != null) {
			p.closeInventory();
			Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent e) {
							if(e.getSlot() == AnvilSlot.OUTPUT) {
								
								e.setWillClose(true);
								e.setWillDestroy(true);
								
								Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {
									
									@Override
									public void run() {
										AdminCustomItemGUI.open(p,ci);
										ci.getItem().setDisplayName(e.getName());
										CustomItemsManager.updateItem(item, ci);
									}
								},1);
							}else {
								e.setWillClose(false);
								e.setWillDestroy(false);
							}
						}
					});
					gui.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).replaceAndSymbol(false).displayname(ci.getItem().getDisplayName().replace("§", "&")).build());
					gui.open();
					
				}
			});}else {
				AdminCustomItemsGUI.open(p,0);
			}
		}));
		con.set(0, 2, ClickableItem.of(new ItemBuilder(Material.APPLE).displayname("&7Slot").build(), e->{
			Player p = (Player)e.getWhoClicked();
			p.closeInventory();
			Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent e) {
							if(e.getSlot() == AnvilSlot.OUTPUT) {
							try {
								int slot = Integer.parseInt(e.getName());
								CustomItemsManager.updateItem(item, ci.setSlot(slot));
								Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {
									
									@Override
									public void run() {
										AdminCustomItemGUI.open(p, ci);
									}
								});
							}catch (Exception ex) {
								e.setWillClose(false);
								e.setWillDestroy(false);
							}
							
						}else {
							e.setWillClose(false);
							e.setWillDestroy(false);
						}}
					});
					gui.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).displayname(String.valueOf(ci.getSlot())).build());
					gui.open();
					
				}
			});
		}));
		//etitem
		con.set(0,3, ClickableItem.of(new ItemBuilder(Material.ANVIL).displayname("&6Set ItemMaterial").build(), e->{
			Player p = (Player)e.getWhoClicked();
			p.closeInventory();
			Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(),new Runnable() {
				
				@Override
				public void run() {
					AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {
						
						@Override
						public void onAnvilClick(AnvilClickEvent e) {
							Material m = Tools.parseToMaterial(e.getName());
							if(e.getSlot() == AnvilSlot.OUTPUT && m != null) {
								Bukkit.getScheduler().scheduleAsyncDelayedTask(LobbyPlugin.getInstance(), new Runnable() {
									
									@Override
									public void run() {
										AdminCustomItemGUI.open(p, ci);
										ci.getItem().setMaterial(m);
										CustomItemsManager.updateItem(item, ci);
										
									}
								},1);
								e.setWillClose(true);
								e.setWillDestroy(true);
							}else {
								e.setWillClose(false);
								e.setWillDestroy(false);
							}
							
							
						}
					});
					gui.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).displayname(ci.getItem().getDisplayName()).build());
					gui.open();
					
				}
			});
		}));
	}

	@Override
	public void update(Player p, InventoryContents con) {
		// TODO Auto-generated method stub
		
	}

}
