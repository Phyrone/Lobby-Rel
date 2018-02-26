package de.phyrone.lobbyrel.lib.item;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ItemAPI {

    private ItemStack itemStack;
    private SkullMeta skullMeta;
    @SuppressWarnings("deprecation")
	public ItemAPI(Object item) {
    	if(item instanceof ItemStack) {
    		itemStack = (ItemStack) item;
    	}if(item instanceof Material) {
    		itemStack = new ItemStack((Material)item);
    	}else if(item instanceof String) {
    		try {
				itemStack = new ItemStack(Material.valueOf(((String)item).toUpperCase()));
			}
    		catch (Exception e) {
    			try {
					itemStack = new ItemStack(Integer.parseInt((String) item));
				} catch (Exception e2) {
					itemStack = new ItemStack(Material.AIR);
				}
			}
    	}else if(item instanceof Integer) {
    		try {
    			itemStack = new ItemStack((Integer)item);
    		}catch (Exception e) {
    			e.printStackTrace();
    			itemStack = new ItemStack(Material.AIR);
			}
    	}else if(item instanceof ItemStack) {
    		itemStack = (ItemStack) item;
    	}else {
    		itemStack = new ItemStack(Material.AIR);
    	}
    }

    /**
     * Erstellt ein neues Item mit
     * einem Material.
     *
     * @param material
     */
    public ItemAPI(Material material) {
        this.itemStack = new ItemStack(material);
    }

    /**
     * [Erstellt ein neues Item mit
     * einer ID anstatt einem Material.
     *
     * @param id
     */
    @SuppressWarnings("deprecation")
	public ItemAPI(int id) {
        this.itemStack = new ItemStack(id);
    }

    /**
     * Erstellt ein Item mit einer
     * spezifischen Anzahl von
     * Items und einem Material.
     *
     * @param material
     * @param amount
     */
    public ItemAPI(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
    }

    /**
     * Erstellt ein Item mit einer spezifischen Anzahl
     * von Items mit einer ID anstatt einem Material.
     *
     * @param id
     * @param amount
     */
    @SuppressWarnings("deprecation")
	public ItemAPI(int id, int amount) {
        this.itemStack = new ItemStack(id, amount);
    }

    /**
     * Erstellt ein Item mit einem Material, einer Anzahl
     * von Items und einer Meta.
     *
     * @param material
     * @param amount
     * @param damage
     */
    public ItemAPI(Material material, int amount, short damage) {
        this.itemStack = new ItemStack(material, amount, damage);
    }

    /**
     * Erstellt ein Item mit einer ID anstatt einem Material,
     * einer Anzahl und einer Meta.
     *
     * @param id
     * @param amount
     * @param damage
     */
    @SuppressWarnings("deprecation")
	public ItemAPI(int id, int amount, short damage) {
        this.itemStack = new ItemStack(id, amount, damage);
    }

    /**
     * Setzt das Item mit einem Material
     * @param material
     * @return
     */
    public ItemAPI setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    /**
     * Setzt das Material mit einer ID
     * @param id
     * @return
     */
    @SuppressWarnings("deprecation")
	public ItemAPI setMaterial(int id) {
        this.itemStack.setType(Material.getMaterial(id));
        return this;
    }

    /**
     * Setzt die ItemMeta des Items
     * @param itemMeta
     * @return
     */
    public ItemAPI setItemMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Setzt den Namen des Items
     * @param displayName
     * @return
     */
    public ItemAPI setDisplayName(String displayName) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Setzt die Lore des Items mit einer Liste
     * @param lore
     * @return
     */
    public ItemAPI setLore(List<String> lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(lore);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Setzt die Lore des Items mit einem String
     * @param lore
     * @return
     */
    public ItemAPI setLore(String... lore) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Setzt das Item als UnzerstÃ¶rbar
     * @param unbreakable
     * @return
     */
    public ItemAPI setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.spigot().setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Setzt eine ItemFlag fÃ¼r das Item
     * @param itemFlags
     * @return
     */
    public ItemAPI setItemFlags(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * Setzt die Farbe einer LederrÃ¼stung
     * @param color
     * @return
     */
    public ItemAPI setColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.itemStack.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.itemStack.setItemMeta(leatherArmorMeta);
        return this;
    }

    /**
     * FÃ¼gt ein Effekt fÃ¼r das FeuerwerksItem hinzu
     * @param fireworkEffect
     * @return
     */
    public ItemAPI addEffects(FireworkEffect fireworkEffect) {
        FireworkMeta fireworkMeta = (FireworkMeta) this.itemStack.getItemMeta();
        fireworkMeta.addEffect(fireworkEffect);
        this.itemStack.setItemMeta(fireworkMeta);
        return this;
    }

    /**
     * FÃ¼gt mehrere Effekte fÃ¼r das FeuerwerksItem hinzu
     * @param fireworkEffect
     * @return
     */
    public ItemAPI addEffects(FireworkEffect... fireworkEffect) {
        FireworkMeta fireworkMeta = (FireworkMeta) this.itemStack.getItemMeta();
        fireworkMeta.addEffects(fireworkEffect);
        this.itemStack.setItemMeta(fireworkMeta);
        return this;
    }

    /**
     * Setzt ein Effekt fÃ¼r das FeuerwerksItem
     * @param fireworkEffect
     * @return
     */
    public ItemAPI setEffect(FireworkEffect fireworkEffect) {
        FireworkEffectMeta fireworkMeta = (FireworkEffectMeta) this.itemStack.getItemMeta();
        fireworkMeta.setEffect(fireworkEffect);
        this.itemStack.setItemMeta(fireworkMeta);
        return this;
    }

    /**
     * Setzt die StÃ¤rke des FeuerwerkItems
     * @param power
     * @return
     */
    public ItemAPI setPower(int power) {
        FireworkMeta fireworkMeta = (FireworkMeta) this.itemStack.getItemMeta();
        fireworkMeta.setPower(power);
        this.itemStack.setItemMeta(fireworkMeta);
        return this;
    }

    /**
     * FÃ¼gt eine Verzauberung fÃ¼r das Item hinzu
     * @param enchantment
     * @param level
     * @param force
     * @return
     */
    public ItemAPI addEnchantment(Enchantment enchantment, int level, boolean force) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, level, force);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    /**
     * FÃ¼gt dem Item eine Beleuchtung hinzu
     * @param glow
     * @return
     */
    public ItemAPI addGlow(boolean glow) {
    	if(glow) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.itemStack.setItemMeta(itemMeta);
        itemStack.addUnsafeEnchantment(itemStack.getType() != Material.BOW ? Enchantment.ARROW_INFINITE : Enchantment.LUCK, 10);}
        return this;
    }

    /**
     * FÃ¼gt ein TrankEffekt fÃ¼r den Trank hinzu
     * @param potionEffectType
     * @return
     */
    public ItemAPI addPotionEffect(PotionEffectType potionEffectType) {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        potionMeta.setMainEffect(potionEffectType);
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }

    /**
     * FÃ¼gt ein Custom TrankEffekt hinzu
     * @param potionEffect
     * @param overwrite
     * @return
     */
    public ItemAPI addCustomPotionEffect(PotionEffect potionEffect, boolean overwrite) {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        potionMeta.addCustomEffect(potionEffect, overwrite);
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }

    /**
     * Entfernt ein Custom TrankEffekt
     * @param potionEffectType
     * @return
     */
    public ItemAPI removeCustomEffect(PotionEffectType potionEffectType) {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        potionMeta.removeCustomEffect(potionEffectType);
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }

    /**
     * Entfernt alle Custom TrankEffekte
     * @return
     */
    public ItemAPI clearCustomEffects() {
        PotionMeta potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        potionMeta.clearCustomEffects();
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }

    /**
     * Setzt den Titel des Buches
     * @param title
     * @return
     */
    public ItemAPI setTitle(String title) {
        BookMeta bookMeta = (BookMeta) this.itemStack.getItemMeta();
        bookMeta.setTitle(title);
        this.itemStack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Setzt den Authoren des Buches
     * @param author
     * @return
     */
    public ItemAPI setAuthor(String author) {
        BookMeta bookMeta = (BookMeta) this.itemStack.getItemMeta();
        bookMeta.setAuthor(author);
        this.itemStack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Setzt die Seiten des Buches
     * @param page
     * @param content
     * @return
     */
    public ItemAPI setPage(int page, String content) {
        BookMeta bookMeta = (BookMeta) this.itemStack.getItemMeta();
        bookMeta.setPage(page, content);
        this.itemStack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Setzt die Seiten des Buches mit einer Liste
     * @param pages
     * @return
     */
    public ItemAPI setPages(List<String> pages) {
        BookMeta bookMeta = (BookMeta) this.itemStack.getItemMeta();
        bookMeta.setPages(pages);
        this.itemStack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * FÃ¼gt eine Seite des Buches hinzu
     * @param pages
     * @return
     */
    public ItemAPI addPage(String... pages) {
        BookMeta bookMeta = (BookMeta) this.itemStack.getItemMeta();
        bookMeta.addPage(pages);
        this.itemStack.setItemMeta(bookMeta);
        return this;
    }

    /**
     * Setzt den BaseColor des Banners
     * @param dyeColor
     * @return
     */
    public ItemAPI setBaseColor(DyeColor dyeColor) {
        BannerMeta bannerMeta = (BannerMeta) this.itemStack.getItemMeta();
        bannerMeta.setBaseColor(dyeColor);
        this.itemStack.setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Setzt den Pattern des Banners
     * @param patterns
     * @return
     */
    public ItemAPI setPattern(Pattern... patterns) {
        BannerMeta bannerMeta = (BannerMeta) this.itemStack.getItemMeta();
        for (Pattern pattern : patterns) {
            bannerMeta.addPattern(pattern);
        }
        this.itemStack.setItemMeta(bannerMeta);
        return this;
    }

    /**
     * Setzt den EigentÃ¼mer des Kopfes
     * @param owner
     * @return
     */
    public ItemAPI setSkullOwner(String owner) {
        (this.skullMeta = (SkullMeta) this.itemStack.getItemMeta()).setOwner(owner);
        this.itemStack.setItemMeta(skullMeta);
        return this;
    }

    /**
     * Erstellt das Item.
     * @return
     */
    public ItemStack build() {
        return this.itemStack;
    }

    /**
     * Erstellt das Item mit dem SpielerKopf
     * @return
     */
    @SuppressWarnings("deprecation")
	public ItemStack buildSkull() {
        this.itemStack.getData().setData((byte) 3);
        return this.itemStack;
    }
}