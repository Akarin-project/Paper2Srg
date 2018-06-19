package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block {

    public static final PropertyInteger field_176589_a = PropertyInteger.func_177719_a("bites", 0, 6);
    protected static final AxisAlignedBB[] field_185595_b = new AxisAlignedBB[] { new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)};

    protected BlockCake() {
        super(Material.field_151568_F);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockCake.field_176589_a, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCake.field_185595_b[((Integer) iblockdata.func_177229_b(BlockCake.field_176589_a)).intValue()];
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.field_72995_K) {
            return this.func_180682_b(world, blockposition, iblockdata, entityhuman);
        } else {
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            return this.func_180682_b(world, blockposition, iblockdata, entityhuman) || itemstack.func_190926_b();
        }
    }

    private boolean func_180682_b(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (!entityhuman.func_71043_e(false)) {
            return false;
        } else {
            entityhuman.func_71029_a(StatList.field_188076_J);
            // CraftBukkit start
            // entityhuman.getFoodData().eat(2, 0.1F);
            int oldFoodLevel = entityhuman.func_71024_bL().field_75127_a;

            org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, 2 + oldFoodLevel);

            if (!event.isCancelled()) {
                entityhuman.func_71024_bL().func_75122_a(event.getFoodLevel() - oldFoodLevel, 0.1F);
            }

            ((EntityPlayerMP) entityhuman).getBukkitEntity().sendHealthUpdate();
            // CraftBukkit end
            int i = ((Integer) iblockdata.func_177229_b(BlockCake.field_176589_a)).intValue();

            if (i < 6) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCake.field_176589_a, Integer.valueOf(i + 1)), 3);
            } else {
                world.func_175698_g(blockposition);
            }

            return true;
        }
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) ? this.func_176588_d(world, blockposition) : false;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_176588_d(world, blockposition)) {
            world.func_175698_g(blockposition);
        }

    }

    private boolean func_176588_d(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185904_a().func_76220_a();
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151105_aU);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockCake.field_176589_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockCake.field_176589_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCake.field_176589_a});
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return (7 - ((Integer) iblockdata.func_177229_b(BlockCake.field_176589_a)).intValue()) * 2;
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
