package de.phyrone.lobbyrel.groups;

public class DisplayGroup {
    String Permission;
    String ChatLayout;
    String TabLayout;

    public DisplayGroup(String name, char colorcode) {
        this.Permission = "lobby.group." + name.toLowerCase();
        this.ChatLayout = "&8[&" + colorcode + name + "&8] &7%player%&b» &r%message%";
        this.TabLayout = "&" + colorcode + name + " | &7%player%";
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
