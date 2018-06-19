package net.minecraft.world.gen.structure;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;


public class MapGenStructureData extends WorldSavedData {

    private NBTTagCompound field_143044_a = new NBTTagCompound();

    public MapGenStructureData(String s) {
        super(s);
    }

    public void func_76184_a(NBTTagCompound nbttagcompound) {
        this.field_143044_a = nbttagcompound.func_74775_l("Features");
    }

    public NBTTagCompound func_189551_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74782_a("Features", this.field_143044_a);
        return nbttagcompound;
    }

    public void func_143043_a(NBTTagCompound nbttagcompound, int i, int j) {
        this.field_143044_a.func_74782_a(func_143042_b(i, j), nbttagcompound);
    }

    public static String func_143042_b(int i, int j) {
        return "[" + i + "," + j + "]";
    }

    public NBTTagCompound func_143041_a() {
        return this.field_143044_a;
    }
}
