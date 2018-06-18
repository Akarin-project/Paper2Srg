package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockFlower extends BlockBush {

    protected PropertyEnum<BlockFlower.EnumFlowerType> type;

    protected BlockFlower() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getTypeProperty(), this.getBlockType() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION));
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return super.getBoundingBox(iblockdata, iblockaccess, blockposition).offset(iblockdata.getOffset(iblockaccess, blockposition));
    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockFlower.EnumFlowerType) iblockdata.getValue(this.getTypeProperty())).getMeta();
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockFlower.EnumFlowerType[] ablockflowers_enumflowervarient = BlockFlower.EnumFlowerType.getTypes(this.getBlockType());
        int i = ablockflowers_enumflowervarient.length;

        for (int j = 0; j < i; ++j) {
            BlockFlower.EnumFlowerType blockflowers_enumflowervarient = ablockflowers_enumflowervarient[j];

            nonnulllist.add(new ItemStack(this, 1, blockflowers_enumflowervarient.getMeta()));
        }

    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(this.getTypeProperty(), BlockFlower.EnumFlowerType.getType(this.getBlockType(), i));
    }

    public abstract BlockFlower.EnumFlowerColor getBlockType();

    public IProperty<BlockFlower.EnumFlowerType> getTypeProperty() {
        if (this.type == null) {
            this.type = PropertyEnum.create("type", BlockFlower.EnumFlowerType.class, new Predicate() {
                public boolean a(@Nullable BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
                    return blockflowers_enumflowervarient.getBlockType() == BlockFlower.this.getBlockType();
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((BlockFlower.EnumFlowerType) object);
                }
            });
        }

        return this.type;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockFlower.EnumFlowerType) iblockdata.getValue(this.getTypeProperty())).getMeta();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { this.getTypeProperty()});
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerType implements IStringSerializable {

        DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"), POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"), BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"), ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"), HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"), RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"), ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"), WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"), PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"), OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

        private static final BlockFlower.EnumFlowerType[][] TYPES_FOR_BLOCK = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
        private final BlockFlower.EnumFlowerColor blockType;
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumFlowerType(BlockFlower.EnumFlowerColor blockflowers_enumflowertype, int i, String s) {
            this(blockflowers_enumflowertype, i, s, s);
        }

        private EnumFlowerType(BlockFlower.EnumFlowerColor blockflowers_enumflowertype, int i, String s, String s1) {
            this.blockType = blockflowers_enumflowertype;
            this.meta = i;
            this.name = s;
            this.unlocalizedName = s1;
        }

        public BlockFlower.EnumFlowerColor getBlockType() {
            return this.blockType;
        }

        public int getMeta() {
            return this.meta;
        }

        public static BlockFlower.EnumFlowerType getType(BlockFlower.EnumFlowerColor blockflowers_enumflowertype, int i) {
            BlockFlower.EnumFlowerType[] ablockflowers_enumflowervarient = BlockFlower.EnumFlowerType.TYPES_FOR_BLOCK[blockflowers_enumflowertype.ordinal()];

            if (i < 0 || i >= ablockflowers_enumflowervarient.length) {
                i = 0;
            }

            return ablockflowers_enumflowervarient[i];
        }

        public static BlockFlower.EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor blockflowers_enumflowertype) {
            return BlockFlower.EnumFlowerType.TYPES_FOR_BLOCK[blockflowers_enumflowertype.ordinal()];
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockFlower.EnumFlowerColor[] ablockflowers_enumflowertype = BlockFlower.EnumFlowerColor.values();
            int i = ablockflowers_enumflowertype.length;

            for (int j = 0; j < i; ++j) {
                final BlockFlower.EnumFlowerColor blockflowers_enumflowertype = ablockflowers_enumflowertype[j];
                Collection collection = Collections2.filter(Lists.newArrayList(values()), new Predicate() {
                    public boolean a(@Nullable BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
                        return blockflowers_enumflowervarient.getBlockType() == blockflowers_enumflowertype;
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((BlockFlower.EnumFlowerType) object);
                    }
                });

                BlockFlower.EnumFlowerType.TYPES_FOR_BLOCK[blockflowers_enumflowertype.ordinal()] = (BlockFlower.EnumFlowerType[]) collection.toArray(new BlockFlower.EnumFlowerType[collection.size()]);
            }

        }
    }

    public static enum EnumFlowerColor {

        YELLOW, RED;

        private EnumFlowerColor() {}

        public BlockFlower getBlock() {
            return this == BlockFlower.EnumFlowerColor.YELLOW ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
        }
    }
}
