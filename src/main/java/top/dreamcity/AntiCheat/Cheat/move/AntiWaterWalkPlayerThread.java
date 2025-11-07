package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import top.dreamcity.AntiCheat.AntiCheatAPI;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup All rights reserved.
 * Created by WetABQ(Administrator) on 2017/12/9.
 */
public class AntiWaterWalkPlayerThread extends AsyncTask {

    private final String playerName;
    private final boolean onGround;

    public AntiWaterWalkPlayerThread(Player player, boolean onGround) {
        this.playerName = player.getName();
        this.onGround = onGround;
        // Registrar esta tarea en el scheduler asincrónico
        Server.getInstance().getScheduler().scheduleAsyncTask(AntiCheatAPI.getInstance(), this);
    }

    @Override
    public void onRun() {
        try {
            Player player = Server.getInstance().getPlayerExact(playerName);
            if (player == null || !player.isOnline()) {
                return;
            }

            boolean flag = false;
            Level level = player.getLevel();

            if (onGround) {
                double y = player.getY();
                Thread.sleep(3000);
                player = Server.getInstance().getPlayerExact(playerName);
                if (player == null || !player.isOnline()) return;

                double deltaY = player.getY() - y;
                if (Math.abs(deltaY) < 0.01 &&
                        level.getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                        !player.isOnGround()) {
                    flag = true;
                }
            } else {
                double y = player.getY();
                Thread.sleep(3000);
                player = Server.getInstance().getPlayerExact(playerName);
                if (player == null || !player.isOnline()) return;

                double deltaY = player.getY() - y;
                if (Math.abs(deltaY) < 0.01 && player.isOnGround()) {
                    int id = level.getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ());
                    if (id == Block.WATER || id == Block.WATER_LILY || id == Block.STILL_WATER) {
                        player.move(0, -1, 0);
                        y = player.getY();
                        Thread.sleep(1000);
                        player = Server.getInstance().getPlayerExact(playerName);
                        if (player == null || !player.isOnline()) return;

                        double deltaY2 = player.getY() - y;
                        if (Math.abs(deltaY2) < 0.01 && player.isOnGround()) {
                            int waterY = 1;
                            int checkId = level.getBlockIdAt((int) player.getX(), (int) player.getY() - waterY, (int) player.getZ());
                            while ((checkId == 0 || checkId == Block.WATER || checkId == Block.WATER_LILY || checkId == Block.STILL_WATER)
                                    && player.isOnline()) {
                                waterY++;
                                checkId = level.getBlockIdAt((int) player.getX(), (int) player.getY() - waterY, (int) player.getZ());
                            }
                            waterY -= 1;
                            Vector3 dest = new Vector3(player.getX(), player.getY() - waterY, player.getZ());
                            player.teleport(dest);
                        }
                    }
                }
            }

            if (flag) {
                Player finalPlayer = Server.getInstance().getPlayerExact(playerName);
                if (finalPlayer != null && finalPlayer.isOnline()) {
                    finalPlayer.setMotion(new Vector3(0, 0, 0));
                    finalPlayer.teleport(new Vector3(finalPlayer.getX(), finalPlayer.getY() - 1, finalPlayer.getZ()));
                    finalPlayer.move(0, -1, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
