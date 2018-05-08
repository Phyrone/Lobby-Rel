package de.phyrone.lobbyrel.hotbar.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerHotbar {
    void changePage(PageDirection direction);

    void changePage(int page);

    void setItem(int slot, HotbarItem item);

    void setItem(int slot, ItemStack item);

    void removeItem(int slot);

    void dispatch(Dispatch dispatch);

    void update();

    void setMutiPageDisplay(MutiPageDisplay display);

    HotbarItem getItem(int slot);

    int getCurrendPage();

    /**
     * open the Default Hotbar
     */
    void close();

    enum PageDirection {
        FORWARD, BACKWARD, FIRST, LAST
    }

    enum MutiPageDisplay {
        ACTION_SHORT, ACTION, XP, NONE
    }

    enum DispachType {
        CLICK, HIT, DROP, SELECT, OTHER
    }

    class Dispatch {

        int slot;
        DispachType type = DispachType.CLICK;
        Entity toEntity = null;

        public Dispatch(int totalSlot) {
            this.slot = totalSlot;
        }

        public Dispatch(int page, int slot) {
            this.slot = (page * I_Hotbar.IPP) + slot;
        }

        public Dispatch(int totalSlot, DispachType type) {
            this.slot = totalSlot;
            this.type = type;
        }

        public Dispatch(int totalSlot, DispachType type, Entity toEntity) {
            this.slot = totalSlot;
            this.type = type;
            this.toEntity = toEntity;
        }

        public Dispatch(int totalSlot, DispachType type, Player toPlayer) {
            this.slot = totalSlot;
            this.type = type;
            this.toEntity = toPlayer;
        }

        public Entity getToEntity() {
            return toEntity;
        }

        public void setToEntity(Player toPlayer) {
            this.toEntity = toPlayer;
        }

        public void setToEntity(Entity toEntity) {
            this.toEntity = toEntity;
        }

        public int getSlot() {
            return slot;
        }

        public DispachType getType() {
            return type;
        }

        public boolean hasInteractEntity() {
            return toEntity != null;
        }

    }
}
