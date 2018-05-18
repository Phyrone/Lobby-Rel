package de.phyrone.lobbyrel.lib;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.lib.item.SecondItemBuilder;
import de.phyrone.lobbyrel.lib.json.FancyMessage;
import de.phyrone.lobbyrel.lib.protokoll.Reflection;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class Tools {
    private static Class<?> playoutchat = Reflection.getClass("{nms}.PacketPlayOutChat");
    private static Class<Object> ICBC = Reflection.getUntypedClass("{nms}.IChatBaseComponent");
    private static Reflection.FieldAccessor<Object> message = Reflection.getField(playoutchat, ICBC, 0);
    private static Reflection.FieldAccessor<Byte> actionposition = Reflection.getField(playoutchat, byte.class, 0);

    static void sendPacket(Player player, Object packet) {
        LobbyPlugin.getProtocol().sendPacket(player, packet);
    }

    /* Actionbar */
    public static void sendActionbar(Player player, String message) {
        sendActionbar(player, new FancyMessage(message));

    }

    public static void sendActionbar(Player player, FancyMessage actionbarmessage) {
        try {
            Object packet = Reflection.getConstructor("{nms}.PacketPlayOutChat").invoke();
            message.set(packet, getChatComponent(actionbarmessage.toJSONString()));
            actionposition.set(packet, (byte) 2);
            LobbyPlugin.getProtocol().sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendJsonChat(Player player, FancyMessage actionbarmessage) {
        try {
            Object packet = Reflection.getConstructor("{nms}.PacketPlayOutChat").invoke();
            message.set(packet, getChatComponent(actionbarmessage.toJSONString()));
            actionposition.set(packet, (byte) 0);
            LobbyPlugin.getProtocol().sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int getTotalID(int site, int slot, int itemsperpage) {
        return site * itemsperpage + slot;
    }

    public static Object getChatComponent(String rawMessage) throws Exception {
        return getNMSClass2("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, rawMessage);
    }

    public static Class<?> getNMSClass2(String name) {
        // org.bukkit.craftbukkit.v1_8_R3...
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Material parseToMaterial(String materialmane) {
        Material ret = null;
        try {
            int id = Integer.parseInt(materialmane);
            ret = new ItemStack(id).getType();
        } catch (Exception e) {
            try {
                ret = Material.valueOf(materialmane.toUpperCase());
            } catch (Exception e21) {
            }
        }
        return ret;
    }

    public static Class<?> getNMSClass(String name) {
        return getNMSClass2(name);
    }

    public static ItemStack getSkullFromBASE64(String texture, String name) {
        return getSkullFromBASE64(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), texture, name);
    }

    public static ItemStack getSkullFromBASE64(ItemStack head, String texture, String name) {
        if (texture.isEmpty()) return head;
        head.setDurability((short) SkullType.PLAYER.ordinal());
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack getSkullFromURL(String url) {
        return getSkullFromURL(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), url);
    }

    public static ItemStack getSkullFromURL(ItemStack head, String url) {
        head.setDurability((short) SkullType.PLAYER.ordinal());
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
    }

    public static ItemStack getSkullFromPlayer(ItemStack item, String owner) {
        return new SecondItemBuilder(item)
                .setDurability((short) SkullType.PLAYER.ordinal()).setSkullOwner(owner).toItemStack();
    }

    public static ItemStack getSkullFromPlayer(String owner) {
        return getSkullFromPlayer(new ItemStack(Material.SKULL_ITEM, 1, (short) 3), owner);
    }

    public static void sendTitle(Player player, FancyMessage title, int fadeInTime, int showTime, int fadeOutTime) {
        sendTitle(player, title, false, fadeInTime, showTime, fadeOutTime);
    }

    //Title
    public static void sendTitle(Player player, FancyMessage title, boolean subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        try {

            try {
                Object msgcomponent = getChatComponent(title.toJSONString());

                /* Times */
                Constructor timesConst = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent")});
                Object timesPacket = timesConst.newInstance(getNMSClass2("PacketPlayOutTitle").getDeclaredClasses()[0].getField(
                        "TIMES").get(null), msgcomponent);
                setPrivateField(timesPacket, "c", fadeInTime);
                setPrivateField(timesPacket, "d", showTime);
                setPrivateField(timesPacket, "e", fadeOutTime);
                sendPacket(player, timesPacket);

                /* Title */
                Object type = getNMSClass2("PacketPlayOutTitle").getDeclaredClasses()[0].getField(subtitle ? "SUBTITLE" : "TITLE").get(null);
                Constructor subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent")});
                Object titlePacket = subtitleConstructor.newInstance(type, msgcomponent);
                sendPacket(player, titlePacket);

            } catch (Exception e1) {
                Bukkit.getConsoleSender().sendMessage("§6Title send Compatibility -> §4Please Contact Developer [" + e1 + "]");
                player.sendTitle(subtitle ? "" : title.toOldMessageFormat(), subtitle ? title.toOldMessageFormat() : "");
                if (LobbyPlugin.getDebug())
                    e1.printStackTrace();

            }
        } catch (Exception ex) {
            player.sendMessage("§4Send Title Failed -> " + ex.getMessage());
            ex.printStackTrace();
        }


    }

    public static void setPrivateField(Object instance, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = instance.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(instance, value);
    }

    static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static void saveJson(String jsonConfig, File configFile) {
        try {
            FileWriter writer = new FileWriter(configFile);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}