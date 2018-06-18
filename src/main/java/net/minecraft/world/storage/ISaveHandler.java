package net.minecraft.world.storage;

import java.io.File;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;

public interface ISaveHandler {

    @Nullable
    WorldInfo loadWorldInfo();

    void checkSessionLock() throws MinecraftException;

    IChunkLoader getChunkLoader(WorldProvider worldprovider);

    void saveWorldInfoWithPlayer(WorldInfo worlddata, NBTTagCompound nbttagcompound);

    void saveWorldInfo(WorldInfo worlddata);

    IPlayerFileData getPlayerNBTManager();

    void flush();

    File getWorldDirectory();

    File getMapFileFromName(String s);

    TemplateManager getStructureTemplateManager();

    java.util.UUID getUUID(); // CraftBukkit
}
