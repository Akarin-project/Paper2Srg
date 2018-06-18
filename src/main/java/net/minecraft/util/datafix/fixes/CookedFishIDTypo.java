package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;


public class CookedFishIDTypo implements IFixableData {

    private static final ResourceLocation WRONG = new ResourceLocation("cooked_fished");

    public CookedFishIDTypo() {}

    public int getFixVersion() {
        return 502;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("id", 8) && CookedFishIDTypo.WRONG.equals(new ResourceLocation(nbttagcompound.getString("id")))) {
            nbttagcompound.setString("id", "minecraft:cooked_fish");
        }

        return nbttagcompound;
    }
}
