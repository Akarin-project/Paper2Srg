package net.minecraft.tileentity;
import net.minecraft.nbt.NBTTagCompound;


public class TileEntityComparator extends TileEntity {

    private int outputSignal;

    public TileEntityComparator() {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("OutputSignal", this.outputSignal);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.outputSignal = nbttagcompound.getInteger("OutputSignal");
    }

    public int getOutputSignal() {
        return this.outputSignal;
    }

    public void setOutputSignal(int i) {
        this.outputSignal = i;
    }
}
