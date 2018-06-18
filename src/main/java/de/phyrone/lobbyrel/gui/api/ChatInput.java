package de.phyrone.lobbyrel.gui.api;

import de.phyrone.lobbyrel.LobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatInput implements InputGUI.InputInterface, Listener {
    ArrayList<Player> activePlayers = new ArrayList<>();
    HashMap<Player, InputGUI.InputHandler> handlers = new HashMap<>();

    public ChatInput() {
        if (LobbyPlugin.getDebug())
            System.out.println("INIT Chat-Listner");
        Bukkit.getPluginManager().registerEvents(this, LobbyPlugin.getInstance());
    }

    @Override
    public void build(InputGUI.InputHandler inputHandler, Player player) {
        setActivePlayer(player, true);
        handlers.put(player, inputHandler);
        player.sendMessage(new String[]{
                "§8-----------------------------",
                "§aChatinput §6(type #cancel to cancel)",
                "§8-----------------------------"
        });
        player.closeInventory();
    }

    public void setActivePlayer(Player player, boolean active) {
        if (activePlayers.contains(player) && !active)
            activePlayers.remove(player);
        if (!activePlayers.contains(player) && active)
            activePlayers.add(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {
        setActivePlayer(e.getPlayer(), false);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (activePlayers.contains(e.getPlayer())) {
            e.setCancelled(true);
            setActivePlayer(e.getPlayer(), false);
            if (handlers.containsKey(e.getPlayer())) {
                InputGUI.InputHandler handler = handlers.get(e.getPlayer());
                if (e.getMessage().equalsIgnoreCase("#cancel")) {
                    e.getPlayer().sendMessage("§cInput aborted!");
                    handler.onDeny(e.getPlayer());
                } else {
                    e.getPlayer().sendMessage("§8> §r" + ChatColor.translateAlternateColorCodes('&', e.getMessage()));
                    e.getPlayer().sendMessage("§aInput successful!");
                    handler.onAccept(e.getPlayer(), handler.multiLine() ? e.getMessage().replace("\\n", "\n")
                            : e.getMessage());
                }
            }
            handlers.remove(e.getPlayer());
        }
    }
}
