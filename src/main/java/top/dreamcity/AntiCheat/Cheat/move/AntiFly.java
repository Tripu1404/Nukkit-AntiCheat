package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import top.dreamcity.AntiCheat.Cheat.AntiCheat;

/**
 * Adapted for modern Nukkit versions (2025)
 * AntiFly detection placeholder.
 */
public class AntiFly extends Move {

    public AntiFly(Player player) {
        super(player);
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.FLY;
    }

    /**
     * Ejecuta la detección de vuelo ilegal.
     *
     * @return true si el jugador está haciendo fly sin permiso.
     */
    @Override
    public boolean isCheat() {
        // TODO: Agregar detección de vuelo aquí.
        return false;
    }
}
