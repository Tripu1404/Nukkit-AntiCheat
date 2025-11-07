package top.dreamcity.AntiCheat.Cheat.chat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import top.dreamcity.AntiCheat.Event.CheckCheatEvent;
import top.dreamcity.AntiCheat.Event.PlayerCheating;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * Adaptado para Nukkit moderno (2025)
 *
 * Clase encargada de verificar si un jugador está enviando mensajes demasiado rápido (FAST_CHAT).
 */
public class CheckChat extends Chat {

    public CheckChat(Player player, String message) {
        super(player, message);
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.FAST_CHAT;
    }

    @Override
    public boolean isCheat() {
        // Dispara el evento de verificación
        CheckCheatEvent event = new CheckCheatEvent(player, getCheatType());
        Server.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        // Si el jugador ya fue registrado recientemente, podría estar spameando
        if (CheckChatThread.hasPlayer(player.getName())) {
            PlayerCheating cheatingEvent = new PlayerCheating(player, getCheatType());
            Server.getInstance().getPluginManager().callEvent(cheatingEvent);
            return !cheatingEvent.isCancelled();
        } else {
            // Registra al jugador en la lista de mensajes recientes
            CheckChatThread.addPlayer(player.getName());
        }

        return false;
    }
}
