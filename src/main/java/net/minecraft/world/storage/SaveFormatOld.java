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

    private static final Logger field_151479_b = LogManager.getLogger();
    protected final File field_75808_a;
    protected final DataFixer field_186354_b;

    public SaveFormatOld(File file, DataFixer dataconvertermanager) {
        this.field_186354_b = dataconvertermanager;
        if (!file.exists()) {
            file.mkdirs();
        }

        this.field_75808_a = file;
    }

    @Nullable
    public WorldInfo func_75803_c(String s) {
        File file = new File(this.field_75808_a, s);

        if (!file.exists()) {
            return null;
        } else {
            File file1 = new File(file, "level.dat");

            if (file1.exists()) {
                WorldInfo worlddata = func_186353_a(file1, this.field_186354_b);

                if (worlddata != null) {
                    return worlddata;
                }
            }

            file1 = new File(file, "level.dat_old");
            return file1.exists() ? func_186353_a(file1, this.field_186354_b) : null;
        }
    }

    @Nullable
    public static WorldInfo func_186353_a(File file, DataFixer dataconvertermanager) {
        try {
            NBTTagCompound nbttagcompound = CompressedStreamTools.func_74796_a((InputStream) (new FileInputStream(file)));
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Data");

            return new WorldInfo(dataconvertermanager.func_188257_a((IFixType) FixTypes.LEVEL, nbttagcompound1));
        } catch (Exception exception) {
            SaveFormatOld.field_151479_b.error("Exception reading {}", file, exception);
            return null;
        }
    }

    public ISaveHandler func_75804_a(String s, boolean flag) {
        return new SaveHandler(this.field_75808_a, s, flag, this.field_186354_b);
    }

    public boolean func_75801_b(String s) {
        return false;
    }

    public boolean func_75805_a(String s, IProgressUpdate iprogressupdate) {
        return false;
    }

    public File func_186352_b(String s, String s1) {
        return new File(new File(this.field_75808_a, s), s1);
    }
}
