package net.minecraft.world.biome;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class BiomeSnow extends Biome {

    private final boolean superIcy;
    private final WorldGenIceSpike iceSpike = new WorldGenIceSpike();
    private final WorldGenIcePath icePatch = new WorldGenIcePath(4);

    public BiomeSnow(boolean flag, Biome.a biomebase_a) {
        super(biomebase_a);
        this.superIcy = flag;
        if (flag) {
            this.topBlock = Blocks.SNOW.getDefaultState();
        }

        this.spawnableCreatureList.clear();
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityRabbit.class, 10, 2, 3));
        this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
        Iterator iterator = this.spawnableMonsterList.iterator();

        while (iterator.hasNext()) {
            Biome.SpawnListEntry biomebase_biomemeta = (Biome.SpawnListEntry) iterator.next();

            if (biomebase_biomemeta.entityClass == EntitySkeleton.class) {
                iterator.remove();
            }
        }

        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntitySkeleton.class, 20, 4, 4));
        this.spawnableMonsterList.add(new Biome.SpawnListEntry(EntityStray.class, 80, 4, 4));
    }

    @Override
    public float getSpawningChance() {
        return 0.07F;
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockposition) {
        if (this.superIcy) {
            int i;
            int j;
            int k;

            for (i = 0; i < 3; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                this.iceSpike.generate(world, random, world.getHeight(blockposition.add(j, 0, k)));
            }

            for (i = 0; i < 2; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                this.icePatch.generate(world, random, world.getHeight(blockposition.add(j, 0, k)));
            }
        }

        super.decorate(world, random, blockposition);
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random random) {
        return new WorldGenTaiga2(false);
    }
}
