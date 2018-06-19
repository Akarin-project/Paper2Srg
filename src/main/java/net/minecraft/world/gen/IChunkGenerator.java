package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator {

    Chunk func_185932_a(int i, int j);

    void func_185931_b(int i, int j);

    boolean func_185933_a(Chunk chunk, int i, int j);

    List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition);

    @Nullable
    BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag);

    void func_180514_a(Chunk chunk, int i, int j);

    boolean func_193414_a(World world, String s, BlockPos blockposition);
}
