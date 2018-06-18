package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.datafix.IFixableData;


public class SpawnerEntityTypes implements IFixableData {

    public SpawnerEntityTypes() {}

    public int getFixVersion() {
        return 107;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if (!"MobSpawner".equals(nbttagcompound.getString("id"))) {
            return nbttagcompound;
        } else {
            if (nbttagcompound.hasKey("EntityId", 8)) {
                String s = nbttagcompound.getString("EntityId");
                NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("SpawnData");

                nbttagcompound1.setString("id", s.isEmpty() ? "Pig" : s);
                nbttagcompound.setTag("SpawnData", nbttagcompound1);
                nbttagcompound.removeTag("EntityId");
            }

            if (nbttagcompound.hasKey("SpawnPotentials", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("SpawnPotentials", 10);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);

                    if (nbttagcompound2.hasKey("Type", 8)) {
                        NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompoundTag("Properties");

                        nbttagcompound3.setString("id", nbttagcompound2.getString("Type"));
                        nbttagcompound2.setTag("Entity", nbttagcompound3);
                        nbttagcompound2.removeTag("Type");
                        nbttagcompound2.removeTag("Properties");
                    }
                }
            }

            return nbttagcompound;
        }
    }
}
