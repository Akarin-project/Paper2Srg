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

    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public BlockStainedGlassPane() {
        super(Material.GLASS, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStainedGlassPane.NORTH, Boolean.valueOf(false)).withProperty(BlockStainedGlassPane.EAST, Boolean.valueOf(false)).withProperty(BlockStainedGlassPane.SOUTH, Boolean.valueOf(false)).withProperty(BlockStainedGlassPane.WEST, Boolean.valueOf(false)).withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.getValue(BlockStainedGlassPane.COLOR)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        for (int i = 0; i < EnumDyeColor.values().length; ++i) {
            nonnulllist.add(new ItemStack(this, 1, i));
        }

    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.getBlockColor((EnumDyeColor) iblockdata.getValue(BlockStainedGlassPane.COLOR));
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.getValue(BlockStainedGlassPane.COLOR)).getMetadata();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.withProperty(BlockStainedGlassPane.NORTH, iblockdata.getValue(BlockStainedGlassPane.SOUTH)).withProperty(BlockStainedGlassPane.EAST, iblockdata.getValue(BlockStainedGlassPane.WEST)).withProperty(BlockStainedGlassPane.SOUTH, iblockdata.getValue(BlockStainedGlassPane.NORTH)).withProperty(BlockStainedGlassPane.WEST, iblockdata.getValue(BlockStainedGlassPane.EAST));

        case COUNTERCLOCKWISE_90:
            return iblockdata.withProperty(BlockStainedGlassPane.NORTH, iblockdata.getValue(BlockStainedGlassPane.EAST)).withProperty(BlockStainedGlassPane.EAST, iblockdata.getValue(BlockStainedGlassPane.SOUTH)).withProperty(BlockStainedGlassPane.SOUTH, iblockdata.getValue(BlockStainedGlassPane.WEST)).withProperty(BlockStainedGlassPane.WEST, iblockdata.getValue(BlockStainedGlassPane.NORTH));

        case CLOCKWISE_90:
            return iblockdata.withProperty(BlockStainedGlassPane.NORTH, iblockdata.getValue(BlockStainedGlassPane.WEST)).withProperty(BlockStainedGlassPane.EAST, iblockdata.getValue(BlockStainedGlassPane.NORTH)).withProperty(BlockStainedGlassPane.SOUTH, iblockdata.getValue(BlockStainedGlassPane.EAST)).withProperty(BlockStainedGlassPane.WEST, iblockdata.getValue(BlockStainedGlassPane.SOUTH));

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.withProperty(BlockStainedGlassPane.NORTH, iblockdata.getValue(BlockStainedGlassPane.SOUTH)).withProperty(BlockStainedGlassPane.SOUTH, iblockdata.getValue(BlockStainedGlassPane.NORTH));

        case FRONT_BACK:
            return iblockdata.withProperty(BlockStainedGlassPane.EAST, iblockdata.getValue(BlockStainedGlassPane.WEST)).withProperty(BlockStainedGlassPane.WEST, iblockdata.getValue(BlockStainedGlassPane.EAST));

        default:
            return super.withMirror(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockStainedGlassPane.NORTH, BlockStainedGlassPane.EAST, BlockStainedGlassPane.WEST, BlockStainedGlassPane.SOUTH, BlockStainedGlassPane.COLOR});
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isRemote) {
            BlockBeacon.updateColorAsync(world, blockposition);
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.isRemote) {
            BlockBeacon.updateColorAsync(world, blockposition);
        }

    }
}
