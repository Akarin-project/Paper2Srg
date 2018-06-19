package net.minecraft.util;
import net.minecraft.nbt.NBTTagCompound;


public class WeightedSpawnerEntity extends WeightedRandom.Item {

    private final NBTTagCompound field_185279_b;

    public WeightedSpawnerEntity() {
        super(1);
        this.field_185279_b = new NBTTagCompound();
        this.field_185279_b.func_74778_a("id", "minecraft:pig");
    }

    public WeightedSpawnerEntity(NBTTagCompound nbttagcompound) {
        this(nbttagcompound.func_150297_b("Weight", 99) ? nbttagcompound.func_74762_e("Weight") : 1, nbttagcompound.func_74775_l("Entity"));
    }

    public WeightedSpawnerEntity(int i, NBTTagCompound nbttagcompound) {
        super(i);
        this.field_185279_b = nbttagcompound;
    }

    public NBTTagCompound func_185278_a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!this.field_185279_b.func_150297_b("id", 8)) {
            this.field_185279_b.func_74778_a("id", "minecraft:pig");
        } else if (!this.field_185279_b.func_74779_i("id").contains(":")) {
            this.field_185279_b.func_74778_a("id", (new ResourceLocation(this.field_185279_b.func_74779_i("id"))).toString());
        }

        nbttagcompound.func_74782_a("Entity", this.field_185279_b);
        nbttagcompound.func_74768_a("Weight", this.field_76292_a);
        return nbttagcompound;
    }

    public NBTTagCompound func_185277_b() {
        return this.field_185279_b;
    }
}
