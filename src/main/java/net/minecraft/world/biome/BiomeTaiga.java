package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeTaiga extends Biome {

    private static final WorldGenTaiga1 field_150639_aC = new WorldGenTaiga1();
    private static final WorldGenTaiga2 field_150640_aD = new WorldGenTaiga2(false);
    private static final WorldGenMegaPineTree field_150641_aE = new WorldGenMegaPineTree(false, false);
    private static final WorldGenMegaPineTree field_150642_aF = new WorldGenMegaPineTree(false, true);
    private static final WorldGenBlockBlob field_150643_aG = new WorldGenBlockBlob(Blocks.field_150341_Y, 0);
    private final BiomeTaiga.Type field_150644_aH;

    public BiomeTaiga(BiomeTaiga.Type biometaiga_type, Biome.a biomebase_a) {
        super(biomebase_a);
        this.field_150644_aH = biometaiga_type;
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityRabbit.class, 4, 2, 3));
        this.field_76760_I.field_76832_z = 10;
        if (biometaiga_type != BiomeTaiga.Type.MEGA && biometaiga_type != BiomeTaiga.Type.MEGA_SPRUCE) {
            this.field_76760_I.field_76803_B = 1;
            this.field_76760_I.field_76798_D = 1;
        } else {
            this.field_76760_I.field_76803_B = 7;
            this.field_76760_I.field_76804_C = 1;
            this.field_76760_I.field_76798_D = 3;
        }

    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return (WorldGenAbstractTree) ((this.field_150644_aH == BiomeTaiga.Type.MEGA || this.field_150644_aH == BiomeTaiga.Type.MEGA_SPRUCE) && random.nextInt(3) == 0 ? (this.field_150644_aH != BiomeTaiga.Type.MEGA_SPRUCE && random.nextInt(13) != 0 ? BiomeTaiga.field_150641_aE : BiomeTaiga.field_150642_aF) : (random.nextInt(3) == 0 ? BiomeTaiga.field_150639_aC : BiomeTaiga.field_150640_aD));
    }

    public WorldGenerator func_76730_b(Random random) {
        return random.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        int i;
        int j;
        int k;
        int l;

        if (this.field_150644_aH == BiomeTaiga.Type.MEGA || this.field_150644_aH == BiomeTaiga.Type.MEGA_SPRUCE) {
            i = random.nextInt(3);

            for (j = 0; j < i; ++j) {
                k = random.nextInt(16) + 8;
                l = random.nextInt(16) + 8;
                BlockPos blockposition1 = world.func_175645_m(blockposition.func_177982_a(k, 0, l));

                BiomeTaiga.field_150643_aG.func_180709_b(world, random, blockposition1);
            }
        }

        BiomeTaiga.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.FERN);

        for (i = 0; i < 7; ++i) {
            j = random.nextInt(16) + 8;
            k = random.nextInt(16) + 8;
            l = random.nextInt(world.func_175645_m(blockposition.func_177982_a(j, 0, k)).func_177956_o() + 32);
            BiomeTaiga.field_180280_ag.func_180709_b(world, random, blockposition.func_177982_a(j, l, k));
        }

        super.func_180624_a(world, random, blockposition);
    }

    public void func_180622_a(World world, Random random, ChunkPrimer chunksnapshot, int i, int j, double d0) {
        if (this.field_150644_aH == BiomeTaiga.Type.MEGA || this.field_150644_aH == BiomeTaiga.Type.MEGA_SPRUCE) {
            this.field_76752_A = Blocks.field_150349_c.func_176223_P();
            this.field_76753_B = Blocks.field_150346_d.func_176223_P();
            if (d0 > 1.75D) {
                this.field_76752_A = Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.COARSE_DIRT);
            } else if (d0 > -0.95D) {
                this.field_76752_A = Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.PODZOL);
            }
        }

        this.func_180628_b(world, random, chunksnapshot, i, j, d0);
    }

    public static enum Type {

        NORMAL, MEGA, MEGA_SPRUCE;

        private Type() {}
    }
}
