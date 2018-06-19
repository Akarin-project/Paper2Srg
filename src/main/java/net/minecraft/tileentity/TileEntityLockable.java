package net.minecraft.tileentity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;


public abstract class TileEntityLockable extends TileEntity implements ILockableContainer {

    private LockCode field_174901_a;

    public TileEntityLockable() {
        this.field_174901_a = LockCode.field_180162_a;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_174901_a = LockCode.func_180158_b(nbttagcompound);
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        if (this.field_174901_a != null) {
            this.field_174901_a.func_180157_a(nbttagcompound);
        }

        return nbttagcompound;
    }

    public boolean func_174893_q_() {
        return this.field_174901_a != null && !this.field_174901_a.func_180160_a();
    }

    public LockCode func_174891_i() {
        return this.field_174901_a;
    }

    public void func_174892_a(LockCode chestlock) {
        this.field_174901_a = chestlock;
    }

    public ITextComponent func_145748_c_() {
        return (ITextComponent) (this.func_145818_k_() ? new TextComponentString(this.func_70005_c_()) : new TextComponentTranslation(this.func_70005_c_(), new Object[0]));
    }

    // CraftBukkit start
    @Override
    public org.bukkit.Location getLocation() {
        if (field_145850_b == null) return null;
        return new org.bukkit.Location(field_145850_b.getWorld(), field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p());
    }
    // CraftBukkit end
}
