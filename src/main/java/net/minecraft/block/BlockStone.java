package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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

public class BlockStone extends Block {

    public static final PropertyEnum<BlockStone.EnumType> VARIANT = PropertyEnum.create("variant", BlockStone.EnumType.class);

    public BlockStone() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockStone.EnumType.STONE.getUnlocalizedName() + ".name");
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockStone.EnumType) iblockdata.getValue(BlockStone.VARIANT)).getMapColor();
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return iblockdata.getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE ? Item.getItemFromBlock(Blocks.COBBLESTONE) : Item.getItemFromBlock(Blocks.STONE);
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockStone.EnumType) iblockdata.getValue(BlockStone.VARIANT)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStone.EnumType[] ablockstone_enumstonevariant = BlockStone.EnumType.values();
        int i = ablockstone_enumstonevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockStone.EnumType blockstone_enumstonevariant = ablockstone_enumstonevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockstone_enumstonevariant.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockStone.EnumType) iblockdata.getValue(BlockStone.VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockStone.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        STONE(0, MapColor.STONE, "stone", true), GRANITE(1, MapColor.DIRT, "granite", true), GRANITE_SMOOTH(2, MapColor.DIRT, "smooth_granite", "graniteSmooth", false), DIORITE(3, MapColor.QUARTZ, "diorite", true), DIORITE_SMOOTH(4, MapColor.QUARTZ, "smooth_diorite", "dioriteSmooth", false), ANDESITE(5, MapColor.STONE, "andesite", true), ANDESITE_SMOOTH(6, MapColor.STONE, "smooth_andesite", "andesiteSmooth", false);

        private static final BlockStone.EnumType[] META_LOOKUP = new BlockStone.EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor mapColor;
        private final boolean isNatural;

        private EnumType(int i, MapColor materialmapcolor, String s, boolean flag) {
            this(i, materialmapcolor, s, s, flag);
        }

        private EnumType(int i, MapColor materialmapcolor, String s, String s1, boolean flag) {
            this.meta = i;
            this.name = s;
            this.unlocalizedName = s1;
            this.mapColor = materialmapcolor;
            this.isNatural = flag;
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

        public static BlockStone.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockStone.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockStone.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public boolean isNatural() {
            return this.isNatural;
        }

        static {
            BlockStone.EnumType[] ablockstone_enumstonevariant = values();
            int i = ablockstone_enumstonevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockStone.EnumType blockstone_enumstonevariant = ablockstone_enumstonevariant[j];

                BlockStone.EnumType.META_LOOKUP[blockstone_enumstonevariant.getMetadata()] = blockstone_enumstonevariant;
            }

        }
    }
}
