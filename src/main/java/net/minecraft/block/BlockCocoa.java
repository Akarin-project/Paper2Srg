package net.minecraft.block;

import java.util.Random;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class BlockCocoa extends BlockHorizontal implements IGrowable {

    public static final PropertyInteger field_176501_a = PropertyInteger.func_177719_a("age", 0, 2);
    protected static final AxisAlignedBB[] field_185535_b = new AxisAlignedBB[] { new AxisAlignedBB(0.6875D, 0.4375D, 0.375D, 0.9375D, 0.75D, 0.625D), new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 0.9375D, 0.75D, 0.6875D), new AxisAlignedBB(0.4375D, 0.1875D, 0.25D, 0.9375D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] field_185536_c = new AxisAlignedBB[] { new AxisAlignedBB(0.0625D, 0.4375D, 0.375D, 0.3125D, 0.75D, 0.625D), new AxisAlignedBB(0.0625D, 0.3125D, 0.3125D, 0.4375D, 0.75D, 0.6875D), new AxisAlignedBB(0.0625D, 0.1875D, 0.25D, 0.5625D, 0.75D, 0.75D)};
    protected static final AxisAlignedBB[] field_185537_d = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.4375D, 0.0625D, 0.625D, 0.75D, 0.3125D), new AxisAlignedBB(0.3125D, 0.3125D, 0.0625D, 0.6875D, 0.75D, 0.4375D), new AxisAlignedBB(0.25D, 0.1875D, 0.0625D, 0.75D, 0.75D, 0.5625D)};
    protected static final AxisAlignedBB[] field_185538_e = new AxisAlignedBB[] { new AxisAlignedBB(0.375D, 0.4375D, 0.6875D, 0.625D, 0.75D, 0.9375D), new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.75D, 0.9375D), new AxisAlignedBB(0.25D, 0.1875D, 0.4375D, 0.75D, 0.75D, 0.9375D)};

    public BlockCocoa() {
        super(Material.field_151585_k);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockCocoa.field_185512_D, EnumFacing.NORTH).func_177226_a(BlockCocoa.field_176501_a, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.func_176499_e(world, blockposition, iblockdata)) {
            this.func_176500_f(world, blockposition, iblockdata);
        } else if (world.field_73012_v.nextInt(Math.max(1, (int) (100.0F / world.spigotConfig.cocoaModifier) * 5)) == 0) { // Spigot
            int i = ((Integer) iblockdata.func_177229_b(BlockCocoa.field_176501_a)).intValue();

            if (i < 2) {
                // CraftBukkit start
                IBlockState data = iblockdata.func_177226_a(field_176501_a, Integer.valueOf(i + 1));
                CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, func_176201_c(data));
                // CraftBukkit end
            }
        }

    }

    public boolean func_176499_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        blockposition = blockposition.func_177972_a((EnumFacing) iblockdata.func_177229_b(BlockCocoa.field_185512_D));
        IBlockState iblockdata1 = world.func_180495_p(blockposition);

        return iblockdata1.func_177230_c() == Blocks.field_150364_r && iblockdata1.func_177229_b(BlockOldLog.field_176301_b) == BlockPlanks.EnumType.JUNGLE;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.func_177229_b(BlockCocoa.field_176501_a)).intValue();

        switch ((EnumFacing) iblockdata.func_177229_b(BlockCocoa.field_185512_D)) {
        case SOUTH:
            return BlockCocoa.field_185538_e[i];

        case NORTH:
        default:
            return BlockCocoa.field_185537_d[i];

        case WEST:
            return BlockCocoa.field_185536_c[i];

        case EAST:
            return BlockCocoa.field_185535_b[i];
        }
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockCocoa.field_185512_D, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockCocoa.field_185512_D)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockCocoa.field_185512_D)));
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        EnumFacing enumdirection = EnumFacing.func_176733_a((double) entityliving.field_70177_z);

        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCocoa.field_185512_D, enumdirection), 2);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (!enumdirection.func_176740_k().func_176722_c()) {
            enumdirection = EnumFacing.NORTH;
        }

        return this.func_176223_P().func_177226_a(BlockCocoa.field_185512_D, enumdirection.func_176734_d()).func_177226_a(BlockCocoa.field_176501_a, Integer.valueOf(0));
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_176499_e(world, blockposition, iblockdata)) {
            this.func_176500_f(world, blockposition, iblockdata);
        }

    }

    private void func_176500_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 3);
        this.func_176226_b(world, blockposition, iblockdata, 0);
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        int j = ((Integer) iblockdata.func_177229_b(BlockCocoa.field_176501_a)).intValue();
        byte b0 = 1;

        if (j >= 2) {
            b0 = 3;
        }

        for (int k = 0; k < b0; ++k) {
            func_180635_a(world, blockposition, new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BROWN.func_176767_b()));
        }

    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151100_aR, 1, EnumDyeColor.BROWN.func_176767_b());
    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return ((Integer) iblockdata.func_177229_b(BlockCocoa.field_176501_a)).intValue() < 2;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        // CraftBukkit start
        IBlockState data = iblockdata.func_177226_a(field_176501_a, Integer.valueOf(((Integer) iblockdata.func_177229_b(field_176501_a)).intValue() + 1));
        CraftEventFactory.handleBlockGrowEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this, func_176201_c(data));
        // CraftBukkit end
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockCocoa.field_185512_D, EnumFacing.func_176731_b(i)).func_177226_a(BlockCocoa.field_176501_a, Integer.valueOf((i & 15) >> 2));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockCocoa.field_185512_D)).func_176736_b();

        i |= ((Integer) iblockdata.func_177229_b(BlockCocoa.field_176501_a)).intValue() << 2;
        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCocoa.field_185512_D, BlockCocoa.field_176501_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
