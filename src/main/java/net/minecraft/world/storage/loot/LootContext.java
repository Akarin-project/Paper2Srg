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

    private final float field_186498_a;
    private final WorldServer field_186499_b;
    private final LootTableManager field_186500_c;
    @Nullable
    private final Entity field_186501_d;
    @Nullable
    private final EntityPlayer field_186502_e;
    @Nullable
    private final DamageSource field_186503_f;
    private final Set<LootTable> field_186504_g = Sets.newLinkedHashSet();

    public LootContext(float f, WorldServer worldserver, LootTableManager loottableregistry, @Nullable Entity entity, @Nullable EntityPlayer entityhuman, @Nullable DamageSource damagesource) {
        this.field_186498_a = f;
        this.field_186499_b = worldserver;
        this.field_186500_c = loottableregistry;
        this.field_186501_d = entity;
        this.field_186502_e = entityhuman;
        this.field_186503_f = damagesource;
    }

    @Nullable
    public Entity func_186493_a() {
        return this.field_186501_d;
    }

    @Nullable
    public Entity func_186495_b() {
        return this.field_186502_e;
    }

    @Nullable
    public Entity func_186492_c() {
        return this.field_186503_f == null ? null : this.field_186503_f.func_76346_g();
    }

    public boolean func_186496_a(LootTable loottable) {
        return this.field_186504_g.add(loottable);
    }

    public void func_186490_b(LootTable loottable) {
        this.field_186504_g.remove(loottable);
    }

    public LootTableManager func_186497_e() {
        return this.field_186500_c;
    }

    public float func_186491_f() {
        return this.field_186498_a;
    }

    @Nullable
    public Entity func_186494_a(LootContext.EntityTarget loottableinfo_entitytarget) {
        switch (loottableinfo_entitytarget) {
        case THIS:
            return this.func_186493_a();

        case KILLER:
            return this.func_186492_c();

        case KILLER_PLAYER:
            return this.func_186495_b();

        default:
            return null;
        }
    }

    public static enum EntityTarget {

        THIS("this"), KILLER("killer"), KILLER_PLAYER("killer_player");

        private final String field_186488_d;

        private EntityTarget(String s) {
            this.field_186488_d = s;
        }

        public static LootContext.EntityTarget func_186482_a(String s) {
            LootContext.EntityTarget[] aloottableinfo_entitytarget = values();
            int i = aloottableinfo_entitytarget.length;

            for (int j = 0; j < i; ++j) {
                LootContext.EntityTarget loottableinfo_entitytarget = aloottableinfo_entitytarget[j];

                if (loottableinfo_entitytarget.field_186488_d.equals(s)) {
                    return loottableinfo_entitytarget;
                }
            }

            throw new IllegalArgumentException("Invalid entity target " + s);
        }

        public static class a extends TypeAdapter<LootContext.EntityTarget> {

            public a() {}

            public void a(JsonWriter jsonwriter, LootContext.EntityTarget loottableinfo_entitytarget) throws IOException {
                jsonwriter.value(loottableinfo_entitytarget.field_186488_d);
            }

            public LootContext.EntityTarget a(JsonReader jsonreader) throws IOException {
                return LootContext.EntityTarget.func_186482_a(jsonreader.nextString());
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
            return new LootContext(this.b, this.a, this.a.func_184146_ak(), this.c, this.d, this.e);
        }
    }
}
