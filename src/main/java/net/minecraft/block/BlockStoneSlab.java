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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockStoneSlab extends BlockSlab {

    public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
    public static final PropertyEnum<BlockStoneSlab.EnumType> VARIANT = PropertyEnum.create("variant", BlockStoneSlab.EnumType.class);

    public BlockStoneSlab() {
        super(Material.ROCK);
        IBlockState iblockdata = this.blockState.getBaseState();

        if (this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockStoneSlab.SEAMLESS, Boolean.valueOf(false));
        } else {
            iblockdata = iblockdata.withProperty(BlockStoneSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(iblockdata.withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(Blocks.STONE_SLAB);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Blocks.STONE_SLAB, 1, ((BlockStoneSlab.EnumType) iblockdata.getValue(BlockStoneSlab.VARIANT)).getMetadata());
    }

    public String getUnlocalizedName(int i) {
        return super.getUnlocalizedName() + "." + BlockStoneSlab.EnumType.byMetadata(i).getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty() {
        return BlockStoneSlab.VARIANT;
    }

    public Comparable<?> getTypeForItem(ItemStack itemstack) {
        return BlockStoneSlab.EnumType.byMetadata(itemstack.getMetadata() & 7);
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockStoneSlab.EnumType[] ablockdoublestepabstract_enumstoneslabvariant = BlockStoneSlab.EnumType.values();
        int i = ablockdoublestepabstract_enumstoneslabvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockStoneSlab.EnumType blockdoublestepabstract_enumstoneslabvariant = ablockdoublestepabstract_enumstoneslabvariant[j];

            if (blockdoublestepabstract_enumstoneslabvariant != BlockStoneSlab.EnumType.WOOD) {
                nonnulllist.add(new ItemStack(this, 1, blockdoublestepabstract_enumstoneslabvariant.getMetadata()));
            }
        }

    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.byMetadata(i & 7));

        if (this.isDouble()) {
            iblockdata = iblockdata.withProperty(BlockStoneSlab.SEAMLESS, Boolean.valueOf((i & 8) != 0));
        } else {
            iblockdata = iblockdata.withProperty(BlockStoneSlab.HALF, (i & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockStoneSlab.EnumType) iblockdata.getValue(BlockStoneSlab.VARIANT)).getMetadata();

        if (this.isDouble()) {
            if (((Boolean) iblockdata.getValue(BlockStoneSlab.SEAMLESS)).booleanValue()) {
                i |= 8;
            }
        } else if (iblockdata.getValue(BlockStoneSlab.HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { BlockStoneSlab.SEAMLESS, BlockStoneSlab.VARIANT}) : new BlockStateContainer(this, new IProperty[] { BlockStoneSlab.HALF, BlockStoneSlab.VARIANT});
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockStoneSlab.EnumType) iblockdata.getValue(BlockStoneSlab.VARIANT)).getMetadata();
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return ((BlockStoneSlab.EnumType) iblockdata.getValue(BlockStoneSlab.VARIANT)).getMapColor();
    }

    public static enum EnumType implements IStringSerializable {

        STONE(0, MapColor.STONE, "stone"), SAND(1, MapColor.SAND, "sandstone", "sand"), WOOD(2, MapColor.WOOD, "wood_old", "wood"), COBBLESTONE(3, MapColor.STONE, "cobblestone", "cobble"), BRICK(4, MapColor.RED, "brick"), SMOOTHBRICK(5, MapColor.STONE, "stone_brick", "smoothStoneBrick"), NETHERBRICK(6, MapColor.NETHERRACK, "nether_brick", "netherBrick"), QUARTZ(7, MapColor.QUARTZ, "quartz");

        private static final BlockStoneSlab.EnumType[] META_LOOKUP = new BlockStoneSlab.EnumType[values().length];
        private final int meta;
        private final MapColor mapColor;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int i, MapColor materialmapcolor, String s) {
            this(i, materialmapcolor, s, s);
        }

        private EnumType(int i, MapColor materialmapcolor, String s, String s1) {
            this.meta = i;
            this.mapColor = materialmapcolor;
            this.name = s;
            this.unlocalizedName = s1;
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

        public static BlockStoneSlab.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockStoneSlab.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockStoneSlab.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockStoneSlab.EnumType[] ablockdoublestepabstract_enumstoneslabvariant = values();
            int i = ablockdoublestepabstract_enumstoneslabvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockStoneSlab.EnumType blockdoublestepabstract_enumstoneslabvariant = ablockdoublestepabstract_enumstoneslabvariant[j];

                BlockStoneSlab.EnumType.META_LOOKUP[blockdoublestepabstract_enumstoneslabvariant.getMetadata()] = blockdoublestepabstract_enumstoneslabvariant;
            }

        }
    }
}
