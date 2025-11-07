package top.dreamcity.AntiCheat.Cheat.combat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import top.dreamcity.AntiCheat.Event.CheckCheatEvent;

/**
 * Adaptado para Nukkit moderno (2025)
 * Detección de KillAura o ataques automáticos.
 */
public class AntiKillAura extends Combat {

    public AntiKillAura(Player player) {
        super(player);
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.KILL_AURA;
    }

    @Override
    public boolean isCheat() {
        // Lanza evento de chequeo antes de continuar
        CheckCheatEvent event = new CheckCheatEvent(player, getCheatType());
        Server.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        // TODO: Aquí podrías implementar tu detección de KillAura
        // (por ejemplo, detección de múltiples ataques por tick o rotaciones instantáneas)

        // Por ahora, solo devuelve false (sin detección activa)
        return false;
    }
}
