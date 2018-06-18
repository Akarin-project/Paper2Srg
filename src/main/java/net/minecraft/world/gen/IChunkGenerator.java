package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator {

    Chunk generateChunk(int i, int j);

    void populate(int i, int j);

    boolean generateStructures(Chunk chunk, int i, int j);

    List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition);

    @Nullable
    BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag);

    void recreateStructures(Chunk chunk, int i, int j);

    boolean isInsideStructure(World world, String s, BlockPos blockposition);
}
