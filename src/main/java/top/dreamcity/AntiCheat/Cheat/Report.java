package top.dreamcity.AntiCheat.Cheat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import java.util.UUID;

/**
 * Adapted for modern Nukkit versions (2025)
 */
public class Report extends AsyncTask {

    private final UUID playerUUID;

    /**
     * Recibe el jugador y guarda solo su UUID (seguro para tareas asíncronas).
     */
    public Report(Player player) {
        this.playerUUID = player.getUniqueId();
    }

    @Override
    public void onRun() {
        // Aquí iría la lógica de procesamiento en segundo plano,
        // por ejemplo, recopilar datos de detección o enviar reportes a un servidor.
    }

    /**
     * Método auxiliar para obtener el jugador dentro del hilo principal (si es necesario).
     */
    public Player getPlayer() {
        return Server.getInstance().getPlayer(this.playerUUID);
    }
}
