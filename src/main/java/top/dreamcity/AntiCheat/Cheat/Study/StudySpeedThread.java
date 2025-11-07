package top.dreamcity.AntiCheat.Cheat.Study;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import top.dreamcity.AntiCheat.AntiCheat;
import top.dreamcity.AntiCheat.Cheat.move.AntiSpeedThread;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup
 * Adaptado para Nukkit moderno (2025)
 *
 * Esta clase ejecuta un estudio de velocidad (Machine Learning demo).
 */
public class StudySpeedThread implements Runnable {

    private final Player player;
    private final boolean isCheating;

    /**
     * Inicia el hilo de estudio de velocidad
     *
     * @param player Jugador a analizar
     * @param isCheating Indica si el estudio es de tipo "trampa simulada"
     */
    public StudySpeedThread(Player player, boolean isCheating) {
        this.player = player;
        this.isCheating = isCheating;

        // Ejecuta el hilo de forma segura sin bloquear Nukkit
        Server.getInstance().getScheduler().scheduleTask(AntiCheat.getInstance(), this, true);
    }

    @Override
    public void run() {
        try {
            ArrayList<Double> speedList = new ArrayList<>();

            for (int i = 0; i < 10 && player.isOnline(); i++) {
                speedList.add((double) AntiSpeedThread.getMove(player.getName()));

                // Mostrar información visual al jugador (demo)
                player.sendTitle(
                        "", // Título vacío
                        TextFormat.colorize("&bDemo&6: &a" + isCheating + " &eCount&6: &a" + i)
                );

                Thread.sleep(1000);
            }

            if (player.isOnline()) {
                double maxSpeed = Collections.max(speedList);
                double totalSpeed = 0.0;

                for (double s : speedList) {
                    totalSpeed += s;
                }

                double avgSpeed = totalSpeed / speedList.size();

                Server.getInstance().getLogger().warning(
                        "StudyData: MaxSpeed=" + maxSpeed + " AvgSpeed=" + avgSpeed +
                                " isCheating=" + isCheating + " Player=" + player.getName()
                );

                // Enviar datos al sistema ML externo
                Study.SpeedStudy(maxSpeed, avgSpeed, isCheating);

                player.sendMessage(
                        TextFormat.colorize("&b[AntiCheat Study] &eMaxSpeed: &a" + maxSpeed +
                                " &eAvgSpeed: &a" + avgSpeed + " &eCheat: &c" + isCheating)
                );
            }

            AntiCheat.DemoPlayer.remove(player.getName());

        } catch (Exception e) {
            Server.getInstance().getLogger().error("Error en StudySpeedThread para " + player.getName(), e);
        }
    }
}
