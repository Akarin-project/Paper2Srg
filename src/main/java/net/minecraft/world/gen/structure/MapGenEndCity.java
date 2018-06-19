package net.minecraft.world.gen.structure;

import java.util.Random;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;

public class MapGenEndCity extends MapGenStructure {

    private final int field_186131_a = 20;
    private final int field_186132_b = 11;
    private final ChunkGeneratorEnd field_186133_d;

    public MapGenEndCity(ChunkGeneratorEnd chunkprovidertheend) {
        this.field_186133_d = chunkprovidertheend;
    }

    public String func_143025_a() {
        return "EndCity";
    }

    protected boolean func_75047_a(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            i -= 19;
        }

        if (j < 0) {
            j -= 19;
        }

        int i1 = i / 20;
        int j1 = j / 20;
        Random random = this.field_75039_c.func_72843_D(i1, j1, 10387313);

        i1 *= 20;
        j1 *= 20;
        i1 += (random.nextInt(9) + random.nextInt(9)) / 2;
        j1 += (random.nextInt(9) + random.nextInt(9)) / 2;
        if (k == i1 && l == j1 && this.field_186133_d.func_185961_c(k, l)) {
            int k1 = func_191070_b(k, l, this.field_186133_d);

            return k1 >= 60;
        } else {
            return false;
        }
    }

    protected StructureStart func_75049_b(int i, int j) {
        return new MapGenEndCity.Start(this.field_75039_c, this.field_186133_d, this.field_75038_b, i, j);
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        this.field_75039_c = world;
        return func_191069_a(world, this, blockposition, 20, 11, 10387313, true, 100, flag);
    }

    private static int func_191070_b(int i, int j, ChunkGeneratorEnd chunkprovidertheend) {
        Random random = new Random((long) (i + j * 10387313));
        Rotation enumblockrotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        chunkprovidertheend.func_180518_a(i, j, chunksnapshot);
        byte b0 = 5;
        byte b1 = 5;

        if (enumblockrotation == Rotation.CLOCKWISE_90) {
            b0 = -5;
        } else if (enumblockrotation == Rotation.CLOCKWISE_180) {
            b0 = -5;
            b1 = -5;
        } else if (enumblockrotation == Rotation.COUNTERCLOCKWISE_90) {
            b1 = -5;
        }

        int k = chunksnapshot.func_186138_a(7, 7);
        int l = chunksnapshot.func_186138_a(7, 7 + b1);
        int i1 = chunksnapshot.func_186138_a(7 + b0, 7);
        int j1 = chunksnapshot.func_186138_a(7 + b0, 7 + b1);
        int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));

        return k1;
    }

    public static class Start extends StructureStart {

        private boolean field_186163_c;

        public Start() {}

        public Start(World world, ChunkGeneratorEnd chunkprovidertheend, Random random, int i, int j) {
            super(i, j);
            this.func_186162_a(world, chunkprovidertheend, random, i, j);
        }

        private void func_186162_a(World world, ChunkGeneratorEnd chunkprovidertheend, Random random, int i, int j) {
            Random random1 = new Random((long) (i + j * 10387313));
            Rotation enumblockrotation = Rotation.values()[random1.nextInt(Rotation.values().length)];
            int k = MapGenEndCity.func_191070_b(i, j, chunkprovidertheend);

            if (k < 60) {
                this.field_186163_c = false;
            } else {
                BlockPos blockposition = new BlockPos(i * 16 + 8, k, j * 16 + 8);

                StructureEndCityPieces.func_191087_a(world.func_72860_G().func_186340_h(), blockposition, enumblockrotation, this.field_75075_a, random);
                this.func_75072_c();
                this.field_186163_c = true;
            }
        }

        public boolean func_75069_d() {
            return this.field_186163_c;
        }
    }
}
