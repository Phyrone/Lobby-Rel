package de.phyrone.lobbyrel.player.settings;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.phyrone.lobbyrel.lib.ItemBuilder;
import de.phyrone.lobbyrel.navigator.Navigator;
import de.phyrone.lobbyrel.navigator.NavigatorManager;
import de.phyrone.lobbyrel.player.PlayerManager;
import de.phyrone.lobbyrel.player.data.OfflinePlayerData;
import de.phyrone.lobbyrel.player.scoreboard.ScoreboardManager;
import fr.minuskube.inv.ClickableItem;

public class SettingsManager {
	static ArrayList<SettingsModule> modules = new ArrayList<>();
	public static void addModule(SettingsModule module) {
		modules.add(module);
	}public static ArrayList<SettingsModule> getModulesAsArryList() {
		return modules;
	}@SuppressWarnings("deprecation")
	public static void setup() {
		modules.clear();

		addModule( new SettingsModule(new ItemBuilder(Material.COMPASS).displayname("&6Navigator").build()).setAction(new SettingsModuleAction() {
			
			@Override
			public ArrayList<ClickableItem> getOptions(Player player) {
				ArrayList<ClickableItem> ret = new ArrayList<ClickableItem>();
				int i = 0;
				for(Navigator nav : NavigatorManager.getNavigators()) {
					final int i2 = i;
					ret.add(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1,nav.getColor().getDye().getData())).displayname(nav.getColor().getChatColor()+nav.getName()).build(), e -> {
						PlayerManager.getPlayerData((Player)e.getWhoClicked()).setNavigator(i2);
						
					}));
					i++;
				}
				return ret;
			}
			
			@Override
			public ItemStack getIcon(Player player) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ItemStack getCurrend(Player player) {
				Navigator nav = NavigatorManager.getNavigator(PlayerManager.getPlayerData(player).getNavigator());
				return new ItemBuilder(new ItemStack(Material.WOOL,1,nav.getColor().getDye().getData())).displayname("§8Now: "+nav.getColor().getChatColor()+nav.getName()).build();
			}
		}));

		addModule( new SettingsModule(new ItemBuilder(Material.NOTE_BLOCK).displayname("&6Sound").build()).setOptions(
				new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData())).displayname("&aSound ON").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setSound(true);
				}),ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cSound OFF").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setSound(false);
				})))).setAction(new SettingsModuleAction() {
					
					@Override
					public ArrayList<ClickableItem> getOptions(Player player) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public ItemStack getIcon(Player player) {
						return null;
					}
					
					@Override
					public ItemStack getCurrend(Player player) {
						boolean sound = new OfflinePlayerData(player).getSound();
						if(sound) {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.LIME.getData())).displayname("&aON").build();
						}else {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cOff").build();
						}
					}
				}));
		addModule( new SettingsModule(new ItemBuilder(Material.IRON_PLATE).displayname("&6JumpPad").build()).setOptions(
				new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData())).displayname("&aJumpPad ON").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setJumpPad(true);
				}),ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cJumpPad OFF").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setJumpPad(false);
				})))).setAction(new SettingsModuleAction() {
					
					@Override
					public ArrayList<ClickableItem> getOptions(Player player) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public ItemStack getIcon(Player player) {
						return null;
					}
					
					@Override
					public ItemStack getCurrend(Player player) {
						boolean sb = new OfflinePlayerData(player).getJumpPads();
						if(sb) {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.LIME.getData())).displayname("&aON").build();
						}else {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cOff").build();
						}
					}
				}));
		addModule( new SettingsModule(new ItemBuilder(Material.IRON_BOOTS).glow().displayname("&6DoubleJump").build()).setOptions(
				new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData())).displayname("&aDoubleJump ON").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setDoubleJump(true);
					((Player)e.getWhoClicked()).setAllowFlight(true);
				}),ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cDoubleJump OFF").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setDoubleJump(false);
					((Player)e.getWhoClicked()).setAllowFlight(false);
				})))).setAction(new SettingsModuleAction() {
					
					@Override
					public ArrayList<ClickableItem> getOptions(Player player) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public ItemStack getIcon(Player player) {
						return null;
					}
					
					@Override
					public ItemStack getCurrend(Player player) {
						boolean sb = new OfflinePlayerData(player).getDoubleJump();
						if(sb) {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.LIME.getData())).displayname("&aON").build();
						}else {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cOff").build();
						}
					}
				}));
		if(ScoreboardManager.isEnabled())
		addModule( new SettingsModule(new ItemBuilder(Material.NETHER_STAR).displayname("&6Scoreboard").build()).setOptions(
				new ArrayList<>(Arrays.asList(ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData())).displayname("&aScoreboard ON").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setScoreboard(true);
					ScoreboardManager.update((Player) e.getWhoClicked());
				}),ClickableItem.of(new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cScoreboard OFF").build(), e -> {
					new OfflinePlayerData(e.getWhoClicked().getUniqueId()).setScoreboard(false);
					ScoreboardManager.update((Player) e.getWhoClicked());
				})))).setAction(new SettingsModuleAction() {
					
					@Override
					public ArrayList<ClickableItem> getOptions(Player player) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public ItemStack getIcon(Player player) {
						return null;
					}
					
					@Override
					public ItemStack getCurrend(Player player) {
						boolean sb = new OfflinePlayerData(player).getScoreboard();
						if(sb) {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.LIME.getData())).displayname("&aON").build();
						}else {
							return new ItemBuilder(new ItemStack(Material.WOOL,1,DyeColor.RED.getData())).displayname("&cOff").build();
						}
					}
				}));
	}
	
}
