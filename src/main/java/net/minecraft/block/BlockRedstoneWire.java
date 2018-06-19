package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneWire extends Block {

    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> field_176348_a = PropertyEnum.func_177709_a("north", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> field_176347_b = PropertyEnum.func_177709_a("east", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> field_176349_M = PropertyEnum.func_177709_a("south", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum<BlockRedstoneWire.EnumAttachPosition> field_176350_N = PropertyEnum.func_177709_a("west", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyInteger field_176351_O = PropertyInteger.func_177719_a("power", 0, 15);
    protected static final AxisAlignedBB[] field_185700_f = new AxisAlignedBB[] { new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
    private boolean field_150181_a = true;
    private final Set<BlockPos> field_150179_b = Sets.newHashSet();

    public BlockRedstoneWire() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockRedstoneWire.field_176348_a, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(BlockRedstoneWire.field_176347_b, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(BlockRedstoneWire.field_176349_M, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(BlockRedstoneWire.field_176350_N, BlockRedstoneWire.EnumAttachPosition.NONE).func_177226_a(BlockRedstoneWire.field_176351_O, Integer.valueOf(0)));
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockRedstoneWire.field_185700_f[func_185699_x(iblockdata.func_185899_b(iblockaccess, blockposition))];
    }

    private static int func_185699_x(IBlockState iblockdata) {
        int i = 0;
        boolean flag = iblockdata.func_177229_b(BlockRedstoneWire.field_176348_a) != BlockRedstoneWire.EnumAttachPosition.NONE;
        boolean flag1 = iblockdata.func_177229_b(BlockRedstoneWire.field_176347_b) != BlockRedstoneWire.EnumAttachPosition.NONE;
        boolean flag2 = iblockdata.func_177229_b(BlockRedstoneWire.field_176349_M) != BlockRedstoneWire.EnumAttachPosition.NONE;
        boolean flag3 = iblockdata.func_177229_b(BlockRedstoneWire.field_176350_N) != BlockRedstoneWire.EnumAttachPosition.NONE;

        if (flag || flag2 && !flag && !flag1 && !flag3) {
            i |= 1 << EnumFacing.NORTH.func_176736_b();
        }

        if (flag1 || flag3 && !flag && !flag1 && !flag2) {
            i |= 1 << EnumFacing.EAST.func_176736_b();
        }

        if (flag2 || flag && !flag1 && !flag2 && !flag3) {
            i |= 1 << EnumFacing.SOUTH.func_176736_b();
        }

        if (flag3 || flag1 && !flag && !flag2 && !flag3) {
            i |= 1 << EnumFacing.WEST.func_176736_b();
        }

        return i;
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        iblockdata = iblockdata.func_177226_a(BlockRedstoneWire.field_176350_N, this.func_176341_c(iblockaccess, blockposition, EnumFacing.WEST));
        iblockdata = iblockdata.func_177226_a(BlockRedstoneWire.field_176347_b, this.func_176341_c(iblockaccess, blockposition, EnumFacing.EAST));
        iblockdata = iblockdata.func_177226_a(BlockRedstoneWire.field_176348_a, this.func_176341_c(iblockaccess, blockposition, EnumFacing.NORTH));
        iblockdata = iblockdata.func_177226_a(BlockRedstoneWire.field_176349_M, this.func_176341_c(iblockaccess, blockposition, EnumFacing.SOUTH));
        return iblockdata;
    }

    private BlockRedstoneWire.EnumAttachPosition func_176341_c(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition.func_177972_a(enumdirection));

        if (!func_176343_a(iblockaccess.func_180495_p(blockposition1), enumdirection) && (iblockdata.func_185915_l() || !func_176346_d(iblockaccess.func_180495_p(blockposition1.func_177977_b())))) {
            IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition.func_177984_a());

            if (!iblockdata1.func_185915_l()) {
                boolean flag = iblockaccess.func_180495_p(blockposition1).func_185896_q() || iblockaccess.func_180495_p(blockposition1).func_177230_c() == Blocks.field_150426_aN;

                if (flag && func_176346_d(iblockaccess.func_180495_p(blockposition1.func_177984_a()))) {
                    if (iblockdata.func_185898_k()) {
                        return BlockRedstoneWire.EnumAttachPosition.UP;
                    }

                    return BlockRedstoneWire.EnumAttachPosition.SIDE;
                }
            }

            return BlockRedstoneWire.EnumAttachPosition.NONE;
        } else {
            return BlockRedstoneWire.EnumAttachPosition.SIDE;
        }
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockRedstoneWire.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185896_q() || world.func_180495_p(blockposition.func_177977_b()).func_177230_c() == Blocks.field_150426_aN;
    }

    private IBlockState func_176338_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        iblockdata = this.func_176345_a(world, blockposition, blockposition, iblockdata);
        ArrayList arraylist = Lists.newArrayList(this.field_150179_b);

        this.field_150179_b.clear();
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition1 = (BlockPos) iterator.next();

            world.func_175685_c(blockposition1, this, false);
        }

        return iblockdata;
    }

    private IBlockState func_176345_a(World world, BlockPos blockposition, BlockPos blockposition1, IBlockState iblockdata) {
        IBlockState iblockdata1 = iblockdata;
        int i = ((Integer) iblockdata.func_177229_b(BlockRedstoneWire.field_176351_O)).intValue();
        byte b0 = 0;
        int j = this.func_176342_a(world, blockposition1, b0);

        this.field_150181_a = false;
        int k = world.func_175687_A(blockposition);

        this.field_150181_a = true;
        if (k > 0 && k > j - 1) {
            j = k;
        }

        int l = 0;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition2 = blockposition.func_177972_a(enumdirection);
            boolean flag = blockposition2.func_177958_n() != blockposition1.func_177958_n() || blockposition2.func_177952_p() != blockposition1.func_177952_p();

            if (flag) {
                l = this.func_176342_a(world, blockposition2, l);
            }

            if (world.func_180495_p(blockposition2).func_185915_l() && !world.func_180495_p(blockposition.func_177984_a()).func_185915_l()) {
                if (flag && blockposition.func_177956_o() >= blockposition1.func_177956_o()) {
                    l = this.func_176342_a(world, blockposition2.func_177984_a(), l);
                }
            } else if (!world.func_180495_p(blockposition2).func_185915_l() && flag && blockposition.func_177956_o() <= blockposition1.func_177956_o()) {
                l = this.func_176342_a(world, blockposition2.func_177977_b(), l);
            }
        }

        if (l > j) {
            j = l - 1;
        } else if (j > 0) {
            --j;
        } else {
            j = 0;
        }

        if (k > j - 1) {
            j = k;
        }

        // CraftBukkit start
        if (i != j) {
            BlockRedstoneEvent event = new BlockRedstoneEvent(world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), i, j);
            world.getServer().getPluginManager().callEvent(event);

            j = event.getNewCurrent();
        }
        // CraftBukkit end

        if (i != j) {
            iblockdata = iblockdata.func_177226_a(BlockRedstoneWire.field_176351_O, Integer.valueOf(j));
            if (world.func_180495_p(blockposition) == iblockdata1) {
                world.func_180501_a(blockposition, iblockdata, 2);
            }

            this.field_150179_b.add(blockposition);
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i1 = aenumdirection.length;

            for (int j1 = 0; j1 < i1; ++j1) {
                EnumFacing enumdirection1 = aenumdirection[j1];

                this.field_150179_b.add(blockposition.func_177972_a(enumdirection1));
            }
        }

        return iblockdata;
    }

    private void func_176344_d(World world, BlockPos blockposition) {
        if (world.func_180495_p(blockposition).func_177230_c() == this) {
            world.func_175685_c(blockposition, this, false);
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }

        }
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            this.func_176338_e(world, blockposition, iblockdata);
            Iterator iterator = EnumFacing.Plane.VERTICAL.iterator();

            EnumFacing enumdirection;

            while (iterator.hasNext()) {
                enumdirection = (EnumFacing) iterator.next();
                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }

            iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                enumdirection = (EnumFacing) iterator.next();
                this.func_176344_d(world, blockposition.func_177972_a(enumdirection));
            }

            iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                enumdirection = (EnumFacing) iterator.next();
                BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);

                if (world.func_180495_p(blockposition1).func_185915_l()) {
                    this.func_176344_d(world, blockposition1.func_177984_a());
                } else {
                    this.func_176344_d(world, blockposition1.func_177977_b());
                }
            }

        }
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        if (!world.field_72995_K) {
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                world.func_175685_c(blockposition.func_177972_a(enumdirection), this, false);
            }

            this.func_176338_e(world, blockposition, iblockdata);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            EnumFacing enumdirection1;

            while (iterator.hasNext()) {
                enumdirection1 = (EnumFacing) iterator.next();
                this.func_176344_d(world, blockposition.func_177972_a(enumdirection1));
            }

            iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                enumdirection1 = (EnumFacing) iterator.next();
                BlockPos blockposition1 = blockposition.func_177972_a(enumdirection1);

                if (world.func_180495_p(blockposition1).func_185915_l()) {
                    this.func_176344_d(world, blockposition1.func_177984_a());
                } else {
                    this.func_176344_d(world, blockposition1.func_177977_b());
                }
            }

        }
    }

    public int func_176342_a(World world, BlockPos blockposition, int i) {
        if (world.func_180495_p(blockposition).func_177230_c() != this) {
            return i;
        } else {
            int j = ((Integer) world.func_180495_p(blockposition).func_177229_b(BlockRedstoneWire.field_176351_O)).intValue();

            return j > i ? j : i;
        }
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            if (this.func_176196_c(world, blockposition)) {
                this.func_176338_e(world, blockposition, iblockdata);
            } else {
                this.func_176226_b(world, blockposition, iblockdata, 0);
                world.func_175698_g(blockposition);
            }

        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151137_ax;
    }

    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return !this.field_150181_a ? 0 : iblockdata.func_185911_a(iblockaccess, blockposition, enumdirection);
    }

    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        if (!this.field_150181_a) {
            return 0;
        } else {
            int i = ((Integer) iblockdata.func_177229_b(BlockRedstoneWire.field_176351_O)).intValue();

            if (i == 0) {
                return 0;
            } else if (enumdirection == EnumFacing.UP) {
                return i;
            } else {
                EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection1 = (EnumFacing) iterator.next();

                    if (this.func_176339_d(iblockaccess, blockposition, enumdirection1)) {
                        enumset.add(enumdirection1);
                    }
                }

                if (enumdirection.func_176740_k().func_176722_c() && enumset.isEmpty()) {
                    return i;
                } else if (enumset.contains(enumdirection) && !enumset.contains(enumdirection.func_176735_f()) && !enumset.contains(enumdirection.func_176746_e())) {
                    return i;
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean func_176339_d(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition1);
        boolean flag = iblockdata.func_185915_l();
        boolean flag1 = iblockaccess.func_180495_p(blockposition.func_177984_a()).func_185915_l();

        return !flag1 && flag && func_176340_e(iblockaccess, blockposition1.func_177984_a()) ? true : (func_176343_a(iblockdata, enumdirection) ? true : (iblockdata.func_177230_c() == Blocks.field_150416_aS && iblockdata.func_177229_b(BlockRedstoneDiode.field_185512_D) == enumdirection ? true : !flag && func_176340_e(iblockaccess, blockposition1.func_177977_b())));
    }

    protected static boolean func_176340_e(IBlockAccess iblockaccess, BlockPos blockposition) {
        return func_176346_d(iblockaccess.func_180495_p(blockposition));
    }

    protected static boolean func_176346_d(IBlockState iblockdata) {
        return func_176343_a(iblockdata, (EnumFacing) null);
    }

    protected static boolean func_176343_a(IBlockState iblockdata, @Nullable EnumFacing enumdirection) {
        Block block = iblockdata.func_177230_c();

        if (block == Blocks.field_150488_af) {
            return true;
        } else if (Blocks.field_150413_aR.func_185547_C(iblockdata)) {
            EnumFacing enumdirection1 = (EnumFacing) iblockdata.func_177229_b(BlockRedstoneRepeater.field_185512_D);

            return enumdirection1 == enumdirection || enumdirection1.func_176734_d() == enumdirection;
        } else {
            return Blocks.field_190976_dk == iblockdata.func_177230_c() ? enumdirection == iblockdata.func_177229_b(BlockObserver.field_176387_N) : iblockdata.func_185897_m() && enumdirection != null;
        }
    }

    public boolean func_149744_f(IBlockState iblockdata) {
        return this.field_150181_a;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151137_ax);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockRedstoneWire.field_176351_O, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockRedstoneWire.field_176351_O)).intValue();
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.func_177226_a(BlockRedstoneWire.field_176348_a, iblockdata.func_177229_b(BlockRedstoneWire.field_176349_M)).func_177226_a(BlockRedstoneWire.field_176347_b, iblockdata.func_177229_b(BlockRedstoneWire.field_176350_N)).func_177226_a(BlockRedstoneWire.field_176349_M, iblockdata.func_177229_b(BlockRedstoneWire.field_176348_a)).func_177226_a(BlockRedstoneWire.field_176350_N, iblockdata.func_177229_b(BlockRedstoneWire.field_176347_b));

        case COUNTERCLOCKWISE_90:
            return iblockdata.func_177226_a(BlockRedstoneWire.field_176348_a, iblockdata.func_177229_b(BlockRedstoneWire.field_176347_b)).func_177226_a(BlockRedstoneWire.field_176347_b, iblockdata.func_177229_b(BlockRedstoneWire.field_176349_M)).func_177226_a(BlockRedstoneWire.field_176349_M, iblockdata.func_177229_b(BlockRedstoneWire.field_176350_N)).func_177226_a(BlockRedstoneWire.field_176350_N, iblockdata.func_177229_b(BlockRedstoneWire.field_176348_a));

        case CLOCKWISE_90:
            return iblockdata.func_177226_a(BlockRedstoneWire.field_176348_a, iblockdata.func_177229_b(BlockRedstoneWire.field_176350_N)).func_177226_a(BlockRedstoneWire.field_176347_b, iblockdata.func_177229_b(BlockRedstoneWire.field_176348_a)).func_177226_a(BlockRedstoneWire.field_176349_M, iblockdata.func_177229_b(BlockRedstoneWire.field_176347_b)).func_177226_a(BlockRedstoneWire.field_176350_N, iblockdata.func_177229_b(BlockRedstoneWire.field_176349_M));

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.func_177226_a(BlockRedstoneWire.field_176348_a, iblockdata.func_177229_b(BlockRedstoneWire.field_176349_M)).func_177226_a(BlockRedstoneWire.field_176349_M, iblockdata.func_177229_b(BlockRedstoneWire.field_176348_a));

        case FRONT_BACK:
            return iblockdata.func_177226_a(BlockRedstoneWire.field_176347_b, iblockdata.func_177229_b(BlockRedstoneWire.field_176350_N)).func_177226_a(BlockRedstoneWire.field_176350_N, iblockdata.func_177229_b(BlockRedstoneWire.field_176347_b));

        default:
            return super.func_185471_a(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockRedstoneWire.field_176348_a, BlockRedstoneWire.field_176347_b, BlockRedstoneWire.field_176349_M, BlockRedstoneWire.field_176350_N, BlockRedstoneWire.field_176351_O});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    static enum EnumAttachPosition implements IStringSerializable {

        UP("up"), SIDE("side"), NONE("none");

        private final String field_176820_d;

        private EnumAttachPosition(String s) {
            this.field_176820_d = s;
        }

        public String toString() {
            return this.func_176610_l();
        }

        public String func_176610_l() {
            return this.field_176820_d;
        }
    }
}
