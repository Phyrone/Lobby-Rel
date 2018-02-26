package de.phyrone.lobbyrel.lib.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Easily create itemstacks, without messing your hands.
 * <i>Note that if you do use this in one of your projects, leave this notice.</i>
 * <i>Please do credit me if you do use this in one of your projects.</i>
 * @author NonameSL
 */
public class SecondItemBuilder {
   private ItemStack is;
   /**
    * Create a new ItemBuilder from scratch.
    * @param m The material to create the ItemBuilder with.
    */
   public SecondItemBuilder(Material m){
     this(m, 1);
   }
   /**
    * Create a new ItemBuilder over an existing itemstack.
    * @param is The itemstack to create the ItemBuilder over.
    */
   public SecondItemBuilder(ItemStack is){
     this.is=is;
   }
   /**
    * Create a new ItemBuilder from scratch.
    * @param m The material of the item.
    * @param amount The amount of the item.
    */
   public SecondItemBuilder(Material m, int amount){
     is= new ItemStack(m, amount);
   }
   /**
    * Create a new ItemBuilder from scratch.
    * @param m The material of the item.
    * @param amount The amount of the item.
    * @param durability The durability of the item.
    */
   public SecondItemBuilder(Material m, int amount, byte durability){
     is = new ItemStack(m, amount, durability);
   }
   /**
    * Clone the ItemBuilder into a new one.
    * @return The cloned instance.
    */
   public SecondItemBuilder clone(){
     return new SecondItemBuilder(is);
   }
   /**
    * Change the durability of the item.
    * @param dur The durability to set it to.
    */
   public SecondItemBuilder setDurability(short dur){
     is.setDurability(dur);
     return this;
   }
   /**
    * Set the displayname of the item.
    * @param name The name to change it to.
    */
   public SecondItemBuilder setName(String name){
     ItemMeta im = is.getItemMeta();
     im.setDisplayName(name);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Add an unsafe enchantment.
    * @param ench The enchantment to add.
    * @param level The level to put the enchant on.
    */
   public SecondItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
     is.addUnsafeEnchantment(ench, level);
     return this;
   }
   /**
    * Remove a certain enchant from the item.
    * @param ench The enchantment to remove
    */
   public SecondItemBuilder removeEnchantment(Enchantment ench){
     is.removeEnchantment(ench);
     return this;
   }
   /**
    * Set the skull owner for the item. Works on skulls only.
    * @param owner The name of the skull's owner.
    */
   public SecondItemBuilder setSkullOwner(String owner){
     try{
       SkullMeta im = (SkullMeta)is.getItemMeta();
       im.setOwner(owner);
       is.setItemMeta(im);
     }catch(ClassCastException expected){}
     return this;
   }
   /**
    * Add an enchant to the item.
    * @param ench The enchant to add
    * @param level The level
    */
   public SecondItemBuilder addEnchant(Enchantment ench, int level){
     ItemMeta im = is.getItemMeta();
     im.addEnchant(ench, level, true);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Add multiple enchants at once.
    * @param enchantments The enchants to add.
    */
   public SecondItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
     is.addEnchantments(enchantments);
     return this;
   }
   /**
    * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
    */
   public SecondItemBuilder setInfinityDurability(){
     is.setDurability(Short.MAX_VALUE);
     return this;
   }
   /**
    * Re-sets the lore.
    * @param lore The lore to set it to.
    */
   public SecondItemBuilder setLore(String... lore){
     ItemMeta im = is.getItemMeta();
     im.setLore(Arrays.asList(lore));
     is.setItemMeta(im);
     return this;
   }
   /**
    * Re-sets the lore.
    * @param lore The lore to set it to.
    */
   public SecondItemBuilder setLore(List<String> lore) {
     ItemMeta im = is.getItemMeta();
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Remove a lore line.
    * @param lore The lore to remove.
    */
   public SecondItemBuilder removeLoreLine(String line){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>(im.getLore());
     if(!lore.contains(line))return this;
     lore.remove(line);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Remove a lore line.
    * @param index The index of the lore line to remove.
    */
   public SecondItemBuilder removeLoreLine(int index){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>(im.getLore());
     if(index<0||index>lore.size())return this;
     lore.remove(index);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Add a lore line.
    * @param line The lore line to add.
    */
   public SecondItemBuilder addLoreLine(String line){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>();
     if(im.hasLore())lore = new ArrayList<>(im.getLore());
     lore.add(line);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Add a lore line.
    * @param line The lore line to add.
    * @param pos The index of where to put it.
    */
   public SecondItemBuilder addLoreLine(String line, int pos){
     ItemMeta im = is.getItemMeta();
     List<String> lore = new ArrayList<>(im.getLore());
     lore.set(pos, line);
     im.setLore(lore);
     is.setItemMeta(im);
     return this;
   }
   /**
    * Sets the dye color on an item.
    * <b>* Notice that this doesn't check for item type, sets the literal data of the dyecolor as durability.</b>
    * @param color The color to put.
    */
   @SuppressWarnings("deprecation")
   public SecondItemBuilder setDyeColor(DyeColor color){
     this.is.setDurability(color.getData());
     return this;
   }
   /**
    * Sets the dye color of a wool item. Works only on wool.
    * @deprecated As of version 1.2 changed to setDyeColor.
    * @see ItemBuilder@setDyeColor(DyeColor)
    * @param color The DyeColor to set the wool item to.
    */
   @Deprecated
   public SecondItemBuilder setWoolColor(DyeColor color){
    if(!is.getType().equals(Material.WOOL))return this;
    this.is.setDurability(color.getData());
    return this;
   }
   /**
    * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
    * @param color The color to set it to.
    */
   public SecondItemBuilder setLeatherArmorColor(Color color){
     try{
       LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
       im.setColor(color);
       is.setItemMeta(im);
     }catch(ClassCastException expected){}
     return this;
   }
   /**
    * Retrieves the itemstack from the ItemBuilder.
    * @return The itemstack created/modified by the ItemBuilder instance.
    */
   public ItemStack toItemStack(){
     return is;
   }
}
