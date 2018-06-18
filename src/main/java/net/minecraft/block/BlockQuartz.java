package net.minecraft.block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockQuartz extends Block {

    public static final PropertyEnum<BlockQuartz.EnumType> VARIANT = PropertyEnum.create("variant", BlockQuartz.EnumType.class);

    public BlockQuartz() {
        super(Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (i == BlockQuartz.EnumType.LINES_Y.getMetadata()) {
            switch (enumdirection.getAxis()) {
            case Z:
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Z);

            case X:
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_X);

            case Y:
                return this.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Y);
            }
        }

        return i == BlockQuartz.EnumType.CHISELED.getMetadata() ? this.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.CHISELED) : this.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT);
    }

    public int damageDropped(IBlockState iblockdata) {
        BlockQuartz.EnumType blockquartz_enumquartzvariant = (BlockQuartz.EnumType) iblockdata.getValue(BlockQuartz.VARIANT);

        return blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_X && blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_Z ? blockquartz_enumquartzvariant.getMetadata() : BlockQuartz.EnumType.LINES_Y.getMetadata();
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        BlockQuartz.EnumType blockquartz_enumquartzvariant = (BlockQuartz.EnumType) iblockdata.getValue(BlockQuartz.VARIANT);

        return blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_X && blockquartz_enumquartzvariant != BlockQuartz.EnumType.LINES_Z ? super.getSilkTouchDrop(iblockdata) : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetadata());
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this, 1, BlockQuartz.EnumType.DEFAULT.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockQuartz.EnumType.CHISELED.getMetadata()));
        nonnulllist.add(new ItemStack(this, 1, BlockQuartz.EnumType.LINES_Y.getMetadata()));
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.QUARTZ;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockQuartz.EnumType) iblockdata.getValue(BlockQuartz.VARIANT)).getMetadata();
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((BlockQuartz.EnumType) iblockdata.getValue(BlockQuartz.VARIANT)) {
            case LINES_X:
                return iblockdata.withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_Z);

            case LINES_Z:
                return iblockdata.withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.LINES_X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockQuartz.VARIANT});
    }

    public static enum EnumType implements IStringSerializable {

        DEFAULT(0, "default", "default"), CHISELED(1, "chiseled", "chiseled"), LINES_Y(2, "lines_y", "lines"), LINES_X(3, "lines_x", "lines"), LINES_Z(4, "lines_z", "lines");

        private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[values().length];
        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        private EnumType(int i, String s, String s1) {
            this.meta = i;
            this.serializedName = s;
            this.unlocalizedName = s1;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public static BlockQuartz.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockQuartz.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockQuartz.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.serializedName;
        }

        static {
            BlockQuartz.EnumType[] ablockquartz_enumquartzvariant = values();
            int i = ablockquartz_enumquartzvariant.length;

            for (int j = 0; j < i; ++j) {
                BlockQuartz.EnumType blockquartz_enumquartzvariant = ablockquartz_enumquartzvariant[j];

                BlockQuartz.EnumType.META_LOOKUP[blockquartz_enumquartzvariant.getMetadata()] = blockquartz_enumquartzvariant;
            }

        }
    }
}
