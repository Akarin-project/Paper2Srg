package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockDragonEgg extends Block {

    protected static final AxisAlignedBB DRAGON_EGG_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public BlockDragonEgg() {
        super(Material.DRAGON_EGG, MapColor.BLACK);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDragonEgg.DRAGON_EGG_AABB;
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.checkFall(world, blockposition);
    }

    private void checkFall(World world, BlockPos blockposition) {
        if (BlockFalling.canFallThrough(world.getBlockState(blockposition.down())) && blockposition.getY() >= 0) {
            boolean flag = true;

            if (!BlockFalling.fallInstantly && world.isAreaLoaded(blockposition.add(-32, -32, -32), blockposition.add(32, 32, 32))) {
                world.spawnEntity(new EntityFallingBlock(world, (double) ((float) blockposition.getX() + 0.5F), (double) blockposition.getY(), (double) ((float) blockposition.getZ() + 0.5F), this.getDefaultState()));
            } else {
                world.setBlockToAir(blockposition);

                BlockPos blockposition1;

                for (blockposition1 = blockposition; BlockFalling.canFallThrough(world.getBlockState(blockposition1)) && blockposition1.getY() > 0; blockposition1 = blockposition1.down()) {
                    ;
                }

                if (blockposition1.getY() > 0) {
                    world.setBlockState(blockposition1.up(), this.getDefaultState(), 2); // Paper
                }
            }

        }
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        this.teleport(world, blockposition);
        return true;
    }

    public void onBlockClicked(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        this.teleport(world, blockposition);
    }

    private void teleport(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.getBlockState(blockposition);

        if (iblockdata.getBlock() == this) {
            for (int i = 0; i < 1000; ++i) {
                BlockPos blockposition1 = blockposition.add(world.rand.nextInt(16) - world.rand.nextInt(16), world.rand.nextInt(8) - world.rand.nextInt(8), world.rand.nextInt(16) - world.rand.nextInt(16));

                if (world.getBlockState(blockposition1).getBlock().blockMaterial == Material.AIR) {
                    // CraftBukkit start
                    org.bukkit.block.Block from = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                    org.bukkit.block.Block to = world.getWorld().getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ());
                    BlockFromToEvent event = new BlockFromToEvent(from, to);
                    org.bukkit.Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }

                    blockposition1 = new BlockPos(event.getToBlock().getX(), event.getToBlock().getY(), event.getToBlock().getZ());
                    // CraftBukkit end
                    if (world.isRemote) {
                        for (int j = 0; j < 128; ++j) {
                            double d0 = world.rand.nextDouble();
                            float f = (world.rand.nextFloat() - 0.5F) * 0.2F;
                            float f1 = (world.rand.nextFloat() - 0.5F) * 0.2F;
                            float f2 = (world.rand.nextFloat() - 0.5F) * 0.2F;
                            double d1 = (double) blockposition1.getX() + (double) (blockposition.getX() - blockposition1.getX()) * d0 + (world.rand.nextDouble() - 0.5D) + 0.5D;
                            double d2 = (double) blockposition1.getY() + (double) (blockposition.getY() - blockposition1.getY()) * d0 + world.rand.nextDouble() - 0.5D;
                            double d3 = (double) blockposition1.getZ() + (double) (blockposition.getZ() - blockposition1.getZ()) * d0 + (world.rand.nextDouble() - 0.5D) + 0.5D;

                            world.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1, (double) f2, new int[0]);
                        }
                    } else {
                        world.setBlockState(blockposition1, iblockdata, 2);
                        world.setBlockToAir(blockposition);
                    }

                    return;
                }
            }

        }
    }

    public int tickRate(World world) {
        return 5;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
