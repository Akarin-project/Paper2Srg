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

    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    protected BlockCarpet() {
        super(Material.CARPET);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCarpet.COLOR, EnumDyeColor.WHITE));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCarpet.CARPET_AABB;
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.getBlockColor((EnumDyeColor) iblockdata.getValue(BlockCarpet.COLOR));
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return super.canPlaceBlockAt(world, blockposition) && this.canBlockStay(world, blockposition);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.checkForDrop(world, blockposition, iblockdata);
    }

    private boolean checkForDrop(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.canBlockStay(world, blockposition)) {
            this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            world.setBlockToAir(blockposition);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World world, BlockPos blockposition) {
        return !world.isAirBlock(blockposition.down());
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.getValue(BlockCarpet.COLOR)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        for (int i = 0; i < 16; ++i) {
            nonnulllist.add(new ItemStack(this, 1, i));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.getValue(BlockCarpet.COLOR)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockCarpet.COLOR});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
