package net.minecraft.world.gen.feature;

import com.google.common.base.Predicate;
import java.util.Random;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class WorldGenMinable extends WorldGenerator {

    private final IBlockState oreBlock;
    private final int numberOfBlocks;
    private final Predicate<IBlockState> predicate;

    public WorldGenMinable(IBlockState iblockdata, int i) {
        this(iblockdata, i, new WorldGenMinable.a(null));
    }

    public WorldGenMinable(IBlockState iblockdata, int i, Predicate<IBlockState> predicate) {
        this.oreBlock = iblockdata;
        this.numberOfBlocks = i;
        this.predicate = predicate;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockposition) {
        float f = random.nextFloat() * 3.1415927F;
        double d0 = blockposition.getX() + 8 + MathHelper.sin(f) * this.numberOfBlocks / 8.0F;
        double d1 = blockposition.getX() + 8 - MathHelper.sin(f) * this.numberOfBlocks / 8.0F;
        double d2 = blockposition.getZ() + 8 + MathHelper.cos(f) * this.numberOfBlocks / 8.0F;
        double d3 = blockposition.getZ() + 8 - MathHelper.cos(f) * this.numberOfBlocks / 8.0F;
        double d4 = blockposition.getY() + random.nextInt(3) - 2;
        double d5 = blockposition.getY() + random.nextInt(3) - 2;

        for (int i = 0; i < this.numberOfBlocks; ++i) {
            float f1 = (float) i / (float) this.numberOfBlocks;
            double d6 = d0 + (d1 - d0) * f1;
            double d7 = d4 + (d5 - d4) * f1;
            double d8 = d2 + (d3 - d2) * f1;
            double d9 = random.nextDouble() * this.numberOfBlocks / 16.0D;
            double d10 = (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
            double d11 = (MathHelper.sin(3.1415927F * f1) + 1.0F) * d9 + 1.0D;
            int j = MathHelper.floor(d6 - d10 / 2.0D);
            int k = MathHelper.floor(d7 - d11 / 2.0D);
            int l = MathHelper.floor(d8 - d10 / 2.0D);
            int i1 = MathHelper.floor(d6 + d10 / 2.0D);
            int j1 = MathHelper.floor(d7 + d11 / 2.0D);
            int k1 = MathHelper.floor(d8 + d10 / 2.0D);

            for (int l1 = j; l1 <= i1; ++l1) {
                double d12 = (l1 + 0.5D - d6) / (d10 / 2.0D);

                if (d12 * d12 < 1.0D) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        double d13 = (i2 + 0.5D - d7) / (d11 / 2.0D);

                        if (d12 * d12 + d13 * d13 < 1.0D) {
                            for (int j2 = l; j2 <= k1; ++j2) {
                                double d14 = (j2 + 0.5D - d8) / (d10 / 2.0D);

                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
                                    BlockPos blockposition1 = new BlockPos(l1, i2, j2);

                                    if (this.predicate.apply(world.getBlockState(blockposition1))) {
                                        world.setBlockState(blockposition1, this.oreBlock, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    static class a implements Predicate<IBlockState> {

        private a() {}

        public boolean a(IBlockState iblockdata) {
            if (iblockdata != null && iblockdata.getBlock() == Blocks.STONE) {
                BlockStone.EnumType blockstone_enumstonevariant = iblockdata.getValue(BlockStone.VARIANT);

                return blockstone_enumstonevariant.isNatural();
            } else {
                return false;
            }
        }

        @Override
        public boolean apply(IBlockState object) {
            return this.a(object);
        }

        a(Object object) {
            this();
        }
    }
}
