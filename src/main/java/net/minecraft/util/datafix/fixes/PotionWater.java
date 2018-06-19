package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class PotionWater implements IFixableData {

    public PotionWater() {}

    public int func_188216_a() {
        return 806;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.func_74779_i("id");

        if ("minecraft:potion".equals(s) || "minecraft:splash_potion".equals(s) || "minecraft:lingering_potion".equals(s) || "minecraft:tipped_arrow".equals(s)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("tag");

            if (!nbttagcompound1.func_150297_b("Potion", 8)) {
                nbttagcompound1.func_74778_a("Potion", "minecraft:water");
            }

            if (!nbttagcompound.func_150297_b("tag", 10)) {
                nbttagcompound.func_74782_a("tag", nbttagcompound1);
            }
        }

        return nbttagcompound;
    }
}
