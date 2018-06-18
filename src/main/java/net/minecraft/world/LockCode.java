package net.minecraft.world;

import javax.annotation.concurrent.Immutable;

import net.minecraft.nbt.NBTTagCompound;

@Immutable
public class LockCode {

    public static final LockCode EMPTY_CODE = new LockCode("");
    private final String lock;

    public LockCode(String s) {
        this.lock = s;
    }

    public boolean isEmpty() {
        return this.lock == null || this.lock.isEmpty();
    }

    public String getLock() {
        return this.lock;
    }

    public void toNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("Lock", this.lock);
    }

    public static LockCode fromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("Lock", 8)) {
            String s = nbttagcompound.getString("Lock");

            return new LockCode(s);
        } else {
            return LockCode.EMPTY_CODE;
        }
    }
}
