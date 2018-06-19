package net.minecraft.pathfinding;
import net.minecraft.util.math.MathHelper;


public class PathPoint {

    public final int field_75839_a;
    public final int field_75837_b;
    public final int field_75838_c;
    private final int field_75840_j;
    public int field_75835_d = -1;
    public float field_75836_e;
    public float field_75833_f;
    public float field_75834_g;
    public PathPoint field_75841_h;
    public boolean field_75842_i;
    public float field_186284_j;
    public float field_186285_k;
    public float field_186286_l;
    public PathNodeType field_186287_m;

    public PathPoint(int i, int j, int k) {
        this.field_186287_m = PathNodeType.BLOCKED;
        this.field_75839_a = i;
        this.field_75837_b = j;
        this.field_75838_c = k;
        this.field_75840_j = func_75830_a(i, j, k);
    }

    public PathPoint func_186283_a(int i, int j, int k) {
        PathPoint pathpoint = new PathPoint(i, j, k);

        pathpoint.field_75835_d = this.field_75835_d;
        pathpoint.field_75836_e = this.field_75836_e;
        pathpoint.field_75833_f = this.field_75833_f;
        pathpoint.field_75834_g = this.field_75834_g;
        pathpoint.field_75841_h = this.field_75841_h;
        pathpoint.field_75842_i = this.field_75842_i;
        pathpoint.field_186284_j = this.field_186284_j;
        pathpoint.field_186285_k = this.field_186285_k;
        pathpoint.field_186286_l = this.field_186286_l;
        pathpoint.field_186287_m = this.field_186287_m;
        return pathpoint;
    }

    public static int func_75830_a(int i, int j, int k) {
        return j & 255 | (i & 32767) << 8 | (k & 32767) << 24 | (i < 0 ? Integer.MIN_VALUE : 0) | (k < 0 ? '\u8000' : 0);
    }

    public float func_75829_a(PathPoint pathpoint) {
        float f = (float) (pathpoint.field_75839_a - this.field_75839_a);
        float f1 = (float) (pathpoint.field_75837_b - this.field_75837_b);
        float f2 = (float) (pathpoint.field_75838_c - this.field_75838_c);

        return MathHelper.func_76129_c(f * f + f1 * f1 + f2 * f2);
    }

    public float func_75832_b(PathPoint pathpoint) {
        float f = (float) (pathpoint.field_75839_a - this.field_75839_a);
        float f1 = (float) (pathpoint.field_75837_b - this.field_75837_b);
        float f2 = (float) (pathpoint.field_75838_c - this.field_75838_c);

        return f * f + f1 * f1 + f2 * f2;
    }

    public float func_186281_c(PathPoint pathpoint) {
        float f = (float) Math.abs(pathpoint.field_75839_a - this.field_75839_a);
        float f1 = (float) Math.abs(pathpoint.field_75837_b - this.field_75837_b);
        float f2 = (float) Math.abs(pathpoint.field_75838_c - this.field_75838_c);

        return f + f1 + f2;
    }

    public boolean equals(Object object) {
        if (!(object instanceof PathPoint)) {
            return false;
        } else {
            PathPoint pathpoint = (PathPoint) object;

            return this.field_75840_j == pathpoint.field_75840_j && this.field_75839_a == pathpoint.field_75839_a && this.field_75837_b == pathpoint.field_75837_b && this.field_75838_c == pathpoint.field_75838_c;
        }
    }

    public int hashCode() {
        return this.field_75840_j;
    }

    public boolean func_75831_a() {
        return this.field_75835_d >= 0;
    }

    public String toString() {
        return this.field_75839_a + ", " + this.field_75837_b + ", " + this.field_75838_c;
    }
}
