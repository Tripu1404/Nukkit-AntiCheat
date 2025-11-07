package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import top.dreamcity.AntiCheat.Cheat.AntiCheat;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * Created by WetABQ (Administrator) on 2017/10/8.
 */
public abstract class Move extends AntiCheat {

    protected float playerMoveSpeed;

    public Move(Player player) {
        super(player);

        // Recupera la velocidad del jugador desde AntiSpeedThread (si está activo)
        float moveSpeed = 0.1F;
        try {
            moveSpeed = AntiSpeedThread.getMove(player.getName());
        } catch (Exception ignored) {
            // Evita errores si AntiSpeedThread no está inicializado
        }
        this.playerMoveSpeed = moveSpeed;
    }

    public float getPlayerMoveSpeed() {
        return playerMoveSpeed;
    }
}
