package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenBase {

    protected int field_75040_a = 8;
    protected Random field_75038_b = new Random();
    protected World field_75039_c;

    public MapGenBase() {}

    public void func_186125_a(World world, int i, int j, ChunkPrimer chunksnapshot) {
        int k = this.field_75040_a;

        this.field_75039_c = world;
        this.field_75038_b.setSeed(world.func_72905_C());
        long l = this.field_75038_b.nextLong();
        long i1 = this.field_75038_b.nextLong();

        for (int j1 = i - k; j1 <= i + k; ++j1) {
            for (int k1 = j - k; k1 <= j + k; ++k1) {
                long l1 = (long) j1 * l;
                long i2 = (long) k1 * i1;

                this.field_75038_b.setSeed(l1 ^ i2 ^ world.func_72905_C());
                this.func_180701_a(world, j1, k1, i, j, chunksnapshot);
            }
        }

    }

    public static void func_191068_a(long i, Random random, int j, int k) {
        random.setSeed(i);
        long l = random.nextLong();
        long i1 = random.nextLong();
        long j1 = (long) j * l;
        long k1 = (long) k * i1;

        random.setSeed(j1 ^ k1 ^ i);
    }

    protected void func_180701_a(World world, int i, int j, int k, int l, ChunkPrimer chunksnapshot) {}
}
