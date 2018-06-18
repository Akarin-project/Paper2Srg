package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlabNew extends BlockSlab {

    public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
    public static final PropertyEnum<BlockStoneSlabNew.EnumType> VARIANT = PropertyEnum.create("variant", BlockStoneSlabNew.EnumType.class);

    public BlockStoneSlabNew() {
        super(Material.ROCK);
        IBlockState iblockdata = this.blockState.getBaseState();

        if (this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockStoneSlabNew.SEAMLESS, Boolean.valueOf(false));
        } else {
            iblockdata = iblockdata.withProperty(BlockStoneSlabNew.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(iblockdata.withProperty(BlockStoneSlabNew.VARIANT, BlockStoneSlabNew.EnumType.RED_SANDSTONE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + ".red_sandstone.name");
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.STONE_SLAB2);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.STONE_SLAB2, 1, ((BlockStoneSlabNew.EnumType) iblockdata.getValue(BlockStoneSlabNew.VARIANT)).getMetadata());
    }

    public String getUnlocalizedName(int i) {
        return super.getUnlocalizedName() + "." + BlockStoneSlabNew.EnumType.byMetadata(i).getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty() {
        return BlockStoneSlabNew.VARIANT;
    }

    public Comparable<?> getTypeForItem(ItemStack itemstack) {
        return BlockStoneSlabNew.EnumType.byMetadata(itemstack.getMetadata() & 7);
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStoneSlabNew.EnumType[] ablockdoublestonestepabstract_enumstoneslab2variant = BlockStoneSlabNew.EnumType.values();
        int i = ablockdoublestonestepabstract_enumstoneslab2variant.length;

        for (int j = 0; j < i; ++j) {
            BlockStoneSlabNew.EnumType blockdoublestonestepabstract_enumstoneslab2variant = ablockdoublestonestepabstract_enumstoneslab2variant[j];

            nonnulllist.add(new ItemStack(this, 1, blockdoublestonestepabstract_enumstoneslab2variant.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockStoneSlabNew.VARIANT, BlockStoneSlabNew.EnumType.byMetadata(i & 7));

        if (this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockStoneSlabNew.SEAMLESS, Boolean.valueOf((i & 8) != 0));
        } else {
            iblockdata = iblockdata.withProperty(BlockStoneSlabNew.HALF, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockStoneSlabNew.EnumType) iblockdata.getValue(BlockStoneSlabNew.VARIANT)).getMetadata();

        if (this.isDouble()) {
            if (((Boolean) iblockdata.getValue(BlockStoneSlabNew.SEAMLESS)).booleanValue()) {
                i |= 8;
            }
        } else if (iblockdata.getValue(BlockStoneSlabNew.HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { BlockStoneSlabNew.SEAMLESS, BlockStoneSlabNew.VARIANT}) : new BlockStateContainer(this, new IProperty[] { BlockStoneSlabNew.HALF, BlockStoneSlabNew.VARIANT});
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockStoneSlabNew.EnumType) iblockdata.getValue(BlockStoneSlabNew.VARIANT)).getMapColor();
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockStoneSlabNew.EnumType) iblockdata.getValue(BlockStoneSlabNew.VARIANT)).getMetadata();
    }

    public static enum EnumType implements IStringSerializable {

        RED_SANDSTONE(0, "red_sandstone", BlockSand.EnumType.RED_SAND.getMapColor());

        private static final BlockStoneSlabNew.EnumType[] META_LOOKUP = new BlockStoneSlabNew.EnumType[values().length];
        private final int meta;
        private final String name;
        private final MapColor mapColor;

        private EnumType(int i, String s, MapColor materialmapcolor) {
            this.meta = i;
            this.name = s;
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

        public static BlockStoneSlabNew.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockStoneSlabNew.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockStoneSlabNew.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.name;
        }

        static {
            BlockStoneSlabNew.EnumType[] ablockdoublestonestepabstract_enumstoneslab2variant = values();
            int i = ablockdoublestonestepabstract_enumstoneslab2variant.length;

            for (int j = 0; j < i; ++j) {
                BlockStoneSlabNew.EnumType blockdoublestonestepabstract_enumstoneslab2variant = ablockdoublestonestepabstract_enumstoneslab2variant[j];

                BlockStoneSlabNew.EnumType.META_LOOKUP[blockdoublestonestepabstract_enumstoneslab2variant.getMetadata()] = blockdoublestonestepabstract_enumstoneslab2variant;
            }

        }
    }
}
