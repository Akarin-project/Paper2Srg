package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class SpawnerEntityTypes implements IFixableData {

    public SpawnerEntityTypes() {}

    public int func_188216_a() {
        return 107;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if (!"MobSpawner".equals(nbttagcompound.func_74779_i("id"))) {
            return nbttagcompound;
        } else {
            if (nbttagcompound.func_150297_b("EntityId", 8)) {
                String s = nbttagcompound.func_74779_i("EntityId");
                NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("SpawnData");

                nbttagcompound1.func_74778_a("id", s.isEmpty() ? "Pig" : s);
                nbttagcompound.func_74782_a("SpawnData", nbttagcompound1);
                nbttagcompound.func_82580_o("EntityId");
            }

            if (nbttagcompound.func_150297_b("SpawnPotentials", 9)) {
                NBTTagList nbttaglist = nbttagcompound.func_150295_c("SpawnPotentials", 10);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    NBTTagCompound nbttagcompound2 = nbttaglist.func_150305_b(i);

                    if (nbttagcompound2.func_150297_b("Type", 8)) {
                        NBTTagCompound nbttagcompound3 = nbttagcompound2.func_74775_l("Properties");

                        nbttagcompound3.func_74778_a("id", nbttagcompound2.func_74779_i("Type"));
                        nbttagcompound2.func_74782_a("Entity", nbttagcompound3);
                        nbttagcompound2.func_82580_o("Type");
                        nbttagcompound2.func_82580_o("Properties");
                    }
                }
            }

            return nbttagcompound;
        }
    }
}
