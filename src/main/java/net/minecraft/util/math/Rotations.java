package net.minecraft.util.math;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;


public class Rotations {

    protected final float field_179419_a;
    protected final float field_179417_b;
    protected final float field_179418_c;

    public Rotations(float f, float f1, float f2) {
        this.field_179419_a = !Float.isInfinite(f) && !Float.isNaN(f) ? f % 360.0F : 0.0F;
        this.field_179417_b = !Float.isInfinite(f1) && !Float.isNaN(f1) ? f1 % 360.0F : 0.0F;
        this.field_179418_c = !Float.isInfinite(f2) && !Float.isNaN(f2) ? f2 % 360.0F : 0.0F;
    }

    public Rotations(NBTTagList nbttaglist) {
        this(nbttaglist.func_150308_e(0), nbttaglist.func_150308_e(1), nbttaglist.func_150308_e(2));
    }

    public NBTTagList func_179414_a() {
        NBTTagList nbttaglist = new NBTTagList();

        nbttaglist.func_74742_a(new NBTTagFloat(this.field_179419_a));
        nbttaglist.func_74742_a(new NBTTagFloat(this.field_179417_b));
        nbttaglist.func_74742_a(new NBTTagFloat(this.field_179418_c));
        return nbttaglist;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Rotations)) {
            return false;
        } else {
            Rotations vector3f = (Rotations) object;

            return this.field_179419_a == vector3f.field_179419_a && this.field_179417_b == vector3f.field_179417_b && this.field_179418_c == vector3f.field_179418_c;
        }
    }

    public float func_179415_b() {
        return this.field_179419_a;
    }

    public float func_179416_c() {
        return this.field_179417_b;
    }

    public float func_179413_d() {
        return this.field_179418_c;
    }
}
