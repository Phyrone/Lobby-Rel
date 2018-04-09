package de.phyrone.lobbyrel.lib;

import de.phyrone.lobbyrel.lib.item.ItemAPI;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LobbyItem {
    public String Material;
    public String DisplayName = "";
    public byte Amount = 1;
    public byte Data = 0;
    public List<String> Lore = new ArrayList<>();
    public boolean Glow = false;
    public boolean isPlayerHead = false;
    public String Skin = "%player%";

    public LobbyItem(ItemStack item) {
        this.Material = item.getType().toString();
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName())
                this.DisplayName = meta.getDisplayName().replace("§", "&");
            if (meta.hasLore())
                this.Lore = meta.getLore();
        }

    }

    public LobbyItem() {
        Material = org.bukkit.Material.STONE.toString();
    }

    public ItemStack getAsItemStack() {
        return getAsItemStack(null);
    }

    public ItemStack getAsItemStack(Player player) {
        String dm = player != null ? getLangString(DisplayName, player) : DisplayName;
        if (player != null) {
            dm.replace("%player%", player.getName());
            dm.replace("%displayname%", player.getDisplayName());
        }
        String plSkin = player != null ? Skin.replace("%player%", player.getName()) : Skin;
        ItemStack item = isPlayerHead ? getSkull(plSkin) : new ItemStack(getMaterialAsMaterial(), 1, Data);
        item.setAmount(Amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dm);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return new ItemAPI(item).addGlow(Glow).build();
    }

    private ItemStack getSkull(String Skin) {
        if (Skin.toLowerCase().startsWith("http://") || Skin.toLowerCase().startsWith("https://")) {
            return Skull.getCustomSkull(Skin);
        } else if (Skin.toUpperCase().startsWith("ITEM:")) {
            try {
                return Skull.valueOf(Skin.toUpperCase().substring(5)).getSkull();
            } catch (Exception e) {
            }
        } else if (Skin.length() > 16) {
            return Tools.getSkullFromBASE64(Skin, new RandomString(16).nextString());
        } else {
            return Skull.getPlayerSkull(Skin);
        }

        return Skull.QUESTION.getSkull();
    }

    private String getLangString(String content, Player player) {
        if (content == null) return "";
        else if (content.toLowerCase().startsWith("lang:")) {
            if (content.length() < 6) {
                return "aCustomString";
            }
            String name = content.substring(5);
            return LangManager.getMessage(player, "Items." + name, "aCustomString");
        } else return ChatColor.translateAlternateColorCodes('&', content);
    }

    //GetterSetter
    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public Material getMaterialAsMaterial() {
        String matter = getMaterial();
        try {
            return org.bukkit.Material.valueOf(matter.toUpperCase());
        } catch (Exception e) {
        }
        try {
            return org.bukkit.Material.getMaterial(Integer.parseInt(matter));
        } catch (Exception e) {
        }
        return org.bukkit.Material.BARRIER;
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
    }

    public LobbyItem setLore(String... lore) {
        Lore.clear();
        for (String line : lore)
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

    public LobbyItem setMaterial(Material material) {
        Material = material.toString();
        return this;
    }

    public byte getData() {
        return Data;
    }

    public LobbyItem setData(byte data) {
        Data = data;
        return this;
    }
}
