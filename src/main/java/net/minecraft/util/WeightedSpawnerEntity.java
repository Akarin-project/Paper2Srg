package net.minecraft.util;
import net.minecraft.nbt.NBTTagCompound;


public class WeightedSpawnerEntity extends WeightedRandom.Item {

    private final NBTTagCompound nbt;

    public WeightedSpawnerEntity() {
        super(1);
        this.nbt = new NBTTagCompound();
        this.nbt.setString("id", "minecraft:pig");
    }

    public WeightedSpawnerEntity(NBTTagCompound nbttagcompound) {
        this(nbttagcompound.hasKey("Weight", 99) ? nbttagcompound.getInteger("Weight") : 1, nbttagcompound.getCompoundTag("Entity"));
    }

    public WeightedSpawnerEntity(int i, NBTTagCompound nbttagcompound) {
        super(i);
        this.nbt = nbttagcompound;
    }

    public NBTTagCompound toCompoundTag() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!this.nbt.hasKey("id", 8)) {
            this.nbt.setString("id", "minecraft:pig");
        } else if (!this.nbt.getString("id").contains(":")) {
            this.nbt.setString("id", (new ResourceLocation(this.nbt.getString("id"))).toString());
        }

        nbttagcompound.setTag("Entity", this.nbt);
        nbttagcompound.setInteger("Weight", this.itemWeight);
        return nbttagcompound;
    }

    public NBTTagCompound getNbt() {
        return this.nbt;
    }
}
