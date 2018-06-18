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

    public static final Map<File, RegionFile> REGIONS_BY_FILE = new LinkedHashMap(PaperConfig.regionFileCacheSize, 0.75f, true); // Spigot - private -> public, Paper - HashMap -> LinkedHashMap

    public static synchronized RegionFile createOrLoadRegionFile(File file, int i, int j) {
        File file1 = new File(file, "region");
        File file2 = new File(file1, "r." + (i >> 5) + "." + (j >> 5) + ".mca");
        RegionFile regionfile = (RegionFile) RegionFileCache.REGIONS_BY_FILE.get(file2);

        if (regionfile != null) {
            return regionfile;
        } else {
            if (!file1.exists()) {
                file1.mkdirs();
            }

            if (RegionFileCache.REGIONS_BY_FILE.size() >= PaperConfig.regionFileCacheSize) { // Paper
                trimCache(); // Paper
            }

            RegionFile regionfile1 = new RegionFile(file2);

            RegionFileCache.REGIONS_BY_FILE.put(file2, regionfile1);
            return regionfile1;
        }
    }

    public static synchronized RegionFile getRegionFileIfExists(File file, int i, int j) {
        File file1 = new File(file, "region");
        File file2 = new File(file1, "r." + (i >> 5) + "." + (j >> 5) + ".mca");
        RegionFile regionfile = (RegionFile) RegionFileCache.REGIONS_BY_FILE.get(file2);

        if (regionfile != null) {
            return regionfile;
        } else if (file1.exists() && file2.exists()) {
            if (RegionFileCache.REGIONS_BY_FILE.size() >= 256) {
                clearRegionFileReferences();
            }

            RegionFile regionfile1 = new RegionFile(file2);

            RegionFileCache.REGIONS_BY_FILE.put(file2, regionfile1);
            return regionfile1;
        } else {
            return null;
        }
    }

    // Paper Start
    private static synchronized void trimCache() {
        Iterator<Map.Entry<File, RegionFile>> itr = RegionFileCache.REGIONS_BY_FILE.entrySet().iterator();
        int count = RegionFileCache.REGIONS_BY_FILE.size() - PaperConfig.regionFileCacheSize;
        while (count-- >= 0 && itr.hasNext()) {
            try {
                itr.next().getValue().close();
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                ServerInternalException.reportInternalException(ioexception);
            }
            itr.remove();
        }
    }
    // Paper End

    public static synchronized void clearRegionFileReferences() {
        Iterator iterator = RegionFileCache.REGIONS_BY_FILE.values().iterator();

        while (iterator.hasNext()) {
            RegionFile regionfile = (RegionFile) iterator.next();

            try {
                if (regionfile != null) {
                    regionfile.close();
                }
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                ServerInternalException.reportInternalException(ioexception); // Paper
            }
        }

        RegionFileCache.REGIONS_BY_FILE.clear();
    }

    // CraftBukkit start - call sites hoisted for synchronization
    public static synchronized NBTTagCompound d(File file, int i, int j) throws IOException {
        RegionFile regionfile = createOrLoadRegionFile(file, i, j);

        DataInputStream datainputstream = regionfile.getChunkDataInputStream(i & 31, j & 31);

        if (datainputstream == null) {
            return null;
        }

        return CompressedStreamTools.read(datainputstream);
    }

    public static synchronized void e(File file, int i, int j, NBTTagCompound nbttagcompound) throws IOException {
        RegionFile regionfile = createOrLoadRegionFile(file, i, j);

        DataOutputStream dataoutputstream = regionfile.getChunkDataOutputStream(i & 31, j & 31);
        CompressedStreamTools.write(nbttagcompound, (java.io.DataOutput) dataoutputstream);
        dataoutputstream.close();
    }
    // CraftBukkit end

    public static synchronized boolean chunkExists(File file, int i, int j) {
        RegionFile regionfile = getRegionFileIfExists(file, i, j);

        return regionfile != null ? regionfile.isChunkSaved(i & 31, j & 31) : false;
    }
}
