package net.minecraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;


public class BlockStoneBrick extends Block {

    public static final PropertyEnum<BlockStoneBrick.EnumType> VARIANT = PropertyEnum.create("variant", BlockStoneBrick.EnumType.class);
    public static final int DEFAULT_META = BlockStoneBrick.EnumType.DEFAULT.getMetadata();
    public static final int MOSSY_META = BlockStoneBrick.EnumType.MOSSY.getMetadata();
    public static final int CRACKED_META = BlockStoneBrick.EnumType.CRACKED.getMetadata();
    public static final int CHISELED_META = BlockStoneBrick.EnumType.CHISELED.getMetadata();

    public BlockStoneBrick() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockStoneBrick.EnumType) iblockdata.getValue(BlockStoneBrick.VARIANT)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStoneBrick.EnumType[] ablocksmoothbrick_enumstonebricktype = BlockStoneBrick.EnumType.values();
        int i = ablocksmoothbrick_enumstonebricktype.length;

        for (int j = 0; j < i; ++j) {
            BlockStoneBrick.EnumType blocksmoothbrick_enumstonebricktype = ablocksmoothbrick_enumstonebricktype[j];

            nonnulllist.add(new ItemStack(this, 1, blocksmoothbrick_enumstonebricktype.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockStoneBrick.EnumType) iblockdata.getValue(BlockStoneBrick.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockStoneBrick.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "stonebrick", "default"), MOSSY(1, "mossy_stonebrick", "mossy"), CRACKED(2, "cracked_stonebrick", "cracked"), CHISELED(3, "chiseled_stonebrick", "chiseled");

        private static final BlockStoneBrick.EnumType[] META_LOOKUP = new BlockStoneBrick.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int i, String s, String s1) {
            this.meta = i;
            this.name = s;
            this.unlocalizedName = s1;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static BlockStoneBrick.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockStoneBrick.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockStoneBrick.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockStoneBrick.EnumType[] ablocksmoothbrick_enumstonebricktype = values();
            int i = ablocksmoothbrick_enumstonebricktype.length;

            for (int j = 0; j < i; ++j) {
                BlockStoneBrick.EnumType blocksmoothbrick_enumstonebricktype = ablocksmoothbrick_enumstonebricktype[j];

                BlockStoneBrick.EnumType.META_LOOKUP[blocksmoothbrick_enumstonebricktype.getMetadata()] = blocksmoothbrick_enumstonebricktype;
            }

        }
    }
}
