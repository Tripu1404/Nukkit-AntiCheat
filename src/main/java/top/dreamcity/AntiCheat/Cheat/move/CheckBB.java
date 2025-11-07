package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.math.AxisAlignedBB;
import top.dreamcity.AntiCheat.Event.CheckCheatEvent;
import top.dreamcity.AntiCheat.Event.PlayerCheating;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * Created by WetABQ (Administrator) on 2017/10/8.
 */
public class CheckBB extends Move {

    public CheckBB(Player player) {
        super(player);
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.BOUNDING_BOX;
    }

    @Override
    public boolean isCheat() {
        // Lanza el evento de verificación
        CheckCheatEvent event = new CheckCheatEvent(player, getCheatType());
        Server.getInstance().getPluginManager().callEvent(event);

        // Evita falsos positivos fuera del modo supervivencia
        if (player.getGamemode() != Player.SURVIVAL) {
            event.setCancelled();
        }

        if (event.isCancelled()) {
            return false;
        }

        // Define el área de colisión del jugador
        double radius = player.getWidth() / 2.0D;
        AxisAlignedBB bb = player.getBoundingBox().clone().setBounds(
                player.x - radius + 0.3D,
                player.y + 1.1D,
                player.z - radius + 0.3D,
                player.x + radius - 0.3D,
                player.y + (player.getHeight() * player.scale) - 0.1D,
                player.z + radius - 0.3D
        );

        // Revisa colisión con bloques sólidos
        for (Block block : player.getBlocksAround()) {
            if (block == null) continue;

            if (block.collidesWithBB(bb) && !block.canPassThrough() && block.getId() != Block.LADDER) {
                PlayerCheating event2 = new PlayerCheating(player, getCheatType());
                Server.getInstance().getPluginManager().callEvent(event2);

                // Devuelve true si el evento no fue cancelado (trampa detectada)
                return !event2.isCancelled();
            }
        }

        return false;
    }
}
