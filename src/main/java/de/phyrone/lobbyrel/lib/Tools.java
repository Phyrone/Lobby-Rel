package de.phyrone.lobbyrel.lib;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.phyrone.lobbyrel.lib.item.SecondItemBuilder;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class Tools {
	//Actionbar
	public static void sendActionbar(Player p, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
                ChatColor.translateAlternateColorCodes('&', message) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
    }public static void sendActionbar(Player p, FancyMessage message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a(
        		message.toJSONString());
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(bar);
    }
    //Title
    /**
     * Send a title to player
     * @param player Player to send the title to
     * @param text The text displayed in the title
     * @param fadeInTime The time the title takes to fade in
     * @param showTime The time the title is displayed
     * @param fadeOutTime The time the title takes to fade out
     * @param color The color of the title
     */
     public static void sendTitle(Player player, FancyMessage text, int fadeInTime, int showTime, int fadeOutTime)
     {
    	 IChatBaseComponent chatTitle = ChatSerializer.a(text.toJSONString());
    	 PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
    	 PacketPlayOutTitle length = new PacketPlayOutTitle(fadeInTime, showTime, fadeOutTime);


    	 ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
    	 ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
     }public static int getTotalID(int site,int slot,int itemsperpage){
    	 return site*itemsperpage+slot;
     }public static int getSiteSlot(){
    	 return 0;
     }
     
     public static void addFakeTabPlayer(Player player,String name){
    	 GameProfile profile = new GameProfile(UUID.randomUUID(), name);
    	 MinecraftServer server = MinecraftServer.getServer();
    	// Get the Minecraft server object, required to create a player.
    	// UPDATE:  In newer versions, this method returns null, so instead get the  MinecraftServer
//    	         with this code instead:
//    	    ((CraftServer) Bukkit.getServer()).getServer();

    	WorldServer world = server.getWorldServer(0);
    	// Get the world server for the overworld (0). Also required.

    	PlayerInteractManager manager = new PlayerInteractManager(world);
    	// Create a new player interact manager for the overworld.  Required.

    	EntityPlayer pp = new EntityPlayer(server, world, profile, manager);
    	PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, pp);
    	((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

     }
	public static Material parseToMaterial(String materialmane) {
    	 Material ret = null;
    	 try {
    		 int id = Integer.parseInt(materialmane);
    		 ret = new ItemStack(id).getType();
    	 }catch (Exception e) {
			try {
				ret = Material.valueOf(materialmane.toUpperCase());
			}catch (Exception e21) {}
		}return ret;
     }
     public static ItemStack createSkull(String texture, String name) {
    	 return createSkull(new ItemStack(Material.SKULL_ITEM, 1, (short)3), texture, name);
     }
     public static ItemStack createSkull(ItemStack head,String texture, String name)
     {
         if (texture.isEmpty()) return head;
        head.setDurability((short)SkullType.PLAYER.ordinal());
         SkullMeta headMeta = (SkullMeta) head.getItemMeta();
         
         GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        
         profile.getProperties().put("textures", new Property("textures", texture));
        
         try
         {
             Field profileField = headMeta.getClass().getDeclaredField("profile");
             profileField.setAccessible(true);
             profileField.set(headMeta, profile);
            
         }
         catch (IllegalArgumentException|NoSuchFieldException|SecurityException | IllegalAccessException error)
         {
             error.printStackTrace();
         }
         head.setItemMeta(headMeta);
         return head;
     }public static ItemStack getSkullFromURL(String url) {
    	 return getSkullFromURL(new ItemStack(Material.SKULL_ITEM, 1, (short)3), url);
     }public static ItemStack getSkullFromURL(ItemStack head,String url) {
    	head.setDurability((short)SkullType.PLAYER.ordinal());
		//return SkullCreator.withUrl(head, url);
        Validate.notNull(url, "url");
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String base64 = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
        base64 = Base64.getEncoder().encodeToString(base64.getBytes());
        return createSkull(head, base64, new RandomString(16).nextString());
     }public static ItemStack getSkullFromPlayer(ItemStack item,String owner) {
    	return new SecondItemBuilder(item)
		 .setDurability((short)SkullType.PLAYER.ordinal()).setSkullOwner(owner).toItemStack();
	}public static ItemStack getSkullFromPlayer(String owner) {
		return getSkullFromPlayer(new ItemStack(Material.SKULL_ITEM, 1, (short)3), owner);
	}

}
