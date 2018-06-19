package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import java.util.AbstractList;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

// CraftBukkit start
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import com.google.common.collect.ImmutableList;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
// CraftBukkit end

public class BlockPistonBase extends BlockDirectional {

    public static final PropertyBool field_176320_b = PropertyBool.func_177716_a("extended");
    protected static final AxisAlignedBB field_185648_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185649_c = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185650_d = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
    protected static final AxisAlignedBB field_185651_e = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185652_f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB field_185653_g = new AxisAlignedBB(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
    private final boolean field_150082_a;

    public BlockPistonBase(boolean flag) {
        super(Material.field_76233_E);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.NORTH).func_177226_a(BlockPistonBase.field_176320_b, Boolean.valueOf(false)));
        this.field_150082_a = flag;
        this.func_149672_a(SoundType.field_185851_d);
        this.func_149711_c(0.5F);
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public boolean func_176214_u(IBlockState iblockdata) {
        return !((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue();
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
            switch ((EnumFacing) iblockdata.func_177229_b(BlockPistonBase.field_176387_N)) {
            case DOWN:
                return BlockPistonBase.field_185653_g;

            case UP:
            default:
                return BlockPistonBase.field_185652_f;

            case NORTH:
                return BlockPistonBase.field_185651_e;

            case SOUTH:
                return BlockPistonBase.field_185650_d;

            case WEST:
                return BlockPistonBase.field_185649_c;

            case EAST:
                return BlockPistonBase.field_185648_b;
            }
        } else {
            return BlockPistonBase.field_185505_j;
        }
    }

    public boolean func_185481_k(IBlockState iblockdata) {
        return !((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue() || iblockdata.func_177229_b(BlockPistonBase.field_176387_N) == EnumFacing.DOWN;
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, iblockdata.func_185900_c(world, blockposition));
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.func_190914_a(blockposition, entityliving)), 2);
        if (!world.field_72995_K) {
            this.func_176316_e(world, blockposition, iblockdata);
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            this.func_176316_e(world, blockposition, iblockdata);
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K && world.func_175625_s(blockposition) == null) {
            this.func_176316_e(world, blockposition, iblockdata);
        }

    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, EnumFacing.func_190914_a(blockposition, entityliving)).func_177226_a(BlockPistonBase.field_176320_b, Boolean.valueOf(false));
    }

    private void func_176316_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockPistonBase.field_176387_N);
        boolean flag = this.func_176318_b(world, blockposition, enumdirection);

        if (flag && !((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
            if ((new BlockPistonStructureHelper(world, blockposition, enumdirection, true)).func_177253_a()) {
                world.func_175641_c(blockposition, this, 0, enumdirection.func_176745_a());
            }
        } else if (!flag && ((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
            // CraftBukkit start
            if (!this.field_150082_a) {
                org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                BlockPistonRetractEvent event = new BlockPistonRetractEvent(block, ImmutableList.<org.bukkit.block.Block>of(), CraftBlock.notchToBlockFace(enumdirection));
                world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            // PAIL: checkME - what happened to setTypeAndData?
            // CraftBukkit end
            world.func_175641_c(blockposition, this, 1, enumdirection.func_176745_a());
        }

    }

    private boolean func_176318_b(World world, BlockPos blockposition, EnumFacing enumdirection) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        int j;

        for (j = 0; j < i; ++j) {
            EnumFacing enumdirection1 = aenumdirection[j];

            if (enumdirection1 != enumdirection && world.func_175709_b(blockposition.func_177972_a(enumdirection1), enumdirection1)) {
                return true;
            }
        }

        if (world.func_175709_b(blockposition, EnumFacing.DOWN)) {
            return true;
        } else {
            BlockPos blockposition1 = blockposition.func_177984_a();
            EnumFacing[] aenumdirection1 = EnumFacing.values();

            j = aenumdirection1.length;

            for (int k = 0; k < j; ++k) {
                EnumFacing enumdirection2 = aenumdirection1[k];

                if (enumdirection2 != EnumFacing.DOWN && world.func_175709_b(blockposition1.func_177972_a(enumdirection2), enumdirection2)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean func_189539_a(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockPistonBase.field_176387_N);

        if (!world.field_72995_K) {
            boolean flag = this.func_176318_b(world, blockposition, enumdirection);

            if (flag && i == 1) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockPistonBase.field_176320_b, Boolean.valueOf(true)), 2);
                return false;
            }

            if (!flag && i == 0) {
                return false;
            }
        }

        if (i == 0) {
            if (!this.func_176319_a(world, blockposition, enumdirection, true)) {
                return false;
            }

            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockPistonBase.field_176320_b, Boolean.valueOf(true)), 3);
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187715_dR, SoundCategory.BLOCKS, 0.5F, world.field_73012_v.nextFloat() * 0.25F + 0.6F);
        } else if (i == 1) {
            TileEntity tileentity = world.func_175625_s(blockposition.func_177972_a(enumdirection));

            if (tileentity instanceof TileEntityPiston) {
                ((TileEntityPiston) tileentity).func_145866_f();
            }

            world.func_180501_a(blockposition, Blocks.field_180384_M.func_176223_P().func_177226_a(BlockPistonMoving.field_176426_a, enumdirection).func_177226_a(BlockPistonMoving.field_176425_b, this.field_150082_a ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
            world.func_175690_a(blockposition, BlockPistonMoving.func_185588_a(this.func_176203_a(j), enumdirection, false, true));
            if (this.field_150082_a) {
                BlockPos blockposition1 = blockposition.func_177982_a(enumdirection.func_82601_c() * 2, enumdirection.func_96559_d() * 2, enumdirection.func_82599_e() * 2);
                IBlockState iblockdata1 = world.func_180495_p(blockposition1);
                Block block = iblockdata1.func_177230_c();
                boolean flag1 = false;

                if (block == Blocks.field_180384_M) {
                    TileEntity tileentity1 = world.func_175625_s(blockposition1);

                    if (tileentity1 instanceof TileEntityPiston) {
                        TileEntityPiston tileentitypiston = (TileEntityPiston) tileentity1;

                        if (tileentitypiston.func_174930_e() == enumdirection && tileentitypiston.func_145868_b()) {
                            tileentitypiston.func_145866_f();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1 && func_185646_a(iblockdata1, world, blockposition1, enumdirection.func_176734_d(), false, enumdirection) && (iblockdata1.func_185905_o() == EnumPushReaction.NORMAL || block == Blocks.field_150331_J || block == Blocks.field_150320_F)) { // CraftBukkit - remove 'block.getMaterial() != Material.AIR' condition
                    this.func_176319_a(world, blockposition, enumdirection, false);
                }
            } else {
                world.func_175698_g(blockposition.func_177972_a(enumdirection));
            }

            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187712_dQ, SoundCategory.BLOCKS, 0.5F, world.field_73012_v.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    @Nullable
    public static EnumFacing func_176317_b(int i) {
        int j = i & 7;

        return j > 5 ? null : EnumFacing.func_82600_a(j);
    }

    public static boolean func_185646_a(IBlockState iblockdata, World world, BlockPos blockposition, EnumFacing enumdirection, boolean flag, EnumFacing enumdirection1) {
        Block block = iblockdata.func_177230_c();

        if (block == Blocks.field_150343_Z) {
            return false;
        } else if (!world.func_175723_af().func_177746_a(blockposition)) {
            return false;
        } else if (blockposition.func_177956_o() >= 0 && (enumdirection != EnumFacing.DOWN || blockposition.func_177956_o() != 0)) {
            if (blockposition.func_177956_o() <= world.func_72800_K() - 1 && (enumdirection != EnumFacing.UP || blockposition.func_177956_o() != world.func_72800_K() - 1)) {
                if (block != Blocks.field_150331_J && block != Blocks.field_150320_F) {
                    if (iblockdata.func_185887_b(world, blockposition) == -1.0F) {
                        return false;
                    }

                    switch (iblockdata.func_185905_o()) {
                    case BLOCK:
                        return false;

                    case DESTROY:
                        return flag;

                    case PUSH_ONLY:
                        return enumdirection == enumdirection1;
                    }
                } else if (((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
                    return false;
                }

                return !block.func_149716_u();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean func_176319_a(World world, BlockPos blockposition, EnumFacing enumdirection, boolean flag) {
        if (!flag) {
            world.func_175698_g(blockposition.func_177972_a(enumdirection));
        }

        BlockPistonStructureHelper pistonextendschecker = new BlockPistonStructureHelper(world, blockposition, enumdirection, flag);

        if (!pistonextendschecker.func_177253_a()) {
            return false;
        } else {
            List list = pistonextendschecker.func_177254_c();
            ArrayList arraylist = Lists.newArrayList();

            for (int i = 0; i < list.size(); ++i) {
                BlockPos blockposition1 = (BlockPos) list.get(i);

                arraylist.add(world.func_180495_p(blockposition1).func_185899_b(world, blockposition1));
            }

            List list1 = pistonextendschecker.func_177252_d();
            int j = list.size() + list1.size();
            IBlockState[] aiblockdata = new IBlockState[j];
            EnumFacing enumdirection1 = flag ? enumdirection : enumdirection.func_176734_d();
            // CraftBukkit start
            final org.bukkit.block.Block bblock = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

            final List<BlockPos> moved = pistonextendschecker.func_177254_c();
            final List<BlockPos> broken = pistonextendschecker.func_177252_d();

            List<org.bukkit.block.Block> blocks = new AbstractList<org.bukkit.block.Block>() {

                @Override
                public int size() {
                    return moved.size() + broken.size();
                }

                @Override
                public org.bukkit.block.Block get(int index) {
                    if (index >= size() || index < 0) {
                        throw new ArrayIndexOutOfBoundsException(index);
                    }
                    BlockPos pos = (BlockPos) (index < moved.size() ? moved.get(index) : broken.get(index - moved.size()));
                    return bblock.getWorld().getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
                }
            };
            org.bukkit.event.block.BlockPistonEvent event;
            if (flag) {
                event = new BlockPistonExtendEvent(bblock, blocks, CraftBlock.notchToBlockFace(enumdirection1));
            } else {
                event = new BlockPistonRetractEvent(bblock, blocks, CraftBlock.notchToBlockFace(enumdirection1));
            }
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                for (BlockPos b : broken) {
                    world.func_184138_a(b, Blocks.field_150350_a.func_176223_P(), world.func_180495_p(b), 3);
                }
                for (BlockPos b : moved) {
                    world.func_184138_a(b, Blocks.field_150350_a.func_176223_P(), world.func_180495_p(b), 3);
                    b = b.func_177972_a(enumdirection1);
                    world.func_184138_a(b, Blocks.field_150350_a.func_176223_P(), world.func_180495_p(b), 3);
                }
                return false;
            }
            // CraftBukkit end

            int k;
            BlockPos blockposition2;
            IBlockState iblockdata;

            for (k = list1.size() - 1; k >= 0; --k) {
                blockposition2 = (BlockPos) list1.get(k);
                iblockdata = world.func_180495_p(blockposition2);
                iblockdata.func_177230_c().func_176226_b(world, blockposition2, iblockdata, 0);
                world.func_180501_a(blockposition2, Blocks.field_150350_a.func_176223_P(), 4);
                --j;
                aiblockdata[j] = iblockdata;
            }

            for (k = list.size() - 1; k >= 0; --k) {
                blockposition2 = (BlockPos) list.get(k);
                iblockdata = world.func_180495_p(blockposition2);
                world.func_180501_a(blockposition2, Blocks.field_150350_a.func_176223_P(), 2);
                blockposition2 = blockposition2.func_177972_a(enumdirection1);
                world.func_180501_a(blockposition2, Blocks.field_180384_M.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, enumdirection), 4);
                world.func_175690_a(blockposition2, BlockPistonMoving.func_185588_a((IBlockState) arraylist.get(k), enumdirection, flag, false));
                --j;
                aiblockdata[j] = iblockdata;
            }

            BlockPos blockposition3 = blockposition.func_177972_a(enumdirection);

            if (flag) {
                BlockPistonExtension.EnumPistonType blockpistonextension_enumpistontype = this.field_150082_a ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;

                iblockdata = Blocks.field_150332_K.func_176223_P().func_177226_a(BlockPistonExtension.field_176387_N, enumdirection).func_177226_a(BlockPistonExtension.field_176325_b, blockpistonextension_enumpistontype);
                IBlockState iblockdata1 = Blocks.field_180384_M.func_176223_P().func_177226_a(BlockPistonMoving.field_176426_a, enumdirection).func_177226_a(BlockPistonMoving.field_176425_b, this.field_150082_a ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);

                world.func_180501_a(blockposition3, iblockdata1, 4);
                world.func_175690_a(blockposition3, BlockPistonMoving.func_185588_a(iblockdata, enumdirection, true, true));
            }

            int l;

            for (l = list1.size() - 1; l >= 0; --l) {
                world.func_175685_c((BlockPos) list1.get(l), aiblockdata[j++].func_177230_c(), false);
            }

            for (l = list.size() - 1; l >= 0; --l) {
                world.func_175685_c((BlockPos) list.get(l), aiblockdata[j++].func_177230_c(), false);
            }

            if (flag) {
                world.func_175685_c(blockposition3, Blocks.field_150332_K, false);
            }

            return true;
        }
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPistonBase.field_176387_N, func_176317_b(i)).func_177226_a(BlockPistonBase.field_176320_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockPistonBase.field_176387_N)).func_176745_a();

        if (((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockPistonBase.field_176387_N, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockPistonBase.field_176387_N)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockPistonBase.field_176387_N)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPistonBase.field_176387_N, BlockPistonBase.field_176320_b});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        iblockdata = this.func_176221_a(iblockdata, iblockaccess, blockposition);
        return iblockdata.func_177229_b(BlockPistonBase.field_176387_N) != enumdirection.func_176734_d() && ((Boolean) iblockdata.func_177229_b(BlockPistonBase.field_176320_b)).booleanValue() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
}
