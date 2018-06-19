package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FlyingNodeProcessor extends WalkNodeProcessor {

    public FlyingNodeProcessor() {}

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

        if (this.func_186322_e() && this.field_186326_b.func_70090_H()) {
            i = (int) this.field_186326_b.func_174813_aQ().field_72338_b;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(MathHelper.func_76128_c(this.field_186326_b.field_70165_t), i, MathHelper.func_76128_c(this.field_186326_b.field_70161_v));

            for (Block block = this.field_176169_a.func_180495_p(blockposition_mutableblockposition).func_177230_c(); block == Blocks.field_150358_i || block == Blocks.field_150355_j; block = this.field_176169_a.func_180495_p(blockposition_mutableblockposition).func_177230_c()) {
                ++i;
                blockposition_mutableblockposition.func_181079_c(MathHelper.func_76128_c(this.field_186326_b.field_70165_t), i, MathHelper.func_76128_c(this.field_186326_b.field_70161_v));
            }
        } else {
            i = MathHelper.func_76128_c(this.field_186326_b.func_174813_aQ().field_72338_b + 0.5D);
        }

        BlockPos blockposition = new BlockPos(this.field_186326_b);
        PathNodeType pathtype = this.func_192558_a(this.field_186326_b, blockposition.func_177958_n(), i, blockposition.func_177952_p());

        if (this.field_186326_b.func_184643_a(pathtype) < 0.0F) {
            HashSet hashset = Sets.newHashSet();

            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72340_a, (double) i, this.field_186326_b.func_174813_aQ().field_72339_c));
            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72340_a, (double) i, this.field_186326_b.func_174813_aQ().field_72334_f));
            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72336_d, (double) i, this.field_186326_b.func_174813_aQ().field_72339_c));
            hashset.add(new BlockPos(this.field_186326_b.func_174813_aQ().field_72336_d, (double) i, this.field_186326_b.func_174813_aQ().field_72334_f));
            Iterator iterator = hashset.iterator();

            while (iterator.hasNext()) {
                BlockPos blockposition1 = (BlockPos) iterator.next();
                PathNodeType pathtype1 = this.func_192559_a(this.field_186326_b, blockposition1);

                if (this.field_186326_b.func_184643_a(pathtype1) >= 0.0F) {
                    return super.func_176159_a(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p());
                }
            }
        }

        return super.func_176159_a(blockposition.func_177958_n(), i, blockposition.func_177952_p());
    }

    public PathPoint func_186325_a(double d0, double d1, double d2) {
        return super.func_176159_a(MathHelper.func_76128_c(d0), MathHelper.func_76128_c(d1), MathHelper.func_76128_c(d2));
    }

    public int func_186320_a(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f) {
        int i = 0;
        PathPoint pathpoint2 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c + 1);
        PathPoint pathpoint3 = this.func_176159_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b, pathpoint.field_75838_c);
        PathPoint pathpoint4 = this.func_176159_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b, pathpoint.field_75838_c);
        PathPoint pathpoint5 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b, pathpoint.field_75838_c - 1);
        PathPoint pathpoint6 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b + 1, pathpoint.field_75838_c);
        PathPoint pathpoint7 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b - 1, pathpoint.field_75838_c);

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

        if (pathpoint6 != null && !pathpoint6.field_75842_i && pathpoint6.func_75829_a(pathpoint1) < f) {
            apathpoint[i++] = pathpoint6;
        }

        if (pathpoint7 != null && !pathpoint7.field_75842_i && pathpoint7.func_75829_a(pathpoint1) < f) {
            apathpoint[i++] = pathpoint7;
        }

        boolean flag = pathpoint5 == null || pathpoint5.field_186286_l != 0.0F;
        boolean flag1 = pathpoint2 == null || pathpoint2.field_186286_l != 0.0F;
        boolean flag2 = pathpoint4 == null || pathpoint4.field_186286_l != 0.0F;
        boolean flag3 = pathpoint3 == null || pathpoint3.field_186286_l != 0.0F;
        boolean flag4 = pathpoint6 == null || pathpoint6.field_186286_l != 0.0F;
        boolean flag5 = pathpoint7 == null || pathpoint7.field_186286_l != 0.0F;
        PathPoint pathpoint8;

        if (flag && flag3) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b, pathpoint.field_75838_c - 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag && flag2) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b, pathpoint.field_75838_c - 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag3) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b, pathpoint.field_75838_c + 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag2) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b, pathpoint.field_75838_c + 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag && flag4) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b + 1, pathpoint.field_75838_c - 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag4) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b + 1, pathpoint.field_75838_c + 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag2 && flag4) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b + 1, pathpoint.field_75838_c);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag3 && flag4) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b + 1, pathpoint.field_75838_c);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag && flag5) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b - 1, pathpoint.field_75838_c - 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag1 && flag5) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a, pathpoint.field_75837_b - 1, pathpoint.field_75838_c + 1);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag2 && flag5) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a + 1, pathpoint.field_75837_b - 1, pathpoint.field_75838_c);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        if (flag3 && flag5) {
            pathpoint8 = this.func_176159_a(pathpoint.field_75839_a - 1, pathpoint.field_75837_b - 1, pathpoint.field_75838_c);
            if (pathpoint8 != null && !pathpoint8.field_75842_i && pathpoint8.func_75829_a(pathpoint1) < f) {
                apathpoint[i++] = pathpoint8;
            }
        }

        return i;
    }

    @Nullable
    protected PathPoint func_176159_a(int i, int j, int k) {
        PathPoint pathpoint = null;
        PathNodeType pathtype = this.func_192558_a(this.field_186326_b, i, j, k);
        float f = this.field_186326_b.func_184643_a(pathtype);

        if (f >= 0.0F) {
            pathpoint = super.func_176159_a(i, j, k);
            pathpoint.field_186287_m = pathtype;
            pathpoint.field_186286_l = Math.max(pathpoint.field_186286_l, f);
            if (pathtype == PathNodeType.WALKABLE) {
                ++pathpoint.field_186286_l;
            }
        }

        return pathtype != PathNodeType.OPEN && pathtype != PathNodeType.WALKABLE ? pathpoint : pathpoint;
    }

    public PathNodeType func_186319_a(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1) {
        EnumSet enumset = EnumSet.noneOf(PathNodeType.class);
        PathNodeType pathtype = PathNodeType.BLOCKED;
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

    public PathNodeType func_186330_a(IBlockAccess iblockaccess, int i, int j, int k) {
        PathNodeType pathtype = this.func_189553_b(iblockaccess, i, j, k);

        if (pathtype == PathNodeType.OPEN && j >= 1) {
            Block block = iblockaccess.func_180495_p(new BlockPos(i, j - 1, k)).func_177230_c();
            PathNodeType pathtype1 = this.func_189553_b(iblockaccess, i, j - 1, k);

            if (pathtype1 != PathNodeType.DAMAGE_FIRE && block != Blocks.field_189877_df && pathtype1 != PathNodeType.LAVA) {
                if (pathtype1 == PathNodeType.DAMAGE_CACTUS) {
                    pathtype = PathNodeType.DAMAGE_CACTUS;
                } else {
                    pathtype = pathtype1 != PathNodeType.WALKABLE && pathtype1 != PathNodeType.OPEN && pathtype1 != PathNodeType.WATER ? PathNodeType.WALKABLE : PathNodeType.OPEN;
                }
            } else {
                pathtype = PathNodeType.DAMAGE_FIRE;
            }
        }

        pathtype = this.func_193578_a(iblockaccess, i, j, k, pathtype);
        return pathtype;
    }

    private PathNodeType func_192559_a(EntityLiving entityinsentient, BlockPos blockposition) {
        return this.func_192558_a(entityinsentient, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
    }

    private PathNodeType func_192558_a(EntityLiving entityinsentient, int i, int j, int k) {
        return this.func_186319_a(this.field_176169_a, i, j, k, entityinsentient, this.field_176168_c, this.field_176165_d, this.field_176166_e, this.func_186324_d(), this.func_186323_c());
    }
}
