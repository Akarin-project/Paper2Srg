package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Biomes;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatOld;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveConverter extends SaveFormatOld {

    private static final Logger field_151480_b = LogManager.getLogger();

    public AnvilSaveConverter(File file, DataFixer dataconvertermanager) {
        super(file, dataconvertermanager);
    }

    protected int func_75812_c() {
        return 19133;
    }

    public ISaveHandler func_75804_a(String s, boolean flag) {
        return new AnvilSaveHandler(this.field_75808_a, s, flag, this.field_186354_b);
    }

    public boolean func_75801_b(String s) {
        WorldInfo worlddata = this.func_75803_c(s);

        return worlddata != null && worlddata.func_76088_k() != this.func_75812_c();
    }

    public boolean func_75805_a(String s, IProgressUpdate iprogressupdate) {
        iprogressupdate.func_73718_a(0);
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        ArrayList arraylist2 = Lists.newArrayList();
        File file = new File(this.field_75808_a, s);
        File file1 = new File(file, "DIM-1");
        File file2 = new File(file, "DIM1");

        AnvilSaveConverter.field_151480_b.info("Scanning folders...");
        this.func_75810_a(file, (Collection) arraylist);
        if (file1.exists()) {
            this.func_75810_a(file1, (Collection) arraylist1);
        }

        if (file2.exists()) {
            this.func_75810_a(file2, (Collection) arraylist2);
        }

        int i = arraylist.size() + arraylist1.size() + arraylist2.size();

        AnvilSaveConverter.field_151480_b.info("Total conversion count is {}", Integer.valueOf(i));
        WorldInfo worlddata = this.func_75803_c(s);
        Object object;

        if (worlddata != null && worlddata.func_76067_t() == WorldType.field_77138_c) {
            object = new BiomeProviderSingle(Biomes.field_76772_c);
        } else {
            object = new BiomeProvider(worlddata);
        }

        this.func_75813_a(new File(file, "region"), (Iterable) arraylist, (BiomeProvider) object, 0, i, iprogressupdate);
        this.func_75813_a(new File(file1, "region"), (Iterable) arraylist1, new BiomeProviderSingle(Biomes.field_76778_j), arraylist.size(), i, iprogressupdate);
        this.func_75813_a(new File(file2, "region"), (Iterable) arraylist2, new BiomeProviderSingle(Biomes.field_76779_k), arraylist.size() + arraylist1.size(), i, iprogressupdate);
        worlddata.func_76078_e(19133);
        if (worlddata.func_76067_t() == WorldType.field_77136_e) {
            worlddata.func_76085_a(WorldType.field_77137_b);
        }

        this.func_75809_f(s);
        ISaveHandler idatamanager = this.func_75804_a(s, false);

        idatamanager.func_75761_a(worlddata);
        return true;
    }

    private void func_75809_f(String s) {
        File file = new File(this.field_75808_a, s);

        if (!file.exists()) {
            AnvilSaveConverter.field_151480_b.warn("Unable to create level.dat_mcr backup");
        } else {
            File file1 = new File(file, "level.dat");

            if (!file1.exists()) {
                AnvilSaveConverter.field_151480_b.warn("Unable to create level.dat_mcr backup");
            } else {
                File file2 = new File(file, "level.dat_mcr");

                if (!file1.renameTo(file2)) {
                    AnvilSaveConverter.field_151480_b.warn("Unable to create level.dat_mcr backup");
                }

            }
        }
    }

    private void func_75813_a(File file, Iterable<File> iterable, BiomeProvider worldchunkmanager, int i, int j, IProgressUpdate iprogressupdate) {
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            File file1 = (File) iterator.next();

            this.func_75811_a(file, file1, worldchunkmanager, i, j, iprogressupdate);
            ++i;
            int k = (int) Math.round(100.0D * (double) i / (double) j);

            iprogressupdate.func_73718_a(k);
        }

    }

    private void func_75811_a(File file, File file1, BiomeProvider worldchunkmanager, int i, int j, IProgressUpdate iprogressupdate) {
        try {
            String s = file1.getName();
            RegionFile regionfile = new RegionFile(file1);
            RegionFile regionfile1 = new RegionFile(new File(file, s.substring(0, s.length() - ".mcr".length()) + ".mca"));

            for (int k = 0; k < 32; ++k) {
                int l;

                for (l = 0; l < 32; ++l) {
                    if (regionfile.func_76709_c(k, l) && !regionfile1.func_76709_c(k, l)) {
                        DataInputStream datainputstream = regionfile.func_76704_a(k, l);

                        if (datainputstream == null) {
                            AnvilSaveConverter.field_151480_b.warn("Failed to fetch input stream");
                        } else {
                            NBTTagCompound nbttagcompound = CompressedStreamTools.func_74794_a(datainputstream);

                            datainputstream.close();
                            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Level");
                            ChunkLoader.AnvilConverterData oldchunkloader_oldchunk = ChunkLoader.func_76691_a(nbttagcompound1);
                            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                            NBTTagCompound nbttagcompound3 = new NBTTagCompound();

                            nbttagcompound2.func_74782_a("Level", nbttagcompound3);
                            ChunkLoader.func_76690_a(oldchunkloader_oldchunk, nbttagcompound3, worldchunkmanager);
                            DataOutputStream dataoutputstream = regionfile1.func_76710_b(k, l);

                            CompressedStreamTools.func_74800_a(nbttagcompound2, (DataOutput) dataoutputstream);
                            dataoutputstream.close();
                        }
                    }
                }

                l = (int) Math.round(100.0D * (double) (i * 1024) / (double) (j * 1024));
                int i1 = (int) Math.round(100.0D * (double) ((k + 1) * 32 + i * 1024) / (double) (j * 1024));

                if (i1 > l) {
                    iprogressupdate.func_73718_a(i1);
                }
            }

            regionfile.func_76708_c();
            regionfile1.func_76708_c();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    private void func_75810_a(File file, Collection<File> collection) {
        File file1 = new File(file, "region");
        File[] afile = file1.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.endsWith(".mcr");
            }
        });

        if (afile != null) {
            Collections.addAll(collection, afile);
        }

    }
}
