package net.minecraft.world.biome;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenFossils;

public class BiomeDesert extends Biome {

    public BiomeDesert(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.SAND.getDefaultState();
        this.fillerBlock = Blocks.SAND.getDefaultState();
        this.decorator.treesPerChunk = -999;
        this.decorator.deadBushPerChunk = 2;
        this.decorator.reedsPerChunk = 50;
        this.decorator.cactiPerChunk = 10;
        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        Iterator iterator = this.spawnableMonsterList.iterator();

        while (iterator.hasNext()) {
            Biome.SpawnListEntry biomebase_biomemeta = (Biome.SpawnListEntry) iterator.next();

            if (biomebase_biomemeta.entityClass == EntityZombie.class || biomebase_biomemeta.entityClass == EntityZombieVillager.class) {
                iterator.remove();
            }
        }

        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombie.class, 19, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityZombieVillager.class, 1, 1, 1));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityHusk.class, 80, 4, 4));
    }

    public void decorate(World world, Random random, BlockPos blockposition) {
        super.decorate(world, random, blockposition);
        if (random.nextInt(1000) == 0) {
            int i = random.nextInt(16) + 8;
            int j = random.nextInt(16) + 8;
            BlockPos blockposition1 = world.getHeight(blockposition.add(i, 0, j)).up();

            (new WorldGenDesertWells()).generate(world, random, blockposition1);
        }

        if (random.nextInt(64) == 0) {
            (new WorldGenFossils()).generate(world, random, blockposition);
        }

    }
}
