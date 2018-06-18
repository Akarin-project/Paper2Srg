package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.LootTableInfo.EntityTarget.a;
import net.minecraft.server.LootTableInfo.a;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class LootContext {

    private final float luck;
    private final WorldServer world;
    private final LootTableManager lootTableManager;
    @Nullable
    private final Entity lootedEntity;
    @Nullable
    private final EntityPlayer player;
    @Nullable
    private final DamageSource damageSource;
    private final Set<LootTable> lootTables = Sets.newLinkedHashSet();

    public LootContext(float f, WorldServer worldserver, LootTableManager loottableregistry, @Nullable Entity entity, @Nullable EntityPlayer entityhuman, @Nullable DamageSource damagesource) {
        this.luck = f;
        this.world = worldserver;
        this.lootTableManager = loottableregistry;
        this.lootedEntity = entity;
        this.player = entityhuman;
        this.damageSource = damagesource;
    }

    @Nullable
    public Entity getLootedEntity() {
        return this.lootedEntity;
    }

    @Nullable
    public Entity getKillerPlayer() {
        return this.player;
    }

    @Nullable
    public Entity getKiller() {
        return this.damageSource == null ? null : this.damageSource.getTrueSource();
    }

    public boolean addLootTable(LootTable loottable) {
        return this.lootTables.add(loottable);
    }

    public void removeLootTable(LootTable loottable) {
        this.lootTables.remove(loottable);
    }

    public LootTableManager getLootTableManager() {
        return this.lootTableManager;
    }

    public float getLuck() {
        return this.luck;
    }

    @Nullable
    public Entity getEntity(LootContext.EntityTarget loottableinfo_entitytarget) {
        switch (loottableinfo_entitytarget) {
        case THIS:
            return this.getLootedEntity();

        case KILLER:
            return this.getKiller();

        case KILLER_PLAYER:
            return this.getKillerPlayer();

        default:
            return null;
        }
    }

    public static enum EntityTarget {

        THIS("this"), KILLER("killer"), KILLER_PLAYER("killer_player");

        private final String targetType;

        private EntityTarget(String s) {
            this.targetType = s;
        }

        public static LootContext.EntityTarget fromString(String s) {
            LootContext.EntityTarget[] aloottableinfo_entitytarget = values();
            int i = aloottableinfo_entitytarget.length;

            for (int j = 0; j < i; ++j) {
                LootContext.EntityTarget loottableinfo_entitytarget = aloottableinfo_entitytarget[j];

                if (loottableinfo_entitytarget.targetType.equals(s)) {
                    return loottableinfo_entitytarget;
                }
            }

            throw new IllegalArgumentException("Invalid entity target " + s);
        }

        public static class a extends TypeAdapter<LootContext.EntityTarget> {

            public a() {}

            public void a(JsonWriter jsonwriter, LootContext.EntityTarget loottableinfo_entitytarget) throws IOException {
                jsonwriter.value(loottableinfo_entitytarget.targetType);
            }

            public LootContext.EntityTarget a(JsonReader jsonreader) throws IOException {
                return LootContext.EntityTarget.fromString(jsonreader.nextString());
            }

            public Object read(JsonReader jsonreader) throws IOException {
                return this.a(jsonreader);
            }

            public void write(JsonWriter jsonwriter, Object object) throws IOException {
                this.a(jsonwriter, (LootContext.EntityTarget) object);
            }
        }
    }

    public static class a {

        private final WorldServer a;
        private float b;
        private Entity c;
        private EntityPlayer d;
        private DamageSource e;

        public a(WorldServer worldserver) {
            this.a = worldserver;
        }

        public LootTableInfo.a a(float f) {
            this.b = f;
            return this;
        }

        public LootTableInfo.a a(Entity entity) {
            this.c = entity;
            return this;
        }

        public LootTableInfo.a a(EntityPlayer entityhuman) {
            this.d = entityhuman;
            return this;
        }

        public LootTableInfo.a a(DamageSource damagesource) {
            this.e = damagesource;
            return this;
        }

        public LootContext a() {
            return new LootContext(this.b, this.a, this.a.getLootTableManager(), this.c, this.d, this.e);
        }
    }
}
