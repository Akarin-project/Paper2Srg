package net.minecraft.world.chunk.storage;

import com.destroystokyo.paper.exception.ServerInternalException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.destroystokyo.paper.PaperConfig;
import java.util.LinkedHashMap;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class RegionFileCache {

    public static final Map<File, RegionFile> field_76553_a = new LinkedHashMap(PaperConfig.regionFileCacheSize, 0.75f, true); // Spigot - private -> public, Paper - HashMap -> LinkedHashMap

    public static synchronized RegionFile func_76550_a(File file, int i, int j) {
        File file1 = new File(file, "region");
        File file2 = new File(file1, "r." + (i >> 5) + "." + (j >> 5) + ".mca");
        RegionFile regionfile = (RegionFile) RegionFileCache.field_76553_a.get(file2);

        if (regionfile != null) {
            return regionfile;
        } else {
            if (!file1.exists()) {
                file1.mkdirs();
            }

            if (RegionFileCache.field_76553_a.size() >= PaperConfig.regionFileCacheSize) { // Paper
                trimCache(); // Paper
            }

            RegionFile regionfile1 = new RegionFile(file2);

            RegionFileCache.field_76553_a.put(file2, regionfile1);
            return regionfile1;
        }
    }

    public static synchronized RegionFile func_191065_b(File file, int i, int j) {
        File file1 = new File(file, "region");
        File file2 = new File(file1, "r." + (i >> 5) + "." + (j >> 5) + ".mca");
        RegionFile regionfile = (RegionFile) RegionFileCache.field_76553_a.get(file2);

        if (regionfile != null) {
            return regionfile;
        } else if (file1.exists() && file2.exists()) {
            if (RegionFileCache.field_76553_a.size() >= 256) {
                func_76551_a();
            }

            RegionFile regionfile1 = new RegionFile(file2);

            RegionFileCache.field_76553_a.put(file2, regionfile1);
            return regionfile1;
        } else {
            return null;
        }
    }

    // Paper Start
    private static synchronized void trimCache() {
        Iterator<Map.Entry<File, RegionFile>> itr = RegionFileCache.field_76553_a.entrySet().iterator();
        int count = RegionFileCache.field_76553_a.size() - PaperConfig.regionFileCacheSize;
        while (count-- >= 0 && itr.hasNext()) {
            try {
                itr.next().getValue().func_76708_c();
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                ServerInternalException.reportInternalException(ioexception);
            }
            itr.remove();
        }
    }
    // Paper End

    public static synchronized void func_76551_a() {
        Iterator iterator = RegionFileCache.field_76553_a.values().iterator();

        while (iterator.hasNext()) {
            RegionFile regionfile = (RegionFile) iterator.next();

            try {
                if (regionfile != null) {
                    regionfile.func_76708_c();
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                ServerInternalException.reportInternalException(ioexception); // Paper
            }
        }

        RegionFileCache.field_76553_a.clear();
    }

    // CraftBukkit start - call sites hoisted for synchronization
    public static synchronized NBTTagCompound d(File file, int i, int j) throws IOException {
        RegionFile regionfile = func_76550_a(file, i, j);

        DataInputStream datainputstream = regionfile.func_76704_a(i & 31, j & 31);

        if (datainputstream == null) {
            return null;
        }

        return CompressedStreamTools.func_74794_a(datainputstream);
    }

    public static synchronized void e(File file, int i, int j, NBTTagCompound nbttagcompound) throws IOException {
        RegionFile regionfile = func_76550_a(file, i, j);

        DataOutputStream dataoutputstream = regionfile.func_76710_b(i & 31, j & 31);
        CompressedStreamTools.func_74800_a(nbttagcompound, (java.io.DataOutput) dataoutputstream);
        dataoutputstream.close();
    }
    // CraftBukkit end

    public static synchronized boolean func_191064_f(File file, int i, int j) {
        RegionFile regionfile = func_191065_b(file, i, j);

        return regionfile != null ? regionfile.func_76709_c(i & 31, j & 31) : false;
    }
}
