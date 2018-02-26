package de.phyrone.lobbyrel.hotbar.customitems;

import de.phyrone.lobbyrel.config.ItemsConfig;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.hotbar.api.HotbarItemAction;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;

public class CustomItemsManager {
	public static final File file = new File("plugins/Lobby-Rel","Items.json");
	public static void init() {
		ArrayList<CustomItem> items = ItemsConfig.getInstance().CustomItems;
		for(CustomItem customitem : items) {
			try {
				MainHotbar.addItem(customitem.Slot, new HotbarItem(customitem.getItem().getAsItemStack()).setAction(new HotbarItemAction() {
					
					@Override
					public ItemStack onSelect(Player player) {
						return customitem.getItem().getAsItemStack(player);
					}
					
					@Override
					public void onClick(PlayerInteractEvent e, Boolean rightClick) {
						if(rightClick)customitem.RunCommandAs.run(e.getPlayer(), customitem.Command);
					}
				}));
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	public static void updateItem(CustomItem olditem,CustomItem newitem) {
		ArrayList<CustomItem> list = ItemsConfig.getInstance().CustomItems;
		if(list.contains(olditem))
			list.remove(olditem);
		addItemtoConf(newitem, true);
		
	}
	public static void loadConfig() {
		file.getParentFile().mkdirs();
		ItemsConfig.load(file);
		ItemsConfig.getInstance().toFile(file);
		
	}public static void save() {
		file.getParentFile().mkdirs();
		ItemsConfig.getInstance().toFile(file);
		
	}public static void addItemtoConf(CustomItem item,boolean save) {
		ItemsConfig.getInstance().CustomItems.add(item);
		if(save)save();
	}

}
