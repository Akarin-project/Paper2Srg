package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWeb extends Block {

    public BlockWeb() {
        super(Material.field_151569_G);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        entity.func_70110_aj();
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockWeb.field_185506_k;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151007_F;
    }

    protected boolean func_149700_E() {
        return true;
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        if (!world.field_72995_K && itemstack.func_77973_b() == Items.field_151097_aZ) {
            entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
            func_180635_a(world, blockposition, new ItemStack(Item.func_150898_a(this), 1));
        } else {
            super.func_180657_a(world, entityhuman, blockposition, iblockdata, tileentity, itemstack);
        }
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
