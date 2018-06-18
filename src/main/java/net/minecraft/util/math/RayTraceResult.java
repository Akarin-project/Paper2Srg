package net.minecraft.util.math;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;


public class RayTraceResult {

    private BlockPos blockPos;
    public RayTraceResult.Type typeOfHit;
    public EnumFacing sideHit;
    public Vec3d hitVec;
    public Entity entityHit;

    public RayTraceResult(Vec3d vec3d, EnumFacing enumdirection, BlockPos blockposition) {
        this(RayTraceResult.Type.BLOCK, vec3d, enumdirection, blockposition);
    }

    public RayTraceResult(Vec3d vec3d, EnumFacing enumdirection) {
        this(RayTraceResult.Type.BLOCK, vec3d, enumdirection, BlockPos.ORIGIN);
    }

    public RayTraceResult(Entity entity) {
        this(entity, new Vec3d(entity.posX, entity.posY, entity.posZ));
    }

    public RayTraceResult(RayTraceResult.Type movingobjectposition_enummovingobjecttype, Vec3d vec3d, EnumFacing enumdirection, BlockPos blockposition) {
        this.typeOfHit = movingobjectposition_enummovingobjecttype;
        this.blockPos = blockposition;
        this.sideHit = enumdirection;
        this.hitVec = new Vec3d(vec3d.x, vec3d.y, vec3d.z);
    }

    public RayTraceResult(Entity entity, Vec3d vec3d) {
        this.typeOfHit = RayTraceResult.Type.ENTITY;
        this.entityHit = entity;
        this.hitVec = vec3d;
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public String toString() {
        return "HitResult{type=" + this.typeOfHit + ", blockpos=" + this.blockPos + ", f=" + this.sideHit + ", pos=" + this.hitVec + ", entity=" + this.entityHit + '}';
    }

    public static enum Type {

        MISS, BLOCK, ENTITY;

        private Type() {}
    }
}
