package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneRepeater extends BlockRedstoneDiode {

    public static final PropertyBool field_176411_a = PropertyBool.func_177716_a("locked");
    public static final PropertyInteger field_176410_b = PropertyInteger.func_177719_a("delay", 1, 4);

    protected BlockRedstoneRepeater(boolean flag) {
        super(flag);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRedstoneRepeater.field_185512_D, EnumFacing.NORTH).func_177226_a(BlockRedstoneRepeater.field_176410_b, Integer.valueOf(1)).func_177226_a(BlockRedstoneRepeater.field_176411_a, Boolean.valueOf(false)));
    }

    public String func_149732_F() {
        return I18n.func_74838_a("item.diode.name");
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177226_a(BlockRedstoneRepeater.field_176411_a, Boolean.valueOf(this.func_176405_b(iblockaccess, blockposition, iblockdata)));
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockRedstoneRepeater.field_185512_D, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockRedstoneRepeater.field_185512_D)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockRedstoneRepeater.field_185512_D)));
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!entityhuman.field_71075_bZ.field_75099_e) {
            return false;
        } else {
            world.func_180501_a(blockposition, iblockdata.func_177231_a((IProperty) BlockRedstoneRepeater.field_176410_b), 3);
            return true;
        }
    }

    protected int func_176403_d(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockRedstoneRepeater.field_176410_b)).intValue() * 2;
    }

    protected IBlockState func_180674_e(IBlockState iblockdata) {
        Integer integer = (Integer) iblockdata.func_177229_b(BlockRedstoneRepeater.field_176410_b);
        Boolean obool = (Boolean) iblockdata.func_177229_b(BlockRedstoneRepeater.field_176411_a);
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneRepeater.field_185512_D);

        return Blocks.field_150416_aS.func_176223_P().func_177226_a(BlockRedstoneRepeater.field_185512_D, enumdirection).func_177226_a(BlockRedstoneRepeater.field_176410_b, integer).func_177226_a(BlockRedstoneRepeater.field_176411_a, obool);
    }

    protected IBlockState func_180675_k(IBlockState iblockdata) {
        Integer integer = (Integer) iblockdata.func_177229_b(BlockRedstoneRepeater.field_176410_b);
        Boolean obool = (Boolean) iblockdata.func_177229_b(BlockRedstoneRepeater.field_176411_a);
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneRepeater.field_185512_D);

        return Blocks.field_150413_aR.func_176223_P().func_177226_a(BlockRedstoneRepeater.field_185512_D, enumdirection).func_177226_a(BlockRedstoneRepeater.field_176410_b, integer).func_177226_a(BlockRedstoneRepeater.field_176411_a, obool);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151107_aW;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151107_aW);
    }

    public boolean func_176405_b(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        return this.func_176407_c(iblockaccess, blockposition, iblockdata) > 0;
    }

    protected boolean func_185545_A(IBlockState iblockdata) {
        return func_185546_B(iblockdata);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        this.func_176400_h(world, blockposition, iblockdata);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRedstoneRepeater.field_185512_D, EnumFacing.func_176731_b(i)).func_177226_a(BlockRedstoneRepeater.field_176411_a, Boolean.valueOf(false)).func_177226_a(BlockRedstoneRepeater.field_176410_b, Integer.valueOf(1 + (i >> 2)));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockRedstoneRepeater.field_185512_D)).func_176736_b();

        i |= ((Integer) iblockdata.func_177229_b(BlockRedstoneRepeater.field_176410_b)).intValue() - 1 << 2;
        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedstoneRepeater.field_185512_D, BlockRedstoneRepeater.field_176410_b, BlockRedstoneRepeater.field_176411_a});
    }
}
