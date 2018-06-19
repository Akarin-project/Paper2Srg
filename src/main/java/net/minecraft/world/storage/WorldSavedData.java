package net.minecraft.world.storage;
import net.minecraft.nbt.NBTTagCompound;


public abstract class WorldSavedData {

    public final String field_76190_i;
    private boolean field_76189_a;

    public WorldSavedData(String s) {
        this.field_76190_i = s;
    }

    public abstract void func_76184_a(NBTTagCompound nbttagcompound);

    public abstract NBTTagCompound func_189551_b(NBTTagCompound nbttagcompound);

    public void func_76185_a() {
        this.func_76186_a(true);
    }

    public void func_76186_a(boolean flag) {
        this.field_76189_a = flag;
    }

    public boolean func_76188_b() {
        return this.field_76189_a;
    }
}
