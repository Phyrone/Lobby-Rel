package de.phyrone.lobbyrel;

import de.phyrone.lobbyrel.config.Config;
import de.phyrone.lobbyrel.lib.protokoll.TinyProtocol;
import de.phyrone.lobbyrel.player.PlayerManager;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class PacketManipulator {
    TinyProtocol protocol;
    HashMap<String, PacketPlayerManipulator> handlers = new HashMap<>();
    boolean soundPacket = Config.getBoolean("Settings.Sound.CancelPackets", true);
    public PacketManipulator(Plugin plugin) {
        if (protocol != null)
            protocol.close();
        protocol = new TinyProtocol(plugin) {
            @Override
            public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
                return handle(receiver, channel, packet);
            }

            @Override
            public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
                return handle(sender, channel, packet);
            }
        };
        defaultHadlers();
    }

    public TinyProtocol getProtocol() {
        return protocol;
    }

    private void defaultHadlers() {
        addHandler("PacketPlayOutNamedSoundEffect", (player, channel, packet) -> {
            if (LobbyPlugin.getDebug()) {
                System.out.println("SoundPacket -> " + player.getName());
            }
            if (!PlayerManager.getPlayerData(player).getSound() && soundPacket
                    )
                return null;
            else return packet;
        });
        if (LobbyPlugin.getDebug()) {
            addHandler("PacketPlayOutTitle", (player, channel, packet) -> {
                System.out.println("TitlePacket -> " + player.getName());
                return packet;
            });
        }

    }

    public void addHandler(String type, PacketPlayerManipulator manipulator) {
        addHandler(type, manipulator, new String[0]);
    }

    public void addHandler(String type, PacketPlayerManipulator manipulator, String... otherTypes) {
        handlers.put(type, manipulator);
        for (String otherType : otherTypes)
            handlers.put(otherType, manipulator);
    }

    private Object handle(Player player, Channel channel, Object packet) {
        try {
            return handlers.getOrDefault(packet.getClass().getSimpleName(), (player1, channel1, packet1) -> packet1)
                    .onPacket(player, channel, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packet;
    }

    public interface PacketPlayerManipulator {
        Object onPacket(Player player, Channel channel, Object packet);
    }
}
