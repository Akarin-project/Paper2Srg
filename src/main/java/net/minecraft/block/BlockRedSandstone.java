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


public class BlockRedSandstone extends Block {

    public static final PropertyEnum<BlockRedSandstone.EnumType> TYPE = PropertyEnum.create("type", BlockRedSandstone.EnumType.class);

    public BlockRedSandstone() {
        super(Material.ROCK, BlockSand.EnumType.RED_SAND.getMapColor());
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockRedSandstone.EnumType) iblockdata.getValue(BlockRedSandstone.TYPE)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockRedSandstone.EnumType[] ablockredsandstone_enumredsandstonevariant = BlockRedSandstone.EnumType.values();
        int i = ablockredsandstone_enumredsandstonevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockRedSandstone.EnumType blockredsandstone_enumredsandstonevariant = ablockredsandstone_enumredsandstonevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockredsandstone_enumredsandstonevariant.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockRedSandstone.EnumType) iblockdata.getValue(BlockRedSandstone.TYPE)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedSandstone.TYPE});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "red_sandstone", "default"), CHISELED(1, "chiseled_red_sandstone", "chiseled"), SMOOTH(2, "smooth_red_sandstone", "smooth");

        private static final BlockRedSandstone.EnumType[] META_LOOKUP = new BlockRedSandstone.EnumType[values().length];
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

        public static BlockRedSandstone.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockRedSandstone.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockRedSandstone.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockRedSandstone.EnumType[] ablockredsandstone_enumredsandstonevariant = values();
            int i = ablockredsandstone_enumredsandstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockRedSandstone.EnumType blockredsandstone_enumredsandstonevariant = ablockredsandstone_enumredsandstonevariant[j];

                BlockRedSandstone.EnumType.META_LOOKUP[blockredsandstone_enumredsandstonevariant.getMetadata()] = blockredsandstone_enumredsandstonevariant;
            }

        }
    }
}
