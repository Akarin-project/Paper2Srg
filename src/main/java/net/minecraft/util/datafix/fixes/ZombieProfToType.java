package net.minecraft.util.datafix.fixes;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class ZombieProfToType implements IFixableData {

    private static final Random RANDOM = new Random();

    public ZombieProfToType() {}

    public int getFixVersion() {
        return 502;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("Zombie".equals(nbttagcompound.getString("id")) && nbttagcompound.getBoolean("IsVillager")) {
            if (!nbttagcompound.hasKey("ZombieType", 99)) {
                int i = -1;

                if (nbttagcompound.hasKey("VillagerProfession", 99)) {
                    try {
                        i = this.getVillagerProfession(nbttagcompound.getInteger("VillagerProfession"));
                    } catch (RuntimeException runtimeexception) {
                        ;
                    }
                }

                if (i == -1) {
                    i = this.getVillagerProfession(ZombieProfToType.RANDOM.nextInt(6));
                }

                nbttagcompound.setInteger("ZombieType", i);
            }

            nbttagcompound.removeTag("IsVillager");
        }

        return nbttagcompound;
    }

    private int getVillagerProfession(int i) {
        return i >= 0 && i < 6 ? i : -1;
    }
}
