package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChorusPlant extends Block {

    public static final PropertyBool field_185609_a = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_185610_b = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_185611_c = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_185612_d = PropertyBool.func_177716_a("west");
    public static final PropertyBool field_185613_e = PropertyBool.func_177716_a("up");
    public static final PropertyBool field_185614_f = PropertyBool.func_177716_a("down");

    protected BlockChorusPlant() {
        super(Material.field_151585_k, MapColor.field_151678_z);
        this.func_149647_a(CreativeTabs.field_78031_c);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockChorusPlant.field_185609_a, Boolean.valueOf(false)).func_177226_a(BlockChorusPlant.field_185610_b, Boolean.valueOf(false)).func_177226_a(BlockChorusPlant.field_185611_c, Boolean.valueOf(false)).func_177226_a(BlockChorusPlant.field_185612_d, Boolean.valueOf(false)).func_177226_a(BlockChorusPlant.field_185613_e, Boolean.valueOf(false)).func_177226_a(BlockChorusPlant.field_185614_f, Boolean.valueOf(false)));
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block block = iblockaccess.func_180495_p(blockposition.func_177977_b()).func_177230_c();
        Block block1 = iblockaccess.func_180495_p(blockposition.func_177984_a()).func_177230_c();
        Block block2 = iblockaccess.func_180495_p(blockposition.func_177978_c()).func_177230_c();
        Block block3 = iblockaccess.func_180495_p(blockposition.func_177974_f()).func_177230_c();
        Block block4 = iblockaccess.func_180495_p(blockposition.func_177968_d()).func_177230_c();
        Block block5 = iblockaccess.func_180495_p(blockposition.func_177976_e()).func_177230_c();

        return iblockdata.func_177226_a(BlockChorusPlant.field_185614_f, Boolean.valueOf(block == this || block == Blocks.field_185766_cS || block == Blocks.field_150377_bs)).func_177226_a(BlockChorusPlant.field_185613_e, Boolean.valueOf(block1 == this || block1 == Blocks.field_185766_cS)).func_177226_a(BlockChorusPlant.field_185609_a, Boolean.valueOf(block2 == this || block2 == Blocks.field_185766_cS)).func_177226_a(BlockChorusPlant.field_185610_b, Boolean.valueOf(block3 == this || block3 == Blocks.field_185766_cS)).func_177226_a(BlockChorusPlant.field_185611_c, Boolean.valueOf(block4 == this || block4 == Blocks.field_185766_cS)).func_177226_a(BlockChorusPlant.field_185612_d, Boolean.valueOf(block5 == this || block5 == Blocks.field_185766_cS));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = iblockdata.func_185899_b(iblockaccess, blockposition);
        float f = 0.1875F;
        float f1 = ((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185612_d)).booleanValue() ? 0.0F : 0.1875F;
        float f2 = ((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185614_f)).booleanValue() ? 0.0F : 0.1875F;
        float f3 = ((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185609_a)).booleanValue() ? 0.0F : 0.1875F;
        float f4 = ((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185610_b)).booleanValue() ? 1.0F : 0.8125F;
        float f5 = ((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185613_e)).booleanValue() ? 1.0F : 0.8125F;
        float f6 = ((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185611_c)).booleanValue() ? 1.0F : 0.8125F;

        return new AxisAlignedBB((double) f1, (double) f2, (double) f3, (double) f4, (double) f5, (double) f6);
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = iblockdata.func_185899_b(world, blockposition);
        }

        float f = 0.1875F;
        float f1 = 0.8125F;

        func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D));
        if (((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185612_d)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.0D, 0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D));
        }

        if (((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185610_b)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.8125D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D));
        }

        if (((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185613_e)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.8125D, 0.1875D, 0.8125D, 1.0D, 0.8125D));
        }

        if (((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185614_f)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.1875D, 0.8125D));
        }

        if (((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185609_a)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D));
        }

        if (((Boolean) iblockdata.func_177229_b(BlockChorusPlant.field_185611_c)).booleanValue()) {
            func_185492_a(blockposition, axisalignedbb, list, new AxisAlignedBB(0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D));
        }

    }

    public int func_176201_c(IBlockState iblockdata) {
        return 0;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!this.func_185608_b(world, blockposition)) {
            world.func_175655_b(blockposition, true);
        }

    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_185161_cS;
    }

    public int func_149745_a(Random random) {
        return random.nextInt(2);
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) ? this.func_185608_b(world, blockposition) : false;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.func_185608_b(world, blockposition)) {
            world.func_175684_a(blockposition, (Block) this, 1);
        }

    }

    public boolean func_185608_b(World world, BlockPos blockposition) {
        boolean flag = world.func_175623_d(blockposition.func_177984_a());
        boolean flag1 = world.func_175623_d(blockposition.func_177977_b());
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        Block block;

        do {
            BlockPos blockposition1;
            Block block1;

            do {
                if (!iterator.hasNext()) {
                    Block block2 = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();

                    return block2 == this || block2 == Blocks.field_150377_bs;
                }

                EnumFacing enumdirection = (EnumFacing) iterator.next();

                blockposition1 = blockposition.func_177972_a(enumdirection);
                block1 = world.func_180495_p(blockposition1).func_177230_c();
            } while (block1 != this);

            if (!flag && !flag1) {
                return false;
            }

            block = world.func_180495_p(blockposition1.func_177977_b()).func_177230_c();
        } while (block != this && block != Blocks.field_150377_bs);

        return true;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockChorusPlant.field_185609_a, BlockChorusPlant.field_185610_b, BlockChorusPlant.field_185611_c, BlockChorusPlant.field_185612_d, BlockChorusPlant.field_185613_e, BlockChorusPlant.field_185614_f});
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
