package net.minecraft.world.chunk.storage;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkLoader {

    @Nullable
    Chunk func_75815_a(World world, int i, int j) throws IOException;

    void saveChunk(World world, Chunk chunk, boolean unloaded) throws IOException, MinecraftException; // Spigot

    // void b(World world, Chunk chunk) throws IOException; // Spigot

    void func_75817_a();

    void func_75818_b();

    boolean func_191063_a(int i, int j);
}
