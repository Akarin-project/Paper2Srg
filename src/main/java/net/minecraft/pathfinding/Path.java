package net.minecraft.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Path {

    private final PathPoint[] points;
    private PathPoint[] openSet = new PathPoint[0];
    private PathPoint[] closedSet = new PathPoint[0];
    private int currentPathIndex;
    private int pathLength;

    public Path(PathPoint[] apathpoint) {
        this.points = apathpoint;
        this.pathLength = apathpoint.length;
    }

    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }

    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }

    @Nullable
    public PathPoint getFinalPathPoint() {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    public PathPoint getPathPointFromIndex(int i) {
        return this.points[i];
    }

    public void setPoint(int i, PathPoint pathpoint) {
        this.points[i] = pathpoint;
    }

    public int getCurrentPathLength() {
        return this.pathLength;
    }

    public void setCurrentPathLength(int i) {
        this.pathLength = i;
    }

    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    public void setCurrentPathIndex(int i) {
        this.currentPathIndex = i;
    }

    public Vec3d getVectorFromIndex(Entity entity, int i) {
        double d0 = (double) this.points[i].x + (double) ((int) (entity.width + 1.0F)) * 0.5D;
        double d1 = (double) this.points[i].y;
        double d2 = (double) this.points[i].z + (double) ((int) (entity.width + 1.0F)) * 0.5D;

        return new Vec3d(d0, d1, d2);
    }

    public Vec3d getPosition(Entity entity) {
        return this.getVectorFromIndex(entity, this.currentPathIndex);
    }

    public Vec3d getCurrentPos() {
        PathPoint pathpoint = this.points[this.currentPathIndex];

        return new Vec3d((double) pathpoint.x, (double) pathpoint.y, (double) pathpoint.z);
    }

    public boolean isSamePath(Path pathentity) {
        if (pathentity == null) {
            return false;
        } else if (pathentity.points.length != this.points.length) {
            return false;
        } else {
            for (int i = 0; i < this.points.length; ++i) {
                if (this.points[i].x != pathentity.points[i].x || this.points[i].y != pathentity.points[i].y || this.points[i].z != pathentity.points[i].z) {
                    return false;
                }
            }

            return true;
        }
    }
}
