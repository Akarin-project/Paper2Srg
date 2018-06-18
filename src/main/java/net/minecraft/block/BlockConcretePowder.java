package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockConcretePowder extends BlockFalling {

    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public BlockConcretePowder() {
        super(Material.SAND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.WHITE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public void onEndFalling(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {
        if (iblockdata1.getMaterial().isLiquid() && world.getBlockState(blockposition).getBlock() != Blocks.CONCRETE) { // CraftBukkit - don't double concrete
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, iblockdata.getValue(BlockConcretePowder.COLOR)), null); // CraftBukkit
        }

    }

    protected boolean tryTouchWater(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = false;
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (enumdirection != EnumFacing.DOWN) {
                BlockPos blockposition1 = blockposition.offset(enumdirection);

                if (world.getBlockState(blockposition1).getMaterial() == Material.WATER) {
                    flag = true;
                    break;
                }
            }
        }

        if (flag) {
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, iblockdata.getValue(BlockConcretePowder.COLOR)), null); // CraftBukkit
        }

        return flag;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!this.tryTouchWater(world, blockposition, iblockdata)) {
            super.neighborChanged(iblockdata, world, blockposition, block, blockposition1);
        }

    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.tryTouchWater(world, blockposition, iblockdata)) {
            super.onBlockAdded(world, blockposition, iblockdata);
        }

    }

    public int damageDropped(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.getValue(BlockConcretePowder.COLOR)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        EnumDyeColor[] aenumcolor = EnumDyeColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            nonnulllist.add(new ItemStack(this, 1, enumcolor.getMetadata()));
        }

    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.getBlockColor((EnumDyeColor) iblockdata.getValue(BlockConcretePowder.COLOR));
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumDyeColor) iblockdata.getValue(BlockConcretePowder.COLOR)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockConcretePowder.COLOR});
    }
}
