package com.bobacadodl.ClickEdit.packetwrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;

public class WrapperPlayServerOpenSignEditor extends AbstractPacket {
    private static final PacketType TYPE = PacketType.Play.Server.OPEN_SIGN_EDITOR;

    public WrapperPlayServerOpenSignEditor() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public void setLocation(Location location) {
        handle.getBlockPositionModifier().write(0,
                new BlockPosition((int) location.getX(), (int) location.getY(), (int) location.getZ()));
    }
}
