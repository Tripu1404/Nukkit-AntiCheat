package top.dreamcity.AntiCheat.Cheat.combat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import top.dreamcity.AntiCheat.Event.CheckCheatEvent;
import top.dreamcity.AntiCheat.Event.PlayerCheating;

/**
 * AntiReach — Detecta jugadores que interactúan con entidades o bloques fuera del rango permitido.
 * Adaptado a Nukkit moderno (2.x, 2025)
 */
public class Reach extends Combat {

    private final Block block;
    private final Entity entity;

    public Reach(Player player, Block block) {
        super(player);
        this.block = block;
        this.entity = null;
    }

    public Reach(Player player, Entity entity) {
        super(player);
        this.entity = entity;
        this.block = null;
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.REACH;
    }

    @Override
    public boolean isCheat() {
        CheckCheatEvent event = new CheckCheatEvent(player, getCheatType());
        Server.getInstance().getPluginManager().callEvent(event);

        // Evita falsos positivos en modos que no sean survival
        if (player.getGamemode() != Player.SURVIVAL) {
            event.setCancelled(true);
        }

        if (event.isCancelled()) return false;

        boolean flag = false;

        // Chequeo de alcance a entidades
        if (entity != null) {
            double distance = entity.distance(player);
            if (distance >= 4.0) {
                flag = true;
            }
        }
        // Chequeo de alcance a bloques
        else if (block != null) {
            double distance = block.distance(player);
            if (distance >= 6.0) {
                flag = true;
            }
        }

        // Si se detecta una posible trampa
        if (flag) {
            PlayerCheating cheatEvent = new PlayerCheating(player, getCheatType());
            Server.getInstance().getPluginManager().callEvent(cheatEvent);
            return !cheatEvent.isCancelled();
        }

        return false;
    }
}
