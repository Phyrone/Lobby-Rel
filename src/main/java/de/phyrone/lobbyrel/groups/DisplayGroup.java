package de.phyrone.lobbyrel.groups;

import de.phyrone.lobbyrel.lib.RandomString;
import org.bukkit.ChatColor;

public class DisplayGroup {
    String Prefix = "";
    String Suffix = "";
    String Permission = "lobby.group." + new RandomString(3).nextString();
    String ChatLayout = "&7%player%&b» &r%message%";
    String TabLayout = "&7%player%";

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', Prefix.length() > 16 ? Prefix.substring(0, 16) : Prefix);
    }

    public String getSuffix() {
        return ChatColor.translateAlternateColorCodes('&', Suffix.length() > 16 ? Suffix.substring(0, 16) : Suffix);
    }

    public DisplayGroup() {
    }
    public DisplayGroup(String name, char colorcode) {
        this.Permission = "lobby.group." + name.toLowerCase();
        this.ChatLayout = "&8[&" + colorcode + name + "&8] &7%player%&b» &r%message%";
        this.TabLayout = "&" + colorcode + name + "&7 | %player%";
        this.Prefix = "&" + colorcode + name + " &r";
        this.Suffix = "";
    }

    public DisplayGroup setPermission(String permission) {
        Permission = permission;
        return this;
    }

    public DisplayGroup setChatLayout(String chatLayout) {
        ChatLayout = chatLayout;
        return this;
    }

    public DisplayGroup setTabLayout(String tabLayout) {
        TabLayout = tabLayout;
        return this;
    }

    public DisplayGroup setPrefix(String prefix) {
        Prefix = prefix;
        return this;
    }

    public DisplayGroup setSuffix(String suffix) {
        Suffix = suffix;
        return this;
    }

    public String getPermission() {
        return Permission;
    }

    public String getChatLayout() {
        return ChatLayout;
    }

    public String getTabLayout() {
        return TabLayout;
    }

}
