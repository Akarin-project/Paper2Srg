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

    private static final Logger LOGGER = LogManager.getLogger();
    public List<NBTBase> tagList = Lists.newArrayList(); // Paper
    // Paper start
    public void sort(java.util.Comparator<? extends NBTBase> comparator) {
        //noinspection unchecked
        java.util.Collections.sort(tagList, (java.util.Comparator<NBTBase>) comparator);
    }
    // Paper end
    private byte tagType = 0;

    public NBTTagList() {}

    void write(DataOutput dataoutput) throws IOException {
        if (this.tagList.isEmpty()) {
            this.tagType = 0;
        } else {
            this.tagType = ((NBTBase) this.tagList.get(0)).getId();
        }

        dataoutput.writeByte(this.tagType);
        dataoutput.writeInt(this.tagList.size());

        for (int i = 0; i < this.tagList.size(); ++i) {
            ((NBTBase) this.tagList.get(i)).write(dataoutput);
        }

    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(296L);
        if (i > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.tagType = datainput.readByte();
            int j = datainput.readInt();

            if (this.tagType == 0 && j > 0) {
                throw new RuntimeException("Missing type on ListTag");
            } else {
                nbtreadlimiter.read(32L * (long) j);
                this.tagList = Lists.newArrayListWithCapacity(j);

                for (int k = 0; k < j; ++k) {
                    NBTBase nbtbase = NBTBase.createNewByType(this.tagType);

                    nbtbase.read(datainput, i + 1, nbtreadlimiter);
                    this.tagList.add(nbtbase);
                }

            }
        }
    }

    public byte getId() {
        return (byte) 9;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[");

        for (int i = 0; i < this.tagList.size(); ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.tagList.get(i));
        }

        return stringbuilder.append(']').toString();
    }

    public void appendTag(NBTBase nbtbase) {
        if (nbtbase.getId() == 0) {
            NBTTagList.LOGGER.warn("Invalid TagEnd added to ListTag");
        } else {
            if (this.tagType == 0) {
                this.tagType = nbtbase.getId();
            } else if (this.tagType != nbtbase.getId()) {
                NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.tagList.add(nbtbase);
        }
    }

    public void set(int i, NBTBase nbtbase) {
        if (nbtbase.getId() == 0) {
            NBTTagList.LOGGER.warn("Invalid TagEnd added to ListTag");
        } else if (i >= 0 && i < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbtbase.getId();
            } else if (this.tagType != nbtbase.getId()) {
                NBTTagList.LOGGER.warn("Adding mismatching tag types to tag list");
                return;
            }

            this.tagList.set(i, nbtbase);
        } else {
            NBTTagList.LOGGER.warn("index out of bounds to set tag in tag list");
        }
    }

    public NBTBase removeTag(int i) {
        return (NBTBase) this.tagList.remove(i);
    }

    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }

    public NBTTagCompound getCompoundTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);

            if (nbtbase.getId() == 10) {
                return (NBTTagCompound) nbtbase;
            }
        }

        return new NBTTagCompound();
    }

    public int getIntAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);

            if (nbtbase.getId() == 3) {
                return ((NBTTagInt) nbtbase).getInt();
            }
        }

        return 0;
    }

    public int[] getIntArrayAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);

            if (nbtbase.getId() == 11) {
                return ((NBTTagIntArray) nbtbase).getIntArray();
            }
        }

        return new int[0];
    }

    public final double getDoubleAt(int i) { return this.getDoubleAt(i); } // Paper - OBFHELPER
    public double getDoubleAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);

            if (nbtbase.getId() == 6) {
                return ((NBTTagDouble) nbtbase).getDouble();
            }
        }

        return 0.0D;
    }

    public float getFloatAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);

            if (nbtbase.getId() == 5) {
                return ((NBTTagFloat) nbtbase).getFloat();
            }
        }

        return 0.0F;
    }

    public String getStringTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);

            return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
        } else {
            return "";
        }
    }

    public NBTBase get(int i) {
        return (NBTBase) (i >= 0 && i < this.tagList.size() ? (NBTBase) this.tagList.get(i) : new NBTTagEnd());
    }

    public int tagCount() {
        return this.tagList.size();
    }

    public NBTTagList copy() {
        NBTTagList nbttaglist = new NBTTagList();

        nbttaglist.tagType = this.tagType;
        Iterator iterator = this.tagList.iterator();

        while (iterator.hasNext()) {
            NBTBase nbtbase = (NBTBase) iterator.next();
            NBTBase nbtbase1 = nbtbase.copy();

            nbttaglist.tagList.add(nbtbase1);
        }

        return nbttaglist;
    }

    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        } else {
            NBTTagList nbttaglist = (NBTTagList) object;

            return this.tagType == nbttaglist.tagType && Objects.equals(this.tagList, nbttaglist.tagList);
        }
    }

    public int hashCode() {
        return super.hashCode() ^ this.tagList.hashCode();
    }

    public int getTagType() {
        return this.tagType;
    }

    public NBTBase clone() {
        return this.copy();
    }
}
