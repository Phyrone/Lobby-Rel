package de.phyrone.lobbyrel.hotbar;

import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.lib.RandomString;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LobbyItem {
	public Material Material;
	public String DisplayName = "";
	public List<String> Lore = new ArrayList<>();
	public boolean Glow = false;
	public boolean isPlayerHead = false;
	public String Skin = "%player%";
	public LobbyItem(ItemStack item) {
		this.Material = item.getType();
		if(item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if(meta.hasDisplayName())
			this.DisplayName = meta.getDisplayName().replace("§", "&");
			if(meta.hasLore())
			this.Lore = meta.getLore();
		}

	}public LobbyItem(){
		Material= org.bukkit.Material.STONE;
	}
	public ItemStack getAsItemStack() {return getAsItemStack(null);}
	public ItemStack getAsItemStack(Player player) {
		String dm = DisplayName;
		if(player != null) {
			 dm.replace("%player%", player.getName());
			 dm.replace("%displayname%", player.getDisplayName());
		 }
		 ItemBuilder item = new ItemBuilder(Material);
		 if(Lore == null)item.lore("");
		 else item.lore(Lore);
		 item.displayname(getLangString(DisplayName, player));
		 if(Glow)
		 item.glow();
		 //if(Glow)item.enchant(Enchantment.KNOCKBACK, 1);
		 //item = new ItemBuilder(new ItemAPI(item.build()).addGlow(Glow).build());
		 if(!isPlayerHead) {
			 return item.build();
		 }else {
			 String own = Skin;
			 item.material(org.bukkit.Material.SKULL_ITEM);
			 if(player != null)
			 own = own.replace("%player%", player.getName());
			 if(own.toLowerCase().startsWith("http://") || own.toLowerCase().startsWith("https://")) {
				 return Tools.getSkullFromURL(item.build(),own);
			 }else if(own.length() > 16) {
				return Tools.createSkull(item.build(), own, new RandomString(16).nextString());
				 
			 }else {
				 return Tools.getSkullFromPlayer(item.build(), own);
			 }
				
			 

		 }
	}
	private String getLangString(String content,Player player) {
		 if(content == null)return "";
		 else if(content.toLowerCase().startsWith("lang:")) {
			 if(content.length() < 6){
				 return "aCustomString";
			 }
			 String name = content.substring(5);
			 return LangManager.getMessage(player, "Items."+name, "aCustomString");
		 }
		 else return content;
	}

	//GetterSetter
	public Material getMaterial() {
		return Material;
	}
	public LobbyItem setMaterial(Material material) {
		Material = material;
		return this;
	}
	public String getDisplayName() {
		return DisplayName;
	}
	public LobbyItem setDisplayName(String displayName) {
		DisplayName = displayName;
		return this;
	}
	public List<String> getLore() {
		return Lore;
	}
	public LobbyItem setLore(List<String> lore) {
		Lore = lore;
		return this;
	}public LobbyItem setLore(String...lore) {
		Lore.clear();
		for(String line:lore)
			Lore.add(line);
		return this;
	}
	public boolean isGlow() {
		return Glow;
	}
	public LobbyItem setGlow(boolean glow) {
		Glow = glow;
		return this;
	}
	public boolean isPlayerHead() {
		return isPlayerHead;
	}
	public LobbyItem setPlayerHead(boolean isPlayerHead) {
		this.isPlayerHead = isPlayerHead;
		return this;

	}
	public String getSkin() {
		return Skin;
	}
	public LobbyItem setSkin(String headOwner) {
		Skin = headOwner;
		return this;

	}
}
