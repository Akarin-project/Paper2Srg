package net.minecraft.util.datafix.fixes;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType implements IFixableData {

    private static final Random field_190049_a = new Random();

    public ZombieProfToType() {}

    public int func_188216_a() {
        return 502;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("Zombie".equals(nbttagcompound.func_74779_i("id")) && nbttagcompound.func_74767_n("IsVillager")) {
            if (!nbttagcompound.func_150297_b("ZombieType", 99)) {
                int i = -1;

                if (nbttagcompound.func_150297_b("VillagerProfession", 99)) {
                    try {
                        i = this.func_191277_a(nbttagcompound.func_74762_e("VillagerProfession"));
                    } catch (RuntimeException runtimeexception) {
                        ;
                    }
                }

                if (i == -1) {
                    i = this.func_191277_a(ZombieProfToType.field_190049_a.nextInt(6));
                }

                nbttagcompound.func_74768_a("ZombieType", i);
            }

            nbttagcompound.func_82580_o("IsVillager");
        }

        return nbttagcompound;
    }

    private int func_191277_a(int i) {
        return i >= 0 && i < 6 ? i : -1;
    }
}
