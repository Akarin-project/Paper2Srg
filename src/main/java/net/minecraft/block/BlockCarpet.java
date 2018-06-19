package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockCarpet extends Block {

    public static final PropertyEnum<EnumDyeColor> field_176330_a = PropertyEnum.func_177709_a("color", EnumDyeColor.class);
    protected static final AxisAlignedBB field_185758_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    protected BlockCarpet() {
        super(Material.field_151593_r);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockCarpet.field_176330_a, EnumDyeColor.WHITE));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCarpet.field_185758_b;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.func_193558_a((EnumDyeColor) iblockdata.func_177229_b(BlockCarpet.field_176330_a));
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return super.func_176196_c(world, blockposition) && this.func_176329_d(world, blockposition);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176328_e(world, blockposition, iblockdata);
    }

    private boolean func_176328_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_176329_d(world, blockposition)) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            return false;
        } else {
            return true;
        }
    }

    private boolean func_176329_d(World world, BlockPos blockposition) {
        return !world.func_175623_d(blockposition.func_177977_b());
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockCarpet.field_176330_a)).func_176765_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        for (int i = 0; i < 16; ++i) {
            nonnulllist.add(new ItemStack(this, 1, i));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockCarpet.field_176330_a, EnumDyeColor.func_176764_b(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.func_177229_b(BlockCarpet.field_176330_a)).func_176765_a();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCarpet.field_176330_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
