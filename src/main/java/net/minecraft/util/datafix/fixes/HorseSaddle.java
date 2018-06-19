package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class HorseSaddle implements IFixableData {

    public HorseSaddle() {}

    public int func_188216_a() {
        return 110;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("EntityHorse".equals(nbttagcompound.func_74779_i("id")) && !nbttagcompound.func_150297_b("SaddleItem", 10) && nbttagcompound.func_74767_n("Saddle")) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.func_74778_a("id", "minecraft:saddle");
            nbttagcompound1.func_74774_a("Count", (byte) 1);
            nbttagcompound1.func_74777_a("Damage", (short) 0);
            nbttagcompound.func_74782_a("SaddleItem", nbttagcompound1);
            nbttagcompound.func_82580_o("Saddle");
        }

        return nbttagcompound;
    }
}
