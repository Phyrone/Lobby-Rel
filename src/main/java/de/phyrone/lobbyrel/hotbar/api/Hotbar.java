package de.phyrone.lobbyrel.hotbar.api;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.events.HotbarSwitchEvent;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Hotbar {
    static final HotbarItem voiditem = new HotbarItem(new ItemStack(Material.AIR));
    final HashMap<UUID, Integer> sites = new HashMap<>();
    final HashMap<Integer, HotbarItem> items = new HashMap<>();
    final HashMap<Integer, HotbarItem> staticitems = new HashMap<>();
    HotbarAction.OpenListner openListner = player -> {

    };
    HotbarAction.CloseListner closeListner = player -> {

    };
    HotbarAction.SwitchListner switchListner = (player, from, to) -> {

    };

    public static ItemStack setMeta(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            try {
                //NBTItem nbt = new NBTItem(item);
                //nbt.setBoolean("LobbyItem", true);
                //item = nbt.getItem();
            } catch (Exception e) {
                System.err.println("Error: setNBT " + item.getItemMeta().getDisplayName());
                e.printStackTrace();

            }
        }
        return item;

    }

    public final void open(Player player, int site) {
        try {
            Hotbar hb = this;

            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
                Player p = player;
                PlayerData pd = PlayerManager.getPlayerData(player);
                Hotbar chb = pd.getCurrendHotbar();
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        try {
                            if (!hb.equals(chb)) {
                                if (chb != null) {
                                    chb.getCloseListner().onClose(player);
                                }
                                hb.getOpenListner().onOpen(player);
                                Bukkit.getPluginManager().callEvent(new HotbarSwitchEvent(player,
                                        pd.getCurrendHotbar(), hb));
                            } else {
                                hb.getSwitchListner().onSwitchSite(player, getSite(player), site);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskAsynchronously(LobbyPlugin.getInstance());
                pd.setCurrendHotbar(hb).save();
                sites.put(p.getUniqueId(), site);
                if (getMaxSite() == 0) {
                    p.setLevel(0);
                } else {
                    p.setLevel(site + 1);
                }
                //Head
                for (int i = 0; i < 9; i++) {
                    updateItem(i, p);
                }

            });

        } catch (Exception e) {
            System.err.println("[Lobby-Re] Error: OpenHotbar");
            e.printStackTrace();

        }

    }

    public final Hotbar updateItem(int slot, Player p) {
        if (!this.equals(PlayerManager.getPlayerData(p).getCurrendHotbar())) {
            return this;
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                PlayerInventory inv = p.getInventory();
                HotbarItem item = getItem(slot, getSite(p));
                ItemStack ini = setMeta(inv.getItem(slot));
                if (ini == null) {
                    inv.setItem(slot, item.getItem(p));
                } else if (ini.equals(item.getItem())) {

                } else {
                    inv.setItem(slot, item.getItem(p));
                }
            }
        }.runTaskAsynchronously(LobbyPlugin.getInstance());
        return this;
    }

    public HotbarAction.CloseListner getCloseListner() {
        return closeListner;
    }

    public Hotbar setCloseListner(HotbarAction.CloseListner closeListner) {
        this.closeListner = closeListner;
        return this;
    }

    public HotbarAction.OpenListner getOpenListner() {
        return openListner;
    }

    public Hotbar setOpenListner(HotbarAction.OpenListner openListner) {
        this.openListner = openListner;
        return this;
    }

    public HotbarAction.SwitchListner getSwitchListner() {
        return switchListner;
    }

    public Hotbar setSwitchListner(HotbarAction.SwitchListner switchListner) {
        this.switchListner = switchListner;
        return this;
    }

    private int getIPP() {
        if (staticitems.isEmpty()) {
            return 9;
        }
        return 9 - staticitems.keySet().size();
    }

    public HotbarItem getItem(int slot, int site) {
        int ipp = getIPP();
        int i = 0;
        if (staticitems.containsKey(slot)) {
            return staticitems.getOrDefault(slot, voiditem);
        }
        for (int i2 = 0; i2 < slot; i2++) {
            if (!staticitems.containsKey(i2)) {
                i++;
            }
        }
        return items.getOrDefault(Tools.getTotalID(site, i, ipp), voiditem);
    }

    public void setItem(int slot, HotbarItem item) {
        items.put(slot, item);
    }

    public void setStaticItem(int slot, HotbarItem item) {
        if (slot < 0 || slot > 8) {
            System.out.println("Lobby-Rel[Debug] Error while set static item (wrong slot)(slot: " + String.valueOf(slot) + ")");
            return;
        }
        staticitems.put(slot, item);
    }

    public int getSite(UUID uuid) {
        return sites.getOrDefault(uuid, 0);
    }

    public int getSite(Player player) {
        return sites.getOrDefault(player.getUniqueId(), 0);
    }

    public void nextSite(Player player) {
        if (getMaxSite() == 0) return;
        int max = getMaxSite();
        int site = getSite(player);
        if (site >= max || site < 0) {
            open(player, 0);
        } else {
            open(player, site + 1);
        }
    }

    public void prevSite(Player player) {
        if (getMaxSite() == 0) return;
        int max = getMaxSite();
        int site = getSite(player);
        if (site > max || site <= 0) {
            open(player, getMaxSite());
        } else {
            open(player, site - 1);
        }
    }

    public int getMaxSite() {
        if (getIPP() == 0) {
            return 0;
        }
        return getMaxSlot() / getIPP();
    }

    public int getMaxSlot() {
        int ms = 0;
        for (Integer i : items.keySet()) {
            if (i > ms) {
                ms = i;
            }
        }
        return ms;
    }

}
