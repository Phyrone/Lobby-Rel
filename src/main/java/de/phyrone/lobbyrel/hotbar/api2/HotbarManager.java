package de.phyrone.lobbyrel.hotbar.api2;

import com.google.gson.GsonBuilder;
import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.hotbar.MainHotbar;
import de.phyrone.lobbyrel.hotbar.api.HotbarItem;
import de.phyrone.lobbyrel.lib.Tools;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.PlayerData;
import de.phyrone.lobbyrel.scheduler.StayActionManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class HotbarManager implements Listener {
    private static HotbarWrapper defaultHotbar = new MainHotbar();
    private static HashMap<UUID, I_Hotbar> hotbars = new HashMap<>();
    private static HashMap<UUID, PlayerHotbar> apis = new HashMap<>();
    private static List<UUID> disabledPlayers = new ArrayList<>();

    public static boolean isDisabled(Player player) {
        return PlayerManager.getPlayerData(player).isBuilder() || disabledPlayers.contains(player.getUniqueId());
    }

    public static void setHotbar(Player player, HotbarWrapper hotbarWrapper) {
        try {
            if (player == null)
                return;
            if (hotbarWrapper == null) {
                if (!disabledPlayers.contains(player.getUniqueId()))
                    disabledPlayers.add(player.getUniqueId());
                if (hotbars.containsKey(player.getUniqueId()))
                    hotbars.remove(player.getUniqueId());
                return;
            } else if (disabledPlayers.contains(player.getUniqueId()))
                disabledPlayers.remove(player.getUniqueId());
            HashMap<Integer, HotbarItem> items = hotbarWrapper.getItems(player);
            if (LobbyPlugin.getDebug()) {
                System.out.println("SetHotbar: ");
                System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(items));
            }
            hotbars.put(player.getUniqueId(), new I_Hotbar());
            hotbars.get(player.getUniqueId()).items = items;
            update(player);
            apis.put(player.getUniqueId(), newAPI(player));
        } catch (Exception e) {
            System.err.println("[Lobby-Re] Error: OpenHotbar");
            e.printStackTrace();
        }
    }

    public static void updatePageDisplay(Player player) {
        if (getI_Hotbar(player).getLastPage() == 0)
            switch (PlayerManager.getInternalPlayerData(player.getUniqueId()).hotbarMutiPageDisplay) {
                case XP:
                    player.setLevel(0);
                    break;
                case ACTION:
                    StayActionManager.getInstance().getAction(player).setEnabled(false);
                    break;
                case ACTION_SHORT:
                case NONE:
                    break;
            }
        else {
            int page = getI_Hotbar(player).currendpage + 1;
            switch (PlayerManager.getInternalPlayerData(player.getUniqueId()).hotbarMutiPageDisplay) {
                case XP:
                    player.setLevel(page);
                    break;
                case ACTION_SHORT:
                    Tools.sendActionbar(player, "&6&l" + String.valueOf(page));
                    break;
                case ACTION:
                    StayActionManager.StaticActionApi action = StayActionManager.getInstance().getAction(player);
                    action.setEnabled(true);
                    action.setAction("&6&l" + String.valueOf(page));
                    break;
                case NONE:
                    break;
            }
        }
    }

    public static PlayerHotbar getHotbar(Player player) {
        if (player == null) return null;
        if (!apis.containsKey(player))
            apis.put(player.getUniqueId(), newAPI(player));
        return apis.get(player);
    }

    public static I_Hotbar getI_Hotbar(Player player) {
        Validate.notNull(player);
        if (isDisabled(player))
            return new I_Hotbar();
        if (!hotbars.containsKey(player.getUniqueId()))
            hotbars.put(player.getUniqueId(), new I_Hotbar());
        return hotbars.get(player.getUniqueId());

    }

    public static void setDefaultHotbar(HotbarWrapper defaultHotbar) {
        HotbarManager.defaultHotbar = defaultHotbar;
    }

    private static PlayerHotbar newAPI(Player player) {
        return new PlayerHotbar() {
            @Override
            public void changePage(PageDirection direction) {
                setPageAndUpdate(direction, player);
            }

            @Override
            public void changePage(int page) {
                if (getI_Hotbar(player).getLastPage() < page)
                    page = getI_Hotbar(player).getLastPage();
                else if (page < 0)
                    page = 0;
                setPageAndUpdate(page, player);
            }

            @Override
            public void setItem(int slot, HotbarItem item) {
                if (getI_Hotbar(player).getItemIgnorePage(slot) == null)
                    getI_Hotbar(player).items.put(slot, item);
                else getI_Hotbar(player).items.put(slot, item);
                updateIfNeedet(player, slot, false);
            }

            @Override
            public void setItem(int slot, ItemStack item) {
                if (getI_Hotbar(player).getItemIgnorePage(slot) == null)
                    getI_Hotbar(player).items.put(slot, new HotbarItem(item));
                else getI_Hotbar(player).getItemIgnorePage(slot).setItem(item);
                updateIfNeedet(player, slot, false);
            }

            @Override
            public void removeItem(int slot) {
                if (getI_Hotbar(player).items.containsKey(slot))
                    getI_Hotbar(player).items.remove(slot);
                updateIfNeedet(player, slot, false);
            }

            @Override
            public void dispatch(Dispatch dispatch) {
                HotbarManager.dispatch(player, dispatch);
            }

            @Override
            public void update() {
                if (!isDisabled(player))
                    HotbarManager.update(player);
            }

            @Override
            public void setMutiPageDisplay(MutiPageDisplay display) {

            }

            @Override
            public HotbarItem getItem(int slot) {
                return getI_Hotbar(player).getItemIgnorePage(slot);
            }

            @Override
            public int getCurrendPage() {
                return getI_Hotbar(player).currendpage;
            }

            @Override
            public void close() {
                setHotbar(player, defaultHotbar);
            }
        };
    }

    private static void updateIfNeedet(Player player, int chanched, boolean isPage) {
        if (isPage)
            if (getI_Hotbar(player).currendpage == chanched)
                update(player);
            else if (getI_Hotbar(player).currendpage == slotToPage(chanched))
                update(player);

    }

    private static void update(Player player) {
        if (isDisabled(player))
            return;
        I_Hotbar hotbar = getI_Hotbar(player);
        PlayerInventory inv = player.getInventory();
        int startFullSlor = hotbar.getPageStartSlot();
        for (int noFinalSlot = 0; noFinalSlot < 9; noFinalSlot++) {
            int hotbarslot = noFinalSlot;
            new Thread(() -> {
                try {
                    inv.setItem(hotbarslot, hbiToItemStack(hotbar.getItemIgnorePage(startFullSlor + hotbarslot), player));
                } catch (Exception e) {
                    inv.clear(hotbarslot);
                }
            }, "SetHotbarItemThread-" + String.valueOf(hotbar.hashCode()) + "-" + String.valueOf(hotbarslot)).start();
        }
        updatePageDisplay(player);
    }

    private static void setPageAndUpdate(int page, Player player) {
        boolean needupdate = !(page == getI_Hotbar(player).currendpage);
        getI_Hotbar(player).currendpage = page;
        if (needupdate)
            update(player);
    }

    private static void setPageAndUpdate(PlayerHotbar.PageDirection direction, Player player) {
        I_Hotbar hb = getI_Hotbar(player);
        if (hb.getMaxSlot() < 9)
            setPageAndUpdate(0, player);
        else
            switch (direction) {
                case FORWARD:
                    if (hb.isLast()) setPageAndUpdate(0, player);
                    else setPageAndUpdate(hb.currendpage + 1, player);
                    break;
                case BACKWARD:
                    if (hb.isFirst()) setPageAndUpdate(hb.getLastPage(), player);
                    else setPageAndUpdate(hb.currendpage - 1, player);
                    break;
                case LAST:
                    setPageAndUpdate(hb.getLastPage(), player);
                    break;
                case FIRST:
                    setPageAndUpdate(0, player);
                    break;
            }
    }

    private static ItemStack hbiToItemStack(HotbarItem hotbarItem, Player player) {
        if (hotbarItem == null)
            return new ItemStack(Material.AIR);
        return hotbarItem.getItem(player);
    }

    private static void dispatch(Player player, PlayerHotbar.Dispatch dispatch) {
        if (isDisabled(player))
            return;
        I_Hotbar hotbar = getI_Hotbar(player);
        HotbarItem item = hotbar.getItemIgnorePage(dispatch.getSlot());
        if (item != null)
            item.interact(player, dispatch);
    }

    private static int slotToPage(int slot) {
        return (int) Math.ceil((double) slot / (double) I_Hotbar.IPP);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwitch(PlayerItemHeldEvent e) {
        int from = e.getPreviousSlot();
        int to = e.getNewSlot();
        Player p = e.getPlayer();
        PlayerData pd = PlayerManager.getPlayerData(p);
        if (isDisabled(e.getPlayer())) {
            return;
        } else {
            I_Hotbar hb = getI_Hotbar(e.getPlayer());
            if (hb == null) {
                return;
            } else {
                boolean next = (from == 8 && to == 0) || (from == 7 && to == 0) || (from == 8 && to == 1);
                boolean prev = (from == 0 && to == 8) || (from == 1 && to == 8) || (from == 0 && to == 7);
                if (next) {
                    setPageAndUpdate(PlayerHotbar.PageDirection.FORWARD, p);
                } else if (prev) {
                    setPageAndUpdate(PlayerHotbar.PageDirection.BACKWARD, p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(PlayerInteractEvent e) {
        if (isDisabled(e.getPlayer())) {
            return;
        }
        e.setCancelled(true);
        if (e.getAction() == Action.PHYSICAL) {
            return;
        }
        if (LobbyPlugin.getDebug())
            System.out.println("Click: " + String.valueOf(e.getAction()));
        PlayerInventory inv = e.getPlayer().getInventory();
        dispatch(e.getPlayer(),
                new PlayerHotbar.Dispatch(getI_Hotbar(e.getPlayer()).getPageStartSlot() + inv.getHeldItemSlot(),
                        (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) ? PlayerHotbar.DispachType.CLICK : PlayerHotbar.DispachType.HIT
                ));
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            dispatch(player, new PlayerHotbar.Dispatch(getI_Hotbar(player).getPageStartSlot()
                    + player.getInventory().getHeldItemSlot(), PlayerHotbar.DispachType.HIT, e.getEntity()));
        }
    }
}

class I_Hotbar {
    public static final int IPP = 9;
    int currendpage = 0;
    HashMap<Integer, HotbarItem> items = new HashMap<>();

    public I_Hotbar() {

    }

    public I_Hotbar(HashMap<Integer, HotbarItem> items) {
        this.items = items;
    }

    public void setItems(HashMap<Integer, HotbarItem> items) {
        this.items = items;
    }

    public int getPageStartSlot(int page) {
        return page * IPP;
    }

    public int getLastPage() {
        return (int) Math.ceil((double) getMaxSlot() / (double) IPP) - 1;
    }

    public int getPageEndSlot(int page) {
        return ((page + 1) * IPP) - 1;
    }

    public int getPageEndSlot() {
        return getPageEndSlot(currendpage);
    }

    public boolean isLast(int page) {
        return getPageEndSlot(page) >= getMaxSlot();
    }

    public boolean isFirst(int page) {
        return page <= 0;
    }

    public boolean isLast() {
        return isLast(currendpage);
    }

    public boolean isFirst() {
        return isFirst(currendpage);
    }

    public int getPageStartSlot() {
        return getPageStartSlot(currendpage);
    }

    public HotbarItem getItem(int page, int slot) {
        return getItemIgnorePage(page * IPP + slot);
    }

    public HotbarItem getItem(int slot) {
        return getItem(currendpage, slot);
    }

    public HotbarItem getItemIgnorePage(int slot) {
        return items.getOrDefault(slot, null);
    }

    public int getMaxSlot() {
        if (items.isEmpty())
            return 0;
        return Collections.max(items.keySet(), Comparator.comparingInt(i -> i));
    }

}
