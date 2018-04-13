package de.phyrone.lobbyrel.player.settings;

import de.phyrone.lobbyrel.lib.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsModule {
    ArrayList<ClickableItem> options = new ArrayList<>();
    ItemStack icon = new ItemBuilder(Material.BEDROCK).glow().displayname("&4&lERROR").build();
    SettingsModuleAction action = new SettingsModuleAction() {

        @Override
        public ArrayList<ClickableItem> getOptions(Player player) {
            return null;
        }

        @Override
        public ItemStack getIcon(Player player) {
            return null;
        }

        @Override
        public ItemStack getCurrent(Player player) {
            return null;
        }
    };

    public SettingsModule(ItemStack item) {
        this.icon = item;
    }

    public SettingsModule() {
    }

    public SettingsModule(SettingsModuleAction action) {
        this.action = action;
    }

    public List<ClickableItem> getOptions(Player player) {
        if (player != null) {
            List<ClickableItem> x = action.getOptions(player);
            if (x != null) {
                return x;
            }
        }
        return options;
    }

    public SettingsModule setOptions(ArrayList<ClickableItem> options) {
        this.options = options;
        return this;
    }

    public ItemStack getIcon(Player player) {
        if (player != null) {
            ItemStack x = action.getIcon(player);
            if (x != null) {
                return x;
            }
        }
        return icon;
    }

    public ItemStack getCurrent(Player player) {
        if (player != null) {
            ItemStack x = action.getCurrent(player);
            if (x != null) {
                return x;
            }
        }
        return icon;
    }

    public SettingsModuleAction getAction() {
        return action;
    }

    public SettingsModule setAction(SettingsModuleAction action) {
        this.action = action;
        return this;
    }


}
