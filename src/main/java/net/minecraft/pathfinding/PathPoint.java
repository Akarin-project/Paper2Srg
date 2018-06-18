package net.minecraft.pathfinding;
import net.minecraft.util.math.MathHelper;


public class PathPoint {

    public final int x;
    public final int y;
    public final int z;
    private final int hash;
    public int index = -1;
    public float totalPathDistance;
    public float distanceToNext;
    public float distanceToTarget;
    public PathPoint previous;
    public boolean visited;
    public float distanceFromOrigin;
    public float cost;
    public float costMalus;
    public PathNodeType nodeType;

    public PathPoint(int i, int j, int k) {
        this.nodeType = PathNodeType.BLOCKED;
        this.x = i;
        this.y = j;
        this.z = k;
        this.hash = makeHash(i, j, k);
    }

    public PathPoint cloneMove(int i, int j, int k) {
        PathPoint pathpoint = new PathPoint(i, j, k);

        pathpoint.index = this.index;
        pathpoint.totalPathDistance = this.totalPathDistance;
        pathpoint.distanceToNext = this.distanceToNext;
        pathpoint.distanceToTarget = this.distanceToTarget;
        pathpoint.previous = this.previous;
        pathpoint.visited = this.visited;
        pathpoint.distanceFromOrigin = this.distanceFromOrigin;
        pathpoint.cost = this.cost;
        pathpoint.costMalus = this.costMalus;
        pathpoint.nodeType = this.nodeType;
        return pathpoint;
    }

    public static int makeHash(int i, int j, int k) {
        return j & 255 | (i & 32767) << 8 | (k & 32767) << 24 | (i < 0 ? Integer.MIN_VALUE : 0) | (k < 0 ? '\u8000' : 0);
    }

    public float distanceTo(PathPoint pathpoint) {
        float f = (float) (pathpoint.x - this.x);
        float f1 = (float) (pathpoint.y - this.y);
        float f2 = (float) (pathpoint.z - this.z);

        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public float distanceToSquared(PathPoint pathpoint) {
        float f = (float) (pathpoint.x - this.x);
        float f1 = (float) (pathpoint.y - this.y);
        float f2 = (float) (pathpoint.z - this.z);

        return f * f + f1 * f1 + f2 * f2;
    }

    public float distanceManhattan(PathPoint pathpoint) {
        float f = (float) Math.abs(pathpoint.x - this.x);
        float f1 = (float) Math.abs(pathpoint.y - this.y);
        float f2 = (float) Math.abs(pathpoint.z - this.z);

        return f + f1 + f2;
    }

    public boolean equals(Object object) {
        if (!(object instanceof PathPoint)) {
            return false;
        } else {
            PathPoint pathpoint = (PathPoint) object;

            return this.hash == pathpoint.hash && this.x == pathpoint.x && this.y == pathpoint.y && this.z == pathpoint.z;
        }
    }

    public int hashCode() {
        return this.hash;
    }

    public boolean isAssigned() {
        return this.index >= 0;
    }

    public String toString() {
        return this.x + ", " + this.y + ", " + this.z;
    }
}
