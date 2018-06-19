package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final IChunkGenerator generator;

    public NormalChunkGenerator(World world, long seed) {
        generator = world.field_73011_w.func_186060_c();
    }

    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().field_73011_w.func_76566_a(x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public Chunk func_185932_a(int i, int i1) {
        return generator.func_185932_a(i, i1);
    }

    @Override
    public void func_185931_b(int i, int i1) {
        generator.func_185931_b(i, i1);
    }

    @Override
    public boolean func_185933_a(Chunk chunk, int i, int i1) {
        return generator.func_185933_a(chunk, i, i1);
    }

    @Override
    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumCreatureType, BlockPos blockPosition) {
        return generator.func_177458_a(enumCreatureType, blockPosition);
    }

    @Override
    public BlockPos func_180513_a(World world, String s, BlockPos blockPosition, boolean flag) {
        return generator.func_180513_a(world, s, blockPosition, flag);
    }

    @Override
    public void func_180514_a(Chunk chunk, int i, int i1) {
        generator.func_180514_a(chunk, i, i1);
    }

    @Override
    public boolean func_193414_a(World world, String string, BlockPos bp) {
        return generator.func_193414_a(world, string, bp);
    }
}
