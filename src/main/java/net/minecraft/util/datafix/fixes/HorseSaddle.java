package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class HorseSaddle implements IFixableData {

    public HorseSaddle() {}

    public int getFixVersion() {
        return 110;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("EntityHorse".equals(nbttagcompound.getString("id")) && !nbttagcompound.hasKey("SaddleItem", 10) && nbttagcompound.getBoolean("Saddle")) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setString("id", "minecraft:saddle");
            nbttagcompound1.setByte("Count", (byte) 1);
            nbttagcompound1.setShort("Damage", (short) 0);
            nbttagcompound.setTag("SaddleItem", nbttagcompound1);
            nbttagcompound.removeTag("Saddle");
        }

        return nbttagcompound;
    }
}
