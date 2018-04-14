package de.phyrone.lobbyrel.lib;

import de.phyrone.lobbyrel.lib.item.ItemAPI;
import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyItem {
    public static final StringReplacer DEFAULT_PLACEHOLDER = (input, player) -> input
            .replace("%player%", player.getName())
            .replace("%displayname%", player.getDisplayName())
            .replace("%uuid%", player.getUniqueId().toString());
    public String Material;
    public String DisplayName = "";
    public byte Amount = 1;
    public byte Data = 0;
    public List<String> Lore = new ArrayList<>();
    public boolean Glow = false;
    public boolean isPlayerHead = false;
    public String Skin = "%player%";
    public HashMap<String, Object> CustomTags = new HashMap<>();

    public HashMap<String, Object> getCustomTags() {
        return CustomTags;
    }

    public LobbyItem setCustomTags(HashMap<String, Object> customTags) {
        CustomTags = customTags;
        return this;
    }

    public LobbyItem setCustom(String key, Object value) {
        CustomTags.put(key, value);
        return this;
    }

    public String getCustomString(String key) {
        Object ret = CustomTags.getOrDefault(key, null);
        if (ret instanceof String)
            return (String) ret;
        return null;
    }

    public int getCustomInteger(String key) {
        Object ret = CustomTags.getOrDefault(key, null);
        if (ret instanceof Integer)
            return (int) ret;
        return 0;
    }

    public byte getAmount() {
        return Amount;
    }

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

    public LobbyItem(Material material) {
        Material = material.toString();
    }

    public ItemStack getAsItemStack() {
        return getAsItemStack(null);
    }

    public ItemStack getAsItemStack(Player player) {
        return getAsItemStack(player, DEFAULT_PLACEHOLDER);
    }

    public ItemStack getAsItemStack(Player player, final StringReplacer replacer) {
        String dm = player != null ? getLangString(DisplayName, player) : DisplayName;
        List<String> lore = new ArrayList<>();
        if (player != null) {
            dm = replacer.replace(dm, player);
            for (String line : Lore)
                lore.add(ChatColor.translateAlternateColorCodes('&',
                        replacer.replace(line, player)));
        } else {
            for (String line : Lore)
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        String plSkin = player != null ? replacer.replace(Skin, player) : Skin;
        ItemStack item = isPlayerHead ? getSkull(plSkin) : new ItemStack(getMaterialAsMaterial(), 1, Data);
        item.setAmount(Amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dm);
        meta.setLore(lore);
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

    public LobbyItem setMaterial(Material material) {
        Material = material.toString();
        return this;
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

    public LobbyItem setAmount(int amount) {
        Amount = (byte) amount;
        return this;
    }

    public LobbyItem setAmount(byte amount) {
        Amount = amount;
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

    public LobbyItem setLore(String... lore) {
        Lore.clear();
        for (String line : lore)
            Lore.add(line);
        return this;
    }

    public LobbyItem setLore(List<String> lore) {
        Lore = lore;
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

    public byte getData() {
        return Data;
    }

    public LobbyItem setData(int data) {
        Data = (byte) data;
        return this;
    }

    public LobbyItem setData(byte data) {
        Data = data;
        return this;
    }
}
