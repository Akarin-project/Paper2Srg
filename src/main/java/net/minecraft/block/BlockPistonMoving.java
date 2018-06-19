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

    public static final PropertyDirection field_176426_a = BlockPistonExtension.field_176387_N;
    public static final PropertyEnum<BlockPistonExtension.EnumPistonType> field_176425_b = BlockPistonExtension.field_176325_b;

    public BlockPistonMoving() {
        super(Material.field_76233_E);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPistonMoving.field_176426_a, EnumFacing.NORTH).func_177226_a(BlockPistonMoving.field_176425_b, BlockPistonExtension.EnumPistonType.DEFAULT));
        this.func_149711_c(-1.0F);
    }

    @Nullable
    public TileEntity func_149915_a(World world, int i) {
        return null;
    }

    public static TileEntity func_185588_a(IBlockState iblockdata, EnumFacing enumdirection, boolean flag, boolean flag1) {
        return new TileEntityPiston(iblockdata, enumdirection, flag, flag1);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityPiston) {
            ((TileEntityPiston) tileentity).func_145866_f();
        } else {
            super.func_180663_b(world, blockposition, iblockdata);
        }

    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return false;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return false;
    }

    public void func_176206_d(World world, BlockPos blockposition, IBlockState iblockdata) {
        BlockPos blockposition1 = blockposition.func_177972_a(((EnumFacing) iblockdata.func_177229_b(BlockPistonMoving.field_176426_a)).func_176734_d());
        IBlockState iblockdata1 = world.func_180495_p(blockposition1);

        if (iblockdata1.func_177230_c() instanceof BlockPistonBase && ((Boolean) iblockdata1.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
            world.func_175698_g(blockposition1);
        }

    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.field_72995_K && world.func_175625_s(blockposition) == null) {
            world.func_175698_g(blockposition);
            return true;
        } else {
            return false;
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.field_72995_K) {
            TileEntityPiston tileentitypiston = this.func_185589_c(world, blockposition);

            if (tileentitypiston != null) {
                IBlockState iblockdata1 = tileentitypiston.func_174927_b();

                iblockdata1.func_177230_c().func_176226_b(world, blockposition, iblockdata1, 0);
            }
        }
    }

    @Nullable
    public RayTraceResult func_180636_a(IBlockState iblockdata, World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
        return null;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            world.func_175625_s(blockposition);
        }

    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntityPiston tileentitypiston = this.func_185589_c(iblockaccess, blockposition);

        return tileentitypiston == null ? null : tileentitypiston.func_184321_a(iblockaccess, blockposition);
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        TileEntityPiston tileentitypiston = this.func_185589_c(world, blockposition);

        if (tileentitypiston != null) {
            tileentitypiston.func_190609_a(world, blockposition, axisalignedbb, list, entity);
        }

    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntityPiston tileentitypiston = this.func_185589_c(iblockaccess, blockposition);

        return tileentitypiston != null ? tileentitypiston.func_184321_a(iblockaccess, blockposition) : BlockPistonMoving.field_185505_j;
    }

    @Nullable
    private TileEntityPiston func_185589_c(IBlockAccess iblockaccess, BlockPos blockposition) {
        TileEntity tileentity = iblockaccess.func_175625_s(blockposition);

        return tileentity instanceof TileEntityPiston ? (TileEntityPiston) tileentity : null;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return ItemStack.field_190927_a;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPistonMoving.field_176426_a, BlockPistonExtension.func_176322_b(i)).func_177226_a(BlockPistonMoving.field_176425_b, (i & 8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockPistonMoving.field_176426_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockPistonMoving.field_176426_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockPistonMoving.field_176426_a)));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockPistonMoving.field_176426_a)).func_176745_a();

        if (iblockdata.func_177229_b(BlockPistonMoving.field_176425_b) == BlockPistonExtension.EnumPistonType.STICKY) {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPistonMoving.field_176426_a, BlockPistonMoving.field_176425_b});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
