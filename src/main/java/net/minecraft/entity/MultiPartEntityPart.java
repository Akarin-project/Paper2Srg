package net.minecraft.entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;


public class MultiPartEntityPart extends Entity {

    public final IEntityMultiPart parent;
    public final String partName;

    public MultiPartEntityPart(IEntityMultiPart icomplex, String s, float f, float f1) {
        super(icomplex.getWorld());
        this.setSize(f, f1);
        this.parent = icomplex;
        this.partName = s;
    }

    protected void entityInit() {}

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        return this.isEntityInvulnerable(damagesource) ? false : this.parent.attackEntityFromPart(this, damagesource, f);
    }

    public boolean isEntityEqual(Entity entity) {
        return this == entity || this.parent == entity;
    }
}
