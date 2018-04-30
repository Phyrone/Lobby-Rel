package de.phyrone.lobbyrel.hider;

public class HiderModule {
	HiderModuleAction action = (viewer, viewed) -> false;
	public HiderModuleAction getAction() {
		return action;
	}
	public HiderModule setAction(HiderModuleAction action) {
		this.action = action;
		return this;
	}public HiderModule setAction(String permision) {
		setAction((viewer, viewed) -> viewed.hasPermission(permision));
		return this;
	}
	
	

}
