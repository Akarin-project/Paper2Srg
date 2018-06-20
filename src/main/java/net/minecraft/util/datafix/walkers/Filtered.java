package net.minecraft.util.datafix.walkers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;


public abstract class Filtered implements IDataWalker {

    private final ResourceLocation field_188272_a;

    public Filtered(Class oclass) {
        if (Entity.class.isAssignableFrom(oclass)) {
            this.field_188272_a = EntityList.func_191306_a(oclass);
        } else if (TileEntity.class.isAssignableFrom(oclass)) {
            this.field_188272_a = TileEntity.func_190559_a(oclass);
        } else {
            this.field_188272_a = null;
        }

    }

    @Override
    public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
        if ((new ResourceLocation(nbttagcompound.func_74779_i("id"))).equals(this.field_188272_a)) {
            nbttagcompound = this.func_188271_b(dataconverter, nbttagcompound, i);
        }

        return nbttagcompound;
    }

    abstract NBTTagCompound func_188271_b(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i);
}
