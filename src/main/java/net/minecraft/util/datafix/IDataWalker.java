package net.minecraft.util.datafix;
import net.minecraft.nbt.NBTTagCompound;


public interface IDataWalker {

    NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i);
}
