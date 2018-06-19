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

    private static final Logger field_191551_b = LogManager.getLogger();
    private static final Pattern field_193583_c = Pattern.compile("[A-Za-z0-9._+-]+");
    public final Map<String, NBTBase> field_74784_a = Maps.newHashMap(); // Paper

    public NBTTagCompound() {}

    void func_74734_a(DataOutput dataoutput) throws IOException {
        Iterator iterator = this.field_74784_a.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            NBTBase nbtbase = (NBTBase) this.field_74784_a.get(s);

            func_150298_a(s, nbtbase, dataoutput);
        }

        dataoutput.writeByte(0);
    }

    void func_152446_a(DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        nbtreadlimiter.func_152450_a(384L);
        if (i > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.field_74784_a.clear();

            byte b0;

            while ((b0 = func_152447_a(datainput, nbtreadlimiter)) != 0) {
                String s = func_152448_b(datainput, nbtreadlimiter);

                nbtreadlimiter.func_152450_a((long) (224 + 16 * s.length()));
                NBTBase nbtbase = func_152449_a(b0, s, datainput, i + 1, nbtreadlimiter);

                if (this.field_74784_a.put(s, nbtbase) != null) {
                    nbtreadlimiter.func_152450_a(288L);
                }
            }

        }
    }

    public Set<String> func_150296_c() {
        return this.field_74784_a.keySet();
    }

    public byte func_74732_a() {
        return (byte) 10;
    }

    public int func_186856_d() {
        return this.field_74784_a.size();
    }

    public void func_74782_a(String s, NBTBase nbtbase) {
        this.field_74784_a.put(s, nbtbase);
    }

    public void func_74774_a(String s, byte b0) {
        this.field_74784_a.put(s, new NBTTagByte(b0));
    }

    public void func_74777_a(String s, short short0) {
        this.field_74784_a.put(s, new NBTTagShort(short0));
    }

    public void func_74768_a(String s, int i) {
        this.field_74784_a.put(s, new NBTTagInt(i));
    }

    public void func_74772_a(String s, long i) {
        this.field_74784_a.put(s, new NBTTagLong(i));
    }

    public void setUUID(String prefix, UUID uuid) { func_186854_a(prefix, uuid); } // Paper - OBFHELPER
    public void func_186854_a(String s, UUID uuid) {
        this.func_74772_a(s + "Most", uuid.getMostSignificantBits());
        this.func_74772_a(s + "Least", uuid.getLeastSignificantBits());
    }

    public UUID getUUID(String prefix) { return func_186857_a(prefix); } // Paper - OBFHELPER
    @Nullable
    public UUID func_186857_a(String s) {
        return new UUID(this.func_74763_f(s + "Most"), this.func_74763_f(s + "Least"));
    }

    public boolean hasUUID(String s) { return func_186855_b(s); } public boolean func_186855_b(String s) { // Paper - OBFHELPER
        return this.func_150297_b(s + "Most", 99) && this.func_150297_b(s + "Least", 99);
    }

    public void func_74776_a(String s, float f) {
        this.field_74784_a.put(s, new NBTTagFloat(f));
    }

    public void func_74780_a(String s, double d0) {
        this.field_74784_a.put(s, new NBTTagDouble(d0));
    }

    public void func_74778_a(String s, String s1) {
        this.field_74784_a.put(s, new NBTTagString(s1));
    }

    public void func_74773_a(String s, byte[] abyte) {
        this.field_74784_a.put(s, new NBTTagByteArray(abyte));
    }

    public void func_74783_a(String s, int[] aint) {
        this.field_74784_a.put(s, new NBTTagIntArray(aint));
    }

    public void func_74757_a(String s, boolean flag) {
        this.func_74774_a(s, (byte) (flag ? 1 : 0));
    }

    public NBTBase func_74781_a(String s) {
        return (NBTBase) this.field_74784_a.get(s);
    }

    public byte func_150299_b(String s) {
        NBTBase nbtbase = (NBTBase) this.field_74784_a.get(s);

        return nbtbase == null ? 0 : nbtbase.func_74732_a();
    }

    public boolean func_74764_b(String s) {
        return this.field_74784_a.containsKey(s);
    }

    public boolean func_150297_b(String s, int i) {
        byte b0 = this.func_150299_b(s);

        return b0 == i ? true : (i != 99 ? false : b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6);
    }

    public byte func_74771_c(String s) {
        try {
            if (this.func_150297_b(s, 99)) {
                return ((NBTPrimitive) this.field_74784_a.get(s)).func_150290_f();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return (byte) 0;
    }

    public short func_74765_d(String s) {
        try {
            if (this.func_150297_b(s, 99)) {
                return ((NBTPrimitive) this.field_74784_a.get(s)).func_150289_e();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return (short) 0;
    }

    public int func_74762_e(String s) {
        try {
            if (this.func_150297_b(s, 99)) {
                return ((NBTPrimitive) this.field_74784_a.get(s)).func_150287_d();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0;
    }

    public long func_74763_f(String s) {
        try {
            if (this.func_150297_b(s, 99)) {
                return ((NBTPrimitive) this.field_74784_a.get(s)).func_150291_c();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0L;
    }

    public float func_74760_g(String s) {
        try {
            if (this.func_150297_b(s, 99)) {
                return ((NBTPrimitive) this.field_74784_a.get(s)).func_150288_h();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0.0F;
    }

    public double func_74769_h(String s) {
        try {
            if (this.func_150297_b(s, 99)) {
                return ((NBTPrimitive) this.field_74784_a.get(s)).func_150286_g();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return 0.0D;
    }

    public String func_74779_i(String s) {
        try {
            if (this.func_150297_b(s, 8)) {
                return ((NBTBase) this.field_74784_a.get(s)).func_150285_a_();
            }
        } catch (ClassCastException classcastexception) {
            ;
        }

        return "";
    }

    public byte[] func_74770_j(String s) {
        try {
            if (this.func_150297_b(s, 7)) {
                return ((NBTTagByteArray) this.field_74784_a.get(s)).func_150292_c();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.func_82581_a(s, 7, classcastexception));
        }

        return new byte[0];
    }

    public int[] func_74759_k(String s) {
        try {
            if (this.func_150297_b(s, 11)) {
                return ((NBTTagIntArray) this.field_74784_a.get(s)).func_150302_c();
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.func_82581_a(s, 11, classcastexception));
        }

        return new int[0];
    }

    public NBTTagCompound func_74775_l(String s) {
        try {
            if (this.func_150297_b(s, 10)) {
                return (NBTTagCompound) this.field_74784_a.get(s);
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.func_82581_a(s, 10, classcastexception));
        }

        return new NBTTagCompound();
    }

    public NBTTagList func_150295_c(String s, int i) {
        try {
            if (this.func_150299_b(s) == 9) {
                NBTTagList nbttaglist = (NBTTagList) this.field_74784_a.get(s);

                if (!nbttaglist.func_82582_d() && nbttaglist.func_150303_d() != i) {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        } catch (ClassCastException classcastexception) {
            throw new ReportedException(this.func_82581_a(s, 9, classcastexception));
        }

        return new NBTTagList();
    }

    public boolean func_74767_n(String s) {
        return this.func_74771_c(s) != 0;
    }

    public void func_82580_o(String s) {
        this.field_74784_a.remove(s);
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Object object = this.field_74784_a.keySet();

        if (NBTTagCompound.field_191551_b.isDebugEnabled()) {
            ArrayList arraylist = Lists.newArrayList(this.field_74784_a.keySet());

            Collections.sort(arraylist);
            object = arraylist;
        }

        String s;

        for (Iterator iterator = ((Collection) object).iterator(); iterator.hasNext(); stringbuilder.append(func_193582_s(s)).append(':').append(this.field_74784_a.get(s))) {
            s = (String) iterator.next();
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }
        }

        return stringbuilder.append('}').toString();
    }

    public boolean func_82582_d() {
        return this.field_74784_a.isEmpty();
    }

    private CrashReport func_82581_a(final String s, final int i, ClassCastException classcastexception) {
        CrashReport crashreport = CrashReport.func_85055_a(classcastexception, "Reading NBT data");
        CrashReportCategory crashreportsystemdetails = crashreport.func_85057_a("Corrupt NBT tag", 1);

        crashreportsystemdetails.func_189529_a("Tag type found", new ICrashReportDetail() {
            public String a() throws Exception {
                return NBTBase.field_82578_b[((NBTBase) NBTTagCompound.this.field_74784_a.get(s)).func_74732_a()];
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_189529_a("Tag type expected", new ICrashReportDetail() {
            public String a() throws Exception {
                return NBTBase.field_82578_b[i];
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        crashreportsystemdetails.func_71507_a("Tag name", (Object) s);
        return crashreport;
    }

    public NBTTagCompound func_74737_b() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator iterator = this.field_74784_a.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            nbttagcompound.func_74782_a(s, ((NBTBase) this.field_74784_a.get(s)).func_74737_b());
        }

        return nbttagcompound;
    }

    public boolean equals(Object object) {
        return super.equals(object) && Objects.equals(this.field_74784_a.entrySet(), ((NBTTagCompound) object).field_74784_a.entrySet());
    }

    public int hashCode() {
        return super.hashCode() ^ this.field_74784_a.hashCode();
    }

    private static void func_150298_a(String s, NBTBase nbtbase, DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(nbtbase.func_74732_a());
        if (nbtbase.func_74732_a() != 0) {
            dataoutput.writeUTF(s);
            nbtbase.func_74734_a(dataoutput);
        }
    }

    private static byte func_152447_a(DataInput datainput, NBTSizeTracker nbtreadlimiter) throws IOException {
        return datainput.readByte();
    }

    private static String func_152448_b(DataInput datainput, NBTSizeTracker nbtreadlimiter) throws IOException {
        return datainput.readUTF();
    }

    static NBTBase func_152449_a(byte b0, String s, DataInput datainput, int i, NBTSizeTracker nbtreadlimiter) throws IOException {
        NBTBase nbtbase = NBTBase.func_150284_a(b0);

        try {
            nbtbase.func_152446_a(datainput, i, nbtreadlimiter);
            return nbtbase;
        } catch (IOException ioexception) {
            CrashReport crashreport = CrashReport.func_85055_a(ioexception, "Loading NBT data");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("NBT Tag");

            crashreportsystemdetails.func_71507_a("Tag name", (Object) s);
            crashreportsystemdetails.func_71507_a("Tag type", (Object) Byte.valueOf(b0));
            throw new ReportedException(crashreport);
        }
    }

    public void func_179237_a(NBTTagCompound nbttagcompound) {
        Iterator iterator = nbttagcompound.field_74784_a.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            NBTBase nbtbase = (NBTBase) nbttagcompound.field_74784_a.get(s);

            if (nbtbase.func_74732_a() == 10) {
                if (this.func_150297_b(s, 10)) {
                    NBTTagCompound nbttagcompound1 = this.func_74775_l(s);

                    nbttagcompound1.func_179237_a((NBTTagCompound) nbtbase);
                } else {
                    this.func_74782_a(s, nbtbase.func_74737_b());
                }
            } else {
                this.func_74782_a(s, nbtbase.func_74737_b());
            }
        }

    }

    protected static String func_193582_s(String s) {
        return NBTTagCompound.field_193583_c.matcher(s).matches() ? s : NBTTagString.func_193588_a(s);
    }

    public NBTBase clone() {
        return this.func_74737_b();
    }
}
