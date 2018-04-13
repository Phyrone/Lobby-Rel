package de.phyrone.lobbyrel.navigator;

import de.phyrone.lobbyrel.lib.LobbyItem;

public class Navigator {
	NavigatorAction action;
	String name = "CustomNavigator";
    LobbyItem item;
	public String getName() {
		return name;
	}
	public Navigator setName(String name) {
		this.name = name;
		return this;
	}

    public Navigator(LobbyItem settings, NavigatorAction action) {
        this.action = action;
        item = settings;
    }

    public LobbyItem getItem() {
        return item;
    }

    public Navigator setItem(LobbyItem item) {
        this.item = item;
		return this;
	}

    public NavigatorAction getAction() {
        return action;
    }

}
