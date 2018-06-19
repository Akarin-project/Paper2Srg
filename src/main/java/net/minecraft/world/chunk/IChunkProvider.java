package net.minecraft.world.chunk;

import javax.annotation.Nullable;

public interface IChunkProvider {

    @Nullable
    Chunk func_186026_b(int i, int j);

    Chunk func_186025_d(int i, int j);

    boolean func_73156_b();

    String func_73148_d();

    boolean func_191062_e(int i, int j);
}
