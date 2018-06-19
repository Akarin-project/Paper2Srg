package net.minecraft.pathfinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;


public abstract class NodeProcessor {

    protected IBlockAccess field_176169_a;
    protected EntityLiving field_186326_b;
    protected final IntHashMap<PathPoint> field_176167_b = new IntHashMap();
    protected int field_176168_c;
    protected int field_176165_d;
    protected int field_176166_e;
    protected boolean field_176180_f;
    protected boolean field_176181_g;
    protected boolean field_176184_i;

    public NodeProcessor() {}

    public void func_186315_a(IBlockAccess iblockaccess, EntityLiving entityinsentient) {
        this.field_176169_a = iblockaccess;
        this.field_186326_b = entityinsentient;
        this.field_176167_b.func_76046_c();
        this.field_176168_c = MathHelper.func_76141_d(entityinsentient.field_70130_N + 1.0F);
        this.field_176165_d = MathHelper.func_76141_d(entityinsentient.field_70131_O + 1.0F);
        this.field_176166_e = MathHelper.func_76141_d(entityinsentient.field_70130_N + 1.0F);
    }

    public void func_176163_a() {
        this.field_176169_a = null;
        this.field_186326_b = null;
    }

    protected PathPoint func_176159_a(int i, int j, int k) {
        int l = PathPoint.func_75830_a(i, j, k);
        PathPoint pathpoint = (PathPoint) this.field_176167_b.func_76041_a(l);

        if (pathpoint == null) {
            pathpoint = new PathPoint(i, j, k);
            this.field_176167_b.func_76038_a(l, pathpoint);
        }

        return pathpoint;
    }

    public abstract PathPoint func_186318_b();

    public abstract PathPoint func_186325_a(double d0, double d1, double d2);

    public abstract int func_186320_a(PathPoint[] apathpoint, PathPoint pathpoint, PathPoint pathpoint1, float f);

    public abstract PathNodeType func_186319_a(IBlockAccess iblockaccess, int i, int j, int k, EntityLiving entityinsentient, int l, int i1, int j1, boolean flag, boolean flag1);

    public abstract PathNodeType func_186330_a(IBlockAccess iblockaccess, int i, int j, int k);

    public void func_186317_a(boolean flag) {
        this.field_176180_f = flag;
    }

    public void func_186321_b(boolean flag) {
        this.field_176181_g = flag;
    }

    public void func_186316_c(boolean flag) {
        this.field_176184_i = flag;
    }

    public boolean func_186323_c() {
        return this.field_176180_f;
    }

    public boolean func_186324_d() {
        return this.field_176181_g;
    }

    public boolean func_186322_e() {
        return this.field_176184_i;
    }
}
