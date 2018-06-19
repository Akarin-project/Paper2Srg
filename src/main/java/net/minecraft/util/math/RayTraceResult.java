package net.minecraft.util.math;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;


public class RayTraceResult {

    private BlockPos field_178783_e;
    public RayTraceResult.Type field_72313_a;
    public EnumFacing field_178784_b;
    public Vec3d field_72307_f;
    public Entity field_72308_g;

    public RayTraceResult(Vec3d vec3d, EnumFacing enumdirection, BlockPos blockposition) {
        this(RayTraceResult.Type.BLOCK, vec3d, enumdirection, blockposition);
    }

    public RayTraceResult(Vec3d vec3d, EnumFacing enumdirection) {
        this(RayTraceResult.Type.BLOCK, vec3d, enumdirection, BlockPos.field_177992_a);
    }

    public RayTraceResult(Entity entity) {
        this(entity, new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
    }

    public RayTraceResult(RayTraceResult.Type movingobjectposition_enummovingobjecttype, Vec3d vec3d, EnumFacing enumdirection, BlockPos blockposition) {
        this.field_72313_a = movingobjectposition_enummovingobjecttype;
        this.field_178783_e = blockposition;
        this.field_178784_b = enumdirection;
        this.field_72307_f = new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }

    public RayTraceResult(Entity entity, Vec3d vec3d) {
        this.field_72313_a = RayTraceResult.Type.ENTITY;
        this.field_72308_g = entity;
        this.field_72307_f = vec3d;
    }

    public BlockPos func_178782_a() {
        return this.field_178783_e;
    }

    public String toString() {
        return "HitResult{type=" + this.field_72313_a + ", blockpos=" + this.field_178783_e + ", f=" + this.field_178784_b + ", pos=" + this.field_72307_f + ", entity=" + this.field_72308_g + '}';
    }

    public static enum Type {

        MISS, BLOCK, ENTITY;

        private Type() {}
    }
}
