package de.phyrone.lobbyrel.lib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.lib.item.SecondItemBuilder;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class Tools {
	//Actionbar
    public static void sendActionbar(Player player, String message) {
        sendActionbar(player, new FancyMessage(message));

    }

    public static void sendActionbar(Player player, FancyMessage message) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);
            packet.getBytes().write(0, (byte) 2);
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(message.toJSONString()));
            LobbyPlugin.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            String space = "<<<<<<<<<<<<ActionBar>>>>>>>>>>";
            player.sendMessage(new String[]{space, " ", message.toOldMessageFormat(), " ", space});
        }
    }
    //Title
    public static void sendTitle(Player player, FancyMessage title, int fadeInTime, int showTime, int fadeOutTime)
     {

         try {
             PacketContainer titlePacket = new PacketContainer(PacketType.Play.Server.TITLE);
             titlePacket.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
             titlePacket.getChatComponents().write(0, WrappedChatComponent.fromJson(title.toJSONString()));
             titlePacket.getIntegers().write(0, fadeInTime);
             titlePacket.getIntegers().write(1, showTime);
             titlePacket.getIntegers().write(2, fadeOutTime);
             LobbyPlugin.getProtocolManager().sendServerPacket(player, titlePacket);
         } catch (Exception e) {
             player.sendTitle(title.toOldMessageFormat(), "");
         }
         //PacketPlayOutTitle titlePacket = new PacketContainer(PacketType.Play.Server.TITLE);
         //PacketPlayOutTitle length = new PacketPlayOutTitle(fadeInTime, showTime, fadeOutTime);
         //((((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
         //((((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
     }public static int getTotalID(int site,int slot,int itemsperpage){
    	 return site*itemsperpage+slot;
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

    public static ItemStack getSkullFromBASE64(String texture, String name) {
        return getSkullFromBASE64(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), texture, name);
     }

    public static ItemStack getSkullFromBASE64(ItemStack head, String texture, String name)
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
        return getSkullFromBASE64(head, base64, new RandomString(16).nextString());
     }public static ItemStack getSkullFromPlayer(ItemStack item,String owner) {
    	return new SecondItemBuilder(item)
		 .setDurability((short)SkullType.PLAYER.ordinal()).setSkullOwner(owner).toItemStack();
	}public static ItemStack getSkullFromPlayer(String owner) {
		return getSkullFromPlayer(new ItemStack(Material.SKULL_ITEM, 1, (short)3), owner);
	}

}
