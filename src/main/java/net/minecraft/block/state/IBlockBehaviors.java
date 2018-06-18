package net.minecraft.block.state;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public interface IBlockBehaviors {

    boolean onBlockEventReceived(World world, BlockPos blockposition, int i, int j);

    void neighborChanged(World world, BlockPos blockposition, Block block, BlockPos blockposition1);
}
