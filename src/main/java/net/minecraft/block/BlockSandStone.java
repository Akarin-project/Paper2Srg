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


public class BlockSandStone extends Block {

    public static final PropertyEnum<BlockSandStone.EnumType> TYPE = PropertyEnum.create("type", BlockSandStone.EnumType.class);

    public BlockSandStone() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockSandStone.EnumType) iblockdata.getValue(BlockSandStone.TYPE)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockSandStone.EnumType[] ablocksandstone_enumsandstonevariant = BlockSandStone.EnumType.values();
        int i = ablocksandstone_enumsandstonevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockSandStone.EnumType blocksandstone_enumsandstonevariant = ablocksandstone_enumsandstonevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blocksandstone_enumsandstonevariant.getMetadata()));
        }

    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.SAND;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockSandStone.EnumType) iblockdata.getValue(BlockSandStone.TYPE)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockSandStone.TYPE});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "sandstone", "default"), CHISELED(1, "chiseled_sandstone", "chiseled"), SMOOTH(2, "smooth_sandstone", "smooth");

        private static final BlockSandStone.EnumType[] META_LOOKUP = new BlockSandStone.EnumType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int i, String s, String s1) {
            this.metadata = i;
            this.name = s;
            this.unlocalizedName = s1;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String toString() {
            return this.name;
        }

        public static BlockSandStone.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockSandStone.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockSandStone.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockSandStone.EnumType[] ablocksandstone_enumsandstonevariant = values();
            int i = ablocksandstone_enumsandstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockSandStone.EnumType blocksandstone_enumsandstonevariant = ablocksandstone_enumsandstonevariant[j];

                BlockSandStone.EnumType.META_LOOKUP[blocksandstone_enumsandstonevariant.getMetadata()] = blocksandstone_enumsandstonevariant;
            }

        }
    }
}
