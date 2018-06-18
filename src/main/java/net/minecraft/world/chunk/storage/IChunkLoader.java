package net.minecraft.world.chunk.storage;

import java.io.IOException;
import javax.annotation.Nullable;

import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface IChunkLoader {

    @Nullable
    Chunk loadChunk(World world, int i, int j) throws IOException;

    void saveChunk(World world, Chunk chunk, boolean unloaded) throws IOException, MinecraftException; // Spigot

    // void b(World world, Chunk chunk) throws IOException; // Spigot

    void chunkTick();

    void flush();

    boolean isChunkGeneratedAt(int i, int j);
}
