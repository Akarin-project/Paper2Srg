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

    private static final Logger LOGGER = LogManager.getLogger();

    public AnvilSaveConverter(File file, DataFixer dataconvertermanager) {
        super(file, dataconvertermanager);
    }

    protected int getSaveVersion() {
        return 19133;
    }

    public ISaveHandler getSaveLoader(String s, boolean flag) {
        return new AnvilSaveHandler(this.savesDirectory, s, flag, this.dataFixer);
    }

    public boolean isOldMapFormat(String s) {
        WorldInfo worlddata = this.getWorldInfo(s);

        return worlddata != null && worlddata.getSaveVersion() != this.getSaveVersion();
    }

    public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate) {
        iprogressupdate.setLoadingProgress(0);
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList();
        ArrayList arraylist2 = Lists.newArrayList();
        File file = new File(this.savesDirectory, s);
        File file1 = new File(file, "DIM-1");
        File file2 = new File(file, "DIM1");

        AnvilSaveConverter.LOGGER.info("Scanning folders...");
        this.addRegionFilesToCollection(file, (Collection) arraylist);
        if (file1.exists()) {
            this.addRegionFilesToCollection(file1, (Collection) arraylist1);
        }

        if (file2.exists()) {
            this.addRegionFilesToCollection(file2, (Collection) arraylist2);
        }

        int i = arraylist.size() + arraylist1.size() + arraylist2.size();

        AnvilSaveConverter.LOGGER.info("Total conversion count is {}", Integer.valueOf(i));
        WorldInfo worlddata = this.getWorldInfo(s);
        Object object;

        if (worlddata != null && worlddata.getTerrainType() == WorldType.FLAT) {
            object = new BiomeProviderSingle(Biomes.PLAINS);
        } else {
            object = new BiomeProvider(worlddata);
        }

        this.convertFile(new File(file, "region"), (Iterable) arraylist, (BiomeProvider) object, 0, i, iprogressupdate);
        this.convertFile(new File(file1, "region"), (Iterable) arraylist1, new BiomeProviderSingle(Biomes.HELL), arraylist.size(), i, iprogressupdate);
        this.convertFile(new File(file2, "region"), (Iterable) arraylist2, new BiomeProviderSingle(Biomes.SKY), arraylist.size() + arraylist1.size(), i, iprogressupdate);
        worlddata.setSaveVersion(19133);
        if (worlddata.getTerrainType() == WorldType.DEFAULT_1_1) {
            worlddata.setTerrainType(WorldType.DEFAULT);
        }

        this.createFile(s);
        ISaveHandler idatamanager = this.getSaveLoader(s, false);

        idatamanager.saveWorldInfo(worlddata);
        return true;
    }

    private void createFile(String s) {
        File file = new File(this.savesDirectory, s);

        if (!file.exists()) {
            AnvilSaveConverter.LOGGER.warn("Unable to create level.dat_mcr backup");
        } else {
            File file1 = new File(file, "level.dat");

            if (!file1.exists()) {
                AnvilSaveConverter.LOGGER.warn("Unable to create level.dat_mcr backup");
            } else {
                File file2 = new File(file, "level.dat_mcr");

                if (!file1.renameTo(file2)) {
                    AnvilSaveConverter.LOGGER.warn("Unable to create level.dat_mcr backup");
                }

            }
        }
    }

    private void convertFile(File file, Iterable<File> iterable, BiomeProvider worldchunkmanager, int i, int j, IProgressUpdate iprogressupdate) {
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            File file1 = (File) iterator.next();

            this.convertChunks(file, file1, worldchunkmanager, i, j, iprogressupdate);
            ++i;
            int k = (int) Math.round(100.0D * (double) i / (double) j);

            iprogressupdate.setLoadingProgress(k);
        }

    }

    private void convertChunks(File file, File file1, BiomeProvider worldchunkmanager, int i, int j, IProgressUpdate iprogressupdate) {
        try {
            String s = file1.getName();
            RegionFile regionfile = new RegionFile(file1);
            RegionFile regionfile1 = new RegionFile(new File(file, s.substring(0, s.length() - ".mcr".length()) + ".mca"));

            for (int k = 0; k < 32; ++k) {
                int l;

                for (l = 0; l < 32; ++l) {
                    if (regionfile.isChunkSaved(k, l) && !regionfile1.isChunkSaved(k, l)) {
                        DataInputStream datainputstream = regionfile.getChunkDataInputStream(k, l);

                        if (datainputstream == null) {
                            AnvilSaveConverter.LOGGER.warn("Failed to fetch input stream");
                        } else {
                            NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);

                            datainputstream.close();
                            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Level");
                            ChunkLoader.AnvilConverterData oldchunkloader_oldchunk = ChunkLoader.load(nbttagcompound1);
                            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                            NBTTagCompound nbttagcompound3 = new NBTTagCompound();

                            nbttagcompound2.setTag("Level", nbttagcompound3);
                            ChunkLoader.convertToAnvilFormat(oldchunkloader_oldchunk, nbttagcompound3, worldchunkmanager);
                            DataOutputStream dataoutputstream = regionfile1.getChunkDataOutputStream(k, l);

                            CompressedStreamTools.write(nbttagcompound2, (DataOutput) dataoutputstream);
                            dataoutputstream.close();
                        }
                    }
                }

                l = (int) Math.round(100.0D * (double) (i * 1024) / (double) (j * 1024));
                int i1 = (int) Math.round(100.0D * (double) ((k + 1) * 32 + i * 1024) / (double) (j * 1024));

                if (i1 > l) {
                    iprogressupdate.setLoadingProgress(i1);
                }
            }

            regionfile.close();
            regionfile1.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    private void addRegionFilesToCollection(File file, Collection<File> collection) {
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
