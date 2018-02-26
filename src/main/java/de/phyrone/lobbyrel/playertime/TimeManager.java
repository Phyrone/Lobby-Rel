package de.phyrone.lobbyrel.playertime;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import de.phyrone.lobbyrel.LobbyPlugin;

public class TimeManager {
	static int taskid = -1;
	private static BukkitRunnable updatetask = new BukkitRunnable() {
		@Override
		public void run() {
			for(World world:Bukkit.getWorlds()) {
				world.setTime(6000);
				world.setGameRuleValue("doDaylightCycle", String.valueOf(false));
			}

		}
	};
	@SuppressWarnings("deprecation")
	public static void init() {
		updatetask.run();
		BukkitScheduler sch = Bukkit.getScheduler();
		if(sch.isCurrentlyRunning(taskid)) {
			sch.cancelTask(taskid);
		}sch.scheduleAsyncRepeatingTask(LobbyPlugin.getInstance(), updatetask, 20, 60*20);
	}

}
