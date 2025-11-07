package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Config.MasterConfig;
import top.dreamcity.AntiCheat.Event.PlayerCheating;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * Adapted for modern Nukkit versions (2025)
 */
public class AntiSpeed extends Move {

    public AntiSpeed(Player player) {
        super(player);
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.SPEED;
    }

    @Override
    public boolean isCheat() {
        if (player == null || !player.isOnline() || player.getGamemode() != 0) {
            return false;
        }

        boolean flag = false;
        MasterConfig config = AntiCheatAPI.getInstance().getMasterConfig();

        if (playerMoveSpeed >= config.getMaxMoveSpeed()) {
            boolean hasEffect = player.hasEffect(1); // SPEED effect ID
            int ping = player.getPing();

            if (config.getAntiSpeedPingCheck()) {
                if (ping < config.getPingNoCheckValue()) {
                    flag = true;
                }
            } else {
                flag = true;
            }

            // Si el jugador tiene Speed, se mantiene la misma lógica duplicada del original.
            if (hasEffect) {
                if (config.getAntiSpeedPingCheck()) {
                    if (ping < config.getPingNoCheckValue()) {
                        flag = true;
                    }
                } else {
                    flag = true;
                }
            }
        }

        if (flag) {
            PlayerCheating event2 = new PlayerCheating(player, getCheatType());
            Server.getInstance().getPluginManager().callEvent(event2);
            return !event2.isCancelled();
        }

        return false;
    }
}
