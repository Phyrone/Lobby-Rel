package de.phyrone.lobbyrel.hotbar.customitems;

import de.phyrone.lobbyrel.player.data.lang.LangManager;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum CommandType {
    PLAYER("Player"), OP("Op"), CONSOLE("Console"), PLAYER2("Player2"), OP2("OP2"), Message("Message"), LANGDATA("LangData"), NONE("none");
    String value;

    CommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static CommandType parseFromString(String arg) {
        for (CommandType type : CommandType.values()) {
            if (arg.equalsIgnoreCase(type.toString())) return type;
        }
        return CommandType.PLAYER;

    }

    @Override
    public String toString() {
        return this.value;
    }

    public void run(Player p, String cmd) {
        Validate.notNull(p);
        cmd = cmd.replace("%name%", p.getName()).replace("%displayname%", p.getDisplayName())
                .replace("%uuid%", p.getUniqueId().toString());
        switch (this) {
            case PLAYER:
                p.performCommand(cmd);
                break;
            case PLAYER2:
                Bukkit.dispatchCommand(p, cmd);
                break;
            case OP:
                if (p.isOp())
                    p.performCommand(cmd);
                else {
                    p.setOp(true);
                    p.performCommand(cmd);
                    p.setOp(false);
                }
                break;
            case OP2:
                if (p.isOp())
                    Bukkit.dispatchCommand(p, cmd);
                else {
                    p.setOp(true);
                    Bukkit.dispatchCommand(p, cmd);
                    p.setOp(false);
                }
                break;
            case Message:
                p.sendMessage(cmd);
                break;
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                break;
            case LANGDATA:
                LangManager.sendMessage(p, "Message.Custom." + cmd, "&cCustom Message here");
                break;
            case NONE:
                break;
            default:
                break;
        }
    }

}
