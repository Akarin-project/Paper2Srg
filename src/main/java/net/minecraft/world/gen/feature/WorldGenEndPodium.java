package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndPodium extends WorldGenerator {

    public static final BlockPos END_PODIUM_LOCATION = BlockPos.ORIGIN;
    public static final BlockPos END_PODIUM_CHUNK_POS = new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION.getX() - 4 & -16, 0, WorldGenEndPodium.END_PODIUM_LOCATION.getZ() - 4 & -16);
    private final boolean activePortal;

    public WorldGenEndPodium(boolean flag) {
        this.activePortal = flag;
    }

    public boolean generate(World world, Random random, BlockPos blockposition) {
        Iterator iterator = BlockPos.getAllInBoxMutable(new BlockPos(blockposition.getX() - 4, blockposition.getY() - 1, blockposition.getZ() - 4), new BlockPos(blockposition.getX() + 4, blockposition.getY() + 32, blockposition.getZ() + 4)).iterator();

        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
            double d0 = blockposition_mutableblockposition.getDistance(blockposition.getX(), blockposition_mutableblockposition.getY(), blockposition.getZ());

            if (d0 <= 3.5D) {
                if (blockposition_mutableblockposition.getY() < blockposition.getY()) {
                    if (d0 <= 2.5D) {
                        this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.BEDROCK.getDefaultState());
                    } else if (blockposition_mutableblockposition.getY() < blockposition.getY()) {
                        this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.END_STONE.getDefaultState());
                    }
                } else if (blockposition_mutableblockposition.getY() > blockposition.getY()) {
                    this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.AIR.getDefaultState());
                } else if (d0 > 2.5D) {
                    this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.BEDROCK.getDefaultState());
                } else if (this.activePortal) {
                    this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition_mutableblockposition), Blocks.END_PORTAL.getDefaultState());
                } else {
                    this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition_mutableblockposition), Blocks.AIR.getDefaultState());
                }
            }
        }

        for (int i = 0; i < 4; ++i) {
            this.setBlockAndNotifyAdequately(world, blockposition.up(i), Blocks.BEDROCK.getDefaultState());
        }

        BlockPos blockposition1 = blockposition.up(2);
        Iterator iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator1.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator1.next();

            this.setBlockAndNotifyAdequately(world, blockposition1.offset(enumdirection), Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, enumdirection));
        }

        return true;
    }
}
