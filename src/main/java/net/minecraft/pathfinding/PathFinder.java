package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PathFinder {

    private final PathHeap field_75866_b = new PathHeap();
    private final Set<PathPoint> field_186337_b = Sets.newHashSet();
    private final PathPoint[] field_75864_d = new PathPoint[32];
    private final NodeProcessor field_176190_c;

    public PathFinder(NodeProcessor pathfinderabstract) {
        this.field_176190_c = pathfinderabstract;
    }

    @Nullable
    public Path func_186333_a(IBlockAccess iblockaccess, EntityLiving entityinsentient, Entity entity, float f) {
        return this.func_186334_a(iblockaccess, entityinsentient, entity.field_70165_t, entity.func_174813_aQ().field_72338_b, entity.field_70161_v, f);
    }

    @Nullable
    public Path func_186336_a(IBlockAccess iblockaccess, EntityLiving entityinsentient, BlockPos blockposition, float f) {
        return this.func_186334_a(iblockaccess, entityinsentient, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) ((float) blockposition.func_177956_o() + 0.5F), (double) ((float) blockposition.func_177952_p() + 0.5F), f);
    }

    @Nullable
    private Path func_186334_a(IBlockAccess iblockaccess, EntityLiving entityinsentient, double d0, double d1, double d2, float f) {
        this.field_75866_b.func_75848_a();
        this.field_176190_c.func_186315_a(iblockaccess, entityinsentient);
        PathPoint pathpoint = this.field_176190_c.func_186318_b();
        PathPoint pathpoint1 = this.field_176190_c.func_186325_a(d0, d1, d2);
        Path pathentity = this.func_186335_a(pathpoint, pathpoint1, f);

        this.field_176190_c.func_176163_a();
        return pathentity;
    }

    @Nullable
    private Path func_186335_a(PathPoint pathpoint, PathPoint pathpoint1, float f) {
        pathpoint.field_75836_e = 0.0F;
        pathpoint.field_75833_f = pathpoint.func_186281_c(pathpoint1);
        pathpoint.field_75834_g = pathpoint.field_75833_f;
        this.field_75866_b.func_75848_a();
        this.field_186337_b.clear();
        this.field_75866_b.func_75849_a(pathpoint);
        PathPoint pathpoint2 = pathpoint;
        int i = 0;

        while (!this.field_75866_b.func_75845_e()) {
            ++i;
            if (i >= 200) {
                break;
            }

            PathPoint pathpoint3 = this.field_75866_b.func_75844_c();

            if (pathpoint3.equals(pathpoint1)) {
                pathpoint2 = pathpoint1;
                break;
            }

            if (pathpoint3.func_186281_c(pathpoint1) < pathpoint2.func_186281_c(pathpoint1)) {
                pathpoint2 = pathpoint3;
            }

            pathpoint3.field_75842_i = true;
            int j = this.field_176190_c.func_186320_a(this.field_75864_d, pathpoint3, pathpoint1, f);

            for (int k = 0; k < j; ++k) {
                PathPoint pathpoint4 = this.field_75864_d[k];
                float f1 = pathpoint3.func_186281_c(pathpoint4);

                pathpoint4.field_186284_j = pathpoint3.field_186284_j + f1;
                pathpoint4.field_186285_k = f1 + pathpoint4.field_186286_l;
                float f2 = pathpoint3.field_75836_e + pathpoint4.field_186285_k;

                if (pathpoint4.field_186284_j < f && (!pathpoint4.func_75831_a() || f2 < pathpoint4.field_75836_e)) {
                    pathpoint4.field_75841_h = pathpoint3;
                    pathpoint4.field_75836_e = f2;
                    pathpoint4.field_75833_f = pathpoint4.func_186281_c(pathpoint1) + pathpoint4.field_186286_l;
                    if (pathpoint4.func_75831_a()) {
                        this.field_75866_b.func_75850_a(pathpoint4, pathpoint4.field_75836_e + pathpoint4.field_75833_f);
                    } else {
                        pathpoint4.field_75834_g = pathpoint4.field_75836_e + pathpoint4.field_75833_f;
                        this.field_75866_b.func_75849_a(pathpoint4);
                    }
                }
            }
        }

        if (pathpoint2 == pathpoint) {
            return null;
        } else {
            Path pathentity = this.func_75853_a(pathpoint, pathpoint2);

            return pathentity;
        }
    }

    private Path func_75853_a(PathPoint pathpoint, PathPoint pathpoint1) {
        int i = 1;

        PathPoint pathpoint2;

        for (pathpoint2 = pathpoint1; pathpoint2.field_75841_h != null; pathpoint2 = pathpoint2.field_75841_h) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];

        pathpoint2 = pathpoint1;
        --i;

        for (apathpoint[i] = pathpoint1; pathpoint2.field_75841_h != null; apathpoint[i] = pathpoint2) {
            pathpoint2 = pathpoint2.field_75841_h;
            --i;
        }

        return new Path(apathpoint);
    }
}
