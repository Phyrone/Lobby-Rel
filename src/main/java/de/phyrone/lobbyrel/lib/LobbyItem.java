package de.phyrone.lobbyrel.lib;

import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName())
                this.DisplayName = meta.getDisplayName().replace("§", "&");
            if (meta.hasLore())
                this.Lore = meta.getLore();
        }

    }

    public LobbyItem() {
        Material = org.bukkit.Material.STONE;
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
        ItemStack item = isPlayerHead ? getSkull(plSkin) : new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(dm);
        meta.setLore(Lore);
        if (Glow) {
            item.addUnsafeEnchantment(item.getType() != org.bukkit.Material.BOW ? Enchantment.ARROW_INFINITE : Enchantment.LUCK, 10);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        } else item.setItemMeta(meta);
        return item;
    }

    private ItemStack getSkull(String Skin) {
        if (Skin.toLowerCase().startsWith("http://") || Skin.toLowerCase().startsWith("https://")) {
            return Skull.getCustomSkull(Skin);
        } else if (Skin.toUpperCase().startsWith("MHF:")) {
            try {
                return Skull.valueOf(Skin.toUpperCase().substring(4)).getSkull();
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
        } else return content;
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
}
