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

    protected PropertyEnum<BlockFlower.EnumFlowerType> field_176496_a;

    protected BlockFlower() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(this.func_176494_l(), this.func_176495_j() == BlockFlower.EnumFlowerColor.RED ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.DANDELION));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return super.func_185496_a(iblockdata, iblockaccess, blockposition).func_191194_a(iblockdata.func_191059_e(iblockaccess, blockposition));
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockFlower.EnumFlowerType) iblockdata.func_177229_b(this.func_176494_l())).func_176968_b();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockFlower.EnumFlowerType[] ablockflowers_enumflowervarient = BlockFlower.EnumFlowerType.func_176966_a(this.func_176495_j());
        int i = ablockflowers_enumflowervarient.length;

        for (int j = 0; j < i; ++j) {
            BlockFlower.EnumFlowerType blockflowers_enumflowervarient = ablockflowers_enumflowervarient[j];

            nonnulllist.add(new ItemStack(this, 1, blockflowers_enumflowervarient.func_176968_b()));
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(this.func_176494_l(), BlockFlower.EnumFlowerType.func_176967_a(this.func_176495_j(), i));
    }

    public abstract BlockFlower.EnumFlowerColor func_176495_j();

    public IProperty<BlockFlower.EnumFlowerType> func_176494_l() {
        if (this.field_176496_a == null) {
            this.field_176496_a = PropertyEnum.func_177708_a("type", BlockFlower.EnumFlowerType.class, new Predicate() {
                public boolean a(@Nullable BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
                    return blockflowers_enumflowervarient.func_176964_a() == BlockFlower.this.func_176495_j();
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((BlockFlower.EnumFlowerType) object);
                }
            });
        }

        return this.field_176496_a;
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockFlower.EnumFlowerType) iblockdata.func_177229_b(this.func_176494_l())).func_176968_b();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { this.func_176494_l()});
    }

    public Block.EnumOffsetType func_176218_Q() {
        return Block.EnumOffsetType.XZ;
    }

    public static enum EnumFlowerType implements IStringSerializable {

        DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"), POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"), BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"), ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"), HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"), RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"), ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"), WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"), PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"), OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");

        private static final BlockFlower.EnumFlowerType[][] field_176981_k = new BlockFlower.EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
        private final BlockFlower.EnumFlowerColor field_176978_l;
        private final int field_176979_m;
        private final String field_176976_n;
        private final String field_176977_o;

        private EnumFlowerType(BlockFlower.EnumFlowerColor blockflowers_enumflowertype, int i, String s) {
            this(blockflowers_enumflowertype, i, s, s);
        }

        private EnumFlowerType(BlockFlower.EnumFlowerColor blockflowers_enumflowertype, int i, String s, String s1) {
            this.field_176978_l = blockflowers_enumflowertype;
            this.field_176979_m = i;
            this.field_176976_n = s;
            this.field_176977_o = s1;
        }

        public BlockFlower.EnumFlowerColor func_176964_a() {
            return this.field_176978_l;
        }

        public int func_176968_b() {
            return this.field_176979_m;
        }

        public static BlockFlower.EnumFlowerType func_176967_a(BlockFlower.EnumFlowerColor blockflowers_enumflowertype, int i) {
            BlockFlower.EnumFlowerType[] ablockflowers_enumflowervarient = BlockFlower.EnumFlowerType.field_176981_k[blockflowers_enumflowertype.ordinal()];

            if (i < 0 || i >= ablockflowers_enumflowervarient.length) {
                i = 0;
            }

            return ablockflowers_enumflowervarient[i];
        }

        public static BlockFlower.EnumFlowerType[] func_176966_a(BlockFlower.EnumFlowerColor blockflowers_enumflowertype) {
            return BlockFlower.EnumFlowerType.field_176981_k[blockflowers_enumflowertype.ordinal()];
        }

        public String toString() {
            return this.field_176976_n;
        }

        public String func_176610_l() {
            return this.field_176976_n;
        }

        public String func_176963_d() {
            return this.field_176977_o;
        }

        static {
            BlockFlower.EnumFlowerColor[] ablockflowers_enumflowertype = BlockFlower.EnumFlowerColor.values();
            int i = ablockflowers_enumflowertype.length;

            for (int j = 0; j < i; ++j) {
                final BlockFlower.EnumFlowerColor blockflowers_enumflowertype = ablockflowers_enumflowertype[j];
                Collection collection = Collections2.filter(Lists.newArrayList(values()), new Predicate() {
                    public boolean a(@Nullable BlockFlower.EnumFlowerType blockflowers_enumflowervarient) {
                        return blockflowers_enumflowervarient.func_176964_a() == blockflowers_enumflowertype;
                    }

                    public boolean apply(@Nullable Object object) {
                        return this.a((BlockFlower.EnumFlowerType) object);
                    }
                });

                BlockFlower.EnumFlowerType.field_176981_k[blockflowers_enumflowertype.ordinal()] = (BlockFlower.EnumFlowerType[]) collection.toArray(new BlockFlower.EnumFlowerType[collection.size()]);
            }

        }
    }

    public static enum EnumFlowerColor {

        YELLOW, RED;

        private EnumFlowerColor() {}

        public BlockFlower func_180346_a() {
            return this == BlockFlower.EnumFlowerColor.YELLOW ? Blocks.field_150327_N : Blocks.field_150328_O;
        }
    }
}
