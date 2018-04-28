package de.phyrone.lobbyrel.scheduler;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.events.LobbyReloadEvent;
import de.phyrone.lobbyrel.lib.Tools;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class StayActionManager implements Listener {
    static StayActionManager instance;

    private HashMap<Player, IHandledAction> Actions = new HashMap<>();
    private HashMap<Player, StaticActionApi> apis = new HashMap<>();

    public StayActionManager() {
        instance = this;

    }

    public static StayActionManager getInstance() {
        return instance;
    }

    private IHandledAction getIhandled(Player player) {
        if (!Actions.containsKey(player))
            Actions.put(player, new IHandledAction(player));
        return Actions.get(player);
    }

    public StaticActionApi getAction(Player player) {
        if (!apis.containsKey(player))
            apis.put(player, new StaticActionApi() {
                @Override
                public void setAction(TextHandler handler) {
                    getIhandled(player).handler = handler;
                }

                @Override
                public void setAction(String action) {
                    getIhandled(player).setText(action);
                }

                @Override
                public boolean getEnabled() {
                    return getIhandled(player).enabled;
                }

                @Override
                public void setEnabled(boolean enabled) {
                    getIhandled(player).enabled = enabled;
                    getIhandled(player).updateSched();
                }

                @Override
                public String getText() {
                    return getIhandled(player).getText();
                }

                @Override
                public String getDynamicText() {
                    return getIhandled(player).getText(player);
                }

                @Override
                public Player getPlayer() {
                    return player;
                }
            });
        return apis.get(player);

    }


    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent e) {
        remove(e.getPlayer());

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onReload(LobbyReloadEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            remove(player);
            add(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e) {
        add(e.getPlayer());
    }

    private void remove(Player player) {
        if (apis.containsKey(player)) apis.remove(player);
        if (Actions.containsKey(player)) {
            getAction(player).setEnabled(false);
            Actions.remove(player);
        }
    }

    private void add(Player player) {
        Actions.put(player, new IHandledAction(player));
    }

    public interface TextHandler {
        String onText(Player player);
    }

    public interface StaticActionApi {
        void setAction(TextHandler handler);

        void setAction(String Action);

        boolean getEnabled();

        void setEnabled(boolean enabled);

        String getText();

        String getDynamicText();

        Player getPlayer();
    }

}

class IHandledAction {
    static BukkitScheduler s = Bukkit.getScheduler();
    Player player;
    StayActionManager.TextHandler handler = null;
    String text = "&k-------";
    boolean enabled = false;
    BukkitTask task = null;
    Runnable runnable = () -> updateAction();

    public IHandledAction(Player player) {
        this.player = player;
    }

    private void updateAction() {
        if (LobbyPlugin.getDebug())
            System.out.println("Send Action-Bar");
        String ctext = getText(player);
        if (ctext == null) {
            enabled = false;
            updateSched();
        }
        ctext = ChatColor.translateAlternateColorCodes('&', ctext);
        Tools.sendActionbar(player, ctext);
    }

    public void updateSched() {
        if (enabled && !isRunning()) task = s.runTaskTimerAsynchronously(LobbyPlugin.getInstance(), runnable, 1, 5);
        else if (!enabled && isRunning()) {
            task.cancel();
            task = null;
        }

    }

    boolean isRunning() {
        if (task == null)
            return false;
        else return s.isCurrentlyRunning(task.getTaskId());
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        handler = null;
        if (enabled)
            updateAction();
    }

    public StayActionManager.TextHandler getHandler() {
        return handler;
    }

    public String getText(Player player) {
        return handler != null ? handler.onText(player) : text;
    }
}
