package net.minecraft.tileentity;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MCUtil;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public abstract class MobSpawnerBaseLogic {

    public int field_98286_b = 20;
    private final List<WeightedSpawnerEntity> field_98285_e = Lists.newArrayList();
    private WeightedSpawnerEntity field_98282_f = new WeightedSpawnerEntity();
    private double field_98287_c;
    private double field_98284_d;
    public int field_98283_g = 200; // CraftBukkit private -> public
    public int field_98293_h = 800; // CraftBukkit private -> public
    public int field_98294_i = 4; // CraftBukkit private -> public
    private Entity field_98291_j;
    public int field_98292_k = 6; // CraftBukkit private -> public
    public int field_98289_l = 16; // CraftBukkit private -> public
    public int field_98290_m = 4; // CraftBukkit private -> public
    private int tickDelay = 0; // Paper

    public MobSpawnerBaseLogic() {}

    @Nullable
    public ResourceLocation func_190895_g() {
        String s = this.field_98282_f.func_185277_b().func_74779_i("id");

        return StringUtils.func_151246_b(s) ? null : new ResourceLocation(s);
    }

    public void func_190894_a(@Nullable ResourceLocation minecraftkey) {
        if (minecraftkey != null) {
            this.field_98282_f.func_185277_b().func_74778_a("id", minecraftkey.toString());
            this.field_98285_e.clear(); // CraftBukkit - SPIGOT-3496, MC-92282
        }

    }

    private boolean func_98279_f() {
        BlockPos blockposition = this.func_177221_b();

        return this.func_98271_a().func_175636_b((double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D, (double) this.field_98289_l);
    }

    public void func_98278_g() {
        // Paper start - Configurable mob spawner tick rate
        if (field_98286_b > 0 && --tickDelay > 0) return;
        tickDelay = this.func_98271_a().paperConfig.mobSpawnerTickRate;
        // Paper end
        if (!this.func_98279_f()) {
            this.field_98284_d = this.field_98287_c;
        } else {
            BlockPos blockposition = this.func_177221_b();

            if (this.func_98271_a().field_72995_K) {
                double d0 = (double) ((float) blockposition.func_177958_n() + this.func_98271_a().field_73012_v.nextFloat());
                double d1 = (double) ((float) blockposition.func_177956_o() + this.func_98271_a().field_73012_v.nextFloat());
                double d2 = (double) ((float) blockposition.func_177952_p() + this.func_98271_a().field_73012_v.nextFloat());

                this.func_98271_a().func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                this.func_98271_a().func_175688_a(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                if (this.field_98286_b > 0) {
                    this.field_98286_b -= tickDelay; // Paper
                }

                this.field_98284_d = this.field_98287_c;
                this.field_98287_c = (this.field_98287_c + (double) (1000.0F / ((float) this.field_98286_b + 200.0F))) % 360.0D;
            } else {
                if (this.field_98286_b < -tickDelay) { // Paper
                    this.func_98273_j();
                }

                if (this.field_98286_b > 0) {
                    this.field_98286_b -= tickDelay; // Paper
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.field_98294_i; ++i) {
                    NBTTagCompound nbttagcompound = this.field_98282_f.func_185277_b();
                    NBTTagList nbttaglist = nbttagcompound.func_150295_c("Pos", 6);
                    World world = this.func_98271_a();
                    int j = nbttaglist.func_74745_c();
                    double d3 = j >= 1 ? nbttaglist.func_150309_d(0) : (double) blockposition.func_177958_n() + (world.field_73012_v.nextDouble() - world.field_73012_v.nextDouble()) * (double) this.field_98290_m + 0.5D;
                    double d4 = j >= 2 ? nbttaglist.func_150309_d(1) : (double) (blockposition.func_177956_o() + world.field_73012_v.nextInt(3) - 1);
                    double d5 = j >= 3 ? nbttaglist.func_150309_d(2) : (double) blockposition.func_177952_p() + (world.field_73012_v.nextDouble() - world.field_73012_v.nextDouble()) * (double) this.field_98290_m + 0.5D;
                    // Paper start
                    if (this.func_190895_g() == null) {
                        return;
                    }
                    String key = this.func_190895_g().func_110623_a();
                    org.bukkit.entity.EntityType type = org.bukkit.entity.EntityType.fromName(key);
                    if (type != null) {
                        com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent event;
                        event = new com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent(
                                MCUtil.toLocation(world, d3, d4, d5),
                                type,
                                org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER
                        );
                        if (!event.callEvent()) {
                            if (event.shouldAbortSpawn()) {
                                break;
                            }
                            continue;
                        }
                    }
                    // Paper end
                    Entity entity = AnvilChunkLoader.func_186054_a(nbttagcompound, world, d3, d4, d5, false);

                    if (entity == null) {
                        return;
                    }

                    int k = world.func_72872_a(entity.getClass(), (new AxisAlignedBB((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), (double) (blockposition.func_177958_n() + 1), (double) (blockposition.func_177956_o() + 1), (double) (blockposition.func_177952_p() + 1))).func_186662_g((double) this.field_98290_m)).size();

                    if (k >= this.field_98292_k) {
                        this.func_98273_j();
                        return;
                    }

                    EntityLiving entityinsentient = entity instanceof EntityLiving ? (EntityLiving) entity : null;

                    entity.func_70012_b(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, world.field_73012_v.nextFloat() * 360.0F, 0.0F);
                    if (entityinsentient == null || entityinsentient.func_70601_bi() && entityinsentient.func_70058_J()) {
                        if (this.field_98282_f.func_185277_b().func_186856_d() == 1 && this.field_98282_f.func_185277_b().func_150297_b("id", 8) && entity instanceof EntityLiving) {
                            ((EntityLiving) entity).func_180482_a(world.func_175649_E(new BlockPos(entity)), (IEntityLivingData) null);
                        }
                        entity.spawnedViaMobSpawner = true; // Paper
                        // Spigot Start
                        if ( entity.field_70170_p.spigotConfig.nerfSpawnerMobs )
                        {
                            entity.fromMobSpawner = true;
                        }

                        flag = true; // Paper

                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callSpawnerSpawnEvent(entity, blockposition).isCancelled()) {
                            continue;
                        }
                        // Spigot End
                        AnvilChunkLoader.a(entity, world, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER); // CraftBukkit
                        world.func_175718_b(2004, blockposition, 0);
                        if (entityinsentient != null) {
                            entityinsentient.func_70656_aK();
                        }

                        /*flag = true;*/ // Paper - moved up above cancellable event
                    }
                }

                if (flag) {
                    this.func_98273_j();
                }
            }

        }
    }

    private void func_98273_j() {
        if (this.field_98293_h <= this.field_98283_g) {
            this.field_98286_b = this.field_98283_g;
        } else {
            int i = this.field_98293_h - this.field_98283_g;

            this.field_98286_b = this.field_98283_g + this.func_98271_a().field_73012_v.nextInt(i);
        }

        if (!this.field_98285_e.isEmpty()) {
            this.func_184993_a((WeightedSpawnerEntity) WeightedRandom.func_76271_a(this.func_98271_a().field_73012_v, this.field_98285_e));
        }

        this.func_98267_a(1);
    }

    public void func_98270_a(NBTTagCompound nbttagcompound) {
        this.field_98286_b = nbttagcompound.func_74765_d("Delay");
        this.field_98285_e.clear();
        if (nbttagcompound.func_150297_b("SpawnPotentials", 9)) {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                this.field_98285_e.add(new WeightedSpawnerEntity(nbttaglist.func_150305_b(i)));
            }
        }

        if (nbttagcompound.func_150297_b("SpawnData", 10)) {
            this.func_184993_a(new WeightedSpawnerEntity(1, nbttagcompound.func_74775_l("SpawnData")));
        } else if (!this.field_98285_e.isEmpty()) {
            this.func_184993_a((WeightedSpawnerEntity) WeightedRandom.func_76271_a(this.func_98271_a().field_73012_v, this.field_98285_e));
        }

        if (nbttagcompound.func_150297_b("MinSpawnDelay", 99)) {
            this.field_98283_g = nbttagcompound.func_74765_d("MinSpawnDelay");
            this.field_98293_h = nbttagcompound.func_74765_d("MaxSpawnDelay");
            this.field_98294_i = nbttagcompound.func_74765_d("SpawnCount");
        }

        if (nbttagcompound.func_150297_b("MaxNearbyEntities", 99)) {
            this.field_98292_k = nbttagcompound.func_74765_d("MaxNearbyEntities");
            this.field_98289_l = nbttagcompound.func_74765_d("RequiredPlayerRange");
        }

        if (nbttagcompound.func_150297_b("SpawnRange", 99)) {
            this.field_98290_m = nbttagcompound.func_74765_d("SpawnRange");
        }

        if (this.func_98271_a() != null) {
            this.field_98291_j = null;
        }

    }

    public NBTTagCompound func_189530_b(NBTTagCompound nbttagcompound) {
        ResourceLocation minecraftkey = this.func_190895_g();

        if (minecraftkey == null) {
            return nbttagcompound;
        } else {
            nbttagcompound.func_74777_a("Delay", (short) this.field_98286_b);
            nbttagcompound.func_74777_a("MinSpawnDelay", (short) this.field_98283_g);
            nbttagcompound.func_74777_a("MaxSpawnDelay", (short) this.field_98293_h);
            nbttagcompound.func_74777_a("SpawnCount", (short) this.field_98294_i);
            nbttagcompound.func_74777_a("MaxNearbyEntities", (short) this.field_98292_k);
            nbttagcompound.func_74777_a("RequiredPlayerRange", (short) this.field_98289_l);
            nbttagcompound.func_74777_a("SpawnRange", (short) this.field_98290_m);
            nbttagcompound.func_74782_a("SpawnData", this.field_98282_f.func_185277_b().func_74737_b());
            NBTTagList nbttaglist = new NBTTagList();

            if (this.field_98285_e.isEmpty()) {
                nbttaglist.func_74742_a(this.field_98282_f.func_185278_a());
            } else {
                Iterator iterator = this.field_98285_e.iterator();

                while (iterator.hasNext()) {
                    WeightedSpawnerEntity mobspawnerdata = (WeightedSpawnerEntity) iterator.next();

                    nbttaglist.func_74742_a(mobspawnerdata.func_185278_a());
                }
            }

            nbttagcompound.func_74782_a("SpawnPotentials", nbttaglist);
            return nbttagcompound;
        }
    }

    public boolean func_98268_b(int i) {
        if (i == 1 && this.func_98271_a().field_72995_K) {
            this.field_98286_b = this.field_98283_g;
            return true;
        } else {
            return false;
        }
    }

    public void func_184993_a(WeightedSpawnerEntity mobspawnerdata) {
        this.field_98282_f = mobspawnerdata;
    }

    public abstract void func_98267_a(int i);

    public abstract World func_98271_a();

    public abstract BlockPos func_177221_b();
}
