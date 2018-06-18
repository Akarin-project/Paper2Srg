package net.minecraft.block;
import net.minecraft.block.material.MapColor;
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


public class BlockSand extends BlockFalling {

    public static final PropertyEnum<BlockSand.EnumType> VARIANT = PropertyEnum.create("variant", BlockSand.EnumType.class);

    public BlockSand() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND));
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockSand.EnumType) iblockdata.getValue(BlockSand.VARIANT)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockSand.EnumType[] ablocksand_enumsandvariant = BlockSand.EnumType.values();
        int i = ablocksand_enumsandvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockSand.EnumType blocksand_enumsandvariant = ablocksand_enumsandvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blocksand_enumsandvariant.getMetadata()));
        }

    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockSand.EnumType) iblockdata.getValue(BlockSand.VARIANT)).getMapColor();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockSand.EnumType) iblockdata.getValue(BlockSand.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockSand.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        SAND(0, "sand", "default", MapColor.SAND, -2370656), RED_SAND(1, "red_sand", "red", MapColor.ADOBE, -5679071);

        private static final BlockSand.EnumType[] META_LOOKUP = new BlockSand.EnumType[values().length];
        private final int meta;
        private final String name;
        private final MapColor mapColor;
        private final String unlocalizedName;
        private final int dustColor;

        private EnumType(int i, String s, String s1, MapColor materialmapcolor, int j) {
            this.meta = i;
            this.name = s;
            this.mapColor = materialmapcolor;
            this.unlocalizedName = s1;
            this.dustColor = j;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public MapColor getMapColor() {
            return this.mapColor;
        }

        public static BlockSand.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockSand.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockSand.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockSand.EnumType[] ablocksand_enumsandvariant = values();
            int i = ablocksand_enumsandvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockSand.EnumType blocksand_enumsandvariant = ablocksand_enumsandvariant[j];

                BlockSand.EnumType.META_LOOKUP[blocksand_enumsandvariant.getMetadata()] = blocksand_enumsandvariant;
            }

        }
    }
}
