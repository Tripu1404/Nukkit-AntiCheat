package top.dreamcity.AntiCheat.Cheat;

import cn.nukkit.Player;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * 
 * Adapted for modern Nukkit versions by ChatGPT (2025)
 */
public abstract class AntiCheat {

    protected final Player player;

    public AntiCheat(Player player) {
        this.player = player;
    }

    /**
     * Devuelve el tipo de cheat que esta clase detecta.
     */
    public abstract CheatType getCheatType();

    /**
     * Ejecuta la detección y devuelve true si el jugador hace trampa.
     */
    public abstract boolean isCheat();

    public enum CheatType {
        SPEED("speed"),
        BOUNDING_BOX("bb"),
        FLY("fly"),
        FAST_CHAT("fastchat"),
        SENSITIVE_WORDS("sensitivewords"),
        KILL_AURA("killaura"),
        AUTO_AIM("autoaim"),
        REACH("reach");

        private final String typeName;

        CheatType(String typeName) {
            this.typeName = typeName;
        }

        public static CheatType fromTypeName(String typeName) {
            for (CheatType type : values()) {
                if (type.typeName.equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            return null;
        }

        public String getTypeName() {
            return this.typeName;
        }

        @Override
        public String toString() {
            return this.typeName;
        }
    }
}
