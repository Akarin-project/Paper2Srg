package net.minecraft.block;

import com.google.common.base.Predicate;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTorch extends Block {

    public static final PropertyDirection field_176596_a = PropertyDirection.func_177712_a("facing", new Predicate() {
        public boolean a(@Nullable EnumFacing enumdirection) {
            return enumdirection != EnumFacing.DOWN;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EnumFacing) object);
        }
    });
    protected static final AxisAlignedBB field_185738_b = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
    protected static final AxisAlignedBB field_185739_c = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB field_185740_d = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
    protected static final AxisAlignedBB field_185741_e = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
    protected static final AxisAlignedBB field_185742_f = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);

    protected BlockTorch() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockTorch.field_176596_a, EnumFacing.UP));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.func_177229_b(BlockTorch.field_176596_a)) {
        case EAST:
            return BlockTorch.field_185742_f;

        case WEST:
            return BlockTorch.field_185741_e;

        case SOUTH:
            return BlockTorch.field_185740_d;

        case NORTH:
            return BlockTorch.field_185739_c;

        default:
            return BlockTorch.field_185738_b;
        }
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTorch.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    private boolean func_176594_d(World world, BlockPos blockposition) {
        Block block = world.func_180495_p(blockposition).func_177230_c();
        boolean flag = block == Blocks.field_185775_db || block == Blocks.field_150428_aP;

        if (world.func_180495_p(blockposition).func_185896_q()) {
            return !flag;
        } else {
            boolean flag1 = block instanceof BlockFence || block == Blocks.field_150359_w || block == Blocks.field_150463_bK || block == Blocks.field_150399_cn;

            return flag1 && !flag;
        }
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        Iterator iterator = BlockTorch.field_176596_a.func_177700_c().iterator();

        EnumFacing enumdirection;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            enumdirection = (EnumFacing) iterator.next();
        } while (!this.func_176595_b(world, blockposition, enumdirection));

        return true;
    }

    private boolean func_176595_b(World world, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176734_d());
        IBlockState iblockdata = world.func_180495_p(blockposition1);
        Block block = iblockdata.func_177230_c();
        BlockFaceShape enumblockfaceshape = iblockdata.func_193401_d(world, blockposition1, enumdirection);

        return enumdirection.equals(EnumFacing.UP) && this.func_176594_d(world, blockposition1) ? true : (enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? !func_193382_c(block) && enumblockfaceshape == BlockFaceShape.SOLID : false);
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (this.func_176595_b(world, blockposition, enumdirection)) {
            return this.func_176223_P().func_177226_a(BlockTorch.field_176596_a, enumdirection);
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    return this.func_176223_P();
                }

                enumdirection1 = (EnumFacing) iterator.next();
            } while (!this.func_176595_b(world, blockposition, enumdirection1));

            return this.func_176223_P().func_177226_a(BlockTorch.field_176596_a, enumdirection1);
        }
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176593_f(world, blockposition, iblockdata);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176592_e(world, blockposition, iblockdata);
    }

    protected boolean func_176592_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_176593_f(world, blockposition, iblockdata)) {
            return true;
        } else {
            EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockTorch.field_176596_a);
            EnumFacing.Axis enumdirection_enumaxis = enumdirection.func_176740_k();
            EnumFacing enumdirection1 = enumdirection.func_176734_d();
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection1);
            boolean flag = false;

            if (enumdirection_enumaxis.func_176722_c() && world.func_180495_p(blockposition1).func_193401_d(world, blockposition1, enumdirection) != BlockFaceShape.SOLID) {
                flag = true;
            } else if (enumdirection_enumaxis.func_176720_b() && !this.func_176594_d(world, blockposition1)) {
                flag = true;
            }

            if (flag) {
                this.func_176226_b(world, blockposition, iblockdata, 0);
                world.func_175698_g(blockposition);
                return true;
            } else {
                return false;
            }
        }
    }

    protected boolean func_176593_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.func_177230_c() == this && this.func_176595_b(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockTorch.field_176596_a))) {
            return true;
        } else {
            if (world.func_180495_p(blockposition).func_177230_c() == this) {
                this.func_176226_b(world, blockposition, iblockdata, 0);
                world.func_175698_g(blockposition);
            }

            return false;
        }
    }

    public IBlockState func_176203_a(int i) {
        IBlockState iblockdata = this.func_176223_P();

        switch (i) {
        case 1:
            iblockdata = iblockdata.func_177226_a(BlockTorch.field_176596_a, EnumFacing.EAST);
            break;

        case 2:
            iblockdata = iblockdata.func_177226_a(BlockTorch.field_176596_a, EnumFacing.WEST);
            break;

        case 3:
            iblockdata = iblockdata.func_177226_a(BlockTorch.field_176596_a, EnumFacing.SOUTH);
            break;

        case 4:
            iblockdata = iblockdata.func_177226_a(BlockTorch.field_176596_a, EnumFacing.NORTH);
            break;

        case 5:
        default:
            iblockdata = iblockdata.func_177226_a(BlockTorch.field_176596_a, EnumFacing.UP);
        }

        return iblockdata;
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i;

        switch ((EnumFacing) iblockdata.func_177229_b(BlockTorch.field_176596_a)) {
        case EAST:
            i = b0 | 1;
            break;

        case WEST:
            i = b0 | 2;
            break;

        case SOUTH:
            i = b0 | 3;
            break;

        case NORTH:
            i = b0 | 4;
            break;

        case DOWN:
        case UP:
        default:
            i = b0 | 5;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockTorch.field_176596_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockTorch.field_176596_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockTorch.field_176596_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockTorch.field_176596_a});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
