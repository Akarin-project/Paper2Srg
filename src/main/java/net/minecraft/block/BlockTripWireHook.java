package net.minecraft.block;

import com.google.common.base.MoreObjects;
import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockTripWireHook extends Block {

    public static final PropertyDirection field_176264_a = BlockHorizontal.field_185512_D;
    public static final PropertyBool field_176263_b = PropertyBool.func_177716_a("powered");
    public static final PropertyBool field_176265_M = PropertyBool.func_177716_a("attached");
    protected static final AxisAlignedBB field_185743_d = new AxisAlignedBB(0.3125D, 0.0D, 0.625D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB field_185744_e = new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.625D, 0.375D);
    protected static final AxisAlignedBB field_185745_f = new AxisAlignedBB(0.625D, 0.0D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB field_185746_g = new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 0.375D, 0.625D, 0.6875D);

    public BlockTripWireHook() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockTripWireHook.field_176264_a, EnumFacing.NORTH).func_177226_a(BlockTripWireHook.field_176263_b, Boolean.valueOf(false)).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78028_d);
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        switch ((EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a)) {
        case EAST:
        default:
            return BlockTripWireHook.field_185746_g;

        case WEST:
            return BlockTripWireHook.field_185745_f;

        case SOUTH:
            return BlockTripWireHook.field_185744_e;

        case NORTH:
            return BlockTripWireHook.field_185743_d;
        }
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTripWireHook.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        EnumFacing enumdirection1 = enumdirection.func_176734_d();
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection1);
        IBlockState iblockdata = world.func_180495_p(blockposition1);
        boolean flag = func_193382_c(iblockdata.func_177230_c());

        return !flag && enumdirection.func_176740_k().func_176722_c() && iblockdata.func_193401_d(world, blockposition1, enumdirection) == BlockFaceShape.SOLID && !iblockdata.func_185897_m();
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        EnumFacing enumdirection;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            enumdirection = (EnumFacing) iterator.next();
        } while (!this.func_176198_a(world, blockposition, enumdirection));

        return true;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        IBlockState iblockdata = this.func_176223_P().func_177226_a(BlockTripWireHook.field_176263_b, Boolean.valueOf(false)).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(false));

        if (enumdirection.func_176740_k().func_176722_c()) {
            iblockdata = iblockdata.func_177226_a(BlockTripWireHook.field_176264_a, enumdirection);
        }

        return iblockdata;
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        this.func_176260_a(world, blockposition, iblockdata, false, false, -1, (IBlockState) null);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (block != this) {
            if (this.func_176261_e(world, blockposition, iblockdata)) {
                EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a);

                if (!this.func_176198_a(world, blockposition, enumdirection)) {
                    this.func_176226_b(world, blockposition, iblockdata, 0);
                    world.func_175698_g(blockposition);
                }
            }

        }
    }

    public void func_176260_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag, boolean flag1, int i, @Nullable IBlockState iblockdata1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a);
        boolean flag2 = ((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176265_M)).booleanValue();
        boolean flag3 = ((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176263_b)).booleanValue();
        boolean flag4 = !flag;
        boolean flag5 = false;
        int j = 0;
        IBlockState[] aiblockdata = new IBlockState[42];

        BlockPos blockposition1;

        for (int k = 1; k < 42; ++k) {
            blockposition1 = blockposition.func_177967_a(enumdirection, k);
            IBlockState iblockdata2 = world.func_180495_p(blockposition1);

            if (iblockdata2.func_177230_c() == Blocks.field_150479_bC) {
                if (iblockdata2.func_177229_b(BlockTripWireHook.field_176264_a) == enumdirection.func_176734_d()) {
                    j = k;
                }
                break;
            }

            if (iblockdata2.func_177230_c() != Blocks.field_150473_bD && k != i) {
                aiblockdata[k] = null;
                flag4 = false;
            } else {
                if (k == i) {
                    iblockdata2 = (IBlockState) MoreObjects.firstNonNull(iblockdata1, iblockdata2);
                }

                boolean flag6 = !((Boolean) iblockdata2.func_177229_b(BlockTripWire.field_176295_N)).booleanValue();
                boolean flag7 = ((Boolean) iblockdata2.func_177229_b(BlockTripWire.field_176293_a)).booleanValue();

                flag5 |= flag6 && flag7;
                aiblockdata[k] = iblockdata2;
                if (k == i) {
                    world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
                    flag4 &= flag6;
                }
            }
        }

        flag4 &= j > 1;
        flag5 &= flag4;
        IBlockState iblockdata3 = this.func_176223_P().func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(flag4)).func_177226_a(BlockTripWireHook.field_176263_b, Boolean.valueOf(flag5));

        if (j > 0) {
            blockposition1 = blockposition.func_177967_a(enumdirection, j);
            EnumFacing enumdirection1 = enumdirection.func_176734_d();

            world.func_180501_a(blockposition1, iblockdata3.func_177226_a(BlockTripWireHook.field_176264_a, enumdirection1), 3);
            this.func_176262_b(world, blockposition1, enumdirection1);
            this.func_180694_a(world, blockposition1, flag4, flag5, flag2, flag3);
        }

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

        BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
        world.getServer().getPluginManager().callEvent(eventRedstone);

        if (eventRedstone.getNewCurrent() > 0) {
            return;
        }
        // CraftBukkit end

        this.func_180694_a(world, blockposition, flag4, flag5, flag2, flag3);
        if (!flag) {
            world.func_180501_a(blockposition, iblockdata3.func_177226_a(BlockTripWireHook.field_176264_a, enumdirection), 3);
            if (flag1) {
                this.func_176262_b(world, blockposition, enumdirection);
            }
        }

        if (flag2 != flag4) {
            for (int l = 1; l < j; ++l) {
                BlockPos blockposition2 = blockposition.func_177967_a(enumdirection, l);
                IBlockState iblockdata4 = aiblockdata[l];

                if (iblockdata4 != null && world.func_180495_p(blockposition2).func_185904_a() != Material.field_151579_a) {
                    world.func_180501_a(blockposition2, iblockdata4.func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf(flag4)), 3);
                }
            }
        }

    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.func_176260_a(world, blockposition, iblockdata, false, true, -1, (IBlockState) null);
    }

    private void func_180694_a(World world, BlockPos blockposition, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        if (flag1 && !flag3) {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187907_gg, SoundCategory.BLOCKS, 0.4F, 0.6F);
        } else if (!flag1 && flag3) {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187906_gf, SoundCategory.BLOCKS, 0.4F, 0.5F);
        } else if (flag && !flag2) {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187905_ge, SoundCategory.BLOCKS, 0.4F, 0.7F);
        } else if (!flag && flag2) {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187908_gh, SoundCategory.BLOCKS, 0.4F, 1.2F / (world.field_73012_v.nextFloat() * 0.2F + 0.9F));
        }

    }

    private void func_176262_b(World world, BlockPos blockposition, EnumFacing enumdirection) {
        world.func_175685_c(blockposition, this, false);
        world.func_175685_c(blockposition.func_177972_a(enumdirection.func_176734_d()), this, false);
    }

    private boolean func_176261_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_176196_c(world, blockposition)) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            return false;
        } else {
            return true;
        }
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        boolean flag = ((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176265_M)).booleanValue();
        boolean flag1 = ((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176263_b)).booleanValue();

        if (flag || flag1) {
            this.func_176260_a(world, blockposition, iblockdata, true, false, -1, (IBlockState) null);
        }

        if (flag1) {
            world.func_175685_c(blockposition, this, false);
            world.func_175685_c(blockposition.func_177972_a(((EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a)).func_176734_d()), this, false);
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176263_b)).booleanValue() ? 15 : 0;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176263_b)).booleanValue() ? 0 : (iblockdata.func_177229_b(BlockTripWireHook.field_176264_a) == enumdirection ? 15 : 0);
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockTripWireHook.field_176264_a, EnumFacing.func_176731_b(i & 3)).func_177226_a(BlockTripWireHook.field_176263_b, Boolean.valueOf((i & 8) > 0)).func_177226_a(BlockTripWireHook.field_176265_M, Boolean.valueOf((i & 4) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a)).func_176736_b();

        if (((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176263_b)).booleanValue()) {
            i |= 8;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockTripWireHook.field_176265_M)).booleanValue()) {
            i |= 4;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockTripWireHook.field_176264_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockTripWireHook.field_176264_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockTripWireHook.field_176264_a, BlockTripWireHook.field_176263_b, BlockTripWireHook.field_176265_M});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
