package net.minecraft.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;

public class NBTTagCompound extends NBTBase {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
    public final Map<String, NBTBase> tagMap = Maps.newHashMap(); // Paper

    public NBTTagCompound() {}

    void write(DataOutput dataoutput) throws IOException {
        Iterator iterator = this.tagMap.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            NBTBase nbtbase = (NBTBase) this.tagMap.get(s);

            writeEntry(s, nbtbase, dataoutput);
        }

        dataoutput.writeByte(0);
    }

    void read(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.read(384L);
        if (i > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.tagMap.clear();

            byte b0;

            while ((b0 = readType(datainput, nbtreadlimiter)) != 0) {
                String s = readKey(datainput, nbtreadlimiter);

                nbtreadlimiter.read((long) (224 + 16 * s.length()));
                NBTBase nbtbase = readNBT(b0, s, datainput, i + 1, nbtreadlimiter);

                if (this.tagMap.put(s, nbtbase) != null) {
                    nbtreadlimiter.read(288L);
                }
            }

        }
    }

    public Set<String> getKeySet() {
        return this.tagMap.keySet();
    }

    public byte getId() {
        return (byte) 10;
    }

    public int getSize() {
        return this.tagMap.size();
    }

    public void setTag(String s, NBTBase nbtbase) {
        this.tagMap.put(s, nbtbase);
    }

    public void setByte(String s, byte b0) {
        this.tagMap.put(s, new NBTTagByte(b0));
    }

    public void setShort(String s, short short0) {
        this.tagMap.put(s, new NBTTagShort(short0));
    }

    public void setInteger(String s, int i) {
        this.tagMap.put(s, new NBTTagInt(i));
    }

    public void setLong(String s, long i) {
        this.tagMap.put(s, new NBTTagLong(i));
    }

    public void setUUID(String prefix, UUID uuid) { setUniqueId(prefix, uuid); } // Paper - OBFHELPER
    public void setUniqueId(String s, UUID uuid) {
        this.setLong(s + "Most", uuid.getMostSignificantBits());
        this.setLong(s + "Least", uuid.getLeastSignificantBits());
    }

    public UUID getUUID(String prefix) { return getUniqueId(prefix); } // Paper - OBFHELPER
    @Nullable
    public UUID getUniqueId(String s) {
        return new UUID(this.getLong(s + "Most"), this.getLong(s + "Least"));
    }

    public boolean hasUUID(String s) { return hasUniqueId(s); } public boolean hasUniqueId(String s) { // Paper - OBFHELPER
        return this.hasKey(s + "Most", 99) && this.hasKey(s + "Least", 99);
    }

    public void setFloat(String s, float f) {
        this.tagMap.put(s, new NBTTagFloat(f));
    }

    public void setDouble(String s, double d0) {
        this.tagMap.put(s, new NBTTagDouble(d0));
    }

    public void setString(String s, String s1) {
        this.tagMap.put(s, new NBTTagString(s1));
    }

    public void setByteArray(String s, byte[] abyte) {
        this.tagMap.put(s, new NBTTagByteArray(abyte));
    }

    public void setIntArray(String s, int[] aint) {
        this.tagMap.put(s, new NBTTagIntArray(aint));
    }

    public void setBoolean(String s, boolean flag) {
        this.setByte(s, (byte) (flag ? 1 : 0));
    }

    public NBTBase getTag(String s) {
        return (NBTBase) this.tagMap.get(s);
    }

    public byte getTagId(String s) {
        NBTBase nbtbase = (NBTBase) this.tagMap.get(s);

        return nbtbase == null ? 0 : nbtbase.getId();
    }

    public boolean hasKey(String s) {
        return this.tagMap.containsKey(s);
    }

    public boolean hasKey(String s, int i) {
        byte b0 = this.getTagId(s);

        return b0 == i ? true : (i != 99 ? false : b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6);
    }

    public byte getByte(String s) {
        try {
            if (this.hasKey(s, 99)) {
                return ((NBTPrimitive) this.tagMap.get(s)).getByte();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return (byte) 0;
    }

    public short getShort(String s) {
        try {
            if (this.hasKey(s, 99)) {
                return ((NBTPrimitive) this.tagMap.get(s)).getShort();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return (short) 0;
    }

    public int getInteger(String s) {
        try {
            if (this.hasKey(s, 99)) {
                return ((NBTPrimitive) this.tagMap.get(s)).getInt();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0;
    }

    public long getLong(String s) {
        try {
            if (this.hasKey(s, 99)) {
                return ((NBTPrimitive) this.tagMap.get(s)).getLong();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0L;
    }

    public float getFloat(String s) {
        try {
            if (this.hasKey(s, 99)) {
                return ((NBTPrimitive) this.tagMap.get(s)).getFloat();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0.0F;
    }

    public double getDouble(String s) {
        try {
            if (this.hasKey(s, 99)) {
                return ((NBTPrimitive) this.tagMap.get(s)).getDouble();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0.0D;
    }

    public String getString(String s) {
        try {
            if (this.hasKey(s, 8)) {
                return ((NBTBase) this.tagMap.get(s)).getString();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return "";
    }

    public byte[] getByteArray(String s) {
        try {
            if (this.hasKey(s, 7)) {
                return ((NBTTagByteArray) this.tagMap.get(s)).getByteArray();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(s, 7, classcastexception));
        }

        return new byte[0];
    }

    public int[] getIntArray(String s) {
        try {
            if (this.hasKey(s, 11)) {
                return ((NBTTagIntArray) this.tagMap.get(s)).getIntArray();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(s, 11, classcastexception));
        }

        return new int[0];
    }

    public NBTTagCompound getCompoundTag(String s) {
        try {
            if (this.hasKey(s, 10)) {
                return (NBTTagCompound) this.tagMap.get(s);
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(s, 10, classcastexception));
        }

        return new NBTTagCompound();
    }

    public NBTTagList getTagList(String s, int i) {
        try {
            if (this.getTagId(s) == 9) {
                NBTTagList nbttaglist = (NBTTagList) this.tagMap.get(s);

                if (!nbttaglist.hasNoTags() && nbttaglist.getTagType() != i) {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.createCrashReport(s, 9, classcastexception));
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public void removeTag(String s) {
        this.tagMap.remove(s);
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Object object = this.tagMap.keySet();

        if (NBTTagCompound.LOGGER.isDebugEnabled()) {
            ArrayList arraylist = Lists.newArrayList(this.tagMap.keySet());

            Collections.sort(arraylist);
            object = arraylist;
        }

        String s;

        for (Iterator iterator = ((Collection) object).iterator(); iterator.hasNext(); stringbuilder.append(handleEscape(s)).append(':').append(this.tagMap.get(s))) {
            s = (String) iterator.next();
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }
        }

        return stringbuilder.append('}').toString();
    }

    public boolean hasNoTags() {
        return this.tagMap.isEmpty();
    }

    private CrashReport createCrashReport(final String s, final int i, ClassCastException classcastexception) {
        CrashReport crashreport = CrashReport.makeCrashReport(classcastexception, "Reading NBT data");
        CrashReportCategory crashreportsystemdetails = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);

        crashreportsystemdetails.addDetail("Tag type found", new ICrashReportDetail() {
            public String a() throws Exception {
                return NBTBase.NBT_TYPES[((NBTBase) NBTTagCompound.this.tagMap.get(s)).getId()];
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addDetail("Tag type expected", new ICrashReportDetail() {
            public String a() throws Exception {
                return NBTBase.NBT_TYPES[i];
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.addCrashSection("Tag name", (Object) s);
        return crashreport;
    }

    public NBTTagCompound copy() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator iterator = this.tagMap.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            nbttagcompound.setTag(s, ((NBTBase) this.tagMap.get(s)).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object object) {
        return super.equals(object) && Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound) object).tagMap.entrySet());
    }

    public int hashCode() {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static void writeEntry(String s, NBTBase nbtbase, DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(nbtbase.getId());
        if (nbtbase.getId() != 0) {
            dataoutput.writeUTF(s);
            nbtbase.write(dataoutput);
        }
    }

    private static byte readType(DataInput datainput, NBTSizeTracker nbtreadlimiter) throws IOException {
        return datainput.readByte();
    }

    private static String readKey(DataInput datainput, NBTSizeTracker nbtreadlimiter) throws IOException {
        return datainput.readUTF();
    }

    static NBTBase readNBT(byte b0, String s, DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        NBTBase nbtbase = NBTBase.createNewByType(b0);

        try {
            nbtbase.read(datainput, i, nbtreadlimiter);
            return nbtbase;
        } catch (IOException ioexception) {
            CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
            CrashReportCategory crashreportsystemdetails = crashreport.makeCategory("NBT Tag");

            crashreportsystemdetails.addCrashSection("Tag name", (Object) s);
            crashreportsystemdetails.addCrashSection("Tag type", (Object) Byte.valueOf(b0));
            throw new ReportedException(crashreport);
        }
    }

    public void merge(NBTTagCompound nbttagcompound) {
        Iterator iterator = nbttagcompound.tagMap.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            NBTBase nbtbase = (NBTBase) nbttagcompound.tagMap.get(s);

            if (nbtbase.getId() == 10) {
                if (this.hasKey(s, 10)) {
                    NBTTagCompound nbttagcompound1 = this.getCompoundTag(s);

                    nbttagcompound1.merge((NBTTagCompound) nbtbase);
                } else {
                    this.setTag(s, nbtbase.copy());
                }
            } else {
                this.setTag(s, nbtbase.copy());
            }
        }

    }

    protected static String handleEscape(String s) {
        return NBTTagCompound.SIMPLE_VALUE.matcher(s).matches() ? s : NBTTagString.quoteAndEscape(s);
    }

    public NBTBase clone() {
        return this.copy();
    }
}
