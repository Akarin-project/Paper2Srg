package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndGateway extends WorldGenerator {

    public WorldGenEndGateway() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        Iterator iterator = BlockPos.getAllInBoxMutable(blockposition.add(-1, -2, -1), blockposition.add(1, 2, 1)).iterator();

        while (iterator.hasNext()) {
            BlockPos.MutableBlockPos blockposition_mutableblockposition = (BlockPos.MutableBlockPos) iterator.next();
            boolean flag = blockposition_mutableblockposition.getX() == blockposition.getX();
            boolean flag1 = blockposition_mutableblockposition.getY() == blockposition.getY();
            boolean flag2 = blockposition_mutableblockposition.getZ() == blockposition.getZ();
            boolean flag3 = Math.abs(blockposition_mutableblockposition.getY() - blockposition.getY()) == 2;

            if (flag && flag1 && flag2) {
                this.setBlockAndNotifyAdequately(world, new BlockPos(blockposition_mutableblockposition), Blocks.END_GATEWAY.getDefaultState());
            } else if (flag1) {
                this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.AIR.getDefaultState());
            } else if (flag3 && flag && flag2) {
                this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.BEDROCK.getDefaultState());
            } else if ((flag || flag2) && !flag3) {
                this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.BEDROCK.getDefaultState());
            } else {
                this.setBlockAndNotifyAdequately(world, blockposition_mutableblockposition, Blocks.AIR.getDefaultState());
            }
        }

        return true;
    }
}
