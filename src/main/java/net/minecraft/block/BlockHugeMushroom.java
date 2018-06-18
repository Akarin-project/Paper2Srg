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

    public static final PropertyEnum<BlockHugeMushroom.EnumType> VARIANT = PropertyEnum.create("variant", BlockHugeMushroom.EnumType.class);
    private final Block smallBlock;

    public BlockHugeMushroom(Material material, MapColor materialmapcolor, Block block) {
        super(material, materialmapcolor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE));
        this.smallBlock = block;
    }

    public int quantityDropped(Random random) {
        return Math.max(0, random.nextInt(10) - 7);
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((BlockHugeMushroom.EnumType) iblockdata.getValue(BlockHugeMushroom.VARIANT)) {
        case ALL_STEM:
            return MapColor.CLOTH;

        case ALL_INSIDE:
            return MapColor.SAND;

        case STEM:
            return MapColor.SAND;

        default:
            return super.getMapColor(iblockdata, iblockaccess, blockposition);
        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(this.smallBlock);
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(this.smallBlock);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockHugeMushroom.EnumType) iblockdata.getValue(BlockHugeMushroom.VARIANT)).getMetadata();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            switch ((BlockHugeMushroom.EnumType) iblockdata.getValue(BlockHugeMushroom.VARIANT)) {
            case STEM:
                break;

            case NORTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case NORTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);

            case WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.EAST);

            case EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);

            case SOUTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);

            default:
                return iblockdata;
            }

        case COUNTERCLOCKWISE_90:
            switch ((BlockHugeMushroom.EnumType) iblockdata.getValue(BlockHugeMushroom.VARIANT)) {
            case STEM:
                break;

            case NORTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);

            case NORTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.WEST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);

            case WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH);

            case EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case SOUTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.EAST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);

            default:
                return iblockdata;
            }

        case CLOCKWISE_90:
            switch ((BlockHugeMushroom.EnumType) iblockdata.getValue(BlockHugeMushroom.VARIANT)) {
            case STEM:
                break;

            case NORTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);

            case NORTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.EAST);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH);

            case EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);

            case SOUTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.WEST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = (BlockHugeMushroom.EnumType) iblockdata.getValue(BlockHugeMushroom.VARIANT);

        switch (enumblockmirror) {
        case LEFT_RIGHT:
            switch (blockhugemushroom_enumhugemushroomvariant) {
            case NORTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);

            case NORTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH);

            case NORTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case WEST:
            case EAST:
            default:
                return super.withMirror(iblockdata, enumblockmirror);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);

            case SOUTH:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);
            }

        case FRONT_BACK:
            switch (blockhugemushroom_enumhugemushroomvariant) {
            case NORTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_EAST);

            case NORTH:
            case SOUTH:
            default:
                break;

            case NORTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.NORTH_WEST);

            case WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.EAST);

            case EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.WEST);

            case SOUTH_WEST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_EAST);

            case SOUTH_EAST:
                return iblockdata.withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.SOUTH_WEST);
            }
        }

        return super.withMirror(iblockdata, enumblockmirror);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockHugeMushroom.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        NORTH_WEST(1, "north_west"), NORTH(2, "north"), NORTH_EAST(3, "north_east"), WEST(4, "west"), CENTER(5, "center"), EAST(6, "east"), SOUTH_WEST(7, "south_west"), SOUTH(8, "south"), SOUTH_EAST(9, "south_east"), STEM(10, "stem"), ALL_INSIDE(0, "all_inside"), ALL_OUTSIDE(14, "all_outside"), ALL_STEM(15, "all_stem");

        private static final BlockHugeMushroom.EnumType[] META_LOOKUP = new BlockHugeMushroom.EnumType[16];
        private final int meta;
        private final String name;

        private EnumType(int i, String s) {
            this.meta = i;
            this.name = s;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static BlockHugeMushroom.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockHugeMushroom.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = BlockHugeMushroom.EnumType.META_LOOKUP[i];

            return blockhugemushroom_enumhugemushroomvariant == null ? BlockHugeMushroom.EnumType.META_LOOKUP[0] : blockhugemushroom_enumhugemushroomvariant;
        }

        public String getName() {
            return this.name;
        }

        static {
            BlockHugeMushroom.EnumType[] ablockhugemushroom_enumhugemushroomvariant = values();
            int i = ablockhugemushroom_enumhugemushroomvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockHugeMushroom.EnumType blockhugemushroom_enumhugemushroomvariant = ablockhugemushroom_enumhugemushroomvariant[j];

                BlockHugeMushroom.EnumType.META_LOOKUP[blockhugemushroom_enumhugemushroomvariant.getMetadata()] = blockhugemushroom_enumhugemushroomvariant;
            }

        }
    }
}
