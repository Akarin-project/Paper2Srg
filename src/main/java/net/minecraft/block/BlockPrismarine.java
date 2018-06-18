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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;


public class BlockPrismarine extends Block {

    public static final PropertyEnum<BlockPrismarine.EnumType> VARIANT = PropertyEnum.create("variant", BlockPrismarine.EnumType.class);
    public static final int ROUGH_META = BlockPrismarine.EnumType.ROUGH.getMetadata();
    public static final int BRICKS_META = BlockPrismarine.EnumType.BRICKS.getMetadata();
    public static final int DARK_META = BlockPrismarine.EnumType.DARK.getMetadata();

    public BlockPrismarine() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.ROUGH));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockPrismarine.EnumType.ROUGH.getUnlocalizedName() + ".name");
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.getValue(BlockPrismarine.VARIANT) == BlockPrismarine.EnumType.ROUGH ? MapColor.CYAN : MapColor.DIAMOND;
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockPrismarine.EnumType) iblockdata.getValue(BlockPrismarine.VARIANT)).getMetadata();
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockPrismarine.EnumType) iblockdata.getValue(BlockPrismarine.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPrismarine.VARIANT});
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.byMetadata(i));
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockPrismarine.ROUGH_META));
        nonnulllist.add(new ItemStack(this, 1, BlockPrismarine.BRICKS_META));
        nonnulllist.add(new ItemStack(this, 1, BlockPrismarine.DARK_META));
    }

    public static enum EnumType implements IStringSerializable {

        ROUGH(0, "prismarine", "rough"), BRICKS(1, "prismarine_bricks", "bricks"), DARK(2, "dark_prismarine", "dark");

        private static final BlockPrismarine.EnumType[] META_LOOKUP = new BlockPrismarine.EnumType[values().length];
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

        public static BlockPrismarine.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockPrismarine.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockPrismarine.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockPrismarine.EnumType[] ablockprismarine_enumprismarinevariant = values();
            int i = ablockprismarine_enumprismarinevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockPrismarine.EnumType blockprismarine_enumprismarinevariant = ablockprismarine_enumprismarinevariant[j];

                BlockPrismarine.EnumType.META_LOOKUP[blockprismarine_enumprismarinevariant.getMetadata()] = blockprismarine_enumprismarinevariant;
            }

        }
    }
}
