package net.minecraft.util.datafix.walkers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;


public abstract class Filtered implements IDataWalker {

    private final ResourceLocation key;

    public Filtered(Class<?> oclass) {
        if (Entity.class.isAssignableFrom(oclass)) {
            this.key = EntityList.getKey(oclass);
        } else if (TileEntity.class.isAssignableFrom(oclass)) {
            this.key = TileEntity.getKey(oclass);
        } else {
            this.key = null;
        }

    }

    public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
        if ((new ResourceLocation(nbttagcompound.getString("id"))).equals(this.key)) {
            nbttagcompound = this.filteredProcess(dataconverter, nbttagcompound, i);
        }

        return nbttagcompound;
    }

    abstract NBTTagCompound filteredProcess(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i);
}
