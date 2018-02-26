package de.phyrone.lobbyrel.gui;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import de.phyrone.lobbyrel.hider.PlayerHiderManager;
import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class PlayerHiderBrewGUI implements InventoryProvider{
	public static void open(Player p) {
		SmartInventory.builder().id("playerhider_Brew-"+p.getUniqueId().toString())
		.title("§cHider").provider(new PlayerHiderBrewGUI()).type(InventoryType.BREWING).build()
		.open(p);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void init(Player player, InventoryContents contents) {
		Dye dye = new Dye();
		dye.setColor(DyeColor.LIME);
		contents.set(0, 0,ClickableItem.of(new ItemBuilder(new ItemStack(Material.INK_SACK,1,dye.getData())).displayname(LangManager.getMessage(player,"GUI.PlayerHider.ALL", "&2Show Everybody")).build(),e->{
			PlayerHiderManager.setHider(player, 1);
		}));
		dye.setColor(DyeColor.PURPLE);
		contents.set(0, 1,ClickableItem.of(new ItemBuilder(new ItemStack(Material.INK_SACK,1,dye.getData())).displayname(LangManager.getMessage(player,"GUI.PlayerHider.TEAM", "&5Show Team")).build(),e->{
			PlayerHiderManager.setHider(player, 2);
		}));
		dye.setColor(DyeColor.RED);
		contents.set(0, 2,ClickableItem.of(new ItemBuilder(new ItemStack(Material.INK_SACK,1,dye.getData())).displayname(LangManager.getMessage(player,"GUI.PlayerHider.NONE", "&cShow Nobody")).build(),e->{
			PlayerHiderManager.setHider(player, 0);
		}));
		DyeColor cd;
		String dm;
		switch (PlayerHiderManager.getHider(player)) {
		case 0:
			cd = DyeColor.RED;
			dm = "&cNONE";
			break;
		case 1:
			cd = DyeColor.LIME;
			dm = "&aALL";
			break;
		default:
			cd = DyeColor.PURPLE;
			dm = "&5Special";
			break;
		}contents.set(0, 3, ClickableItem.empty(new ItemBuilder(new ItemStack(Material.INK_SACK,1,cd.getDyeData())).displayname("&8Now: "+dm).build()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void update(Player player, InventoryContents con) {
		DyeColor cd;
		String dm;
		switch (PlayerHiderManager.getHider(player)) {
		case 0:
			cd = DyeColor.RED;
			dm = "&cNONE";
			break;
		case 1:
			cd = DyeColor.LIME;
			dm = "&aALL";
			break;
		default:
			cd = DyeColor.PURPLE;
			dm = "&5Special";
			break;
		}con.set(0, 3, ClickableItem.empty(new ItemBuilder(new ItemStack(Material.INK_SACK,1,cd.getDyeData())).displayname("&8Now: "+dm).build()));
	}

}
