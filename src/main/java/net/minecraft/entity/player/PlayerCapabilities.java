package net.minecraft.entity.player;
import net.minecraft.nbt.NBTTagCompound;


public class PlayerCapabilities {

    public boolean disableDamage;
    public boolean isFlying;
    public boolean allowFlying;
    public boolean isCreativeMode;
    public boolean allowEdit = true;
    public float flySpeed = 0.05F;
    public float walkSpeed = 0.1F;

    public PlayerCapabilities() {}

    public void writeCapabilitiesToNBT(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.setBoolean("invulnerable", this.disableDamage);
        nbttagcompound1.setBoolean("flying", this.isFlying);
        nbttagcompound1.setBoolean("mayfly", this.allowFlying);
        nbttagcompound1.setBoolean("instabuild", this.isCreativeMode);
        nbttagcompound1.setBoolean("mayBuild", this.allowEdit);
        nbttagcompound1.setFloat("flySpeed", this.flySpeed);
        nbttagcompound1.setFloat("walkSpeed", this.walkSpeed);
        nbttagcompound.setTag("abilities", nbttagcompound1);
    }

    public void readCapabilitiesFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("abilities", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("abilities");

            this.disableDamage = nbttagcompound1.getBoolean("invulnerable");
            this.isFlying = nbttagcompound1.getBoolean("flying");
            this.allowFlying = nbttagcompound1.getBoolean("mayfly");
            this.isCreativeMode = nbttagcompound1.getBoolean("instabuild");
            if (nbttagcompound1.hasKey("flySpeed", 99)) {
                this.flySpeed = nbttagcompound1.getFloat("flySpeed");
                this.walkSpeed = nbttagcompound1.getFloat("walkSpeed");
            }

            if (nbttagcompound1.hasKey("mayBuild", 1)) {
                this.allowEdit = nbttagcompound1.getBoolean("mayBuild");
            }
        }

    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public float getWalkSpeed() {
        return this.walkSpeed;
    }
}
