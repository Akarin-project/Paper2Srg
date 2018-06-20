package net.minecraft.world.biome;

import java.util.Random;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomePlains extends Biome {

    protected boolean field_150628_aC;

    protected BiomePlains(boolean flag, Biome.a biomebase_a) {
        super(biomebase_a);
        this.field_150628_aC = flag;
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityHorse.class, 5, 2, 6));
        this.field_76762_K.add(new Biome.SpawnListEntry(EntityDonkey.class, 1, 1, 3));
        this.field_76760_I.field_76832_z = 0;
        this.field_76760_I.field_189870_A = 0.05F;
        this.field_76760_I.field_76802_A = 4;
        this.field_76760_I.field_76803_B = 10;
    }

    public BlockFlower.EnumFlowerType func_180623_a(Random random, BlockPos blockposition) {
        double d0 = BiomePlains.field_180281_af.func_151601_a((double) blockposition.func_177958_n() / 200.0D, (double) blockposition.func_177952_p() / 200.0D);
        int i;

        if (d0 < -0.8D) {
            i = random.nextInt(4);
            switch (i) {
            case 0:
                return BlockFlower.EnumFlowerType.ORANGE_TULIP;

            case 1:
                return BlockFlower.EnumFlowerType.RED_TULIP;

            case 2:
                return BlockFlower.EnumFlowerType.PINK_TULIP;

            case 3:
            default:
                return BlockFlower.EnumFlowerType.WHITE_TULIP;
            }
        } else if (random.nextInt(3) > 0) {
            i = random.nextInt(3);
            return i == 0 ? BlockFlower.EnumFlowerType.POPPY : (i == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : BlockFlower.EnumFlowerType.OXEYE_DAISY);
        } else {
            return BlockFlower.EnumFlowerType.DANDELION;
        }
    }

    public void func_180624_a(World world, Random random, BlockPos blockposition) {
        double d0 = BiomePlains.field_180281_af.func_151601_a((double) (blockposition.func_177958_n() + 8) / 200.0D, (double) (blockposition.func_177952_p() + 8) / 200.0D);
        int i;
        int j;
        int k;
        int l;

        if (d0 < -0.8D) {
            this.field_76760_I.field_76802_A = 15;
            this.field_76760_I.field_76803_B = 5;
        } else {
            this.field_76760_I.field_76802_A = 4;
            this.field_76760_I.field_76803_B = 10;
            BiomePlains.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.GRASS);

            for (i = 0; i < 7; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = random.nextInt(world.func_175645_m(blockposition.func_177982_a(j, 0, k)).func_177956_o() + 32);
                BiomePlains.field_180280_ag.func_180709_b(world, random, blockposition.func_177982_a(j, l, k));
            }
        }

        if (this.field_150628_aC) {
            BiomePlains.field_180280_ag.func_180710_a(BlockDoublePlant.EnumPlantType.SUNFLOWER);

            for (i = 0; i < 10; ++i) {
                j = random.nextInt(16) + 8;
                k = random.nextInt(16) + 8;
                l = random.nextInt(world.func_175645_m(blockposition.func_177982_a(j, 0, k)).func_177956_o() + 32);
                BiomePlains.field_180280_ag.func_180709_b(world, random, blockposition.func_177982_a(j, l, k));
            }
        }

        super.func_180624_a(world, random, blockposition);
    }

    public WorldGenAbstractTree func_150567_a(Random random) {
        return (WorldGenAbstractTree) (random.nextInt(3) == 0 ? BiomePlains.field_76758_O : BiomePlains.field_76757_N);
    }
}
