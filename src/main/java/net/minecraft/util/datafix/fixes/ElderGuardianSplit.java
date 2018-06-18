package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ElderGuardianSplit implements IFixableData {

    public ElderGuardianSplit() {}

    public int getFixVersion() {
        return 700;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("Guardian".equals(nbttagcompound.getString("id"))) {
            if (nbttagcompound.getBoolean("Elder")) {
                nbttagcompound.setString("id", "ElderGuardian");
            }

            nbttagcompound.removeTag("Elder");
        }

        return nbttagcompound;
    }
}
