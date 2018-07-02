package de.phyrone.lobbyrel.placeholder;
/*
 *   Copyright © 2018 by Phyrone  *
 *   Creation: 30.06.2018 by Phyrone
 */

import de.phyrone.lobbyrel.player.PlayerManager;
import lombok.AccessLevel;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderManager {
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("[%]([^%]+)[%]");
    private static boolean placeholderapi_enabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    @Getter(AccessLevel.PRIVATE)
    private static HashMap<String, PlaceholderHandler> placeholders = new HashMap<>();

    public PlaceholderManager() {
        placeholders.clear();
        placeholderapi_enabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        addPlaceholder("player", (player, args) -> player.getName());
        addPlaceholder("displayname", (player, args) -> player.getDisplayName());
        addPlaceholder("uuid", (player, args) -> player.getUniqueId().toString());
        addPlaceholder("money", (player, args) -> String.valueOf(PlayerManager.getInternalOfflinePlayerData(player.getUniqueId()).getMoney()));
    }

    public static void addPlaceholder(String placeholder, PlaceholderHandler placeholderHandler) {
        placeholders.put(placeholder.toLowerCase(), placeholderHandler);
    }

    public static String setPlaceholders(Player player, String text) {
        if (text == null) {
            return null;
        } else if (placeholders.isEmpty()) {
            return ChatColor.translateAlternateColorCodes('&', text);
        } else {
            Matcher m = PLACEHOLDER_PATTERN.matcher(text);
            Map hooks = getPlaceholders();

            while (m.find()) {
                String format = m.group(1);
                int index = format.indexOf("_");
                if (index > 0) {
                    String params;
                    String identifier;
                    if (index < format.length()) {
                        identifier = format.substring(0, index).toLowerCase();
                        params = format.substring(index + 1).replace(' ', '_');
                    } else {
                        identifier = format.toLowerCase();
                        params = "";
                    }
                    if (hooks.containsKey(identifier)) {
                        String value = getPlaceholders().get(identifier).onCall(player, params.split("_"));
                        if (value != null) {
                            text = text.replaceAll(Pattern.quote(m.group()), Matcher.quoteReplacement(value));
                        }
                    }
                }
            }
            if (placeholderapi_enabled) {
                text = PlaceholderAPI.setPlaceholders(player, text);
                text = PlaceholderAPI.setBracketPlaceholders(player, text);
            }

            return ChatColor.translateAlternateColorCodes('&', text);
        }
    }
}
