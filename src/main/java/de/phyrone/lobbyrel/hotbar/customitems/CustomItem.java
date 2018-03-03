package de.phyrone.lobbyrel.hotbar.customitems;

import de.phyrone.lobbyrel.lib.LobbyItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItem {
    public CustomItem(int slot, ItemStack item) {
        this.Slot = slot;
        this.Item = new LobbyItem(item);
    }

    public CustomItem(int slot) {
        this.Slot = slot;
    }

    public LobbyItem Item = new LobbyItem(new ItemStack(Material.STONE));
    public String Command = "";
    public CommandType RunCommandAs = CommandType.PLAYER;
    public int Slot;

    public LobbyItem getItem() {
        return Item;
    }

    public CustomItem setItem(LobbyItem item) {
        Item = item;
        return this;
    }

    //GetterSetter
    public String getCommand() {
        return Command;
    }

    public CustomItem setCommand(String command) {
        Command = command;
        return this;

    }

    public CommandType getRunCommandAs() {
        return RunCommandAs;
    }

    public CustomItem setRunCommandAs(CommandType runCommandAs) {
        RunCommandAs = runCommandAs;
        return this;

    }

    public int getSlot() {
        return Slot;
    }

    public CustomItem setSlot(int slot) {
        Slot = slot;
        return this;
    }

}
