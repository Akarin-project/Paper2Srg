package net.minecraft.entity.passive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public abstract class EntityShoulderRiding extends EntityTameable {

    private int rideCooldownCounter;

    public EntityShoulderRiding(World world) {
        super(world);
    }

    public boolean setEntityOnShoulder(EntityPlayer entityhuman) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("id", this.getEntityString());
        this.writeToNBT(nbttagcompound);
        if (entityhuman.addShoulderEntity(nbttagcompound)) {
            this.world.removeEntity(this);
            return true;
        } else {
            return false;
        }
    }

    public void onUpdate() {
        ++this.rideCooldownCounter;
        super.onUpdate();
    }

    public boolean canSitOnShoulder() {
        return this.rideCooldownCounter > 100;
    }
}
