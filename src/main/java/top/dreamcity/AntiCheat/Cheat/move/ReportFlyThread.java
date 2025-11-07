package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Cheat.Report;
import top.dreamcity.AntiCheat.Event.Listener.EventListener;

import java.math.BigDecimal;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * Adaptado para NukkitX 2.x / PowerNukkitX 1.20+ por ChatGPT (2025)
 */
public class ReportFlyThread extends Report implements Runnable {

    public ReportFlyThread(Player player) {
        super(player);
        // Ejecuta el chequeo en un hilo asíncrono del servidor
        Server.getInstance().getScheduler().scheduleTask(
                AntiCheatAPI.getInstance(),
                this,
                true // async
        );
    }

    @Override
    public void run() {
        boolean flag = false;

        try {
            for (int f = 0; f < 60; f++) {
                if (!player.isOnline() || player.isOp() || player.getGamemode() != Player.SURVIVAL) {
                    continue;
                }

                // --- Verifica elevación sospechosa ---
                if (player.y != (int) player.y) {
                    double startY = player.y;
                    Thread.sleep(1000);

                    if (player.y != (int) player.y) {
                        double diffY = BigDecimal.valueOf(player.y).subtract(BigDecimal.valueOf(startY)).doubleValue();
                        if (diffY <= 1 && diffY >= 0 && !EventListener.AntiTower.containsKey(player.getName())) {

                            if (AntiFlyPlayerThread.ifPlayerinSky(player)
                                    && areBlocksAir(player, 0, -1, 0)) {

                                int groundY = getAirDepth(player);
                                if (groundY > 2) {
                                    player.teleport(new Vector3(player.x, player.y - groundY, player.z));
                                }
                            }
                        }
                    }
                }

                // --- Si está en tierra, comprobar comportamiento anómalo ---
                if (player.isOnGround()) {
                    boolean suspected = false;

                    if (player.getLevel().getBlockIdAt((int) player.x, (int) player.y - 1, (int) player.z) == 0) {
                        suspected = true;
                    }

                    if (suspected) {
                        double y = player.y;
                        Thread.sleep(3000);

                        if (y <= player.y && player.isOnGround() && !EventListener.AntiTower.containsKey(player.getName())) {
                            if (AntiFlyPlayerThread.ifPlayerinSky(player) && areBlocksAir(player, 0, -1, 0)) {
                                int groundY = getAirDepth(player);
                                if (groundY > 2) {
                                    player.teleport(new Vector3(player.x, player.y - groundY, player.z));
                                }
                            }
                        } else if (player.move(0, -3, 0)) {
                            if (player.move(0, -3, 0) && player.y + 6 <= y) {
                                flag = true;
                            }
                        }
                    }
                } else {
                    // --- Jugador no en tierra, comprobar levitación ---
                    double y = player.y;
                    Thread.sleep(3000);

                    if (Math.abs(player.y - y) < 1e-12 && !player.isOnGround() && !EventListener.AntiTower.containsKey(player.getName())) {
                        flag = true;
                    }

                    if (player.move(0, -3, 0) && player.move(0, -3, 0) && player.y + 6 <= y) {
                        flag = true;
                    }
                }

                if (flag) {
                    player.kick(TextFormat.AQUA + "Cheat Type: " + TextFormat.RED + "Fly");
                    Server.getInstance().broadcastMessage(TextFormat.RED + player.getName() + " kicked by AntiCheatSystem for Fly");
                    break;
                }

                Thread.sleep(1000);
            }

            if (!flag) {
                Server.getInstance().getLogger().info("AntiCheat System checked " + player.getName() + " *NO CHEAT*");
            }

        } catch (Exception e) {
            Server.getInstance().getLogger().warning("Error in ReportFlyThread for " + player.getName() + ": " + e.getMessage());
        } finally {
            AntiCheatAPI api = AntiCheatAPI.getInstance();
            api.reportPlayer.remove(player.getName());
            api.reportThread.remove(player.getName());
        }
    }

    /**
     * Verifica si los bloques alrededor del jugador están vacíos.
     */
    private boolean areBlocksAir(Player player, int dx, int dy, int dz) {
        int x = (int) player.x + dx;
        int y = (int) player.y + dy;
        int z = (int) player.z + dz;
        return player.getLevel().getBlockIdAt(x, y, z) == 0;
    }

    /**
     * Calcula la profundidad del aire hasta encontrar suelo sólido.
     */
    private int getAirDepth(Player player) {
        int groundY = 1;
        while (player.getLevel().getBlockIdAt((int) player.x, (int) player.y - groundY, (int) player.z) == 0) {
            groundY++;
        }
        return groundY - 1;
    }
}
