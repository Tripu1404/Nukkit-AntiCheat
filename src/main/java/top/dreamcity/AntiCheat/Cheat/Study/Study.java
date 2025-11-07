package top.dreamcity.AntiCheat.Cheat.Study;

import cn.nukkit.Server;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Copyright © 2016 WetABQ&DreamCityAdminGroup
 * Adaptado para Nukkit moderno (2025)
 */
public class Study {

    public static final String API_ADDRESS_SPEED = "http://14.29.54.37:88/AntiCheat/SpeedCheat.php";

    /**
     * Envía datos de estudio al servidor externo (entrenamiento ML)
     */
    public static void SpeedStudy(double maxSpeed, double avgSpeed, boolean isCheat) {
        sendGet(API_ADDRESS_SPEED, "maxspeed=" + maxSpeed + "&averageSpeed=" + avgSpeed + "&isCheating=" + isCheat);
    }

    /**
     * Consulta predicción de trampa desde el sistema AntiCheat-ML externo
     */
    public static boolean SpeedPredict(double maxSpeed, double avgSpeed) {
        try {
            String jsonText = sendGet(API_ADDRESS_SPEED, "maxspeed=" + maxSpeed + "&averageSpeed=" + avgSpeed);
            if (jsonText == null || jsonText.isEmpty()) {
                Server.getInstance().getLogger().warning("AntiCheat-MLSystem >> Empty response from API.");
                return false;
            }

            Gson gson = new Gson();
            Map<?, ?> json = gson.fromJson(jsonText, Map.class);
            Object result = json.get("isCheating");
            return result != null && result.toString().equalsIgnoreCase("true");

        } catch (Exception e) {
            Server.getInstance().getLogger().error("AntiCheat-MLSystem >> Error predicting speed cheat.", e);
            return false;
        }
    }

    /**
     * Envía una solicitud GET de manera segura y compatible con Java moderno
     */
    private static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();

        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Nukkit-AntiCheat-ML/1.0");
            connection.setConnectTimeout(4000);
            connection.setReadTimeout(4000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Server.getInstance().getLogger().warning("AntiCheat-MLSystem >> HTTP " + responseCode + " from API");
                return "";
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            }

        } catch (Exception e) {
            Server.getInstance().getLogger().warning("AntiCheat-MLSystem >> Failed to send GET request: " + e.getMessage());
        }

        return result.toString();
    }
}
