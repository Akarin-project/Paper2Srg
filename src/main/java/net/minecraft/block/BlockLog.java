package net.minecraft.block;

import java.util.Iterator;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {

    public static final PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = PropertyEnum.create("axis", BlockLog.EnumAxis.class);

    public BlockLog() {
        super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = true;
        boolean flag1 = true;

        if (world.isAreaLoaded(blockposition.add(-5, -5, -5), blockposition.add(5, 5, 5))) {
            Iterator iterator = BlockPos.getAllInBox(blockposition.add(-4, -4, -4), blockposition.add(4, 4, 4)).iterator();

            while (iterator.hasNext()) {
                BlockPos blockposition1 = (BlockPos) iterator.next();
                IBlockState iblockdata1 = world.getBlockState(blockposition1);

                if (iblockdata1.getMaterial() == Material.LEAVES && !((Boolean) iblockdata1.getValue(BlockLeaves.CHECK_DECAY)).booleanValue()) {
                    world.setBlockState(blockposition1, iblockdata1.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
                }
            }

        }
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getStateFromMeta(i).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(enumdirection.getAxis()));
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((BlockLog.EnumAxis) iblockdata.getValue(BlockLog.LOG_AXIS)) {
            case X:
                return iblockdata.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);

            case Z:
                return iblockdata.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    public static enum EnumAxis implements IStringSerializable {

        X("x"), Y("y"), Z("z"), NONE("none");

        private final String name;

        private EnumAxis(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public static BlockLog.EnumAxis fromFacingAxis(EnumFacing.Axis enumdirection_enumaxis) {
            switch (enumdirection_enumaxis) {
            case X:
                return BlockLog.EnumAxis.X;

            case Y:
                return BlockLog.EnumAxis.Y;

            case Z:
                return BlockLog.EnumAxis.Z;

            default:
                return BlockLog.EnumAxis.NONE;
            }
        }

        public String getName() {
            return this.name;
        }
    }
}
