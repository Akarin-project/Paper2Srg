package net.minecraft.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class Path {

    private final PathPoint[] field_75884_a;
    private PathPoint[] field_186312_b = new PathPoint[0];
    private PathPoint[] field_186313_c = new PathPoint[0];
    private int field_75882_b;
    private int field_75883_c;

    public Path(PathPoint[] apathpoint) {
        this.field_75884_a = apathpoint;
        this.field_75883_c = apathpoint.length;
    }

    public void func_75875_a() {
        ++this.field_75882_b;
    }

    public boolean func_75879_b() {
        return this.field_75882_b >= this.field_75883_c;
    }

    @Nullable
    public PathPoint func_75870_c() {
        return this.field_75883_c > 0 ? this.field_75884_a[this.field_75883_c - 1] : null;
    }

    public PathPoint func_75877_a(int i) {
        return this.field_75884_a[i];
    }

    public void func_186309_a(int i, PathPoint pathpoint) {
        this.field_75884_a[i] = pathpoint;
    }

    public int func_75874_d() {
        return this.field_75883_c;
    }

    public void func_75871_b(int i) {
        this.field_75883_c = i;
    }

    public int func_75873_e() {
        return this.field_75882_b;
    }

    public void func_75872_c(int i) {
        this.field_75882_b = i;
    }

    public Vec3d func_75881_a(Entity entity, int i) {
        double d0 = (double) this.field_75884_a[i].field_75839_a + (double) ((int) (entity.field_70130_N + 1.0F)) * 0.5D;
        double d1 = (double) this.field_75884_a[i].field_75837_b;
        double d2 = (double) this.field_75884_a[i].field_75838_c + (double) ((int) (entity.field_70130_N + 1.0F)) * 0.5D;

        return new Vec3d(d0, d1, d2);
    }

    public Vec3d func_75878_a(Entity entity) {
        return this.func_75881_a(entity, this.field_75882_b);
    }

    public Vec3d func_186310_f() {
        PathPoint pathpoint = this.field_75884_a[this.field_75882_b];

        return new Vec3d((double) pathpoint.field_75839_a, (double) pathpoint.field_75837_b, (double) pathpoint.field_75838_c);
    }

    public boolean func_75876_a(Path pathentity) {
        if (pathentity == null) {
            return false;
        } else if (pathentity.field_75884_a.length != this.field_75884_a.length) {
            return false;
        } else {
            for (int i = 0; i < this.field_75884_a.length; ++i) {
                if (this.field_75884_a[i].field_75839_a != pathentity.field_75884_a[i].field_75839_a || this.field_75884_a[i].field_75837_b != pathentity.field_75884_a[i].field_75837_b || this.field_75884_a[i].field_75838_c != pathentity.field_75884_a[i].field_75838_c) {
                    return false;
                }
            }

            return true;
        }
    }
}
