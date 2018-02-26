package de.phyrone.lobbyrel.hider;

import org.bukkit.entity.Player;

public class HiderModule {
	public HiderModule() {
	}
	HiderModuleAction action = new HiderModuleAction() {
		@Override
		public boolean onCheck(Player viewer, Player viewed) {
			return false;
		}
	};
	public HiderModuleAction getAction() {
		return action;
	}
	public HiderModule setAction(HiderModuleAction action) {
		this.action = action;
		return this;
	}public HiderModule setAction(String permision) {
		setAction(new HiderModuleAction() {
			@Override
			public boolean onCheck(Player viewer, Player viewed) {
				return viewed.hasPermission(permision);
			}
		});
		return this;
	}
	
	

}
