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
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;


public class BlockColored extends Block {

    public static final PropertyEnum<EnumDyeColor> field_176581_a = PropertyEnum.func_177709_a("color", EnumDyeColor.class);

    public BlockColored(Material material) {
        super(material);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.WHITE));
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockColored.field_176581_a)).func_176765_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        EnumDyeColor[] aenumcolor = EnumDyeColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            nonnulllist.add(new ItemStack(this, 1, enumcolor.func_176765_a()));
        }

    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.func_193558_a((EnumDyeColor) iblockdata.func_177229_b(BlockColored.field_176581_a));
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.func_176764_b(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockColored.field_176581_a)).func_176765_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockColored.field_176581_a});
    }
}
