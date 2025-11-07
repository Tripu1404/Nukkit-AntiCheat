package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.MobEffectPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Event.Listener.EventListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright © 2017 WetABQ&DreamCityAdminGroup
 * Adapted for modern Nukkit (2025)
 */
public class AntiSpeedThread extends AsyncTask {

    private static final HashMap<String, Float> finalSpeed = new HashMap<>();
    public static final HashMap<String, Position> positionHashMap = new HashMap<>();
    private static final HashMap<String, Long> timeCheck = new HashMap<>();
    private static final HashMap<String, Integer> highJump = new HashMap<>();

    public AntiSpeedThread() {
        Server.getInstance().getScheduler().scheduleAsyncTask(AntiCheatAPI.getInstance(), this);
    }

    @Override
    public void onRun() {
        while (true) {
            try {
                Map<UUID, Player> players = new HashMap<>(Server.getInstance().getOnlinePlayers());

                for (Player player : players.values()) {
                    if (player == null || !player.isOnline()) continue;

                    // Limpieza de jugadores desconectados del mapa de salto alto
                    highJump.entrySet().removeIf(entry ->
                            Server.getInstance().getPlayerExact(entry.getKey()) == null);

                    // Movimiento horizontal
                    if (positionHashMap.containsKey(player.getName())) {
                        if (!EventListener.tp.containsKey(player.getName())) {
                            Position from = positionHashMap.get(player.getName());
                            Position to = player.getPosition();

                            float move = (float) Math.sqrt(Math.pow(from.x - to.x, 2) + Math.pow(from.z - to.z, 2));
                            long delta = System.currentTimeMillis() - timeCheck.getOrDefault(player.getName(), System.currentTimeMillis());
                            move -= 0.1F * (delta - 1000);

                            // Ajuste por efecto Speed
                            if (player.hasEffect(Effect.SPEED)) {
                                Effect speed = player.getEffect(Effect.SPEED);
                                if (speed != null && speed.getAmplifier() >= 0) {
                                    int amp = speed.getAmplifier() + 1;
                                    double factor = (amp <= 2) ? amp * 0.2D : amp * 0.4D;
                                    move -= (float) factor;
                                }
                            }

                            if (move < 0) move = 0;

                            setFinalMove(player.getName(), move);
                            positionHashMap.put(player.getName(), player.getPosition());
                            timeCheck.put(player.getName(), System.currentTimeMillis());
                        } else {
                            int tp = EventListener.tp.get(player.getName());
                            if (tp > 0) {
                                EventListener.tp.put(player.getName(), tp - 1);
                                setFinalMove(player.getName(), 0);
                                positionHashMap.put(player.getName(), player.getPosition());
                                timeCheck.put(player.getName(), System.currentTimeMillis());
                            } else {
                                EventListener.tp.remove(player.getName());
                            }
                        }
                    } else {
                        setFinalMove(player.getName(), 0F);
                        timeCheck.put(player.getName(), System.currentTimeMillis());
                        positionHashMap.put(player.getName(), player.getPosition());
                    }

                    // Actualizar efecto visual (antiguo MobEffectPacket)
                    if (!player.hasEffect(Effect.HASTE)) {
                        MobEffectPacket pk = new MobEffectPacket();
                        pk.eid = player.getId();
                        pk.effectId = Effect.HASTE;
                        pk.eventId = MobEffectPacket.EVENT_ADD;
                        player.dataPacket(pk);
                    }

                    // Detección de HighJump
                    if (EventListener.AntiTower.containsKey(player.getName())) {
                        double y = EventListener.AntiTower.get(player.getName()).y;
                        double jumpY = 1.45D;

                        if (player.hasEffect(Effect.JUMP)) {
                            jumpY = Math.pow((player.getEffect(Effect.JUMP).getAmplifier() + 4.2), 2) / 16D + 0.1;
                        }

                        BigDecimal b1 = new BigDecimal(player.y);
                        BigDecimal b2 = new BigDecimal(y);
                        double dY = b1.subtract(b2).doubleValue();

                        if (dY > jumpY) {
                            int groundY = 1;
                            while (player.isOnline() && player.getLevel().getBlockIdAt((int) player.x, (int) player.y - groundY, (int) player.z) == 0) {
                                groundY++;
                            }
                            groundY -= 1;

                            if (groundY > 1) {
                                player.setMotion(new Vector3(0, -groundY, 0));
                                player.sendMessage(TextFormat.colorize("&cYou suspected to use HighJump cheat, please stop your behavior &eCheck: AntiCheat"));

                                highJump.put(player.getName(), highJump.getOrDefault(player.getName(), 0) + 1);
                                if (highJump.get(player.getName()) >= 10) {
                                    Server.getInstance().broadcastMessage(TextFormat.colorize("&dPlayer &b" + player.getName() + " &6suspected to use high jump cheats kicked by AntiCheat!"));
                                    player.kick(TextFormat.colorize("&cYou suspected to use high jump cheats kicked out by anti-cheat\n&eIf you misjudge the following information to the administrator\nCheck: AntiCheat[HighJump] HighJump:" + highJump.get(player.getName()) + " Speed:" + getMove(player.getName())) + " onGround:" + player.isOnGround() + " inAirTick:" + player.getInAirTicks());
                                }
                            }
                        }
                        EventListener.AntiTower.remove(player.getName());
                    }

                    // AntiSpeed principal
                    if (AntiCheatAPI.getInstance().getMasterConfig().getAntiSpeed()) {
                        AntiSpeed antiSpeed = new AntiSpeed(player);
                        if (antiSpeed.isCheat()) {
                            AntiCheatAPI.getInstance().addRecord(player, antiSpeed.getCheatType());
                            if (!EventListener.tp.containsKey(player.getName())) {
                                player.sendMessage(TextFormat.RED + "We detected that you used to accelerate. Perhaps this is a misjudgment.");
                                player.setMotion(new Vector3(0, 0, 0));
                            }
                        }
                    }
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setFinalMove(String name, float speed) {
        finalSpeed.put(name, speed);
    }

    public static float getMove(String name) {
        return finalSpeed.getOrDefault(name, 0F);
    }
}
