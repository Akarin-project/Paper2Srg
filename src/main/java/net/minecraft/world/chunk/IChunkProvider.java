package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider {

    @Nullable
    Chunk getLoadedChunk(int i, int j);

    Chunk provideChunk(int i, int j);

    boolean tick();

    String makeString();

    boolean isChunkGeneratedAt(int i, int j);
}
