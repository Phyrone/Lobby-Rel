package de.phyrone.lobbyrel.warps;

import de.phyrone.lobbyrel.lib.LobbyItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Warp {
    public int Sort_ID = 0;
    public double X = 0;
    public double Y = 0;
    public double Z = 0;
    public float Pitch = 0;
    public float Yaw = 0;
    public String World = "world";
    public LobbyItem WarpItem = new LobbyItem(new ItemStack(Material.STONE));

    public Warp(Location loc) {
        X = loc.getX();
        Y = loc.getY();
        Z = loc.getZ();
        World = loc.getWorld().getName();

        try {
            Pitch = loc.getPitch();
            Yaw = loc.getYaw();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Warp() {

    }

    public int getSort_ID() {
        return Sort_ID;
    }

    public Warp setSort_ID(int sort_ID) {
        Sort_ID = sort_ID;
        return this;
    }

    public LobbyItem getWarpItem() {
        return WarpItem;
    }

    public Warp setWarpItem(LobbyItem warpItem) {
        WarpItem = warpItem;
        return this;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(World), X, Y, Z, Yaw, Pitch);
    }

    public Warp setLocation(Location loc) {
        X = loc.getX();
        Y = loc.getY();
        Z = loc.getZ();
        World = loc.getWorld().getName();

        try {
            Pitch = loc.getPitch();
            Yaw = loc.getYaw();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Warp setDisplayname(String name) {
        WarpItem = WarpItem.setDisplayName(name);
        return this;
    }

    public ItemStack getItemStack(Player player) {
        return WarpItem.getAsItemStack(player);
    }
}
