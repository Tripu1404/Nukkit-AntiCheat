package top.dreamcity.AntiCheat.Cheat.combat;

import cn.nukkit.Player;
import top.dreamcity.AntiCheat.Cheat.AntiCheat;

/**
 * Copyright © 2017 WetABQ & DreamCityAdminGroup
 * Adaptado para Nukkit moderno (2025)
 *
 * Clase base para detección de trampas de combate (KillAura, AutoAim, etc.)
 */
public abstract class Combat extends AntiCheat {

    public Combat(Player player) {
        super(player);
    }

}
