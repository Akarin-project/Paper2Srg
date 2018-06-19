package net.minecraft.world.end;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.gen.feature.WorldGenSpikes;

public enum DragonSpawnManager {

    START {;
        public void func_186079_a(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
            BlockPos blockposition1 = new BlockPos(0, 128, 0);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal = (EntityEnderCrystal) iterator.next();

                entityendercrystal.func_184516_a(blockposition1);
            }

            enderdragonbattle.func_186095_a(null.PREPARING_TO_SUMMON_PILLARS);
        }
    }, PREPARING_TO_SUMMON_PILLARS {;
    public void func_186079_a(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
        if (i < 100) {
            if (i == 0 || i == 50 || i == 51 || i == 52 || i >= 95) {
                worldserver.func_175718_b(3001, new BlockPos(0, 128, 0), 0);
            }
        } else {
            enderdragonbattle.func_186095_a(null.SUMMONING_PILLARS);
        }

    }
}, SUMMONING_PILLARS {;
    public void func_186079_a(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
        boolean flag = true;
        boolean flag1 = i % 40 == 0;
        boolean flag2 = i % 40 == 39;

        if (flag1 || flag2) {
            WorldGenSpikes.EndSpike[] aworldgenender_spike = BiomeEndDecorator.func_185426_a(worldserver);
            int j = i / 40;

            if (j < aworldgenender_spike.length) {
                WorldGenSpikes.EndSpike worldgenender_spike = aworldgenender_spike[j];

                if (flag1) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityEnderCrystal entityendercrystal = (EntityEnderCrystal) iterator.next();

                        entityendercrystal.func_184516_a(new BlockPos(worldgenender_spike.func_186151_a(), worldgenender_spike.func_186149_d() + 1, worldgenender_spike.func_186152_b()));
                    }
                } else {
                    boolean flag3 = true;
                    Iterator iterator1 = BlockPos.func_177975_b(new BlockPos(worldgenender_spike.func_186151_a() - 10, worldgenender_spike.func_186149_d() - 10, worldgenender_spike.func_186152_b() - 10), new BlockPos(worldgenender_spike.func_186151_a() + 10, worldgenender_spike.func_186149_d() + 10, worldgenender_spike.func_186152_b() + 10)).iterator();

                    while (iterator1.hasNext()) {
                        BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator1.next();

                        worldserver.func_175698_g(blockposition_mutableblockposition);
                    }

                    worldserver.func_72876_a((Entity) null, (double) ((float) worldgenender_spike.func_186151_a() + 0.5F), (double) worldgenender_spike.func_186149_d(), (double) ((float) worldgenender_spike.func_186152_b() + 0.5F), 5.0F, true);
                    WorldGenSpikes worldgenender = new WorldGenSpikes();

                    worldgenender.func_186143_a(worldgenender_spike);
                    worldgenender.func_186144_a(true);
                    worldgenender.func_186142_a(new BlockPos(0, 128, 0));
                    worldgenender.func_180709_b(worldserver, new Random(), new BlockPos(worldgenender_spike.func_186151_a(), 45, worldgenender_spike.func_186152_b()));
                }
            } else if (flag1) {
                enderdragonbattle.func_186095_a(null.SUMMONING_DRAGON);
            }
        }

    }
}, SUMMONING_DRAGON {;
    public void func_186079_a(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
        Iterator iterator;
        EntityEnderCrystal entityendercrystal;

        if (i >= 100) {
            enderdragonbattle.func_186095_a(null.END);
            enderdragonbattle.func_186087_f();
            iterator = list.iterator();

            while (iterator.hasNext()) {
                entityendercrystal = (EntityEnderCrystal) iterator.next();
                entityendercrystal.func_184516_a((BlockPos) null);
                worldserver.func_72876_a(entityendercrystal, entityendercrystal.field_70165_t, entityendercrystal.field_70163_u, entityendercrystal.field_70161_v, 6.0F, false);
                entityendercrystal.func_70106_y();
            }
        } else if (i >= 80) {
            worldserver.func_175718_b(3001, new BlockPos(0, 128, 0), 0);
        } else if (i == 0) {
            iterator = list.iterator();

            while (iterator.hasNext()) {
                entityendercrystal = (EntityEnderCrystal) iterator.next();
                entityendercrystal.func_184516_a(new BlockPos(0, 128, 0));
            }
        } else if (i < 5) {
            worldserver.func_175718_b(3001, new BlockPos(0, 128, 0), 0);
        }

    }
}, END {;
    public void func_186079_a(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {}
};

    private DragonSpawnManager() {}

    public abstract void func_186079_a(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition);

    DragonSpawnManager(Object object) {
        this();
    }
}
