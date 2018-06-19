package net.minecraft.entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;


public class MultiPartEntityPart extends Entity {

    public final IEntityMultiPart field_70259_a;
    public final String field_146032_b;

    public MultiPartEntityPart(IEntityMultiPart icomplex, String s, float f, float f1) {
        super(icomplex.func_82194_d());
        this.func_70105_a(f, f1);
        this.field_70259_a = icomplex;
        this.field_146032_b = s;
    }

    protected void func_70088_a() {}

    protected void func_70037_a(NBTTagCompound nbttagcompound) {}

    protected void func_70014_b(NBTTagCompound nbttagcompound) {}

    public boolean func_70067_L() {
        return true;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        return this.func_180431_b(damagesource) ? false : this.field_70259_a.func_70965_a(this, damagesource, f);
    }

    public boolean func_70028_i(Entity entity) {
        return this == entity || this.field_70259_a == entity;
    }
}
