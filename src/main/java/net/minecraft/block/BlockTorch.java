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

    public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate() {
        public boolean a(@Nullable EnumFacing enumdirection) {
            return enumdirection != EnumFacing.DOWN;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EnumFacing) object);
        }
    });
    protected static final AxisAlignedBB STANDING_AABB = new AxisAlignedBB(0.4000000059604645D, 0.0D, 0.4000000059604645D, 0.6000000238418579D, 0.6000000238418579D, 0.6000000238418579D);
    protected static final AxisAlignedBB TORCH_NORTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.699999988079071D, 0.6499999761581421D, 0.800000011920929D, 1.0D);
    protected static final AxisAlignedBB TORCH_SOUTH_AABB = new AxisAlignedBB(0.3499999940395355D, 0.20000000298023224D, 0.0D, 0.6499999761581421D, 0.800000011920929D, 0.30000001192092896D);
    protected static final AxisAlignedBB TORCH_WEST_AABB = new AxisAlignedBB(0.699999988079071D, 0.20000000298023224D, 0.3499999940395355D, 1.0D, 0.800000011920929D, 0.6499999761581421D);
    protected static final AxisAlignedBB TORCH_EAST_AABB = new AxisAlignedBB(0.0D, 0.20000000298023224D, 0.3499999940395355D, 0.30000001192092896D, 0.800000011920929D, 0.6499999761581421D);

    protected BlockTorch() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTorch.FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.getValue(BlockTorch.FACING)) {
        case EAST:
            return BlockTorch.TORCH_EAST_AABB;

        case WEST:
            return BlockTorch.TORCH_WEST_AABB;

        case SOUTH:
            return BlockTorch.TORCH_SOUTH_AABB;

        case NORTH:
            return BlockTorch.TORCH_NORTH_AABB;

        default:
            return BlockTorch.STANDING_AABB;
        }
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTorch.NULL_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    private boolean canPlaceOn(World world, BlockPos blockposition) {
        Block block = world.getBlockState(blockposition).getBlock();
        boolean flag = block == Blocks.END_GATEWAY || block == Blocks.LIT_PUMPKIN;

        if (world.getBlockState(blockposition).isTopSolid()) {
            return !flag;
        } else {
            boolean flag1 = block instanceof BlockFence || block == Blocks.GLASS || block == Blocks.COBBLESTONE_WALL || block == Blocks.STAINED_GLASS;

            return flag1 && !flag;
        }
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        Iterator iterator = BlockTorch.FACING.getAllowedValues().iterator();

        EnumFacing enumdirection;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            enumdirection = (EnumFacing) iterator.next();
        } while (!this.canPlaceAt(world, blockposition, enumdirection));

        return true;
    }

    private boolean canPlaceAt(World world, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.offset(enumdirection.getOpposite());
        IBlockState iblockdata = world.getBlockState(blockposition1);
        Block block = iblockdata.getBlock();
        BlockFaceShape enumblockfaceshape = iblockdata.getBlockFaceShape(world, blockposition1, enumdirection);

        return enumdirection.equals(EnumFacing.UP) && this.canPlaceOn(world, blockposition1) ? true : (enumdirection != EnumFacing.UP && enumdirection != EnumFacing.DOWN ? !isExceptBlockForAttachWithPiston(block) && enumblockfaceshape == BlockFaceShape.SOLID : false);
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        if (this.canPlaceAt(world, blockposition, enumdirection)) {
            return this.getDefaultState().withProperty(BlockTorch.FACING, enumdirection);
        } else {
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            do {
                if (!iterator.hasNext()) {
                    return this.getDefaultState();
                }

                enumdirection1 = (EnumFacing) iterator.next();
            } while (!this.canPlaceAt(world, blockposition, enumdirection1));

            return this.getDefaultState().withProperty(BlockTorch.FACING, enumdirection1);
        }
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.checkForDrop(world, blockposition, iblockdata);
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.onNeighborChangeInternal(world, blockposition, iblockdata);
    }

    protected boolean onNeighborChangeInternal(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.checkForDrop(world, blockposition, iblockdata)) {
            return true;
        } else {
            EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockTorch.FACING);
            EnumFacing.Axis enumdirection_enumaxis = enumdirection.getAxis();
            EnumFacing enumdirection1 = enumdirection.getOpposite();
            BlockPos blockposition1 = blockposition.offset(enumdirection1);
            boolean flag = false;

            if (enumdirection_enumaxis.isHorizontal() && world.getBlockState(blockposition1).getBlockFaceShape(world, blockposition1, enumdirection) != BlockFaceShape.SOLID) {
                flag = true;
            } else if (enumdirection_enumaxis.isVertical() && !this.canPlaceOn(world, blockposition1)) {
                flag = true;
            }

            if (flag) {
                this.dropBlockAsItem(world, blockposition, iblockdata, 0);
                world.setBlockToAir(blockposition);
                return true;
            } else {
                return false;
            }
        }
    }

    protected boolean checkForDrop(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (iblockdata.getBlock() == this && this.canPlaceAt(world, blockposition, (EnumFacing) iblockdata.getValue(BlockTorch.FACING))) {
            return true;
        } else {
            if (world.getBlockState(blockposition).getBlock() == this) {
                this.dropBlockAsItem(world, blockposition, iblockdata, 0);
                world.setBlockToAir(blockposition);
            }

            return false;
        }
    }

    public IBlockState getStateFromMeta(int i) {
        IBlockState iblockdata = this.getDefaultState();

        switch (i) {
        case 1:
            iblockdata = iblockdata.withProperty(BlockTorch.FACING, EnumFacing.EAST);
            break;

        case 2:
            iblockdata = iblockdata.withProperty(BlockTorch.FACING, EnumFacing.WEST);
            break;

        case 3:
            iblockdata = iblockdata.withProperty(BlockTorch.FACING, EnumFacing.SOUTH);
            break;

        case 4:
            iblockdata = iblockdata.withProperty(BlockTorch.FACING, EnumFacing.NORTH);
            break;

        case 5:
        default:
            iblockdata = iblockdata.withProperty(BlockTorch.FACING, EnumFacing.UP);
        }

        return iblockdata;
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i;

        switch ((EnumFacing) iblockdata.getValue(BlockTorch.FACING)) {
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

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockTorch.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockTorch.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockTorch.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTorch.FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
