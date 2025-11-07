package top.dreamcity.AntiCheat.Cheat.combat;

import cn.nukkit.Player;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;

/**
 * NPC falso utilizado por el sistema AntiCheat (detección de KillAura, etc.)
 * Adaptado para Nukkit 2.x (Java 17+)
 */
public class NPC extends EntityHuman {

    public NPC(Position pos, Skin skin, Player player) {
        super(pos.getChunk(), getEntityNBT(pos, skin));

        this.setSkin(skin);
        this.setNameTagVisible(false);
        this.setNameTagAlwaysVisible(false);
        this.setImmobile(true);
        this.setHealth(999);
        this.setMaxHealth(999);

        // Forzar visibilidad al jugador objetivo
        if (!this.hasSpawned.containsKey(player.getLoaderId())) {
            this.spawnTo(player);
        }
    }

    private static CompoundTag getEntityNBT(Vector3 position, Skin skin) {
        return new CompoundTag()
                .putList("Pos", new ListTag<DoubleTag>()
                        .add(new DoubleTag("", position.x))
                        .add(new DoubleTag("", position.y))
                        .add(new DoubleTag("", position.z)))
                .putList("Motion", new ListTag<DoubleTag>()
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))
                .putList("Rotation", new ListTag<FloatTag>()
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putCompound("Skin", new CompoundTag()
                        .putByteArray("Data", skin.getSkinData().data)
                        .putString("ModelId", skin.getSkinId()))
                .putString("NameTag", "AntiCheat_NPC");
    }
}
