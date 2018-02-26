package de.phyrone.lobbyrel.hider;

import org.bukkit.entity.Player;

public interface HiderModuleAction {
	boolean onCheck(Player viewer,Player viewed);

}
