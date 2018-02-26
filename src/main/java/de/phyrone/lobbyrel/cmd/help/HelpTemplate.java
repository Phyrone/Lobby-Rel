package de.phyrone.lobbyrel.cmd.help;

public class HelpTemplate {
	String cmd;
	String desc = "<no Description set>";
	String[] tooltip = new String[] {""};
	String perm = "";
	public HelpTemplate(String cmd) {
		this.cmd = cmd;
	}public HelpTemplate(String cmd,String descrition){
		this.desc = descrition;
		this.cmd = cmd;
		
	}public HelpTemplate(String cmd,String descrition,String perm){
		this.desc = descrition;
		this.cmd = cmd;
		this.perm = perm;
	}
	public String getCmd() {
		return cmd;
	}
	public HelpTemplate setCmd(String cmd) {
		this.cmd = cmd;
		return this;
	}
	public String getDesciption() {
		return desc;
	}
	public HelpTemplate setDesciption(String desc) {
		this.desc = desc;
		return this;
	}
	public String[] getTooltip() {
		return tooltip;
	}
	public HelpTemplate setTooltip(String[] tooltip) {
		this.tooltip = tooltip;
		return this;
	}
	public String getPermission() {
		return perm;
	}
	public HelpTemplate setPermission(String perm) {
		this.perm = perm;
		return this;
	}

}
