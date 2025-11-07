package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.AdventureSettings;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Event.Listener.EventListener;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * Adapted for modern Nukkit versions (2025)
 */
public class AntiFlyPlayerThread extends AsyncTask {

    private final Player player;
    private int fly;

    public AntiFlyPlayerThread(Player player) {
        this.player = player;
        Server.getInstance().getScheduler().scheduleAsyncTask(AntiCheatAPI.getInstance(), this);
    }

    @Override
    public void onRun() {
        while (player != null && player.isOnline()) {
            if (!player.isOp() && player.getGamemode() == 0) {
                try {
                    if (player.getInAirTicks() >= 100) {
                        player.sendMessage(TextFormat.colorize('&', "&cSuspect that you are using cheating on a flight to stop your behavior &eCheck: Core"));
                    }
                    if (player.getInAirTicks() >= 20 * 12) {
                        Server.getInstance().broadcastMessage(TextFormat.colorize('&', "&dPlayer &b" + player.getName() + " &esuspected cheating was kicked out of AntiCheat!"));
                        player.kick(TextFormat.colorize('&', "&cYou suspect that you are using cheating on a flight to stop your behavior\n&eIf the miscarriage of justice please tell the following information to the administrator\nCheck: Core FlyCount: " + fly + " Speed:" + AntiSpeedThread.getMove(player.getName())) + " onGround:" + player.isOnGround() + " inAirTick:" + player.getInAirTicks());
                    }
                    if (player.getInAirTicks() >= 20) {
                        if (AntiSpeedThread.getMove(player.getName()) > 8.5D) {
                            Server.getInstance().broadcastMessage(TextFormat.colorize('&', "&dPlayer &b" + player.getName() + " &esuspected cheating was kicked out of AntiCheat!"));
                            player.kick(TextFormat.colorize('&', "&cYou suspect that you are using cheating on a flight to stop your behavior\n&eIf the miscarriage of justice please tell the following information to the administrator\nCheck: AntiCheat[JetPacket] FlyCount: " + fly + " Speed:" + AntiSpeedThread.getMove(player.getName())) + " onGround:" + player.isOnGround() + " inAirTick:" + player.getInAirTicks());
                        }
                    }
                    double y = player.getY();
                    Thread.sleep(3000);
                    if (player.getY() >= y && player.isOnGround() && !EventListener.AntiTower.containsKey(player.getName())) {
                        if (player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ()) == 0) {
                            for (int i = 0;
                                 player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ() + 1) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ() - 1) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX() + 1, (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX() - 1, (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY(), (int) player.getZ()) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX() - 1, (int) player.getY() - 1, (int) player.getZ() - 1) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX() - 1, (int) player.getY() - 1, (int) player.getZ() + 1) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX() + 1, (int) player.getY() - 1, (int) player.getZ() - 1) == 0 &&
                                         player.getLevel().getBlockIdAt((int) player.getX() + 1, (int) player.getY() - 1, (int) player.getZ() + 1) == 0 &&
                                         player.isOnline() && i < 50 && ifPlayerInSky(player) && !player.isSneaking();
                                 i++) {
                                System.out.println("cnm " + player.getName());
                                player.setMotion(new Vector3(0, -2, 0));
                                AdventureSettings settings = player.getAdventureSettings();
                                settings.set(AdventureSettings.Type.FLYING, false);
                                settings.update();

                                y = player.getY();
                                Thread.sleep(300);

                                if (player.getY() >= y && player.isOnGround() && !EventListener.AntiTower.containsKey(player.getName())) {
                                    int groundY = 1;
                                    while (player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - groundY, (int) player.getZ()) == 0 && player.isOnline()) {
                                        groundY++;
                                    }
                                    groundY -= 1;
                                    if (groundY > 2 &&
                                            player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ() + 1) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY() - 1, (int) player.getZ() - 1) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX() + 1, (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX() - 1, (int) player.getY() - 1, (int) player.getZ()) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX(), (int) player.getY(), (int) player.getZ()) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX() - 1, (int) player.getY() - 1, (int) player.getZ() - 1) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX() - 1, (int) player.getY() - 1, (int) player.getZ() + 1) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX() + 1, (int) player.getY() - 1, (int) player.getZ() - 1) == 0 &&
                                            player.getLevel().getBlockIdAt((int) player.getX() + 1, (int) player.getY() - 1, (int) player.getZ() + 1) == 0 &&
                                            player.isOnline() &&
                                            !EventListener.AntiTower.containsKey(player.getName()) &&
                                            ifPlayerInSky(player) && !player.isSneaking()) {
                                        System.out.println("cnm+++ " + player.getName());
                                        fly++;
                                        player.sendMessage(TextFormat.colorize('&', "&cYou suspect that you are using cheating on a flight to stop your behavior &eCheck: AntiCheat[Fly+ AirWalk]"));
                                        player.teleport(new Vector3(player.getX(), player.getY() - groundY, player.getZ()));
                                    }
                                }
                            }
                        }
                    }

                    if (fly >= 3) {
                        Server.getInstance().broadcastMessage(TextFormat.colorize('&', "&dPlayer &b" + player.getName() + " &esuspected cheating was kicked out of AntiCheat!"));
                        player.kick(TextFormat.colorize('&', "&cYou suspect that you are using cheating on a flight to stop your behavior\n&eIf the miscarriage of justice please tell the following information to the administrator\nCheck: AntiCheat[FlyMore] FlyCount: " + fly + " Speed:" + AntiSpeedThread.getMove(player.getName())) + " onGround:" + player.isOnGround() + " inAirTick:" + player.getInAirTicks());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean ifPlayerInSky(Player player) {
        for (Block block : player.getBlocksAround()) {
            if (block.getId() != 0) {
                return false;
            }
        }
        return true;
    }
}
