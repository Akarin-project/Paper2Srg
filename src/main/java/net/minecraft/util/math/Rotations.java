package net.minecraft.util.math;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;


public class Rotations {

    protected final float x;
    protected final float y;
    protected final float z;

    public Rotations(float f, float f1, float f2) {
        this.x = !Float.isInfinite(f) && !Float.isNaN(f) ? f % 360.0F : 0.0F;
        this.y = !Float.isInfinite(f1) && !Float.isNaN(f1) ? f1 % 360.0F : 0.0F;
        this.z = !Float.isInfinite(f2) && !Float.isNaN(f2) ? f2 % 360.0F : 0.0F;
    }

    public Rotations(NBTTagList nbttaglist) {
        this(nbttaglist.getFloatAt(0), nbttaglist.getFloatAt(1), nbttaglist.getFloatAt(2));
    }

    public NBTTagList writeToNBT() {
        NBTTagList nbttaglist = new NBTTagList();

        nbttaglist.appendTag(new NBTTagFloat(this.x));
        nbttaglist.appendTag(new NBTTagFloat(this.y));
        nbttaglist.appendTag(new NBTTagFloat(this.z));
        return nbttaglist;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Rotations)) {
            return false;
        } else {
            Rotations vector3f = (Rotations) object;

            return this.x == vector3f.x && this.y == vector3f.y && this.z == vector3f.z;
        }
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }
}
