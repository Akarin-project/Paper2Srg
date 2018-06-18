package net.minecraft.pathfinding;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PathFinder {

    private final PathHeap path = new PathHeap();
    private final Set<PathPoint> closedSet = Sets.newHashSet();
    private final PathPoint[] pathOptions = new PathPoint[32];
    private final NodeProcessor nodeProcessor;

    public PathFinder(NodeProcessor pathfinderabstract) {
        this.nodeProcessor = pathfinderabstract;
    }

    @Nullable
    public Path findPath(IBlockAccess iblockaccess, EntityLiving entityinsentient, Entity entity, float f) {
        return this.findPath(iblockaccess, entityinsentient, entity.posX, entity.getEntityBoundingBox().minY, entity.posZ, f);
    }

    @Nullable
    public Path findPath(IBlockAccess iblockaccess, EntityLiving entityinsentient, BlockPos blockposition, float f) {
        return this.findPath(iblockaccess, entityinsentient, (double) ((float) blockposition.getX() + 0.5F), (double) ((float) blockposition.getY() + 0.5F), (double) ((float) blockposition.getZ() + 0.5F), f);
    }

    @Nullable
    private Path findPath(IBlockAccess iblockaccess, EntityLiving entityinsentient, double d0, double d1, double d2, float f) {
        this.path.clearPath();
        this.nodeProcessor.init(iblockaccess, entityinsentient);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(d0, d1, d2);
        Path pathentity = this.findPath(pathpoint, pathpoint1, f);

        this.nodeProcessor.postProcess();
        return pathentity;
    }

    @Nullable
    private Path findPath(PathPoint pathpoint, PathPoint pathpoint1, float f) {
        pathpoint.totalPathDistance = 0.0F;
        pathpoint.distanceToNext = pathpoint.distanceManhattan(pathpoint1);
        pathpoint.distanceToTarget = pathpoint.distanceToNext;
        this.path.clearPath();
        this.closedSet.clear();
        this.path.addPoint(pathpoint);
        PathPoint pathpoint2 = pathpoint;
        int i = 0;

        while (!this.path.isPathEmpty()) {
            ++i;
            if (i >= 200) {
                break;
            }

            PathPoint pathpoint3 = this.path.dequeue();

            if (pathpoint3.equals(pathpoint1)) {
                pathpoint2 = pathpoint1;
                break;
            }

            if (pathpoint3.distanceManhattan(pathpoint1) < pathpoint2.distanceManhattan(pathpoint1)) {
                pathpoint2 = pathpoint3;
            }

            pathpoint3.visited = true;
            int j = this.nodeProcessor.findPathOptions(this.pathOptions, pathpoint3, pathpoint1, f);

            for (int k = 0; k < j; ++k) {
                PathPoint pathpoint4 = this.pathOptions[k];
                float f1 = pathpoint3.distanceManhattan(pathpoint4);

                pathpoint4.distanceFromOrigin = pathpoint3.distanceFromOrigin + f1;
                pathpoint4.cost = f1 + pathpoint4.costMalus;
                float f2 = pathpoint3.totalPathDistance + pathpoint4.cost;

                if (pathpoint4.distanceFromOrigin < f && (!pathpoint4.isAssigned() || f2 < pathpoint4.totalPathDistance)) {
                    pathpoint4.previous = pathpoint3;
                    pathpoint4.totalPathDistance = f2;
                    pathpoint4.distanceToNext = pathpoint4.distanceManhattan(pathpoint1) + pathpoint4.costMalus;
                    if (pathpoint4.isAssigned()) {
                        this.path.changeDistance(pathpoint4, pathpoint4.totalPathDistance + pathpoint4.distanceToNext);
                    } else {
                        pathpoint4.distanceToTarget = pathpoint4.totalPathDistance + pathpoint4.distanceToNext;
                        this.path.addPoint(pathpoint4);
                    }
                }
            }
        }

        if (pathpoint2 == pathpoint) {
            return null;
        } else {
            Path pathentity = this.createPath(pathpoint, pathpoint2);

            return pathentity;
        }
    }

    private Path createPath(PathPoint pathpoint, PathPoint pathpoint1) {
        int i = 1;

        PathPoint pathpoint2;

        for (pathpoint2 = pathpoint1; pathpoint2.previous != null; pathpoint2 = pathpoint2.previous) {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];

        pathpoint2 = pathpoint1;
        --i;

        for (apathpoint[i] = pathpoint1; pathpoint2.previous != null; apathpoint[i] = pathpoint2) {
            pathpoint2 = pathpoint2.previous;
            --i;
        }

        return new Path(apathpoint);
    }
}
