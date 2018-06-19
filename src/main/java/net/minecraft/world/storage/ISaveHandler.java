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
    WorldInfo func_75757_d();

    void func_75762_c() throws MinecraftException;

    IChunkLoader func_75763_a(WorldProvider worldprovider);

    void func_75755_a(WorldInfo worlddata, NBTTagCompound nbttagcompound);

    void func_75761_a(WorldInfo worlddata);

    IPlayerFileData func_75756_e();

    void func_75759_a();

    File func_75765_b();

    File func_75758_b(String s);

    TemplateManager func_186340_h();

    java.util.UUID getUUID(); // CraftBukkit
}
