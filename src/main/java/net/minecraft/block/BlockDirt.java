package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockDirt extends Block {

    public static final PropertyEnum<BlockDirt.DirtType> VARIANT = PropertyEnum.create("variant", BlockDirt.DirtType.class);
    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    protected BlockDirt() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).withProperty(BlockDirt.SNOWY, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockDirt.DirtType) iblockdata.getValue(BlockDirt.VARIANT)).getColor();
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) {
            Block block = iblockaccess.getBlockState(blockposition.up()).getBlock();

            iblockdata = iblockdata.withProperty(BlockDirt.SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
        }

        return iblockdata;
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.getMetadata()));
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this, 1, ((BlockDirt.DirtType) iblockdata.getValue(BlockDirt.VARIANT)).getMetadata());
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockDirt.DirtType) iblockdata.getValue(BlockDirt.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockDirt.VARIANT, BlockDirt.SNOWY});
    }

    public int damageDropped(IBlockState iblockdata) {
        BlockDirt.DirtType blockdirt_enumdirtvariant = (BlockDirt.DirtType) iblockdata.getValue(BlockDirt.VARIANT);

        if (blockdirt_enumdirtvariant == BlockDirt.DirtType.PODZOL) {
            blockdirt_enumdirtvariant = BlockDirt.DirtType.DIRT;
        }

        return blockdirt_enumdirtvariant.getMetadata();
    }

    public static enum DirtType implements IStringSerializable {

        DIRT(0, "dirt", "default", MapColor.DIRT), COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.DIRT), PODZOL(2, "podzol", MapColor.OBSIDIAN);

        private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;

        private DirtType(int i, String s, MapColor materialmapcolor) {
            this(i, s, s, materialmapcolor);
        }

        private DirtType(int i, String s, String s1, MapColor materialmapcolor) {
            this.metadata = i;
            this.name = s;
            this.unlocalizedName = s1;
            this.color = materialmapcolor;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public MapColor getColor() {
            return this.color;
        }

        public String toString() {
            return this.name;
        }

        public static BlockDirt.DirtType byMetadata(int i) {
            if (i < 0 || i >= BlockDirt.DirtType.METADATA_LOOKUP.length) {
                i = 0;
            }

            return BlockDirt.DirtType.METADATA_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        static {
            BlockDirt.DirtType[] ablockdirt_enumdirtvariant = values();
            int i = ablockdirt_enumdirtvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockDirt.DirtType blockdirt_enumdirtvariant = ablockdirt_enumdirtvariant[j];

                BlockDirt.DirtType.METADATA_LOOKUP[blockdirt_enumdirtvariant.getMetadata()] = blockdirt_enumdirtvariant;
            }

        }
    }
}
