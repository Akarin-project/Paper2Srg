package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer {

    public static final PropertyDirection FACING = BlockPistonExtension.FACING;
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;

    public BlockPistonMoving() {
        super(Material.PISTON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPistonMoving.FACING, EnumFacing.NORTH).withProperty(BlockPistonMoving.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.setHardness(-1.0F);
    }

    @Nullable
    public TileEntity createNewTileEntity(World world, int i) {
        return null;
    }

    public static TileEntity createTilePiston(IBlockState iblockdata, EnumFacing enumdirection, boolean flag, boolean flag1) {
        return new TileEntityPiston(iblockdata, enumdirection, flag, flag1);
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityPiston) {
            ((TileEntityPiston) tileentity).clearPistonTileEntity();
        } else {
            super.breakBlock(world, blockposition, iblockdata);
        }

    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return false;
    }

    public void onBlockDestroyedByPlayer(World world, BlockPos blockposition, IBlockState iblockdata) {
        BlockPos blockposition1 = blockposition.offset(((EnumFacing) iblockdata.getValue(BlockPistonMoving.FACING)).getOpposite());
        IBlockState iblockdata1 = world.getBlockState(blockposition1);

        if (iblockdata1.getBlock() instanceof BlockPistonBase && ((Boolean) iblockdata1.getValue(BlockPistonBase.EXTENDED)).booleanValue()) {
            world.setBlockToAir(blockposition1);
        }

    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.isRemote && world.getTileEntity(blockposition) == null) {
            world.setBlockToAir(blockposition);
            return true;
        } else {
            return false;
        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.AIR;
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.isRemote) {
            TileEntityPiston tileentitypiston = this.getTilePistonAt(world, blockposition);

            if (tileentitypiston != null) {
                IBlockState iblockdata1 = tileentitypiston.getPistonState();

                iblockdata1.getBlock().dropBlockAsItem(world, blockposition, iblockdata1, 0);
            }
        }
    }

    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState iblockdata, World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
        return null;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            world.getTileEntity(blockposition);
        }

    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(iblockaccess, blockposition);

        return tileentitypiston == null ? null : tileentitypiston.getAABB(iblockaccess, blockposition);
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(world, blockposition);

        if (tileentitypiston != null) {
            tileentitypiston.addCollissionAABBs(world, blockposition, axisalignedbb, list, entity);
        }

    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntityPiston tileentitypiston = this.getTilePistonAt(iblockaccess, blockposition);

        return tileentitypiston != null ? tileentitypiston.getAABB(iblockaccess, blockposition) : BlockPistonMoving.FULL_BLOCK_AABB;
    }

    @Nullable
    private TileEntityPiston getTilePistonAt(IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

        return tileentity instanceof TileEntityPiston ? (TileEntityPiston) tileentity : null;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return ItemStack.EMPTY;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPistonMoving.FACING, BlockPistonExtension.getFacing(i)).withProperty(BlockPistonMoving.TYPE, (i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockPistonMoving.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockPistonMoving.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockPistonMoving.FACING)));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockPistonMoving.FACING)).getIndex();

        if (iblockdata.getValue(BlockPistonMoving.TYPE) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPistonMoving.FACING, BlockPistonMoving.TYPE});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
