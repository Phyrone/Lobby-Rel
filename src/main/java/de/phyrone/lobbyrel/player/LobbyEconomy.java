package de.phyrone.lobbyrel.player;

import java.util.UUID;

public class LobbyEconomy {
    static MoneyHandler handler = new MoneyHandler() {
        @Override
        public int getMoney(UUID uuid) {
            return PlayerManager.getInternalOfflinePlayerData(uuid).Money;
        }

        @Override
        public void setMoney(UUID uuid, int money) {
            PlayerManager.getInternalOfflinePlayerData(uuid).Money = money;
            PlayerManager.getPlayerData(uuid).quickSave();
        }
    };

    public static MoneyHandler getHandler() {
        return handler;
    }

    public static void setHandler(MoneyHandler handler) {
        if (handler != null)
            LobbyEconomy.handler = handler;
    }

    public interface MoneyHandler {
        int getMoney(UUID uuid);

        void setMoney(UUID uuid, int money);
    }
}
