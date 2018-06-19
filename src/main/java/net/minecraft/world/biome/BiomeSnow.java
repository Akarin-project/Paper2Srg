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

    private final boolean field_150615_aC;
    private final WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
    private final WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);

    public BiomeSnow(boolean flag, BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_150615_aC = flag;
        if (flag) {
            this.field_76752_A = Blocks.field_150433_aE.func_176223_P();
        }

        this.field_76762_K.clear();
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityRabbit.class, 10, 2, 3));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
        Iterator iterator = this.field_76761_J.iterator();

        while (iterator.hasNext()) {
            Biome.SpawnListEntry biomebase_biomemeta = (Biome.SpawnListEntry) iterator.next();

            if (biomebase_biomemeta.field_76300_b == EntitySkeleton.class) {
                iterator.remove();
            }
        }

        this.field_76761_J.add(new Biome.SpawnListEntry(EntitySkeleton.class, 20, 4, 4));
        this.field_76761_J.add(new Biome.SpawnListEntry(EntityStray.class, 80, 4, 4));
    }

    public float func_76741_f() {
        return 0.07F;
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        if (this.field_150615_aC) {
            int i;
            int j;
            int k;

            for (i = 0; i < 3; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                this.field_150616_aD.func_180709_b(world, random, world.func_175645_m(blockposition.func_177982_a(j, 0, k)));
            }

            for (i = 0; i < 2; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                this.field_150617_aE.func_180709_b(world, random, world.func_175645_m(blockposition.func_177982_a(j, 0, k)));
            }
        }

        super.func_180624_a(world, random, blockposition);
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return new WorldGenTaiga2(false);
    }
}
