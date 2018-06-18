package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFalling extends Block {

    public static boolean fallInstantly;

    public BlockFalling() {
        super(Material.SAND);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public BlockFalling(Material material) {
        super(material);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            this.checkFallable(world, blockposition);
        }

    }

    private void checkFallable(World world, BlockPos blockposition) {
        if (canFallThrough(world.getBlockState(blockposition.down())) && blockposition.getY() >= 0) {
            boolean flag = true;

            if (!BlockFalling.fallInstantly && world.isAreaLoaded(blockposition.add(-32, -32, -32), blockposition.add(32, 32, 32))) {
                if (!world.isRemote) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, world.getBlockState(blockposition));

                    this.onStartFalling(entityfallingblock);
                    world.spawnEntity(entityfallingblock);
                }
            } else {
                world.setBlockToAir(blockposition);

                BlockPos blockposition1;

                for (blockposition1 = blockposition.down(); canFallThrough(world.getBlockState(blockposition1)) && blockposition1.getY() > 0; blockposition1 = blockposition1.down()) {
                    ;
                }

                if (blockposition1.getY() > 0) {
                    world.setBlockState(blockposition1.up(), this.getDefaultState());
                }
            }

        }
    }

    protected void onStartFalling(EntityFallingBlock entityfallingblock) {}

    public int tickRate(World world) {
        return 2;
    }

    public static boolean canFallThrough(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();
        Material material = iblockdata.getMaterial();

        return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
    }

    // Paper start - OBFHELPER
    public static boolean canMoveThrough(IBlockState blockdata) {
        return BlockFalling.canFallThrough(blockdata);
    }
    // Paper end

    public void onEndFalling(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {}

    public void onBroken(World world, BlockPos blockposition) {}
}
