package net.minecraft.entity.player;
import net.minecraft.nbt.NBTTagCompound;


public class PlayerCapabilities {

    public boolean field_75102_a;
    public boolean field_75100_b;
    public boolean field_75101_c;
    public boolean field_75098_d;
    public boolean field_75099_e = true;
    public float field_75096_f = 0.05F;
    public float field_75097_g = 0.1F;

    public PlayerCapabilities() {}

    public void func_75091_a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.func_74757_a("invulnerable", this.field_75102_a);
        nbttagcompound1.func_74757_a("flying", this.field_75100_b);
        nbttagcompound1.func_74757_a("mayfly", this.field_75101_c);
        nbttagcompound1.func_74757_a("instabuild", this.field_75098_d);
        nbttagcompound1.func_74757_a("mayBuild", this.field_75099_e);
        nbttagcompound1.func_74776_a("flySpeed", this.field_75096_f);
        nbttagcompound1.func_74776_a("walkSpeed", this.field_75097_g);
        nbttagcompound.func_74782_a("abilities", nbttagcompound1);
    }

    public void func_75095_b(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("abilities", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("abilities");

            this.field_75102_a = nbttagcompound1.func_74767_n("invulnerable");
            this.field_75100_b = nbttagcompound1.func_74767_n("flying");
            this.field_75101_c = nbttagcompound1.func_74767_n("mayfly");
            this.field_75098_d = nbttagcompound1.func_74767_n("instabuild");
            if (nbttagcompound1.func_150297_b("flySpeed", 99)) {
                this.field_75096_f = nbttagcompound1.func_74760_g("flySpeed");
                this.field_75097_g = nbttagcompound1.func_74760_g("walkSpeed");
            }

            if (nbttagcompound1.func_150297_b("mayBuild", 1)) {
                this.field_75099_e = nbttagcompound1.func_74767_n("mayBuild");
            }
        }

    }

    public float func_75093_a() {
        return this.field_75096_f;
    }

    public float func_75094_b() {
        return this.field_75097_g;
    }
}
