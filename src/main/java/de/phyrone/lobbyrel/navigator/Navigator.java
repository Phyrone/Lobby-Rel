package de.phyrone.lobbyrel.navigator;

import de.phyrone.lobbyrel.lib.Color;

public class Navigator {
	NavigatorAction action;
	Color color = Color.WHITE;
	String name = "CustomNavigator";
	public String getName() {
		return name;
	}
	public Navigator setName(String name) {
		this.name = name;
		return this;
	}
	public Color getColor() {
		return color;
	}
	public Navigator setColor(Color color) {
		this.color = color;
		return this;
	}
	public NavigatorAction getAction() {
		return action;
	}
	public Navigator(NavigatorAction action) {
		this.action = action;
	}

}
