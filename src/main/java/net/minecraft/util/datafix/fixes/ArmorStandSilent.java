package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ArmorStandSilent implements IFixableData {

    public ArmorStandSilent() {}

    public int func_188216_a() {
        return 147;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if ("ArmorStand".equals(nbttagcompound.func_74779_i("id")) && nbttagcompound.func_74767_n("Silent") && !nbttagcompound.func_74767_n("Marker")) {
            nbttagcompound.func_82580_o("Silent");
        }

        return nbttagcompound;
    }
}
