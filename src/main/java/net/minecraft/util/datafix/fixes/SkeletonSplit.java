package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class SkeletonSplit implements IFixableData {

    public SkeletonSplit() {}

    public int func_188216_a() {
        return 701;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        String s = nbttagcompound.func_74779_i("id");

        if ("Skeleton".equals(s)) {
            int i = nbttagcompound.func_74762_e("SkeletonType");

            if (i == 1) {
                nbttagcompound.func_74778_a("id", "WitherSkeleton");
            } else if (i == 2) {
                nbttagcompound.func_74778_a("id", "Stray");
            }

            nbttagcompound.func_82580_o("SkeletonType");
        }

        return nbttagcompound;
    }
}
