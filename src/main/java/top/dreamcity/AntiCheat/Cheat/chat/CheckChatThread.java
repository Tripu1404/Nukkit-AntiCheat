package top.dreamcity.AntiCheat.Cheat.chat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import top.dreamcity.AntiCheat.AntiCheatAPI;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptado para Nukkit moderno (2025)
 * 
 * Hilo encargado de llevar el control de mensajes enviados por los jugadores
 * para detectar chat rápido (FAST_CHAT).
 */
public class CheckChatThread extends Task {

    private static final Map<String, Integer> playerChat = new ConcurrentHashMap<>();

    public CheckChatThread() {
        // Ejecuta cada segundo
        Server.getInstance().getScheduler().scheduleRepeatingTask(
                AntiCheatAPI.getInstance(),
                this,
                20 // 20 ticks = 1 segundo
        );
    }

    @Override
    public void onRun(int currentTick) {
        try {
            Iterator<Map.Entry<String, Integer>> iterator = playerChat.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                int timeLeft = entry.getValue() - 1;
                if (timeLeft > 0) {
                    entry.setValue(timeLeft);
                } else {
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPlayer(String name) {
        int chatSec = AntiCheatAPI.getInstance().getMasterConfig().getChatSec();
        playerChat.put(name, chatSec);
    }

    public static boolean hasPlayer(String name) {
        return playerChat.containsKey(name);
    }
}
