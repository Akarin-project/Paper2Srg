package net.minecraft.entity.ai;

public abstract class EntityAIBase {

    private int field_75254_a;

    public EntityAIBase() {}

    public abstract boolean func_75250_a();

    public boolean func_75253_b() {
        return this.func_75250_a();
    }

    public boolean func_75252_g() {
        return true;
    }

    public void func_75249_e() {}

    public void func_75251_c() {
        onTaskReset(); // Paper
    }
    public void onTaskReset() {} // Paper

    public void func_75246_d() {}

    public void func_75248_a(int i) {
        this.field_75254_a = i;
    }

    public int func_75247_h() {
        return this.field_75254_a;
    }
}
