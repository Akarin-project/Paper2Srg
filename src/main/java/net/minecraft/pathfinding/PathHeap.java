package net.minecraft.pathfinding;

public class PathHeap {

    private PathPoint[] field_75852_a = new PathPoint[128];
    private int field_75851_b;

    public PathHeap() {}

    public PathPoint func_75849_a(PathPoint pathpoint) {
        if (pathpoint.field_75835_d >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        } else {
            if (this.field_75851_b == this.field_75852_a.length) {
                PathPoint[] apathpoint = new PathPoint[this.field_75851_b << 1];

                System.arraycopy(this.field_75852_a, 0, apathpoint, 0, this.field_75851_b);
                this.field_75852_a = apathpoint;
            }

            this.field_75852_a[this.field_75851_b] = pathpoint;
            pathpoint.field_75835_d = this.field_75851_b;
            this.func_75847_a(this.field_75851_b++);
            return pathpoint;
        }
    }

    public void func_75848_a() {
        this.field_75851_b = 0;
    }

    public PathPoint func_75844_c() {
        PathPoint pathpoint = this.field_75852_a[0];

        this.field_75852_a[0] = this.field_75852_a[--this.field_75851_b];
        this.field_75852_a[this.field_75851_b] = null;
        if (this.field_75851_b > 0) {
            this.func_75846_b(0);
        }

        pathpoint.field_75835_d = -1;
        return pathpoint;
    }

    public void func_75850_a(PathPoint pathpoint, float f) {
        float f1 = pathpoint.field_75834_g;

        pathpoint.field_75834_g = f;
        if (f < f1) {
            this.func_75847_a(pathpoint.field_75835_d);
        } else {
            this.func_75846_b(pathpoint.field_75835_d);
        }

    }

    private void func_75847_a(int i) {
        PathPoint pathpoint = this.field_75852_a[i];

        int j;

        for (float f = pathpoint.field_75834_g; i > 0; i = j) {
            j = i - 1 >> 1;
            PathPoint pathpoint1 = this.field_75852_a[j];

            if (f >= pathpoint1.field_75834_g) {
                break;
            }

            this.field_75852_a[i] = pathpoint1;
            pathpoint1.field_75835_d = i;
        }

        this.field_75852_a[i] = pathpoint;
        pathpoint.field_75835_d = i;
    }

    private void func_75846_b(int i) {
        PathPoint pathpoint = this.field_75852_a[i];
        float f = pathpoint.field_75834_g;

        while (true) {
            int j = 1 + (i << 1);
            int k = j + 1;

            if (j >= this.field_75851_b) {
                break;
            }

            PathPoint pathpoint1 = this.field_75852_a[j];
            float f1 = pathpoint1.field_75834_g;
            PathPoint pathpoint2;
            float f2;

            if (k >= this.field_75851_b) {
                pathpoint2 = null;
                f2 = Float.POSITIVE_INFINITY;
            } else {
                pathpoint2 = this.field_75852_a[k];
                f2 = pathpoint2.field_75834_g;
            }

            if (f1 < f2) {
                if (f1 >= f) {
                    break;
                }

                this.field_75852_a[i] = pathpoint1;
                pathpoint1.field_75835_d = i;
                i = j;
            } else {
                if (f2 >= f) {
                    break;
                }

                this.field_75852_a[i] = pathpoint2;
                pathpoint2.field_75835_d = i;
                i = k;
            }
        }

        this.field_75852_a[i] = pathpoint;
        pathpoint.field_75835_d = i;
    }

    public boolean func_75845_e() {
        return this.field_75851_b == 0;
    }
}
