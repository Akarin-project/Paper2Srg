package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeSavanna extends Biome {

    private static final WorldGenSavannaTree field_150627_aC = new WorldGenSavannaTree(false);

    protected BiomeSavanna(BiomeBase.a biomebase_a) {
        super(biomebase_a);
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 1));
        if (this.func_185355_j() > 1.1F) {
            this.field_76762_K.add(new Biome.SpawnListEntry(EntityLlama.class, 8, 4, 4));
        }

        this.field_76760_I.field_76832_z = 1;
        this.field_76760_I.field_76802_A = 4;
        this.field_76760_I.field_76803_B = 20;
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(5) > 0 ? BiomeSavanna.field_150627_aC : BiomeSavanna.field_76757_N);
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        BiomeSavanna.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);

        for (int i = 0; i < 7; ++i) {
            int j = random.nextInt(16) + 8;
            int k = random.nextInt(16) + 8;
            int l = random.nextInt(world.func_175645_m(blockposition.func_177982_a(j, 0, k)).func_177956_o() + 32);

            BiomeSavanna.field_180280_ag.func_180709_b(world, random, blockposition.func_177982_a(j, l, k));
        }

        super.func_180624_a(world, random, blockposition);
    }

    public Class<? extends Biome> func_150562_l() {
        return BiomeSavanna.class;
    }
}
