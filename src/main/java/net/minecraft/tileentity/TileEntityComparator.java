package net.minecraft.tileentity;
import net.minecraft.nbt.NBTTagCompound;


public class TileEntityComparator extends TileEntity {

    private int field_145997_a;

    public TileEntityComparator() {}

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74768_a("OutputSignal", this.field_145997_a);
        return nbttagcompound;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145997_a = nbttagcompound.func_74762_e("OutputSignal");
    }

    public int func_145996_a() {
        return this.field_145997_a;
    }

    public void func_145995_a(int i) {
        this.field_145997_a = i;
    }
}
