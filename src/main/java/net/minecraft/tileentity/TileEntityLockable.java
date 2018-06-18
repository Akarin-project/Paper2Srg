package net.minecraft.tileentity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;


public abstract class TileEntityLockable extends TileEntity implements ILockableContainer {

    private LockCode code;

    public TileEntityLockable() {
        this.code = LockCode.EMPTY_CODE;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.code = LockCode.fromNBT(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.code != null) {
            this.code.toNBT(nbttagcompound);
        }

        return nbttagcompound;
    }

    public boolean isLocked() {
        return this.code != null && !this.code.isEmpty();
    }

    public LockCode getLockCode() {
        return this.code;
    }

    public void setLockCode(LockCode chestlock) {
        this.code = chestlock;
    }

    public ITextComponent getDisplayName() {
        return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    // CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
        if (world == null) return null;
        return new org.bukkit.Location(world.getWorld(), pos.getX(), pos.getY(), pos.getZ());
    }
    // CraftBukkit end
}
