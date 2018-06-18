package net.minecraft.pathfinding;

public class PathHeap {

    private PathPoint[] pathPoints = new PathPoint[128];
    private int count;

    public PathHeap() {}

    public PathPoint addPoint(PathPoint pathpoint) {
        if (pathpoint.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        } else {
            if (this.count == this.pathPoints.length) {
                PathPoint[] apathpoint = new PathPoint[this.count << 1];

                System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
                this.pathPoints = apathpoint;
            }

            this.pathPoints[this.count] = pathpoint;
            pathpoint.index = this.count;
            this.sortBack(this.count++);
            return pathpoint;
        }
    }

    public void clearPath() {
        this.count = 0;
    }

    public PathPoint dequeue() {
        PathPoint pathpoint = this.pathPoints[0];

        this.pathPoints[0] = this.pathPoints[--this.count];
        this.pathPoints[this.count] = null;
        if (this.count > 0) {
            this.sortForward(0);
        }

        pathpoint.index = -1;
        return pathpoint;
    }

    public void changeDistance(PathPoint pathpoint, float f) {
        float f1 = pathpoint.distanceToTarget;

        pathpoint.distanceToTarget = f;
        if (f < f1) {
            this.sortBack(pathpoint.index);
        } else {
            this.sortForward(pathpoint.index);
        }

    }

    private void sortBack(int i) {
        PathPoint pathpoint = this.pathPoints[i];

        int j;

        for (float f = pathpoint.distanceToTarget; i > 0; i = j) {
            j = i - 1 >> 1;
            PathPoint pathpoint1 = this.pathPoints[j];

            if (f >= pathpoint1.distanceToTarget) {
                break;
            }

            this.pathPoints[i] = pathpoint1;
            pathpoint1.index = i;
        }

        this.pathPoints[i] = pathpoint;
        pathpoint.index = i;
    }

    private void sortForward(int i) {
        PathPoint pathpoint = this.pathPoints[i];
        float f = pathpoint.distanceToTarget;

        while (true) {
            int j = 1 + (i << 1);
            int k = j + 1;

            if (j >= this.count) {
                break;
            }

            PathPoint pathpoint1 = this.pathPoints[j];
            float f1 = pathpoint1.distanceToTarget;
            PathPoint pathpoint2;
            float f2;

            if (k >= this.count) {
                pathpoint2 = null;
                f2 = Float.POSITIVE_INFINITY;
            } else {
                pathpoint2 = this.pathPoints[k];
                f2 = pathpoint2.distanceToTarget;
            }

            if (f1 < f2) {
                if (f1 >= f) {
                    break;
                }

                this.pathPoints[i] = pathpoint1;
                pathpoint1.index = i;
                i = j;
            } else {
                if (f2 >= f) {
                    break;
                }

                this.pathPoints[i] = pathpoint2;
                pathpoint2.index = i;
                i = k;
            }
        }

        this.pathPoints[i] = pathpoint;
        pathpoint.index = i;
    }

    public boolean isPathEmpty() {
        return this.count == 0;
    }
}
