package net.minecraft.network.datasync;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ReportedException;

public class EntityDataManager {

    private static final Logger field_190303_a = LogManager.getLogger();
    private static final Map<Class<? extends Entity>, Integer> field_187232_a = Maps.newHashMap();
    private final Entity field_187233_b;
    private final Map<Integer, EntityDataManager.DataEntry<?>> field_187234_c = new Int2ObjectOpenHashMap<>(); // Paper
    private final ReadWriteLock field_187235_d = new ReentrantReadWriteLock();
    private boolean field_187236_e = true;
    private boolean field_187237_f;

    public EntityDataManager(Entity entity) {
        this.field_187233_b = entity;
    }

    public static <T> DataParameter<T> func_187226_a(Class<? extends Entity> oclass, DataSerializer<T> datawatcherserializer) {
        if (EntityDataManager.field_190303_a.isDebugEnabled()) {
            try {
                Class oclass1 = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());

                if (!oclass1.equals(oclass)) {
                    EntityDataManager.field_190303_a.debug("defineId called for: {} from {}", oclass, oclass1, new RuntimeException());
                }
            } catch (ClassNotFoundException classnotfoundexception) {
                ;
            }
        }

        int i;

        if (EntityDataManager.field_187232_a.containsKey(oclass)) {
            i = ((Integer) EntityDataManager.field_187232_a.get(oclass)).intValue() + 1;
        } else {
            int j = 0;
            Class oclass2 = oclass;

            while (oclass2 != Entity.class) {
                oclass2 = oclass2.getSuperclass();
                if (EntityDataManager.field_187232_a.containsKey(oclass2)) {
                    j = ((Integer) EntityDataManager.field_187232_a.get(oclass2)).intValue() + 1;
                    break;
                }
            }

            i = j;
        }

        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        } else {
            EntityDataManager.field_187232_a.put(oclass, Integer.valueOf(i));
            return datawatcherserializer.func_187161_a(i);
        }
    }

    public <T> void func_187214_a(DataParameter<T> datawatcherobject, Object t0) { // CraftBukkit T -> Object
        int i = datawatcherobject.func_187155_a();

        if (i > 254) {
            throw new IllegalArgumentException("Data value id is too big with " + i + "! (Max is " + 254 + ")");
        } else if (this.field_187234_c.containsKey(Integer.valueOf(i))) {
            throw new IllegalArgumentException("Duplicate id value for " + i + "!");
        } else if (DataSerializers.func_187188_b(datawatcherobject.func_187156_b()) < 0) {
            throw new IllegalArgumentException("Unregistered serializer " + datawatcherobject.func_187156_b() + " for " + i + "!");
        } else {
            this.func_187222_c(datawatcherobject, t0);
        }
    }

    private <T> void func_187222_c(DataParameter<T> datawatcherobject, Object t0) { // CraftBukkit Object
        EntityDataManager.DataEntry datawatcher_item = new EntityDataManager.DataEntry(datawatcherobject, t0);

        this.field_187235_d.writeLock().lock();
        this.field_187234_c.put(Integer.valueOf(datawatcherobject.func_187155_a()), datawatcher_item);
        this.field_187236_e = false;
        this.field_187235_d.writeLock().unlock();
    }

    private <T> EntityDataManager.DataEntry<T> func_187219_c(DataParameter<T> datawatcherobject) {
        this.field_187235_d.readLock().lock();

        EntityDataManager.DataEntry datawatcher_item;

        try {
            datawatcher_item = (EntityDataManager.DataEntry) this.field_187234_c.get(Integer.valueOf(datawatcherobject.func_187155_a()));
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Getting synched entity data");
            CrashReportCategory crashreportsystemdetails = crashreport.func_85058_a("Synched entity data");

            crashreportsystemdetails.func_71507_a("Data ID", (Object) datawatcherobject);
            throw new ReportedException(crashreport);
        }

        this.field_187235_d.readLock().unlock();
        return datawatcher_item;
    }

    public <T> T func_187225_a(DataParameter<T> datawatcherobject) {
        return this.func_187219_c(datawatcherobject).func_187206_b();
    }

    public <T> void func_187227_b(DataParameter<T> datawatcherobject, T t0) {
        EntityDataManager.DataEntry datawatcher_item = this.func_187219_c(datawatcherobject);

        if (ObjectUtils.notEqual(t0, datawatcher_item.func_187206_b())) {
            datawatcher_item.func_187210_a(t0);
            this.field_187233_b.func_184206_a(datawatcherobject);
            datawatcher_item.func_187208_a(true);
            this.field_187237_f = true;
        }

    }

    public <T> void func_187217_b(DataParameter<T> datawatcherobject) {
        this.func_187219_c(datawatcherobject).field_187213_c = true;
        this.field_187237_f = true;
    }

    public boolean func_187223_a() {
        return this.field_187237_f;
    }

    public static void func_187229_a(List<EntityDataManager.DataEntry<?>> list, PacketBuffer packetdataserializer) throws IOException {
        if (list != null) {
            int i = 0;

            for (int j = list.size(); i < j; ++i) {
                EntityDataManager.DataEntry datawatcher_item = (EntityDataManager.DataEntry) list.get(i);

                func_187220_a(packetdataserializer, datawatcher_item);
            }
        }

        packetdataserializer.writeByte(255);
    }

    @Nullable
    public List<EntityDataManager.DataEntry<?>> func_187221_b() {
        ArrayList arraylist = null;

        if (this.field_187237_f) {
            this.field_187235_d.readLock().lock();
            Iterator iterator = this.field_187234_c.values().iterator();

            while (iterator.hasNext()) {
                EntityDataManager.DataEntry datawatcher_item = (EntityDataManager.DataEntry) iterator.next();

                if (datawatcher_item.func_187209_c()) {
                    datawatcher_item.func_187208_a(false);
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(datawatcher_item.func_192735_d());
                }
            }

            this.field_187235_d.readLock().unlock();
        }

        this.field_187237_f = false;
        return arraylist;
    }

    public void func_187216_a(PacketBuffer packetdataserializer) throws IOException {
        this.field_187235_d.readLock().lock();
        Iterator iterator = this.field_187234_c.values().iterator();

        while (iterator.hasNext()) {
            EntityDataManager.DataEntry datawatcher_item = (EntityDataManager.DataEntry) iterator.next();

            func_187220_a(packetdataserializer, datawatcher_item);
        }

        this.field_187235_d.readLock().unlock();
        packetdataserializer.writeByte(255);
    }

    @Nullable
    public List<EntityDataManager.DataEntry<?>> func_187231_c() {
        ArrayList arraylist = null;

        this.field_187235_d.readLock().lock();

        EntityDataManager.DataEntry datawatcher_item;

        for (Iterator iterator = this.field_187234_c.values().iterator(); iterator.hasNext(); arraylist.add(datawatcher_item.func_192735_d())) {
            datawatcher_item = (EntityDataManager.DataEntry) iterator.next();
            if (arraylist == null) {
                arraylist = Lists.newArrayList();
            }
        }

        this.field_187235_d.readLock().unlock();
        return arraylist;
    }

    private static <T> void func_187220_a(PacketBuffer packetdataserializer, EntityDataManager.DataEntry<T> datawatcher_item) throws IOException {
        DataParameter datawatcherobject = datawatcher_item.func_187205_a();
        int i = DataSerializers.func_187188_b(datawatcherobject.func_187156_b());

        if (i < 0) {
            throw new EncoderException("Unknown serializer type " + datawatcherobject.func_187156_b());
        } else {
            packetdataserializer.writeByte(datawatcherobject.func_187155_a());
            packetdataserializer.func_150787_b(i);
            datawatcherobject.func_187156_b().func_187160_a(packetdataserializer, datawatcher_item.func_187206_b());
        }
    }

    @Nullable
    public static List<EntityDataManager.DataEntry<?>> func_187215_b(PacketBuffer packetdataserializer) throws IOException {
        ArrayList arraylist = null;

        short short0;

        while ((short0 = packetdataserializer.readUnsignedByte()) != 255) {
            if (arraylist == null) {
                arraylist = Lists.newArrayList();
            }

            int i = packetdataserializer.func_150792_a();
            DataSerializer datawatcherserializer = DataSerializers.func_187190_a(i);

            if (datawatcherserializer == null) {
                throw new DecoderException("Unknown serializer type " + i);
            }

            arraylist.add(new EntityDataManager.DataEntry(datawatcherserializer.func_187161_a(short0), datawatcherserializer.func_187159_a(packetdataserializer)));
        }

        return arraylist;
    }

    public boolean func_187228_d() {
        return this.field_187236_e;
    }

    public void func_187230_e() {
        this.field_187237_f = false;
        this.field_187235_d.readLock().lock();
        Iterator iterator = this.field_187234_c.values().iterator();

        while (iterator.hasNext()) {
            EntityDataManager.DataEntry datawatcher_item = (EntityDataManager.DataEntry) iterator.next();

            datawatcher_item.func_187208_a(false);
        }

        this.field_187235_d.readLock().unlock();
    }

    public static class DataEntry<T> {

        private final DataParameter<T> field_187211_a;
        private T field_187212_b;
        private boolean field_187213_c;

        public DataEntry(DataParameter<T> datawatcherobject, T t0) {
            this.field_187211_a = datawatcherobject;
            this.field_187212_b = t0;
            this.field_187213_c = true;
        }

        public DataParameter<T> func_187205_a() {
            return this.field_187211_a;
        }

        public void func_187210_a(T t0) {
            this.field_187212_b = t0;
        }

        public T func_187206_b() {
            return this.field_187212_b;
        }

        public boolean func_187209_c() {
            return this.field_187213_c;
        }

        public void func_187208_a(boolean flag) {
            this.field_187213_c = flag;
        }

        public EntityDataManager.DataEntry<T> func_192735_d() {
            return new EntityDataManager.DataEntry(this.field_187211_a, this.field_187211_a.func_187156_b().func_192717_a(this.field_187212_b));
        }
    }
}
