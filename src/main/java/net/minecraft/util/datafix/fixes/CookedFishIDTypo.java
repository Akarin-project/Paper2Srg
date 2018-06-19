package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;


public class CookedFishIDTypo implements IFixableData {

    private static final ResourceLocation field_190050_a = new ResourceLocation("cooked_fished");

    public CookedFishIDTypo() {}

    public int func_188216_a() {
        return 502;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("id", 8) && CookedFishIDTypo.field_190050_a.equals(new ResourceLocation(nbttagcompound.func_74779_i("id")))) {
            nbttagcompound.func_74778_a("id", "minecraft:cooked_fish");
        }

        return nbttagcompound;
    }
}
