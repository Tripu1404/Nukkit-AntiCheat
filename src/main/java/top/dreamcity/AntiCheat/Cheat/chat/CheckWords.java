package top.dreamcity.AntiCheat.Cheat.chat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Event.CheckCheatEvent;
import top.dreamcity.AntiCheat.Event.PlayerCheating;

import java.util.List;

/**
 * Adaptado para Nukkit moderno (2025)
 * 
 * Detección de palabras sensibles (SENSITIVE_WORDS)
 */
public class CheckWords extends Chat {

    public CheckWords(Player player, String message) {
        super(player, message);
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.SENSITIVE_WORDS;
    }

    @Override
    public boolean isCheat() {
        CheckCheatEvent event = new CheckCheatEvent(player, getCheatType());
        Server.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        List<String> sensitiveList = AntiCheatAPI.getInstance().getMasterConfig().getSensitiveWords();
        for (String sw : sensitiveList) {
            if (message.toLowerCase().contains(sw.toLowerCase())) { // Ignora mayúsculas/minúsculas
                PlayerCheating event2 = new PlayerCheating(player, getCheatType());
                Server.getInstance().getPluginManager().callEvent(event2);
                return !event.isCancelled();
            }
        }
        return false;
    }

    public String changeMessage() {
        if (isCheat()) {
            List<String> sensitiveList = AntiCheatAPI.getInstance().getMasterConfig().getSensitiveWords();
            for (String sw : sensitiveList) {
                message = message.replaceAll("(?i)" + sw, "**"); // reemplazo sin distinción de mayúsculas
            }
        }
        return message;
    }
}
