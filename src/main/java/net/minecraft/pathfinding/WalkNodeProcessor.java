package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor {

    protected float field_176183_h;

    public WalkNodeProcessor() {}

    public void func_186315_a(IBlockAccess iblockaccess, EntityLiving entityinsentient) {
        super.func_186315_a(iblockaccess, entityinsentient);
        this.field_176183_h = entityinsentient.func_184643_a(PathNodeType.WATER);
    }

    public void func_176163_a() {
        this.field_186326_b.func_184644_a(PathNodeType.WATER, this.field_176183_h);
        super.func_176163_a();
    }

    public PathPoint func_186318_b() {
        int i;
        BlockPos blockposition;

        if (this.func_186322_e() && this.field_186326_b.func_70090_H()) {
            i = (int) this.field_186326_b.func_174813_aQ().field_72338_b;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(MathHelper.func_76128_c(this.field_186326_b.field_70165_t), i, MathHelper.func_76128_c(this.field_186326_b.field_70161_v));

            for (Block block = this.field_176169_a.func_180495_p(blockposition_mutableblockposition).func_177230_c(); block == Blocks.field_150358_i || block == Blocks.field_150355_j; block = this.field_176169_a.func_180495_p(blockposition_mutableblockposition).func_177230_c()) {
                ++i;
                blockposition_mutableblockposition.func_181079_c(MathHelper.func_76128_c(this.field_186326_b.field_70165_t), i, MathHelper.func_76128_c(this.field_186326_b.field_70161_v));
            }
        } else if (this.field_186326_b.field_70122_E) {
            i = MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72338_b + 0.5D);
        } else {
            for (blockposition = new BlockPos(this.field_186326_b); (this.field_176169_a.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a || this.field_176169_a.func_180495_p(blockposition).func_177230_c().func_176205_b(this.field_176169_a, blockposition)) && blockposition.func_177956_o() > 0; blockposition = blockposition.func_177977_b()) {
                ;
            }

            i = blockposition.func_177984_a().func_177956_o();
        }

        blockposition = new BlockPos(this.field_186326_b);
        PathNodeType pathtype = this.func_186331_a(this.field_186326_b, blockposition.func_177958_n(), i, blockposition.func_177952_p());

        if (this.field_186326_b.func_184643_a(pathtype) < 0.0F) {
            HashSet hashset = Sets.newHashSet();

            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72340_a, (double) i, this.field_186326_b.func_174813_aQ().field_72339_c));
            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72340_a, (double) i, this.field_186326_b.func_174813_aQ().field_72334_f));
            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72336_d, (double) i, this.field_186326_b.func_174813_aQ().field_72339_c));
            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72336_d, (double) i, this.field_186326_b.func_174813_aQ().field_72334_f));
            Iterator iterator = hashset.iterator();

            while (iterator.hasNext()) {
                BlockPos blockposition1 = (BlockPos) iterator.next();
                PathNodeType pathtype1 = this.func_186329_a(this.field_186326_b, blockposition1);

                if (this.field_186326_b.func_184643_a(pathtype1) >= 0.0F) {
                    return this.func_176159_a(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p());
                }
            }
        }

        return this.func_176159_a(blockposition.func_177958_n(), i, blockposition.func_177952_p());
    }

    public PathPoint func_186325_a(double d0, double d1, double d2) {
        return this.func_176159_a(MathHelper.func_76128_c(d0), MathHelper.func_76128_c(d1), MathHelper.func_76128_c(d2));
    }

    public int func_186320_a(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f) {
        int i = 0;
        int j = 0;
        PathNodeType pathtype = this.func_186331_a(this.field_186326_b, pathpoint.field_75839_a, pathpoint.field_75837_b + 1, pathpoint.field_75838_c);

        if (this.field_186326_b.func_184643_a(pathtype) >= 0.0F) {
            j = MathHelper.func_76141_d(Math.max(1.0F, this.field_186326_b.field_70138_W));
        }

        BlockPos blockposition = (new BlockPos(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c)).func_177977_b();
        double d0 = (double) pathpoint.field_75837_b - (1.0D - this.field_176169_a.func_180495_p(blockposition).func_185900_c(this.field_176169_a, blockposition).field_72337_e);
        PathPoint pathpoint2 = this.func_186332_a(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c + 1, j, d0, EnumFacing.SOUTH);
        PathPoint pathpoint3 = this.func_186332_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b, pathpoint.field_75838_c, j, d0, EnumFacing.WEST);
        PathPoint pathpoint4 = this.func_186332_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b, pathpoint.field_75838_c, j, d0, EnumFacing.EAST);
        PathPoint pathpoint5 = this.func_186332_a(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c - 1, j, d0, EnumFacing.NORTH);

        if (pathpoint2 != null && !pathpoint2.field_75842_i && pathpoint2.func_75829_a(pathpoint1) < f) {
            apathpoint[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.field_75842_i && pathpoint3.func_75829_a(pathpoint1) < f) {
            apathpoint[i++] = pathpoint3;
        }

        if (pathpoint4 != null && !pathpoint4.field_75842_i && pathpoint4.func_75829_a(pathpoint1) < f) {
            apathpoint[i++] = pathpoint4;
        }

        if (pathpoint5 != null && !pathpoint5.field_75842_i && pathpoint5.func_75829_a(pathpoint1) < f) {
            apathpoint[i++] = pathpoint5;
        }

        boolean flag = pathpoint5 == null || pathpoint5.field_186287_m == PathNodeType.OPEN || pathpoint5.field_186286_l != 0.0F;
        boolean flag1 = pathpoint2 == null || pathpoint2.field_186287_m == PathNodeType.OPEN || pathpoint2.field_186286_l != 0.0F;
        boolean flag2 = pathpoint4 == null || pathpoint4.field_186287_m == PathNodeType.OPEN || pathpoint4.field_186286_l != 0.0F;
        boolean flag3 = pathpoint3 == null || pathpoint3.field_186287_m == PathNodeType.OPEN || pathpoint3.field_186286_l != 0.0F;
        PathPoint pathpoint6;

        if (flag && flag3) {
            pathpoint6 = this.func_186332_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b, pathpoint.field_75838_c - 1, j, d0, EnumFacing.NORTH);
            if (pathpoint6 != null && !pathpoint6.field_75842_i && pathpoint6.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        if (flag && flag2) {
            pathpoint6 = this.func_186332_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b, pathpoint.field_75838_c - 1, j, d0, EnumFacing.NORTH);
            if (pathpoint6 != null && !pathpoint6.field_75842_i && pathpoint6.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        if (flag1 && flag3) {
            pathpoint6 = this.func_186332_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b, pathpoint.field_75838_c + 1, j, d0, EnumFacing.SOUTH);
            if (pathpoint6 != null && !pathpoint6.field_75842_i && pathpoint6.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        if (flag1 && flag2) {
            pathpoint6 = this.func_186332_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b, pathpoint.field_75838_c + 1, j, d0, EnumFacing.SOUTH);
            if (pathpoint6 != null && !pathpoint6.field_75842_i && pathpoint6.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint6;
            }
        }

        return i;
    }

    @Nullable
    private PathPoint func_186332_a(int i, int j, int k, int l, double d0, EnumFacing enumdirection) {
        PathPoint pathpoint = null;
        BlockPos blockposition = new BlockPos(i, j, k);
        BlockPos blockposition1 = blockposition.func_177977_b();
        double d1 = (double) j - (1.0D - this.field_176169_a.func_180495_p(blockposition1).func_185900_c(this.field_176169_a, blockposition1).field_72337_e);

        if (d1 - d0 > 1.125D) {
            return null;
        } else {
            PathNodeType pathtype = this.func_186331_a(this.field_186326_b, i, j, k);
            float f = this.field_186326_b.func_184643_a(pathtype);
            double d2 = (double) this.field_186326_b.field_70130_N / 2.0D;

            if (f >= 0.0F) {
                pathpoint = this.func_176159_a(i, j, k);
                pathpoint.field_186287_m = pathtype;
                pathpoint.field_186286_l = Math.max(pathpoint.field_186286_l, f);
            }

            if (pathtype == PathNodeType.WALKABLE) {
                return pathpoint;
            } else {
                if (pathpoint == null && l > 0 && pathtype != PathNodeType.FENCE && pathtype != PathNodeType.TRAPDOOR) {
                    pathpoint = this.func_186332_a(i, j + 1, k, l - 1, d0, enumdirection);
                    if (pathpoint != null && (pathpoint.field_186287_m == PathNodeType.OPEN || pathpoint.field_186287_m == PathNodeType.WALKABLE) && this.field_186326_b.field_70130_N < 1.0F) {
                        double d3 = (double) (i - enumdirection.func_82601_c()) + 0.5D;
                        double d4 = (double) (k - enumdirection.func_82599_e()) + 0.5D;
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(d3 - d2, (double) j + 0.001D, d4 - d2, d3 + d2, (double) ((float) j + this.field_186326_b.field_70131_O), d4 + d2);
                        AxisAlignedBB axisalignedbb1 = this.field_176169_a.func_180495_p(blockposition).func_185900_c(this.field_176169_a, blockposition);
                        AxisAlignedBB axisalignedbb2 = axisalignedbb.func_72321_a(0.0D, axisalignedbb1.field_72337_e - 0.002D, 0.0D);

                        if (this.field_186326_b.field_70170_p.func_184143_b(axisalignedbb2)) {
                            pathpoint = null;
                        }
                    }
                }

                if (pathtype == PathNodeType.OPEN) {
                    AxisAlignedBB axisalignedbb3 = new AxisAlignedBB((double) i - d2 + 0.5D, (double) j + 0.001D, (double) k - d2 + 0.5D, (double) i + d2 + 0.5D, (double) ((float) j + this.field_186326_b.field_70131_O), (double) k + d2 + 0.5D);

                    if (this.field_186326_b.field_70170_p.func_184143_b(axisalignedbb3)) {
                        return null;
                    }

                    if (this.field_186326_b.field_70130_N >= 1.0F) {
                        PathNodeType pathtype1 = this.func_186331_a(this.field_186326_b, i, j - 1, k);

                        if (pathtype1 == PathNodeType.BLOCKED) {
                            pathpoint = this.func_176159_a(i, j, k);
                            pathpoint.field_186287_m = PathNodeType.WALKABLE;
                            pathpoint.field_186286_l = Math.max(pathpoint.field_186286_l, f);
                            return pathpoint;
                        }
                    }

                    int i1 = 0;

                    while (j > 0 && pathtype == PathNodeType.OPEN) {
                        --j;
                        if (i1++ >= this.field_186326_b.func_82143_as()) {
                            return null;
                        }

                        pathtype = this.func_186331_a(this.field_186326_b, i, j, k);
                        f = this.field_186326_b.func_184643_a(pathtype);
                        if (pathtype != PathNodeType.OPEN && f >= 0.0F) {
                            pathpoint = this.func_176159_a(i, j, k);
                            pathpoint.field_186287_m = pathtype;
                            pathpoint.field_186286_l = Math.max(pathpoint.field_186286_l, f);
                            break;
                        }

                        if (f < 0.0F) {
                            return null;
                        }
                    }
                }

                return pathpoint;
            }
        }
    }

    public PathNodeType func_186319_a(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        EnumSet enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathtype = PathNodeType.BLOCKED;
        double d0 = (double) entityinsentient.field_70130_N / 2.0D;
        BlockPos blockposition = new BlockPos(entityinsentient);

        pathtype = this.func_193577_a(iblockaccess, i, j, k, l, i1, j1, flag, flag1, enumset, pathtype, blockposition);
        if (enumset.contains(PathNodeType.FENCE)) {
            return PathNodeType.FENCE;
        } else {
            PathNodeType pathtype1 = PathNodeType.BLOCKED;
            Iterator iterator = enumset.iterator();

            while (iterator.hasNext()) {
                PathNodeType pathtype2 = (PathNodeType) iterator.next();

                if (entityinsentient.func_184643_a(pathtype2) < 0.0F) {
                    return pathtype2;
                }

                if (entityinsentient.func_184643_a(pathtype2) >= entityinsentient.func_184643_a(pathtype1)) {
                    pathtype1 = pathtype2;
                }
            }

            if (pathtype == PathNodeType.OPEN && entityinsentient.func_184643_a(pathtype1) == 0.0F) {
                return PathNodeType.OPEN;
            } else {
                return pathtype1;
            }
        }
    }

    public PathNodeType func_193577_a(IBlockAccess iblockaccess, int i, int j, int k, int l, int i1, int j1, boolean flag, boolean flag1, EnumSet<PathNodeType> enumset, PathNodeType pathtype, BlockPos blockposition) {
        for (int k1 = 0; k1 < l; ++k1) {
            for (int l1 = 0; l1 < i1; ++l1) {
                for (int i2 = 0; i2 < j1; ++i2) {
                    int j2 = k1 + i;
                    int k2 = l1 + j;
                    int l2 = i2 + k;
                    PathNodeType pathtype1 = this.func_186330_a(iblockaccess, j2, k2, l2);

                    if (pathtype1 == PathNodeType.DOOR_WOOD_CLOSED && flag && flag1) {
                        pathtype1 = PathNodeType.WALKABLE;
                    }

                    if (pathtype1 == PathNodeType.DOOR_OPEN && !flag1) {
                        pathtype1 = PathNodeType.BLOCKED;
                    }

                    if (pathtype1 == PathNodeType.RAIL && !(iblockaccess.func_180495_p(blockposition).func_177230_c() instanceof BlockRailBase) && !(iblockaccess.func_180495_p(blockposition.func_177977_b()).func_177230_c() instanceof BlockRailBase)) {
                        pathtype1 = PathNodeType.FENCE;
                    }

                    if (k1 == 0 && l1 == 0 && i2 == 0) {
                        pathtype = pathtype1;
                    }

                    enumset.add(pathtype1);
                }
            }
        }

        return pathtype;
    }

    private PathNodeType func_186329_a(EntityLiving entityinsentient, BlockPos blockposition) {
        return this.func_186331_a(entityinsentient, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
    }

    private PathNodeType func_186331_a(EntityLiving entityinsentient, int i, int j, int k) {
        return this.func_186319_a(this.field_176169_a, i, j, k, entityinsentient, this.field_176168_c, this.field_176165_d, this.field_176166_e, this.func_186324_d(), this.func_186323_c());
    }

    public PathNodeType func_186330_a(IBlockAccess iblockaccess, int i, int j, int k) {
        PathNodeType pathtype = this.func_189553_b(iblockaccess, i, j, k);

        if (pathtype == PathNodeType.OPEN && j >= 1) {
            Block block = iblockaccess.func_180495_p(new BlockPos(i, j - 1, k)).func_177230_c();
            PathNodeType pathtype1 = this.func_189553_b(iblockaccess, i, j - 1, k);

            pathtype = pathtype1 != PathNodeType.WALKABLE && pathtype1 != PathNodeType.OPEN && pathtype1 != PathNodeType.WATER && pathtype1 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
            if (pathtype1 == PathNodeType.DAMAGE_FIRE || block == Blocks.field_189877_df) {
                pathtype = PathNodeType.DAMAGE_FIRE;
            }

            if (pathtype1 == PathNodeType.DAMAGE_CACTUS) {
                pathtype = PathNodeType.DAMAGE_CACTUS;
            }
        }

        pathtype = this.func_193578_a(iblockaccess, i, j, k, pathtype);
        return pathtype;
    }

    public PathNodeType func_193578_a(IBlockAccess iblockaccess, int i, int j, int k, PathNodeType pathtype) {
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();

        if (pathtype == PathNodeType.WALKABLE) {
            for (int l = -1; l <= 1; ++l) {
                for (int i1 = -1; i1 <= 1; ++i1) {
                    if (l != 0 || i1 != 0) {
                        Block block = iblockaccess.func_180495_p(blockposition_pooledblockposition.func_181079_c(l + i, j, i1 + k)).func_177230_c();

                        if (block == Blocks.field_150434_aF) {
                            pathtype = PathNodeType.DANGER_CACTUS;
                        } else if (block == Blocks.field_150480_ab) {
                            pathtype = PathNodeType.DANGER_FIRE;
                        }
                    }
                }
            }
        }

        blockposition_pooledblockposition.func_185344_t();
        return pathtype;
    }

    protected PathNodeType func_189553_b(IBlockAccess iblockaccess, int i, int j, int k) {
        BlockPos blockposition = new BlockPos(i, j, k);
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();
        Material material = iblockdata.func_185904_a();

        return material == Material.field_151579_a ? PathNodeType.OPEN : (block != Blocks.field_150415_aT && block != Blocks.field_180400_cw && block != Blocks.field_150392_bi ? (block == Blocks.field_150480_ab ? PathNodeType.DAMAGE_FIRE : (block == Blocks.field_150434_aF ? PathNodeType.DAMAGE_CACTUS : (block instanceof BlockDoor && material == Material.field_151575_d && !((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue() ? PathNodeType.DOOR_WOOD_CLOSED : (block instanceof BlockDoor && material == Material.field_151573_f && !((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue() ? PathNodeType.DOOR_IRON_CLOSED : (block instanceof BlockDoor && ((Boolean) iblockdata.func_177229_b(BlockDoor.field_176519_b)).booleanValue() ? PathNodeType.DOOR_OPEN : (block instanceof BlockRailBase ? PathNodeType.RAIL : (!(block instanceof BlockFence) && !(block instanceof BlockWall) && (!(block instanceof BlockFenceGate) || ((Boolean) iblockdata.func_177229_b(BlockFenceGate.field_176466_a)).booleanValue()) ? (material == Material.field_151586_h ? PathNodeType.WATER : (material == Material.field_151587_i ? PathNodeType.LAVA : (block.func_176205_b(iblockaccess, blockposition) ? PathNodeType.OPEN : PathNodeType.BLOCKED))) : PathNodeType.FENCE))))))) : PathNodeType.TRAPDOOR);
    }
}
