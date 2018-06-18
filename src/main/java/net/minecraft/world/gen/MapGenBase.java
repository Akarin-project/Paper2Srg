package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenBase {

    protected int range = 8;
    protected Random rand = new Random();
    protected World world;

    public MapGenBase() {}

    public void generate(World world, int i, int j, ChunkPrimer chunksnapshot) {
        int k = this.range;

        this.world = world;
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = i - k; j1 <= i + k; ++j1) {
            for (int k1 = j - k; k1 <= j + k; ++k1) {
                long l1 = (long) j1 * l;
                long i2 = (long) k1 * i1;

                this.rand.setSeed(l1 ^ i2 ^ world.getSeed());
                this.recursiveGenerate(world, j1, k1, i, j, chunksnapshot);
            }
        }

    }

    public static void setupChunkSeed(long i, Random random, int j, int k) {
        random.setSeed(i);
        long l = random.nextLong();
        long i1 = random.nextLong();
        long j1 = (long) j * l;
        long k1 = (long) k * i1;

        random.setSeed(j1 ^ k1 ^ i);
    }

    protected void recursiveGenerate(World world, int i, int j, int k, int l, ChunkPrimer chunksnapshot) {}
}
