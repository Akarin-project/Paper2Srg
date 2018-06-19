package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockStainedGlassPane extends BlockPane {

    public static final PropertyEnum<EnumDyeColor> field_176245_a = PropertyEnum.func_177709_a("color", EnumDyeColor.class);

    public BlockStainedGlassPane() {
        super(Material.field_151592_s, false);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockStainedGlassPane.field_176241_b, Boolean.valueOf(false)).func_177226_a(BlockStainedGlassPane.field_176242_M, Boolean.valueOf(false)).func_177226_a(BlockStainedGlassPane.field_176243_N, Boolean.valueOf(false)).func_177226_a(BlockStainedGlassPane.field_176244_O, Boolean.valueOf(false)).func_177226_a(BlockStainedGlassPane.field_176245_a, EnumDyeColor.WHITE));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockStainedGlassPane.field_176245_a)).func_176765_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            nonnulllist.add(new ItemStack(this, 1, i));
        }

    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.func_193558_a((EnumDyeColor) iblockdata.func_177229_b(BlockStainedGlassPane.field_176245_a));
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockStainedGlassPane.field_176245_a, EnumDyeColor.func_176764_b(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockStainedGlassPane.field_176245_a)).func_176765_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.func_177226_a(BlockStainedGlassPane.field_176241_b, iblockdata.func_177229_b(BlockStainedGlassPane.field_176243_N)).func_177226_a(BlockStainedGlassPane.field_176242_M, iblockdata.func_177229_b(BlockStainedGlassPane.field_176244_O)).func_177226_a(BlockStainedGlassPane.field_176243_N, iblockdata.func_177229_b(BlockStainedGlassPane.field_176241_b)).func_177226_a(BlockStainedGlassPane.field_176244_O, iblockdata.func_177229_b(BlockStainedGlassPane.field_176242_M));

        case COUNTERCLOCKWISE_90:
            return iblockdata.func_177226_a(BlockStainedGlassPane.field_176241_b, iblockdata.func_177229_b(BlockStainedGlassPane.field_176242_M)).func_177226_a(BlockStainedGlassPane.field_176242_M, iblockdata.func_177229_b(BlockStainedGlassPane.field_176243_N)).func_177226_a(BlockStainedGlassPane.field_176243_N, iblockdata.func_177229_b(BlockStainedGlassPane.field_176244_O)).func_177226_a(BlockStainedGlassPane.field_176244_O, iblockdata.func_177229_b(BlockStainedGlassPane.field_176241_b));

        case CLOCKWISE_90:
            return iblockdata.func_177226_a(BlockStainedGlassPane.field_176241_b, iblockdata.func_177229_b(BlockStainedGlassPane.field_176244_O)).func_177226_a(BlockStainedGlassPane.field_176242_M, iblockdata.func_177229_b(BlockStainedGlassPane.field_176241_b)).func_177226_a(BlockStainedGlassPane.field_176243_N, iblockdata.func_177229_b(BlockStainedGlassPane.field_176242_M)).func_177226_a(BlockStainedGlassPane.field_176244_O, iblockdata.func_177229_b(BlockStainedGlassPane.field_176243_N));

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.func_177226_a(BlockStainedGlassPane.field_176241_b, iblockdata.func_177229_b(BlockStainedGlassPane.field_176243_N)).func_177226_a(BlockStainedGlassPane.field_176243_N, iblockdata.func_177229_b(BlockStainedGlassPane.field_176241_b));

        case FRONT_BACK:
            return iblockdata.func_177226_a(BlockStainedGlassPane.field_176242_M, iblockdata.func_177229_b(BlockStainedGlassPane.field_176244_O)).func_177226_a(BlockStainedGlassPane.field_176244_O, iblockdata.func_177229_b(BlockStainedGlassPane.field_176242_M));

        default:
            return super.func_185471_a(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockStainedGlassPane.field_176241_b, BlockStainedGlassPane.field_176242_M, BlockStainedGlassPane.field_176244_O, BlockStainedGlassPane.field_176243_N, BlockStainedGlassPane.field_176245_a});
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            BlockBeacon.func_176450_d(world, blockposition);
        }

    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            BlockBeacon.func_176450_d(world, blockposition);
        }

    }
}
