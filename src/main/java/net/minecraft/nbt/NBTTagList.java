package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase {

    private static final Logger field_179239_b = LogManager.getLogger();
    public List<NBTBase> field_74747_a = Lists.newArrayList(); // Paper
    // Paper start
    public void sort(java.util.Comparator<? extends NBTBase> comparator) {
        //noinspection unchecked
        java.util.Collections.sort(field_74747_a, (java.util.Comparator<NBTBase>) comparator);
    }
    // Paper end
    private byte field_74746_b = 0;

    public NBTTagList() {}

    void func_74734_a(DataOutput dataoutput) throws IOException {
        if (this.field_74747_a.isEmpty()) {
            this.field_74746_b = 0;
        } else {
            this.field_74746_b = ((NBTBase) this.field_74747_a.get(0)).func_74732_a();
        }

        dataoutput.writeByte(this.field_74746_b);
        dataoutput.writeInt(this.field_74747_a.size());

        for (int i = 0; i < this.field_74747_a.size(); ++i) {
            ((NBTBase) this.field_74747_a.get(i)).func_74734_a(dataoutput);
        }

    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(296L);
        if (i > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.field_74746_b = datainput.readByte();
            int j = datainput.readInt();

            if (this.field_74746_b == 0 && j > 0) {
                throw new RuntimeException("Missing type on ListTag");
            } else {
                nbtreadlimiter.func_152450_a(32L * (long) j);
                this.field_74747_a = Lists.newArrayListWithCapacity(j);

                for (int k = 0; k < j; ++k) {
                    NBTBase nbtbase = NBTBase.func_150284_a(this.field_74746_b);

                    nbtbase.func_152446_a(datainput, i + 1, nbtreadlimiter);
                    this.field_74747_a.add(nbtbase);
                }

            }
        }
    }

    public byte func_74732_a() {
        return (byte) 9;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[");

        for (int i = 0; i < this.field_74747_a.size(); ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.field_74747_a.get(i));
        }

        return stringbuilder.append(']').toString();
    }

    public void func_74742_a(NBTBase nbtbase) {
        if (nbtbase.func_74732_a() == 0) {
            NBTTagList.field_179239_b.warn("Invalid TagEnd added to ListTag");
        } else {
            if (this.field_74746_b == 0) {
                this.field_74746_b = nbtbase.func_74732_a();
            } else if (this.field_74746_b != nbtbase.func_74732_a()) {
                NBTTagList.field_179239_b.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.field_74747_a.add(nbtbase);
        }
    }

    public void func_150304_a(int i, NBTBase nbtbase) {
        if (nbtbase.func_74732_a() == 0) {
            NBTTagList.field_179239_b.warn("Invalid TagEnd added to ListTag");
        } else if (i >= 0 && i < this.field_74747_a.size()) {
            if (this.field_74746_b == 0) {
                this.field_74746_b = nbtbase.func_74732_a();
            } else if (this.field_74746_b != nbtbase.func_74732_a()) {
                NBTTagList.field_179239_b.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.field_74747_a.set(i, nbtbase);
        } else {
            NBTTagList.field_179239_b.warn("index out of bounds to set tag in tag list");
        }
    }

    public NBTBase func_74744_a(int i) {
        return (NBTBase) this.field_74747_a.remove(i);
    }

    public boolean func_82582_d() {
        return this.field_74747_a.isEmpty();
    }

    public NBTTagCompound func_150305_b(int i) {
        if (i >= 0 && i < this.field_74747_a.size()) {
            NBTBase nbtbase = (NBTBase) this.field_74747_a.get(i);

            if (nbtbase.func_74732_a() == 10) {
                return (NBTTagCompound) nbtbase;
            }
        }

        return new NBTTagCompound();
    }

    public int func_186858_c(int i) {
        if (i >= 0 && i < this.field_74747_a.size()) {
            NBTBase nbtbase = (NBTBase) this.field_74747_a.get(i);

            if (nbtbase.func_74732_a() == 3) {
                return ((NBTTagInt) nbtbase).func_150287_d();
            }
        }

        return 0;
    }

    public int[] func_150306_c(int i) {
        if (i >= 0 && i < this.field_74747_a.size()) {
            NBTBase nbtbase = (NBTBase) this.field_74747_a.get(i);

            if (nbtbase.func_74732_a() == 11) {
                return ((NBTTagIntArray) nbtbase).func_150302_c();
            }
        }

        return new int[0];
    }

    public final double getDoubleAt(int i) { return this.func_150309_d(i); } // Paper - OBFHELPER
    public double func_150309_d(int i) {
        if (i >= 0 && i < this.field_74747_a.size()) {
            NBTBase nbtbase = (NBTBase) this.field_74747_a.get(i);

            if (nbtbase.func_74732_a() == 6) {
                return ((NBTTagDouble) nbtbase).func_150286_g();
            }
        }

        return 0.0D;
    }

    public float func_150308_e(int i) {
        if (i >= 0 && i < this.field_74747_a.size()) {
            NBTBase nbtbase = (NBTBase) this.field_74747_a.get(i);

            if (nbtbase.func_74732_a() == 5) {
                return ((NBTTagFloat) nbtbase).func_150288_h();
            }
        }

        return 0.0F;
    }

    public String func_150307_f(int i) {
        if (i >= 0 && i < this.field_74747_a.size()) {
            NBTBase nbtbase = (NBTBase) this.field_74747_a.get(i);

            return nbtbase.func_74732_a() == 8 ? nbtbase.func_150285_a_() : nbtbase.toString();
        } else {
            return "";
        }
    }

    public NBTBase func_179238_g(int i) {
        return (NBTBase) (i >= 0 && i < this.field_74747_a.size() ? (NBTBase) this.field_74747_a.get(i) : new NBTTagEnd());
    }

    public int func_74745_c() {
        return this.field_74747_a.size();
    }

    public NBTTagList func_74737_b() {
        NBTTagList nbttaglist = new NBTTagList();

        nbttaglist.field_74746_b = this.field_74746_b;
        Iterator iterator = this.field_74747_a.iterator();

        while (iterator.hasNext()) {
            NBTBase nbtbase = (NBTBase) iterator.next();
            NBTBase nbtbase1 = nbtbase.func_74737_b();

            nbttaglist.field_74747_a.add(nbtbase1);
        }

        return nbttaglist;
    }

    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        } else {
            NBTTagList nbttaglist = (NBTTagList) object;

            return this.field_74746_b == nbttaglist.field_74746_b && Objects.equals(this.field_74747_a, nbttaglist.field_74747_a);
        }
    }

    public int hashCode() {
        return super.hashCode() ^ this.field_74747_a.hashCode();
    }

    public int func_150303_d() {
        return this.field_74746_b;
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}
