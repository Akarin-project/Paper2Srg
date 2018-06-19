package net.minecraft.block;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSnow extends Block {

    public static final PropertyInteger field_176315_a = PropertyInteger.func_177719_a("layers", 1, 8);
    protected static final AxisAlignedBB[] field_185702_b = new AxisAlignedBB[] { new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    protected BlockSnow() {
        super(Material.field_151597_y);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSnow.field_176315_a, Integer.valueOf(1)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockSnow.field_185702_b[((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue()];
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Integer) iblockaccess.func_180495_p(blockposition).func_177229_b(BlockSnow.field_176315_a)).intValue() < 5;
    }

    public boolean func_185481_k(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue() == 8;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue() - 1;
        float f = 0.125F;
        AxisAlignedBB axisalignedbb = iblockdata.func_185900_c(iblockaccess, blockposition);

        return new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c, axisalignedbb.field_72336_d, (double) ((float) i * 0.125F), axisalignedbb.field_72334_f);
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition.func_177977_b());
        Block block = iblockdata.func_177230_c();

        if (block != Blocks.field_150432_aD && block != Blocks.field_150403_cj && block != Blocks.field_180401_cv) {
            BlockFaceShape enumblockfaceshape = iblockdata.func_193401_d(world, blockposition.func_177977_b(), EnumFacing.UP);

            return enumblockfaceshape == BlockFaceShape.SOLID || iblockdata.func_185904_a() == Material.field_151584_j || block == this && ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue() == 8;
        } else {
            return false;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176314_e(world, blockposition, iblockdata);
    }

    private boolean func_176314_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_176196_c(world, blockposition)) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            return false;
        } else {
            return true;
        }
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        func_180635_a(world, blockposition, new ItemStack(Items.field_151126_ay, ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue() + 1, 0));
        world.func_175698_g(blockposition);
        entityhuman.func_71029_a(StatList.func_188055_a((Block) this));
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151126_ay;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.func_175642_b(EnumSkyBlock.BLOCK, blockposition) > 11) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), Blocks.field_150350_a).isCancelled()) {
                return;
            }
            // CraftBukkit end
            this.func_176226_b(world, blockposition, world.func_180495_p(blockposition), 0);
            world.func_175698_g(blockposition);
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSnow.field_176315_a, Integer.valueOf((i & 7) + 1));
    }

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((Integer) iblockaccess.func_180495_p(blockposition).func_177229_b(BlockSnow.field_176315_a)).intValue() == 1;
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockSnow.field_176315_a)).intValue() - 1;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSnow.field_176315_a});
    }
}
