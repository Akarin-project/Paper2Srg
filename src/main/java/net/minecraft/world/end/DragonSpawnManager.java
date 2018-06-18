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
        @Override
        public void process(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
            BlockPos blockposition1 = new BlockPos(0, 128, 0);
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal = (EntityEnderCrystal) iterator.next();

                entityendercrystal.setBeamTarget(blockposition1);
            }

            enderdragonbattle.setRespawnState(PREPARING_TO_SUMMON_PILLARS);
        }
    }, PREPARING_TO_SUMMON_PILLARS {;
    @Override
    public void process(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
        if (i < 100) {
            if (i == 0 || i == 50 || i == 51 || i == 52 || i >= 95) {
                worldserver.playEvent(3001, new BlockPos(0, 128, 0), 0);
            }
        } else {
            enderdragonbattle.setRespawnState(SUMMONING_PILLARS);
        }

    }
}, SUMMONING_PILLARS {;
    @Override
    public void process(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
        boolean flag = true;
        boolean flag1 = i % 40 == 0;
        boolean flag2 = i % 40 == 39;

        if (flag1 || flag2) {
            WorldGenSpikes.EndSpike[] aworldgenender_spike = BiomeEndDecorator.getSpikesForWorld(worldserver);
            int j = i / 40;

            if (j < aworldgenender_spike.length) {
                WorldGenSpikes.EndSpike worldgenender_spike = aworldgenender_spike[j];

                if (flag1) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityEnderCrystal entityendercrystal = (EntityEnderCrystal) iterator.next();

                        entityendercrystal.setBeamTarget(new BlockPos(worldgenender_spike.getCenterX(), worldgenender_spike.getHeight() + 1, worldgenender_spike.getCenterZ()));
                    }
                } else {
                    boolean flag3 = true;
                    Iterator iterator1 = BlockPos.getAllInBoxMutable(new BlockPos(worldgenender_spike.getCenterX() - 10, worldgenender_spike.getHeight() - 10, worldgenender_spike.getCenterZ() - 10), new BlockPos(worldgenender_spike.getCenterX() + 10, worldgenender_spike.getHeight() + 10, worldgenender_spike.getCenterZ() + 10)).iterator();

                    while (iterator1.hasNext()) {
                        BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator1.next();

                        worldserver.setBlockToAir(blockposition_mutableblockposition);
                    }

                    worldserver.createExplosion((Entity) null, worldgenender_spike.getCenterX() + 0.5F, worldgenender_spike.getHeight(), worldgenender_spike.getCenterZ() + 0.5F, 5.0F, true);
                    WorldGenSpikes worldgenender = new WorldGenSpikes();

                    worldgenender.setSpike(worldgenender_spike);
                    worldgenender.setCrystalInvulnerable(true);
                    worldgenender.setBeamTarget(new BlockPos(0, 128, 0));
                    worldgenender.generate(worldserver, new Random(), new BlockPos(worldgenender_spike.getCenterX(), 45, worldgenender_spike.getCenterZ()));
                }
            } else if (flag1) {
                enderdragonbattle.setRespawnState(SUMMONING_DRAGON);
            }
        }

    }
}, SUMMONING_DRAGON {;
    @Override
    public void process(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {
        Iterator iterator;
        EntityEnderCrystal entityendercrystal;

        if (i >= 100) {
            enderdragonbattle.setRespawnState(END);
            enderdragonbattle.resetSpikeCrystals();
            iterator = list.iterator();

            while (iterator.hasNext()) {
                entityendercrystal = (EntityEnderCrystal) iterator.next();
                entityendercrystal.setBeamTarget((BlockPos) null);
                worldserver.createExplosion(entityendercrystal, entityendercrystal.posX, entityendercrystal.posY, entityendercrystal.posZ, 6.0F, false);
                entityendercrystal.setDead();
            }
        } else if (i >= 80) {
            worldserver.playEvent(3001, new BlockPos(0, 128, 0), 0);
        } else if (i == 0) {
            iterator = list.iterator();

            while (iterator.hasNext()) {
                entityendercrystal = (EntityEnderCrystal) iterator.next();
                entityendercrystal.setBeamTarget(new BlockPos(0, 128, 0));
            }
        } else if (i < 5) {
            worldserver.playEvent(3001, new BlockPos(0, 128, 0), 0);
        }

    }
}, END {;
    @Override
    public void process(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition) {}
};

    private DragonSpawnManager() {}

    public abstract void process(WorldServer worldserver, DragonFightManager enderdragonbattle, List<EntityEnderCrystal> list, int i, BlockPos blockposition);

    DragonSpawnManager(Object object) {
        this();
    }
}
