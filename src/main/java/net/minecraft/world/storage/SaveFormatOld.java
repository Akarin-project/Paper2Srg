package net.minecraft.world.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixType;

public class SaveFormatOld implements ISaveFormat {

    private static final Logger LOGGER = LogManager.getLogger();
    protected final File savesDirectory;
    protected final DataFixer dataFixer;

    public SaveFormatOld(File file, DataFixer dataconvertermanager) {
        this.dataFixer = dataconvertermanager;
        if (!file.exists()) {
            file.mkdirs();
        }

        this.savesDirectory = file;
    }

    @Nullable
    public WorldInfo getWorldInfo(String s) {
        File file = new File(this.savesDirectory, s);

        if (!file.exists()) {
            return null;
        } else {
            File file1 = new File(file, "level.dat");

            if (file1.exists()) {
                WorldInfo worlddata = getWorldData(file1, this.dataFixer);

                if (worlddata != null) {
                    return worlddata;
                }
            }

            file1 = new File(file, "level.dat_old");
            return file1.exists() ? getWorldData(file1, this.dataFixer) : null;
        }
    }

    @Nullable
    public static WorldInfo getWorldData(File file, DataFixer dataconvertermanager) {
        try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed((InputStream) (new FileInputStream(file)));
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Data");

            return new WorldInfo(dataconvertermanager.process((IFixType) FixTypes.LEVEL, nbttagcompound1));
        } catch (Exception exception) {
            SaveFormatOld.LOGGER.error("Exception reading {}", file, exception);
            return null;
        }
    }

    public ISaveHandler getSaveLoader(String s, boolean flag) {
        return new SaveHandler(this.savesDirectory, s, flag, this.dataFixer);
    }

    public boolean isOldMapFormat(String s) {
        return false;
    }

    public boolean convertMapFormat(String s, IProgressUpdate iprogressupdate) {
        return false;
    }

    public File getFile(String s, String s1) {
        return new File(new File(this.savesDirectory, s), s1);
    }
}
