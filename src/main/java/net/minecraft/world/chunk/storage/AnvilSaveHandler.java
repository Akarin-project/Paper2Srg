package net.minecraft.world.chunk.storage;

import java.io.File;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraft.world.storage.WorldInfo;

public class AnvilSaveHandler extends SaveHandler {

    public AnvilSaveHandler(File file, String s, boolean flag, DataFixer dataconvertermanager) {
        super(file, s, flag, dataconvertermanager);
    }

    public IChunkLoader getChunkLoader(WorldProvider worldprovider) {
        File file = this.getWorldDirectory();
        File file1;

        if (worldprovider instanceof WorldProviderHell) {
            file1 = new File(file, "DIM-1");
            file1.mkdirs();
            return new AnvilChunkLoader(file1, this.dataFixer);
        } else if (worldprovider instanceof WorldProviderEnd) {
            file1 = new File(file, "DIM1");
            file1.mkdirs();
            return new AnvilChunkLoader(file1, this.dataFixer);
        } else {
            return new AnvilChunkLoader(file, this.dataFixer);
        }
    }

    public void saveWorldInfoWithPlayer(WorldInfo worlddata, @Nullable NBTTagCompound nbttagcompound) {
        worlddata.setSaveVersion(19133);
        super.saveWorldInfoWithPlayer(worlddata, nbttagcompound);
    }

    public void flush() {
        try {
            ThreadedFileIOBase.getThreadedIOInstance().waitForFinish();
        } catch (InterruptedException interruptedexception) {
            interruptedexception.printStackTrace();
        }

        RegionFileCache.clearRegionFileReferences();
    }
}
