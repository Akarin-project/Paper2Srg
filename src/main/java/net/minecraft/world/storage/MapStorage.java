package net.minecraft.world.storage;

import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagShort;

public class MapStorage {

    private final ISaveHandler field_75751_a;
    protected Map<String, WorldSavedData> field_75749_b = Maps.newHashMap();
    public final List<WorldSavedData> field_75750_c = Lists.newArrayList(); // Spigot
    private final Map<String, Short> field_75748_d = Maps.newHashMap();

    public MapStorage(ISaveHandler idatamanager) {
        this.field_75751_a = idatamanager;
        this.func_75746_b();
    }

    @Nullable
    public WorldSavedData func_75742_a(Class<? extends WorldSavedData> oclass, String s) {
        WorldSavedData persistentbase = (WorldSavedData) this.field_75749_b.get(s);

        if (persistentbase != null) {
            return persistentbase;
        } else {
            if (this.field_75751_a != null) {
                try {
                    File file = this.field_75751_a.func_75758_b(s);

                    if (file != null && file.exists()) {
                        try {
                            persistentbase = (WorldSavedData) oclass.getConstructor(new Class[] { String.class}).newInstance(new Object[] { s});
                        } catch (Exception exception) {
                            throw new RuntimeException("Failed to instantiate " + oclass, exception);
                        }

                        FileInputStream fileinputstream = new FileInputStream(file);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.func_74796_a((InputStream) fileinputstream);

                        fileinputstream.close();
                        persistentbase.func_76184_a(nbttagcompound.func_74775_l("data"));
                    }
                } catch (Exception exception1) {
                    exception1.printStackTrace();
                    ServerInternalException.reportInternalException(exception1); // Paper
                }
            }

            if (persistentbase != null) {
                this.field_75749_b.put(s, persistentbase);
                this.field_75750_c.add(persistentbase);
            }

            return persistentbase;
        }
    }

    public void func_75745_a(String s, WorldSavedData persistentbase) {
        if (this.field_75749_b.containsKey(s)) {
            this.field_75750_c.remove(this.field_75749_b.remove(s));
        }

        this.field_75749_b.put(s, persistentbase);
        this.field_75750_c.add(persistentbase);
    }

    public void func_75744_a() {
        for (int i = 0; i < this.field_75750_c.size(); ++i) {
            WorldSavedData persistentbase = (WorldSavedData) this.field_75750_c.get(i);

            if (persistentbase.func_76188_b()) {
                this.func_75747_a(persistentbase);
                persistentbase.func_76186_a(false);
            }
        }

    }

    private void func_75747_a(WorldSavedData persistentbase) {
        if (this.field_75751_a != null) {
            try {
                File file = this.field_75751_a.func_75758_b(persistentbase.field_76190_i);

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                    nbttagcompound.func_74782_a("data", persistentbase.func_189551_b(new NBTTagCompound()));
                    FileOutputStream fileoutputstream = new FileOutputStream(file);

                    CompressedStreamTools.func_74799_a(nbttagcompound, (OutputStream) fileoutputstream);
                    fileoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                ServerInternalException.reportInternalException(exception); // Paper
            }

        }
    }

    private void func_75746_b() {
        try {
            this.field_75748_d.clear();
            if (this.field_75751_a == null) {
                return;
            }

            File file = this.field_75751_a.func_75758_b("idcounts");

            if (file != null && file.exists()) {
                DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
                NBTTagCompound nbttagcompound = CompressedStreamTools.func_74794_a(datainputstream);

                datainputstream.close();
                Iterator iterator = nbttagcompound.func_150296_c().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    NBTBase nbtbase = nbttagcompound.func_74781_a(s);

                    if (nbtbase instanceof NBTTagShort) {
                        NBTTagShort nbttagshort = (NBTTagShort) nbtbase;
                        short short0 = nbttagshort.func_150289_e();

                        this.field_75748_d.put(s, Short.valueOf(short0));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public int func_75743_a(String s) {
        Short oshort = (Short) this.field_75748_d.get(s);

        if (oshort == null) {
            oshort = Short.valueOf((short) 0);
        } else {
            oshort = Short.valueOf((short) (oshort.shortValue() + 1));
        }

        this.field_75748_d.put(s, oshort);
        if (this.field_75751_a == null) {
            return oshort.shortValue();
        } else {
            try {
                File file = this.field_75751_a.func_75758_b("idcounts");

                if (file != null) {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    Iterator iterator = this.field_75748_d.keySet().iterator();

                    while (iterator.hasNext()) {
                        String s1 = (String) iterator.next();

                        nbttagcompound.func_74777_a(s1, ((Short) this.field_75748_d.get(s1)).shortValue());
                    }

                    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));

                    CompressedStreamTools.func_74800_a(nbttagcompound, (DataOutput) dataoutputstream);
                    dataoutputstream.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return oshort.shortValue();
        }
    }
}
