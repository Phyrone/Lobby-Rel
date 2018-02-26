package de.phyrone.lobbyrel.cmd.help;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import de.phyrone.lobbyrel.LobbyPlugin;
import de.phyrone.lobbyrel.lib.json.FancyMessage;

public class HelpManager {
	final CommandSender sender;
	private boolean lp = false;
	public static void init() {
		cmds.clear();
		//for(int i = 0;i < 100 ;i++) //Only for Tests
		addCMD(new HelpTemplate("<lobbycmd> admingui","open the AdminGUI","lobby.admin"));
		addCMD(new HelpTemplate("<lobbycmd> reload","reloads the LobbyPlugin","lobby.admin"));
		addCMD(new HelpTemplate("<lobbycmd> save","save all Configs","lobby.admin"));
		addCMD(new HelpTemplate("<lobbycmd> update","update the Plugin","lobby.admin"));
		addCMD(new HelpTemplate("build","change the buildmode","lobby.admin"));
		addCMD(new HelpTemplate("setwarp <Warpname>","set a warp","lobby.admin"));
		addCMD(new HelpTemplate("setspawn","set the spawn","lobby.admin"));
		addCMD(new HelpTemplate("spawn" ,"Teleport to Spawn","lobby.admin"));
		addCMD(new HelpTemplate("<lobbycmd> debug ...","use debug commands","lobby.debug"));
	}
	public HelpManager(CommandSender sender) {
		this.sender = sender;
	}
	private static final int IPP = 9;
	static private ArrayList<HelpTemplate> cmds = new ArrayList<>();
	public static void addCMD(HelpTemplate help) {
		cmds.add(help);
	}public ArrayList<FancyMessage> getHelpMessaages(String command,int site){
		lp = false;
		ArrayList<FancyMessage> ret = new ArrayList<FancyMessage>();
		for(int i = site*IPP; (site+1)*IPP>i;i++) {
			try{
				if(cmds.size() <= i) {
					ret.add(new FancyMessage(" "));
					lp = true;
					continue;
				}
				HelpTemplate cmd = cmds.get(i);
				if(cmd.getPermission().equals("")||sender.hasPermission(cmd.getPermission()))ret.add(
							new FancyMessage(" §7/"+cmd.getCmd().toLowerCase().replace("<lobbycmd>", command)+" §8| §a"+cmd.getDesciption())
							.tooltip(cmd.getTooltip()).suggest("/"+cmd.getCmd().toLowerCase().replace("<lobbycmd>", command)).then(" ")
						);
				else ret.add(new FancyMessage(" "));
			}catch (Exception e) {
				System.err.println("Lobby : ERROR : LOAD HELPMESSAGE");
				e.printStackTrace();
			}

		}
		return ret;
	}
	
	public void showHelp(String command,int site) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				//Header
				FancyMessage head = new FancyMessage("§8§l⥭⥭⥭⥭⥭⥭⥭⥭⥭⥭⥭⥭⥭⥭§5Lobby-Rel§8§l⥫⥫⥫⥫⥫⥫⥫⥫⥫⥫⥫⥫⥫⥫\n").then();
				if(site <= 1) {
					head.text("   ");
				}else {
					head.text(" §5§l⤆").command("/"+command+" help "+String.valueOf(site-1));
				}head.then("                          ").then("§bSite "+String.valueOf(site)).then("                        ")
				;
				//Body
				ArrayList<FancyMessage> messages = getHelpMessaages(command, site-1);
				if(!lp)head.then("§5§l⤇   ").command("/"+command+" help "+String.valueOf(site+1));
				
					new BukkitRunnable() {
						
						@Override
						public void run() {
							head.send(sender);
							for(FancyMessage m : messages) {
								m.send(sender);
							}
							//Footer
							sender.sendMessage("§8§m§l---------------------------------------------");
						}
					}.runTask(LobbyPlugin.getInstance());

			}
		}.runTaskAsynchronously(LobbyPlugin.getInstance());
		
	}

}
