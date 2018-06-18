package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;


public class BlockPlanks extends Block {

    public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);

    public BlockPlanks() {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockPlanks.VARIANT)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockPlanks.EnumType[] ablockwood_enumlogvariant = BlockPlanks.EnumType.values();
        int i = ablockwood_enumlogvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockwood_enumlogvariant.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.byMetadata(i));
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockPlanks.VARIANT)).getMapColor();
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.getValue(BlockPlanks.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPlanks.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        OAK(0, "oak", MapColor.WOOD), SPRUCE(1, "spruce", MapColor.OBSIDIAN), BIRCH(2, "birch", MapColor.SAND), JUNGLE(3, "jungle", MapColor.DIRT), ACACIA(4, "acacia", MapColor.ADOBE), DARK_OAK(5, "dark_oak", "big_oak", MapColor.BROWN);

        private static final BlockPlanks.EnumType[] META_LOOKUP = new BlockPlanks.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor mapColor;

        private EnumType(int i, String s, MapColor materialmapcolor) {
            this(i, s, s, materialmapcolor);
        }

        private EnumType(int i, String s, String s1, MapColor materialmapcolor) {
            this.meta = i;
            this.name = s;
            this.unlocalizedName = s1;
            this.mapColor = materialmapcolor;
        }

        public int getMetadata() {
            return this.meta;
        }

        public MapColor getMapColor() {
            return this.mapColor;
        }

        public String toString() {
            return this.name;
        }

        public static BlockPlanks.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockPlanks.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockPlanks.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockPlanks.EnumType[] ablockwood_enumlogvariant = values();
            int i = ablockwood_enumlogvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

                BlockPlanks.EnumType.META_LOOKUP[blockwood_enumlogvariant.getMetadata()] = blockwood_enumlogvariant;
            }

        }
    }
}
