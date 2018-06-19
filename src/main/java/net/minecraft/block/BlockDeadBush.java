package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDeadBush extends BlockBush {

    protected static final AxisAlignedBB field_185516_a = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    protected BlockDeadBush() {
        super(Material.field_151582_l);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockDeadBush.field_185516_a;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151663_o;
    }

    protected boolean func_185514_i(IBlockState iblockdata) {
        return iblockdata.func_177230_c() == Blocks.field_150354_m || iblockdata.func_177230_c() == Blocks.field_150405_ch || iblockdata.func_177230_c() == Blocks.field_150406_ce || iblockdata.func_177230_c() == Blocks.field_150346_d;
    }

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public int func_149745_a(Random random) {
        return random.nextInt(3);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151055_y;
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.field_72995_K && itemstack.func_77973_b() == Items.field_151097_aZ) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            func_180635_a(world, blockposition, new ItemStack(Blocks.field_150330_I, 1, 0));
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }

    }
}
