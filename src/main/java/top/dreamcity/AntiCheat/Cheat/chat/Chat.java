package top.dreamcity.AntiCheat.Cheat.chat;

import cn.nukkit.Player;
import top.dreamcity.AntiCheat.Cheat.AntiCheat;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * Adaptado para Nukkit moderno (2025)
 *
 * Clase base para detecciones de chat (spam, insultos, etc.)
 */
public abstract class Chat extends AntiCheat {

    protected final String message;

    /**
     * Constructor del detector de chat
     *
     * @param player  Jugador que envió el mensaje
     * @param message Mensaje enviado por el jugador
     */
    public Chat(Player player, String message) {
        super(player);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
