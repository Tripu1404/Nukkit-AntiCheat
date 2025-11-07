package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Cheat.Report;
import top.dreamcity.AntiCheat.Cheat.Study.Study;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup
 * Adaptado para Nukkit moderno por ChatGPT (2025)
 */
public class ReportSpeedThread extends Report implements Runnable {

    public ReportSpeedThread(Player player) {
        super(player);
        // Programar tarea asíncrona en Nukkit moderno
        Server.getInstance().getScheduler().scheduleTask(AntiCheatAPI.getInstance(), this, true);
    }

    @Override
    public void run() {
        try {
            boolean flag = false;

            for (int f = 0; f < 3; f++) {
                if (player != null && player.isOnline() && !player.isOp() && player.getGamemode() == 0) {
                    float move = AntiSpeedThread.getMove(player.getName());
                    Thread.sleep(1000);
                    float move2 = AntiSpeedThread.getMove(player.getName());
                    float m = AntiCheatAPI.getInstance().getMasterConfig().getMaxMoveSpeed();

                    if (move >= m || move2 >= m) {
                        player.setMotion(Vector3.ZERO);
                        player.teleport(player);
                        Thread.sleep(2000);

                        move = AntiSpeedThread.getMove(player.getName());
                        Thread.sleep(1000);
                        move2 = AntiSpeedThread.getMove(player.getName());

                        if (move >= m || move2 >= m) {
                            if (move >= m && move2 >= m) {
                                flag = true;
                            } else if (Math.abs(move2 - move) >= m - Math.min(move, move2)) {
                                flag = true;
                            }
                        }
                    }

                    if (flag) {
                        player.kick(TextFormat.AQUA + "Cheat Type: " + TextFormat.RED + "Speed");
                        break;
                    }
                }
                Thread.sleep(1000);
            }

            ArrayList<Double> speedList = new ArrayList<>();
            for (int i = 0; i < 10 && player != null && player.isOnline(); i++) {
                speedList.add((double) AntiSpeedThread.getMove(player.getName()));
                Thread.sleep(1000);
            }

            if (player != null && player.isOnline()) {
                double maxSpeed = Collections.max(speedList);
                double allSpeed = 0;
                for (double speed : speedList) {
                    allSpeed += speed;
                }
                double avgSpeed = allSpeed / speedList.size();
                boolean isCheating = Study.SpeedPredict(maxSpeed, avgSpeed);

                Server.getInstance().getLogger().warning(
                        String.format("AntiCheat-ML System: MaxSpeed=%.2f AvgSpeed=%.2f Cheating=%s Player=%s",
                                maxSpeed, avgSpeed, isCheating, player.getName())
                );

                if (isCheating) {
                    Server.getInstance().broadcastMessage(
                            TextFormat.colorize("&ePlayer &a" + player.getName() +
                                    " &6was detected by AntiCheat-ML machine learning system suspected cheating")
                    );
                }
            }

            if (!flag && player != null) {
                Server.getInstance().getLogger().notice("AntiCheat System Check Player " + player.getName() + " *NO CHEAT*");
            }

            AntiCheatAPI.getInstance().reportPlayer.remove(player.getName());
            AntiCheatAPI.getInstance().reportThread.remove(player.getName());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Server.getInstance().getLogger().error("ReportSpeedThread interrupted for player " + player.getName(), e);
        } catch (Exception e) {
            Server.getInstance().getLogger().error("Error in ReportSpeedThread for " + player.getName(), e);
        }
    }
}
