package de.phyrone.lobbyrel.gui.api;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class InputGUI {
    private final static HashMap<InputType, InputInterface> guiList = getGUIList();
    private InputHandler handler = new InputHandler() {
        @Override
        public void onAccept(Player player, String input) {

        }

        @Override
        public void onDeny(Player player) {

        }
    };
    /* -------------- */
    private InputType type;

    public InputGUI(InputType inputType) {
        this.type = inputType;
    }

    private static HashMap<InputType, InputInterface> getGUIList() {
        HashMap<InputType, InputInterface> ret = new HashMap<>();
        ret.put(InputType.DEFAULT, new ChatInput());
        return ret;
    }

    public InputGUI setHandler(InputHandler handler) {
        this.handler = handler;
        return this;
    }

    public InputGUI open(Player player) {
        guiList.get(type).build(handler, player);
        return this;
    }


    /* --------------- */
    public enum InputType {
        DEFAULT, SIGN
    }

    public interface InputInterface {
        void build(InputHandler inputHandler, Player player);
    }

    public static abstract class InputHandler {
        public String onBuild() {
            return "Input";
        }

        public boolean multiLine() {
            return false;
        }

        public abstract void onAccept(Player player, String input);

        public void onReset(Player player) {
        }

        public abstract void onDeny(Player player);
    }
}
