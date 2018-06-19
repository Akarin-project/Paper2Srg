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

    protected static final AxisAlignedBB field_185660_a = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public BlockDragonEgg() {
        super(Material.field_151566_D, MapColor.field_151646_E);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDragonEgg.field_185660_a;
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.func_180683_d(world, blockposition);
    }

    private void func_180683_d(World world, BlockPos blockposition) {
        if (BlockFalling.func_185759_i(world.func_180495_p(blockposition.func_177977_b())) && blockposition.func_177956_o() >= 0) {
            boolean flag = true;

            if (!BlockFalling.field_149832_M && world.func_175707_a(blockposition.func_177982_a(-32, -32, -32), blockposition.func_177982_a(32, 32, 32))) {
                world.func_72838_d(new EntityFallingBlock(world, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) blockposition.func_177956_o(), (double) ((float) blockposition.func_177952_p() + 0.5F), this.func_176223_P()));
            } else {
                world.func_175698_g(blockposition);

                BlockPos blockposition1;

                for (blockposition1 = blockposition; BlockFalling.func_185759_i(world.func_180495_p(blockposition1)) && blockposition1.func_177956_o() > 0; blockposition1 = blockposition1.func_177977_b()) {
                    ;
                }

                if (blockposition1.func_177956_o() > 0) {
                    world.func_180501_a(blockposition1.func_177984_a(), this.func_176223_P(), 2); // Paper
                }
            }

        }
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        this.func_180684_e(world, blockposition);
        return true;
    }

    public void func_180649_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {
        this.func_180684_e(world, blockposition);
    }

    private void func_180684_e(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() == this) {
            for (int i = 0; i < 1000; ++i) {
                BlockPos blockposition1 = blockposition.func_177982_a(world.field_73012_v.nextInt(16) - world.field_73012_v.nextInt(16), world.field_73012_v.nextInt(8) - world.field_73012_v.nextInt(8), world.field_73012_v.nextInt(16) - world.field_73012_v.nextInt(16));

                if (world.func_180495_p(blockposition1).func_177230_c().field_149764_J == Material.field_151579_a) {
                    // CraftBukkit start
                    org.bukkit.block.Block from = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                    org.bukkit.block.Block to = world.getWorld().getBlockAt(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p());
                    BlockFromToEvent event = new BlockFromToEvent(from, to);
                    org.bukkit.Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }

                    blockposition1 = new BlockPos(event.getToBlock().getX(), event.getToBlock().getY(), event.getToBlock().getZ());
                    // CraftBukkit end
                    if (world.field_72995_K) {
                        for (int j = 0; j < 128; ++j) {
                            double d0 = world.field_73012_v.nextDouble();
                            float f = (world.field_73012_v.nextFloat() - 0.5F) * 0.2F;
                            float f1 = (world.field_73012_v.nextFloat() - 0.5F) * 0.2F;
                            float f2 = (world.field_73012_v.nextFloat() - 0.5F) * 0.2F;
                            double d1 = (double) blockposition1.func_177958_n() + (double) (blockposition.func_177958_n() - blockposition1.func_177958_n()) * d0 + (world.field_73012_v.nextDouble() - 0.5D) + 0.5D;
                            double d2 = (double) blockposition1.func_177956_o() + (double) (blockposition.func_177956_o() - blockposition1.func_177956_o()) * d0 + world.field_73012_v.nextDouble() - 0.5D;
                            double d3 = (double) blockposition1.func_177952_p() + (double) (blockposition.func_177952_p() - blockposition1.func_177952_p()) * d0 + (world.field_73012_v.nextDouble() - 0.5D) + 0.5D;

                            world.func_175688_a(EnumParticleTypes.PORTAL, d1, d2, d3, (double) f, (double) f1, (double) f2, new int[0]);
                        }
                    } else {
                        world.func_180501_a(blockposition1, iblockdata, 2);
                        world.func_175698_g(blockposition);
                    }

                    return;
                }
            }

        }
    }

    public int func_149738_a(World world) {
        return 5;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
