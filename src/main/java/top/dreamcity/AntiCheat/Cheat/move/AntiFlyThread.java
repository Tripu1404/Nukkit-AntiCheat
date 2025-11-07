package top.dreamcity.AntiCheat.Cheat.move;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import top.dreamcity.AntiCheat.AntiCheatAPI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup
 * All rights reserved.
 * Adapted for Nukkit modern versions (2025)
 */
public class AntiFlyThread extends AsyncTask {

    private final HashSet<String> playerThread = new HashSet<>();

    public AntiFlyThread() {
        Server.getInstance().getScheduler().scheduleAsyncTask(AntiCheatAPI.getInstance(), this);
    }

    @Override
    public void onRun() {
        while (true) {
            try {
                Map<UUID, Player> players = new HashMap<>(Server.getInstance().getOnlinePlayers());
                for (Player player : players.values()) {
                    if (player != null && player.isOnline() && !player.isOp() && player.getGamemode() == 0) {
                        if (!playerThread.contains(player.getName())) {
                            new AntiFlyPlayerThread(player);
                            playerThread.add(player.getName());
                        }
                    }
                }

                // Usamos un iterador para evitar ConcurrentModificationException
                playerThread.removeIf(name -> Server.getInstance().getPlayerExact(name) == null);

                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
