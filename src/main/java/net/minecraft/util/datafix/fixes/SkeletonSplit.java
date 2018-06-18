package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class SkeletonSplit implements IFixableData {

    public SkeletonSplit() {}

    public int getFixVersion() {
        return 701;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.getString("id");

        if ("Skeleton".equals(s)) {
            int i = nbttagcompound.getInteger("SkeletonType");

            if (i == 1) {
                nbttagcompound.setString("id", "WitherSkeleton");
            } else if (i == 2) {
                nbttagcompound.setString("id", "Stray");
            }

            nbttagcompound.removeTag("SkeletonType");
        }

        return nbttagcompound;
    }
}
