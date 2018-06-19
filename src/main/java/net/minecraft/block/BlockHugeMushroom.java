package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block {

    public static final PropertyEnum<BlockHugeMushroom.EnumType> field_176380_a = PropertyEnum.func_177709_a("variant", BlockHugeMushroom.EnumType.class);
    private final Block field_176379_b;

    public BlockHugeMushroom(Material material, MapColor materialmapcolor, Block block) {
        super(material, materialmapcolor);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
        this.field_176379_b = block;
    }

    public int func_149745_a(Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((BlockHugeMushroom.EnumType) iblockdata.func_177229_b(BlockHugeMushroom.field_176380_a)) {
        case ALL_STEM:
            return MapColor.field_151659_e;

        case ALL_INSIDE:
            return MapColor.field_151658_d;

        case STEM:
            return MapColor.field_151658_d;

        default:
            return super.func_180659_g(iblockdata, iblockaccess, blockposition);
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(this.field_176379_b);
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this.field_176379_b);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.func_176895_a(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((BlockHugeMushroom.EnumType) iblockdata.func_177229_b(BlockHugeMushroom.field_176380_a)).func_176896_a();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockHugeMushroom.EnumType) iblockdata.func_177229_b(BlockHugeMushroom.field_176380_a)) {
            case STEM:
                break;

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case NORTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

            case WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.EAST);

            case EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

            case SOUTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

            default:
                return iblockdata;
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockHugeMushroom.EnumType) iblockdata.func_177229_b(BlockHugeMushroom.field_176380_a)) {
            case STEM:
                break;

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

            case NORTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.WEST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

            case WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

            case EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case SOUTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.EAST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

            default:
                return iblockdata;
            }

        case CLOCKWISE_90:
            switch ((BlockHugeMushroom.EnumType) iblockdata.func_177229_b(BlockHugeMushroom.field_176380_a)) {
            case STEM:
                break;

            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

            case NORTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.EAST);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH);

            case EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

            case SOUTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.WEST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = (BlockHugeMushroom.EnumType) iblockdata.func_177229_b(BlockHugeMushroom.field_176380_a);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockhugemushroom_enumhugemushroomvariant) {
            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);

            case NORTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH);

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case WEST:
            case EAST:
            default:
                return super.func_185471_a(iblockdata, enumblockmirror);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

            case SOUTH:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);
            }

        case FRONT_BACK:
            switch (blockhugemushroom_enumhugemushroomvariant) {
            case NORTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_EAST);

            case NORTH:
            case SOUTH:
            default:
                break;

            case NORTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.NORTH_WEST);

            case WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.EAST);

            case EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.WEST);

            case SOUTH_WEST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case SOUTH_EAST:
                return iblockdata.func_177226_a(BlockHugeMushroom.field_176380_a, BlockHugeMushroom.EnumType.SOUTH_WEST);
            }
        }

        return super.func_185471_a(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockHugeMushroom.field_176380_a});
    }

    public static enum EnumType implements IStringSerializable {

        NORTH_WEST(1, "north_west"), NORTH(2, "north"), NORTH_EAST(3, "north_east"), WEST(4, "west"), CENTER(5, "center"), EAST(6, "east"), SOUTH_WEST(7, "south_west"), SOUTH(8, "south"), SOUTH_EAST(9, "south_east"), STEM(10, "stem"), ALL_INSIDE(0, "all_inside"), ALL_OUTSIDE(14, "all_outside"), ALL_STEM(15, "all_stem");

        private static final BlockHugeMushroom.EnumType[] field_176905_n = new BlockHugeMushroom.EnumType[16];
        private final int field_176906_o;
        private final String field_176914_p;

        private EnumType(int i, String s) {
            this.field_176906_o = i;
            this.field_176914_p = s;
        }

        public int func_176896_a() {
            return this.field_176906_o;
        }

        public String toString() {
            return this.field_176914_p;
        }

        public static BlockHugeMushroom.EnumType func_176895_a(int i) {
            if (i < 0 || i >= BlockHugeMushroom.EnumType.field_176905_n.length) {
                i = 0;
            }

            BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.field_176905_n[i];

            return blockhugemushroom_enumhugemushroomvariant == null ? BlockHugeMushroom.EnumType.field_176905_n[0] : blockhugemushroom_enumhugemushroomvariant;
        }

        public String func_176610_l() {
            return this.field_176914_p;
        }

        static {
            BlockHugeMushroom.EnumType[] ablockhugemushroom_enumhugemushroomvariant = values();
            int i = ablockhugemushroom_enumhugemushroomvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = ablockhugemushroom_enumhugemushroomvariant[j];

                BlockHugeMushroom.EnumType.field_176905_n[blockhugemushroom_enumhugemushroomvariant.func_176896_a()] = blockhugemushroom_enumhugemushroomvariant;
            }

        }
    }
}
