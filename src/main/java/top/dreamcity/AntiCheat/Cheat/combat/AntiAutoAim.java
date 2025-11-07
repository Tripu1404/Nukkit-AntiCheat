package top.dreamcity.AntiCheat.Cheat.combat;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import top.dreamcity.AntiCheat.AntiCheatAPI;
import top.dreamcity.AntiCheat.Event.CheckCheatEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Adaptado para Nukkit moderno (2025)
 * 
 * Detección de AutoAim mediante creación temporal de NPC fantasma.
 */
public class AntiAutoAim extends Combat {

    private NPC npc;

    public AntiAutoAim(Player player) {
        super(player);
        addDummy();
    }

    @Override
    public CheatType getCheatType() {
        return CheatType.AUTO_AIM;
    }

    @Override
    public boolean isCheat() {
        CheckCheatEvent event = new CheckCheatEvent(player, getCheatType());
        Server.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        // Añade nuevamente el NPC “fantasma”
        addDummy();
        return false;
    }

    private void addDummy() {
        try {
            byte[] skin = image(AntiCheatAPI.getInstance().getMasterConfig().getSkinPath());
            Position pos = new Position(player.getX(), player.getY() - 2, player.getZ(), player.getLevel());

            npc = new NPC(pos, skin, player);
            npc.setNameTagVisible(false);
            npc.setScale(0.001f);
            npc.spawnTo(player);

        } catch (Exception e) {
            Server.getInstance().getLogger().warning("Error al crear dummy AntiAutoAim: " + e.getMessage());
        }
    }

    public void move(Player player) {
        if (npc != null && npc.isSpawned()) {
            npc.teleport(new Location(player.getX(), player.getY() - 2, player.getZ(), player.getYaw(), player.getPitch()));
        }
    }

    public NPC getNpc() {
        return npc;
    }

    private static byte[] image(String path) {
        File file = new File(path);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer imagen del skin: " + e.getMessage(), e);
        }
        return parseBufferedImage(image);
    }

    private static byte[] parseBufferedImage(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();
        return outputStream.toByteArray();
    }
}
