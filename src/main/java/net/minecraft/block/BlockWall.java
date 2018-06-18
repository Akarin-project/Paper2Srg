package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWall extends Block {

    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyEnum<BlockWall.EnumType> VARIANT = PropertyEnum.create("variant", BlockWall.EnumType.class);
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[] { new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[] { BlockWall.AABB_BY_INDEX[0].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[1].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[2].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[3].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[4].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[5].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[6].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[7].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[8].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[9].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[10].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[11].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[12].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[13].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[14].setMaxY(1.5D), BlockWall.AABB_BY_INDEX[15].setMaxY(1.5D)};

    public BlockWall(Block block) {
        super(block.blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockWall.UP, Boolean.valueOf(false)).withProperty(BlockWall.NORTH, Boolean.valueOf(false)).withProperty(BlockWall.EAST, Boolean.valueOf(false)).withProperty(BlockWall.SOUTH, Boolean.valueOf(false)).withProperty(BlockWall.WEST, Boolean.valueOf(false)).withProperty(BlockWall.VARIANT, BlockWall.EnumType.NORMAL));
        this.setHardness(block.blockHardness);
        this.setResistance(block.blockResistance / 3.0F);
        this.setSoundType(block.blockSoundType);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        return BlockWall.AABB_BY_INDEX[getAABBIndex(iblockdata)];
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        if (!flag) {
            iblockdata = this.getActualState(iblockdata, world, blockposition);
        }

        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockWall.CLIP_AABB_BY_INDEX[getAABBIndex(iblockdata)]);
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = this.getActualState(iblockdata, iblockaccess, blockposition);
        return BlockWall.CLIP_AABB_BY_INDEX[getAABBIndex(iblockdata)];
    }

    private static int getAABBIndex(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.getValue(BlockWall.NORTH)).booleanValue()) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (((Boolean) iblockdata.getValue(BlockWall.EAST)).booleanValue()) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (((Boolean) iblockdata.getValue(BlockWall.SOUTH)).booleanValue()) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (((Boolean) iblockdata.getValue(BlockWall.WEST)).booleanValue()) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + "." + BlockWall.EnumType.NORMAL.getUnlocalizedName() + ".name");
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    private boolean canConnectTo(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.getBlockState(blockposition);
        Block block = iblockdata.getBlock();
        BlockFaceShape enumblockfaceshape = iblockdata.getBlockFaceShape(iblockaccess, blockposition, enumdirection);
        boolean flag = enumblockfaceshape == BlockFaceShape.MIDDLE_POLE_THICK || enumblockfaceshape == BlockFaceShape.MIDDLE_POLE && block instanceof BlockFenceGate;

        return !isExcepBlockForAttachWithPiston(block) && enumblockfaceshape == BlockFaceShape.SOLID || flag;
    }

    protected static boolean isExcepBlockForAttachWithPiston(Block block) {
        return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN;
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockWall.EnumType[] ablockcobblewall_enumcobblevariant = BlockWall.EnumType.values();
        int i = ablockcobblewall_enumcobblevariant.length;

        for (int j = 0; j < i; ++j) {
            BlockWall.EnumType blockcobblewall_enumcobblevariant = ablockcobblewall_enumcobblevariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockcobblewall_enumcobblevariant.getMetadata()));
        }

    }

    public int damageDropped(IBlockState iblockdata) {
        return ((BlockWall.EnumType) iblockdata.getValue(BlockWall.VARIANT)).getMetadata();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockWall.VARIANT, BlockWall.EnumType.byMetadata(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((BlockWall.EnumType) iblockdata.getValue(BlockWall.VARIANT)).getMetadata();
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        boolean flag = this.canConnectTo(iblockaccess, blockposition.north(), EnumFacing.SOUTH);
        boolean flag1 = this.canConnectTo(iblockaccess, blockposition.east(), EnumFacing.WEST);
        boolean flag2 = this.canConnectTo(iblockaccess, blockposition.south(), EnumFacing.NORTH);
        boolean flag3 = this.canConnectTo(iblockaccess, blockposition.west(), EnumFacing.EAST);
        boolean flag4 = flag && !flag1 && flag2 && !flag3 || !flag && flag1 && !flag2 && flag3;

        return iblockdata.withProperty(BlockWall.UP, Boolean.valueOf(!flag4 || !iblockaccess.isAirBlock(blockposition.up()))).withProperty(BlockWall.NORTH, Boolean.valueOf(flag)).withProperty(BlockWall.EAST, Boolean.valueOf(flag1)).withProperty(BlockWall.SOUTH, Boolean.valueOf(flag2)).withProperty(BlockWall.WEST, Boolean.valueOf(flag3));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockWall.UP, BlockWall.NORTH, BlockWall.EAST, BlockWall.WEST, BlockWall.SOUTH, BlockWall.VARIANT});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE_THICK : BlockFaceShape.CENTER_BIG;
    }

    public static enum EnumType implements IStringSerializable {

        NORMAL(0, "cobblestone", "normal"), MOSSY(1, "mossy_cobblestone", "mossy");

        private static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[values().length];
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

        public static BlockWall.EnumType byMetadata(int i) {
            if (i < 0 || i >= BlockWall.EnumType.META_LOOKUP.length) {
                i = 0;
            }

            return BlockWall.EnumType.META_LOOKUP[i];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            BlockWall.EnumType[] ablockcobblewall_enumcobblevariant = values();
            int i = ablockcobblewall_enumcobblevariant.length;

            for (int j = 0; j < i; ++j) {
                BlockWall.EnumType blockcobblewall_enumcobblevariant = ablockcobblewall_enumcobblevariant[j];

                BlockWall.EnumType.META_LOOKUP[blockcobblewall_enumcobblevariant.getMetadata()] = blockcobblewall_enumcobblevariant;
            }

        }
    }
}
