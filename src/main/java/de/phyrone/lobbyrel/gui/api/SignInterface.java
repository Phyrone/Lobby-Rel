package de.phyrone.lobbyrel.gui.api;

import com.bobacadodl.ClickEdit.packetwrapper.WrapperPlayServerOpenSignEditor;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.phyrone.lobbyrel.LobbyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class SignInterface implements InputGUI.InputInterface {
    private final static HashMap<UUID, InputGUI.InputHandler> handlers = new HashMap<>();
    private static ArrayList<Player> listnedPlayers = new ArrayList<>();
    private final Listener LISTNER = new Listener() {
        @EventHandler
        public void onLeave(PlayerQuitEvent e) {
            setListner(e.getPlayer(), false);
        }
    };
    private final ArrayList<PacketListener> PACKETLISTNER = new ArrayList<>(Arrays.asList(
            new PacketAdapter(LobbyPlugin.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
                @Override
                public void onPacketReceiving(PacketEvent event) {
                    try {
                        Player player = event.getPlayer();
                        if (isListner(event.getPlayer())) {
                            Bukkit.getScheduler().runTaskAsynchronously(LobbyPlugin.getInstance(), () -> {
                                if (getHandlers().containsKey(player.getUniqueId())) {
                                    String str = "";
                                    for (WrappedChatComponent component : event.getPacket().getChatComponentArrays().read(0)) {
                                        String lineRAW = component.getJson();
                                        String line = lineRAW.substring(1, lineRAW.length() - 1);
                                        str += (str.equalsIgnoreCase("") ? "" :
                                                (handlers.get(player.getUniqueId()).multiLine() ? "\n" : " ")) + line;
                                    }
                                    if (str.equalsIgnoreCase("")) {
                                        getHandlers().get(player.getUniqueId()).onDeny(player);
                                    } else {
                                        getHandlers().get(player.getUniqueId()).onAccept(player, str);
                                    }
                                }
                            });
                            event.setCancelled(true);
                            setListner(event.getPlayer(), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
    ));

    public SignInterface() {
        Bukkit.getPluginManager().registerEvents(LISTNER, LobbyPlugin.getInstance());
        for (PacketListener packetListener : PACKETLISTNER) {
            LobbyPlugin.getProtocolManager().addPacketListener(packetListener);
        }
    }

    public static HashMap<UUID, InputGUI.InputHandler> getHandlers() {
        return handlers;
    }

    @Override
    public void build(InputGUI.InputHandler inputHandler, Player player) {
        WrapperPlayServerOpenSignEditor wrapper = new WrapperPlayServerOpenSignEditor();
        wrapper.setLocation(player.getLocation());
        wrapper.sendPacket(player);
        handlers.put(player.getUniqueId(), inputHandler);
        setListner(player, true);


    }

    public void setListner(Player player, boolean enabled) {
        if (enabled) {
            if (!listnedPlayers.contains(player))
                listnedPlayers.add(player);
        } else {
            if (listnedPlayers.contains(player))
                listnedPlayers.remove(player);
            if (handlers.containsKey(player))
                handlers.remove(player);

        }
    }

    public boolean isListner(Player player) {
        return listnedPlayers.contains(player);
    }
}
