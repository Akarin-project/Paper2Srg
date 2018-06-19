package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block {

    protected static final AxisAlignedBB field_185590_a = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    protected static final AxisAlignedBB field_190959_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected final boolean field_150053_a;

    public static boolean func_176562_d(World world, BlockPos blockposition) {
        return func_176563_d(world.func_180495_p(blockposition));
    }

    public static boolean func_176563_d(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return block == Blocks.field_150448_aq || block == Blocks.field_150318_D || block == Blocks.field_150319_E || block == Blocks.field_150408_cc;
    }

    protected BlockRailBase(boolean flag) {
        super(Material.field_151594_q);
        this.field_150053_a = flag;
        this.func_149647_a(CreativeTabs.field_78029_e);
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockRailBase.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = iblockdata.func_177230_c() == this ? (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(this.func_176560_l()) : null;

        return blockminecarttrackabstract_enumtrackposition != null && blockminecarttrackabstract_enumtrackposition.func_177018_c() ? BlockRailBase.field_190959_b : BlockRailBase.field_185590_a;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185896_q();
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!world.field_72995_K) {
            iblockdata = this.func_176564_a(world, blockposition, iblockdata, true);
            if (this.field_150053_a) {
                iblockdata.func_189546_a(world, blockposition, this, blockposition);
            }
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(this.func_176560_l());
            boolean flag = false;

            if (!world.func_180495_p(blockposition.func_177977_b()).func_185896_q()) {
                flag = true;
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !world.func_180495_p(blockposition.func_177974_f()).func_185896_q()) {
                flag = true;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !world.func_180495_p(blockposition.func_177976_e()).func_185896_q()) {
                flag = true;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !world.func_180495_p(blockposition.func_177978_c()).func_185896_q()) {
                flag = true;
            } else if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !world.func_180495_p(blockposition.func_177968_d()).func_185896_q()) {
                flag = true;
            }

            if (flag && !world.func_175623_d(blockposition)) {
                this.func_176226_b(world, blockposition, iblockdata, 0);
                world.func_175698_g(blockposition);
            } else {
                this.func_189541_b(iblockdata, world, blockposition, block);
            }

        }
    }

    protected void func_189541_b(IBlockState iblockdata, World world, BlockPos blockposition, Block block) {}

    protected IBlockState func_176564_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return world.field_72995_K ? iblockdata : (new BlockRailBase.Rail(world, blockposition, iblockdata)).func_180364_a(world.func_175640_z(blockposition), flag).func_180362_b();
    }

    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return EnumPushReaction.NORMAL;
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_180663_b(world, blockposition, iblockdata);
        if (((BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(this.func_176560_l())).func_177018_c()) {
            world.func_175685_c(blockposition.func_177984_a(), this, false);
        }

        if (this.field_150053_a) {
            world.func_175685_c(blockposition, this, false);
            world.func_175685_c(blockposition.func_177977_b(), this, false);
        }

    }

    public abstract IProperty<BlockRailBase.EnumRailDirection> func_176560_l();

    public static enum EnumRailDirection implements IStringSerializable {

        NORTH_SOUTH(0, "north_south"), EAST_WEST(1, "east_west"), ASCENDING_EAST(2, "ascending_east"), ASCENDING_WEST(3, "ascending_west"), ASCENDING_NORTH(4, "ascending_north"), ASCENDING_SOUTH(5, "ascending_south"), SOUTH_EAST(6, "south_east"), SOUTH_WEST(7, "south_west"), NORTH_WEST(8, "north_west"), NORTH_EAST(9, "north_east");

        private static final BlockRailBase.EnumRailDirection[] field_177030_k = new BlockRailBase.EnumRailDirection[values().length];
        private final int field_177027_l;
        private final String field_177028_m;

        private EnumRailDirection(int i, String s) {
            this.field_177027_l = i;
            this.field_177028_m = s;
        }

        public int func_177015_a() {
            return this.field_177027_l;
        }

        public String toString() {
            return this.field_177028_m;
        }

        public boolean func_177018_c() {
            return this == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || this == BlockRailBase.EnumRailDirection.ASCENDING_EAST || this == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH || this == BlockRailBase.EnumRailDirection.ASCENDING_WEST;
        }

        public static BlockRailBase.EnumRailDirection func_177016_a(int i) {
            if (i < 0 || i >= BlockRailBase.EnumRailDirection.field_177030_k.length) {
                i = 0;
            }

            return BlockRailBase.EnumRailDirection.field_177030_k[i];
        }

        public String func_176610_l() {
            return this.field_177028_m;
        }

        static {
            BlockRailBase.EnumRailDirection[] ablockminecarttrackabstract_enumtrackposition = values();
            int i = ablockminecarttrackabstract_enumtrackposition.length;

            for (int j = 0; j < i; ++j) {
                BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = ablockminecarttrackabstract_enumtrackposition[j];

                BlockRailBase.EnumRailDirection.field_177030_k[blockminecarttrackabstract_enumtrackposition.func_177015_a()] = blockminecarttrackabstract_enumtrackposition;
            }

        }
    }

    public class Rail {

        private final World field_150660_b;
        private final BlockPos field_180367_c;
        private final BlockRailBase field_180365_d;
        private IBlockState field_180366_e;
        private final boolean field_150656_f;
        private final List<BlockPos> field_150657_g = Lists.newArrayList();

        public Rail(World world, BlockPos blockposition, IBlockState iblockdata) {
            this.field_150660_b = world;
            this.field_180367_c = blockposition;
            this.field_180366_e = iblockdata;
            this.field_180365_d = (BlockRailBase) iblockdata.func_177230_c();
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(this.field_180365_d.func_176560_l());

            this.field_150656_f = this.field_180365_d.field_150053_a;
            this.func_180360_a(blockminecarttrackabstract_enumtrackposition);
        }

        public List<BlockPos> func_185763_a() {
            return this.field_150657_g;
        }

        private void func_180360_a(BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition) {
            this.field_150657_g.clear();
            switch (blockminecarttrackabstract_enumtrackposition) {
            case NORTH_SOUTH:
                this.field_150657_g.add(this.field_180367_c.func_177978_c());
                this.field_150657_g.add(this.field_180367_c.func_177968_d());
                break;

            case EAST_WEST:
                this.field_150657_g.add(this.field_180367_c.func_177976_e());
                this.field_150657_g.add(this.field_180367_c.func_177974_f());
                break;

            case ASCENDING_EAST:
                this.field_150657_g.add(this.field_180367_c.func_177976_e());
                this.field_150657_g.add(this.field_180367_c.func_177974_f().func_177984_a());
                break;

            case ASCENDING_WEST:
                this.field_150657_g.add(this.field_180367_c.func_177976_e().func_177984_a());
                this.field_150657_g.add(this.field_180367_c.func_177974_f());
                break;

            case ASCENDING_NORTH:
                this.field_150657_g.add(this.field_180367_c.func_177978_c().func_177984_a());
                this.field_150657_g.add(this.field_180367_c.func_177968_d());
                break;

            case ASCENDING_SOUTH:
                this.field_150657_g.add(this.field_180367_c.func_177978_c());
                this.field_150657_g.add(this.field_180367_c.func_177968_d().func_177984_a());
                break;

            case SOUTH_EAST:
                this.field_150657_g.add(this.field_180367_c.func_177974_f());
                this.field_150657_g.add(this.field_180367_c.func_177968_d());
                break;

            case SOUTH_WEST:
                this.field_150657_g.add(this.field_180367_c.func_177976_e());
                this.field_150657_g.add(this.field_180367_c.func_177968_d());
                break;

            case NORTH_WEST:
                this.field_150657_g.add(this.field_180367_c.func_177976_e());
                this.field_150657_g.add(this.field_180367_c.func_177978_c());
                break;

            case NORTH_EAST:
                this.field_150657_g.add(this.field_180367_c.func_177974_f());
                this.field_150657_g.add(this.field_180367_c.func_177978_c());
            }

        }

        private void func_150651_b() {
            for (int i = 0; i < this.field_150657_g.size(); ++i) {
                BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = this.func_180697_b((BlockPos) this.field_150657_g.get(i));

                if (blockminecarttrackabstract_minecarttracklogic != null && blockminecarttrackabstract_minecarttracklogic.func_150653_a(this)) {
                    this.field_150657_g.set(i, blockminecarttrackabstract_minecarttracklogic.field_180367_c);
                } else {
                    this.field_150657_g.remove(i--);
                }
            }

        }

        private boolean func_180359_a(BlockPos blockposition) {
            return BlockRailBase.func_176562_d(this.field_150660_b, blockposition) || BlockRailBase.func_176562_d(this.field_150660_b, blockposition.func_177984_a()) || BlockRailBase.func_176562_d(this.field_150660_b, blockposition.func_177977_b());
        }

        @Nullable
        private BlockRailBase.Rail func_180697_b(BlockPos blockposition) {
            IBlockState iblockdata = this.field_150660_b.func_180495_p(blockposition);

            if (BlockRailBase.func_176563_d(iblockdata)) {
                return BlockRailBase.this.new Rail(this.field_150660_b, blockposition, iblockdata);
            } else {
                BlockPos blockposition1 = blockposition.func_177984_a();

                iblockdata = this.field_150660_b.func_180495_p(blockposition1);
                if (BlockRailBase.func_176563_d(iblockdata)) {
                    return BlockRailBase.this.new Rail(this.field_150660_b, blockposition1, iblockdata);
                } else {
                    blockposition1 = blockposition.func_177977_b();
                    iblockdata = this.field_150660_b.func_180495_p(blockposition1);
                    return BlockRailBase.func_176563_d(iblockdata) ? BlockRailBase.this.new Rail(this.field_150660_b, blockposition1, iblockdata) : null;
                }
            }
        }

        private boolean func_150653_a(BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic) {
            return this.func_180363_c(blockminecarttrackabstract_minecarttracklogic.field_180367_c);
        }

        private boolean func_180363_c(BlockPos blockposition) {
            for (int i = 0; i < this.field_150657_g.size(); ++i) {
                BlockPos blockposition1 = (BlockPos) this.field_150657_g.get(i);

                if (blockposition1.func_177958_n() == blockposition.func_177958_n() && blockposition1.func_177952_p() == blockposition.func_177952_p()) {
                    return true;
                }
            }

            return false;
        }

        protected int func_150650_a() {
            int i = 0;
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumFacing enumdirection = (EnumFacing) iterator.next();

                if (this.func_180359_a(this.field_180367_c.func_177972_a(enumdirection))) {
                    ++i;
                }
            }

            return i;
        }

        private boolean func_150649_b(BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic) {
            return this.func_150653_a(blockminecarttrackabstract_minecarttracklogic) || this.field_150657_g.size() != 2;
        }

        private void func_150645_c(BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic) {
            this.field_150657_g.add(blockminecarttrackabstract_minecarttracklogic.field_180367_c);
            BlockPos blockposition = this.field_180367_c.func_177978_c();
            BlockPos blockposition1 = this.field_180367_c.func_177968_d();
            BlockPos blockposition2 = this.field_180367_c.func_177976_e();
            BlockPos blockposition3 = this.field_180367_c.func_177974_f();
            boolean flag = this.func_180363_c(blockposition);
            boolean flag1 = this.func_180363_c(blockposition1);
            boolean flag2 = this.func_180363_c(blockposition2);
            boolean flag3 = this.func_180363_c(blockposition3);
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = null;

            if (flag || flag1) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (flag2 || flag3) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.field_150656_f) {
                if (flag1 && flag3 && !flag && !flag2) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition1.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition3.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition2.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == null) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.field_180366_e = this.field_180366_e.func_177226_a(this.field_180365_d.func_176560_l(), blockminecarttrackabstract_enumtrackposition);
            this.field_150660_b.func_180501_a(this.field_180367_c, this.field_180366_e, 3);
        }

        private boolean func_180361_d(BlockPos blockposition) {
            BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = this.func_180697_b(blockposition);

            if (blockminecarttrackabstract_minecarttracklogic == null) {
                return false;
            } else {
                blockminecarttrackabstract_minecarttracklogic.func_150651_b();
                return blockminecarttrackabstract_minecarttracklogic.func_150649_b(this);
            }
        }

        public BlockRailBase.Rail func_180364_a(boolean flag, boolean flag1) {
            BlockPos blockposition = this.field_180367_c.func_177978_c();
            BlockPos blockposition1 = this.field_180367_c.func_177968_d();
            BlockPos blockposition2 = this.field_180367_c.func_177976_e();
            BlockPos blockposition3 = this.field_180367_c.func_177974_f();
            boolean flag2 = this.func_180361_d(blockposition);
            boolean flag3 = this.func_180361_d(blockposition1);
            boolean flag4 = this.func_180361_d(blockposition2);
            boolean flag5 = this.func_180361_d(blockposition3);
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = null;

            if ((flag2 || flag3) && !flag4 && !flag5) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if ((flag4 || flag5) && !flag2 && !flag3) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.field_150656_f) {
                if (flag3 && flag5 && !flag2 && !flag4) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag3 && flag4 && !flag2 && !flag5) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag2 && flag4 && !flag3 && !flag5) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag2 && flag5 && !flag3 && !flag4) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == null) {
                if (flag2 || flag3) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                }

                if (flag4 || flag5) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.EAST_WEST;
                }

                if (!this.field_150656_f) {
                    if (flag) {
                        if (flag3 && flag5) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }

                        if (flag4 && flag3) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag5 && flag2) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag2 && flag4) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }
                    } else {
                        if (flag2 && flag4) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }

                        if (flag5 && flag2) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag4 && flag3) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag3 && flag5) {
                            blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition1.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition3.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.func_176562_d(this.field_150660_b, blockposition2.func_177984_a())) {
                    blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (blockminecarttrackabstract_enumtrackposition == null) {
                blockminecarttrackabstract_enumtrackposition = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.func_180360_a(blockminecarttrackabstract_enumtrackposition);
            this.field_180366_e = this.field_180366_e.func_177226_a(this.field_180365_d.func_176560_l(), blockminecarttrackabstract_enumtrackposition);
            if (flag1 || this.field_150660_b.func_180495_p(this.field_180367_c) != this.field_180366_e) {
                this.field_150660_b.func_180501_a(this.field_180367_c, this.field_180366_e, 3);

                for (int i = 0; i < this.field_150657_g.size(); ++i) {
                    BlockRailBase.Rail blockminecarttrackabstract_minecarttracklogic = this.func_180697_b((BlockPos) this.field_150657_g.get(i));

                    if (blockminecarttrackabstract_minecarttracklogic != null) {
                        blockminecarttrackabstract_minecarttracklogic.func_150651_b();
                        if (blockminecarttrackabstract_minecarttracklogic.func_150649_b(this)) {
                            blockminecarttrackabstract_minecarttracklogic.func_150645_c(this);
                        }
                    }
                }
            }

            return this;
        }

        public IBlockState func_180362_b() {
            return this.field_180366_e;
        }
    }
}
