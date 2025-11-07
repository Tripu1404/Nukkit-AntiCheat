package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import top.dreamcity.AntiCheat.AntiCheatAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup All rights reserved.
 * Created by WetABQ(Administrator) on 2017/12/6.
 */
public class AntiWaterWalkThread extends AsyncTask {

    public AntiWaterWalkThread() {
        // Inicia la tarea asincrónica en el servidor
        Server.getInstance().getScheduler().scheduleAsyncTask(AntiCheatAPI.getInstance(), this);
    }

    @Override
    public void onRun() {
        while (true) {
            try {
                // Copia segura del mapa de jugadores
                Map<UUID, Player> players = new HashMap<>(Server.getInstance().getOnlinePlayers());

                for (Player player : players.values()) {
                    if (player == null || !player.isOnline()) continue;
                    if (player.isOp()) continue; // Ignorar operadores
                    if (player.getGamemode() != Player.SURVIVAL) continue;

                    // Iniciar chequeo de WaterWalk para el jugador actual
                    new AntiWaterWalkPlayerThread(player, player.isOnGround());
                }

                // Espera 7.5 segundos entre cada revisión
                Thread.sleep(7500);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
