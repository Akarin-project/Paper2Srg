package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;

// CraftBukkit start
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public abstract class BlockButton extends BlockDirectional {

    public static final PropertyBool field_176584_b = PropertyBool.func_177716_a("powered");
    protected static final AxisAlignedBB field_185618_b = new AxisAlignedBB(0.3125D, 0.875D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185620_c = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.125D, 0.625D);
    protected static final AxisAlignedBB field_185622_d = new AxisAlignedBB(0.3125D, 0.375D, 0.875D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB field_185624_e = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
    protected static final AxisAlignedBB field_185626_f = new AxisAlignedBB(0.875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB field_185628_g = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.125D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB field_185619_B = new AxisAlignedBB(0.3125D, 0.9375D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB field_185621_C = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.0625D, 0.625D);
    protected static final AxisAlignedBB field_185623_D = new AxisAlignedBB(0.3125D, 0.375D, 0.9375D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB field_185625_E = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.0625D);
    protected static final AxisAlignedBB field_185627_F = new AxisAlignedBB(0.9375D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB field_185629_G = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.0625D, 0.625D, 0.6875D);
    private final boolean field_150047_a;

    protected BlockButton(boolean flag) {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockButton.field_176387_N, EnumFacing.NORTH).func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78028_d);
        this.field_150047_a = flag;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockButton.field_185506_k;
    }

    public int func_149738_a(World world) {
        return this.field_150047_a ? 30 : 20;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return func_181088_a(world, blockposition, enumdirection);
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (func_181088_a(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean func_181088_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176734_d());
        IBlockState iblockdata = world.func_180495_p(blockposition1);
        boolean flag = iblockdata.func_193401_d(world, blockposition1, enumdirection) == BlockFaceShape.SOLID;
        Block block = iblockdata.func_177230_c();

        return enumdirection == EnumFacing.UP ? block == Blocks.field_150438_bZ || !func_193384_b(block) && flag : !func_193382_c(block) && flag;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return func_181088_a(world, blockposition, enumdirection) ? this.func_176223_P().func_177226_a(BlockButton.field_176387_N, enumdirection).func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(false)) : this.func_176223_P().func_177226_a(BlockButton.field_176387_N, EnumFacing.DOWN).func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(false));
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (this.func_176583_e(world, blockposition, iblockdata) && !func_181088_a(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N))) {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
        }

    }

    private boolean func_176583_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.func_176196_c(world, blockposition)) {
            return true;
        } else {
            this.func_176226_b(world, blockposition, iblockdata, 0);
            world.func_175698_g(blockposition);
            return false;
        }
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N);
        boolean flag = ((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue();

        switch (enumdirection) {
        case EAST:
            return flag ? BlockButton.field_185629_G : BlockButton.field_185628_g;

        case WEST:
            return flag ? BlockButton.field_185627_F : BlockButton.field_185626_f;

        case SOUTH:
            return flag ? BlockButton.field_185625_E : BlockButton.field_185624_e;

        case NORTH:
        default:
            return flag ? BlockButton.field_185623_D : BlockButton.field_185622_d;

        case UP:
            return flag ? BlockButton.field_185621_C : BlockButton.field_185620_c;

        case DOWN:
            return flag ? BlockButton.field_185619_B : BlockButton.field_185618_b;
        }
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue()) {
            return true;
        } else {
            // CraftBukkit start
            boolean powered = ((Boolean) iblockdata.func_177229_b(field_176584_b));
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return true;
            }
            // CraftBukkit end
            world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(true)), 3);
            world.func_175704_b(blockposition, blockposition);
            this.func_185615_a(entityhuman, world, blockposition);
            this.func_176582_b(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N));
            world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
            return true;
        }
    }

    protected abstract void func_185615_a(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition);

    protected abstract void func_185617_b(World world, BlockPos blockposition);

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue()) {
            this.func_176582_b(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N));
        }

        super.func_180663_b(world, blockposition, iblockdata);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return ((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue() ? 15 : 0;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue() ? 0 : (iblockdata.func_177229_b(BlockButton.field_176387_N) == enumdirection ? 15 : 0);
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return true;
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            if (((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue()) {
                if (this.field_150047_a) {
                    this.func_185616_e(iblockdata, world, blockposition);
                } else {
                    // CraftBukkit start
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
                    world.getServer().getPluginManager().callEvent(eventRedstone);

                    if (eventRedstone.getNewCurrent() > 0) {
                        return;
                    }
                    // CraftBukkit end
                    world.func_175656_a(blockposition, iblockdata.func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(false)));
                    this.func_176582_b(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N));
                    this.func_185617_b(world, blockposition);
                    world.func_175704_b(blockposition, blockposition);
                }

            }
        }
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.field_72995_K) {
            if (this.field_150047_a) {
                if (!((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue()) {
                    this.func_185616_e(iblockdata, world, blockposition);
                }
            }
        }
    }

    private void func_185616_e(IBlockState iblockdata, World world, BlockPos blockposition) {
        List list = world.func_72872_a(EntityArrow.class, iblockdata.func_185900_c(world, blockposition).func_186670_a(blockposition));
        boolean flag = !list.isEmpty();
        boolean flag1 = ((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue();

        // CraftBukkit start - Call interact event when arrows turn on wooden buttons
        if (flag1 != flag && flag) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            boolean allowed = false;

            // If all of the events are cancelled block the button press, else allow
            for (Object object : list) {
                if (object != null) {
                    EntityInteractEvent event = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag && !flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 0, 15);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() <= 0) {
                return;
            }
            // CraftBukkit end
            world.func_175656_a(blockposition, iblockdata.func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(true)));
            this.func_176582_b(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N));
            world.func_175704_b(blockposition, blockposition);
            this.func_185615_a((EntityPlayer) null, world, blockposition);
        }

        if (!flag && flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() > 0) {
                return;
            }
            // CraftBukkit end
            world.func_175656_a(blockposition, iblockdata.func_177226_a(BlockButton.field_176584_b, Boolean.valueOf(false)));
            this.func_176582_b(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N));
            world.func_175704_b(blockposition, blockposition);
            this.func_185617_b(world, blockposition);
        }

        if (flag) {
            world.func_175684_a(new BlockPos(blockposition), (Block) this, this.func_149738_a(world));
        }

    }

    private void func_176582_b(World world, BlockPos blockposition, EnumFacing enumdirection) {
        world.func_175685_c(blockposition, this, false);
        world.func_175685_c(blockposition.func_177972_a(enumdirection.func_176734_d()), this, false);
    }

    public IBlockState func_176203_a(int i) {
        EnumFacing enumdirection;

        switch (i & 7) {
        case 0:
            enumdirection = EnumFacing.DOWN;
            break;

        case 1:
            enumdirection = EnumFacing.EAST;
            break;

        case 2:
            enumdirection = EnumFacing.WEST;
            break;

        case 3:
            enumdirection = EnumFacing.SOUTH;
            break;

        case 4:
            enumdirection = EnumFacing.NORTH;
            break;

        case 5:
        default:
            enumdirection = EnumFacing.UP;
        }

        return this.func_176223_P().func_177226_a(BlockButton.field_176387_N, enumdirection).func_177226_a(BlockButton.field_176584_b, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i;

        switch ((EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N)) {
        case EAST:
            i = 1;
            break;

        case WEST:
            i = 2;
            break;

        case SOUTH:
            i = 3;
            break;

        case NORTH:
            i = 4;
            break;

        case UP:
        default:
            i = 5;
            break;

        case DOWN:
            i = 0;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockButton.field_176584_b)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockButton.field_176387_N, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockButton.field_176387_N)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockButton.field_176387_N, BlockButton.field_176584_b});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
