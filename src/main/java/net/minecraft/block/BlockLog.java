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

    public static final PropertyEnum<BlockLog.EnumAxis> field_176299_a = PropertyEnum.func_177709_a("axis", BlockLog.EnumAxis.class);

    public BlockLog() {
        super(Material.field_151575_d);
        this.func_149647_a(CreativeTabs.field_78030_b);
        this.func_149711_c(2.0F);
        this.func_149672_a(SoundType.field_185848_a);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = true;
        boolean flag1 = true;

        if (world.func_175707_a(blockposition.func_177982_a(-5, -5, -5), blockposition.func_177982_a(5, 5, 5))) {
            Iterator iterator = BlockPos.func_177980_a(blockposition.func_177982_a(-4, -4, -4), blockposition.func_177982_a(4, 4, 4)).iterator();

            while (iterator.hasNext()) {
                BlockPos blockposition1 = (BlockPos) iterator.next();
                IBlockState iblockdata1 = world.func_180495_p(blockposition1);

                if (iblockdata1.func_185904_a() == Material.field_151584_j && !((Boolean) iblockdata1.func_177229_b(BlockLeaves.field_176236_b)).booleanValue()) {
                    world.func_180501_a(blockposition1, iblockdata1.func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(true)), 4);
                }
            }

        }
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176203_a(i).func_177226_a(BlockLog.field_176299_a, BlockLog.EnumAxis.func_176870_a(enumdirection.func_176740_k()));
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch ((BlockLog.EnumAxis) iblockdata.func_177229_b(BlockLog.field_176299_a)) {
            case X:
                return iblockdata.func_177226_a(BlockLog.field_176299_a, BlockLog.EnumAxis.Z);

            case Z:
                return iblockdata.func_177226_a(BlockLog.field_176299_a, BlockLog.EnumAxis.X);

            default:
                return iblockdata;
            }

        default:
            return iblockdata;
        }
    }

    public static enum EnumAxis implements IStringSerializable {

        X("x"), Y("y"), Z("z"), NONE("none");

        private final String field_176874_e;

        private EnumAxis(String s) {
            this.field_176874_e = s;
        }

        public String toString() {
            return this.field_176874_e;
        }

        public static BlockLog.EnumAxis func_176870_a(EnumFacing.Axis enumdirection_enumaxis) {
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

        public String func_176610_l() {
            return this.field_176874_e;
        }
    }
}
