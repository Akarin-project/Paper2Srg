package net.minecraft.world.gen.structure;

import java.util.Random;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;

public class MapGenEndCity extends MapGenStructure {

    private final int citySpacing = 20;
    private final int minCitySeparation = 11;
    private final ChunkGeneratorEnd endProvider;

    public MapGenEndCity(ChunkGeneratorEnd chunkprovidertheend) {
        this.endProvider = chunkprovidertheend;
    }

    public String getStructureName() {
        return "EndCity";
    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
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
        Random random = this.world.setRandomSeed(i1, j1, 10387313);

        i1 *= 20;
        j1 *= 20;
        i1 += (random.nextInt(9) + random.nextInt(9)) / 2;
        j1 += (random.nextInt(9) + random.nextInt(9)) / 2;
        if (k == i1 && l == j1 && this.endProvider.isIslandChunk(k, l)) {
            int k1 = getYPosForStructure(k, l, this.endProvider);

            return k1 >= 60;
        } else {
            return false;
        }
    }

    protected StructureStart getStructureStart(int i, int j) {
        return new MapGenEndCity.Start(this.world, this.endProvider, this.rand, i, j);
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        this.world = world;
        return findNearestStructurePosBySpacing(world, this, blockposition, 20, 11, 10387313, true, 100, flag);
    }

    private static int getYPosForStructure(int i, int j, ChunkGeneratorEnd chunkprovidertheend) {
        Random random = new Random((long) (i + j * 10387313));
        Rotation enumblockrotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        chunkprovidertheend.setBlocksInChunk(i, j, chunksnapshot);
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

        int k = chunksnapshot.findGroundBlockIdx(7, 7);
        int l = chunksnapshot.findGroundBlockIdx(7, 7 + b1);
        int i1 = chunksnapshot.findGroundBlockIdx(7 + b0, 7);
        int j1 = chunksnapshot.findGroundBlockIdx(7 + b0, 7 + b1);
        int k1 = Math.min(Math.min(k, l), Math.min(i1, j1));

        return k1;
    }

    public static class Start extends StructureStart {

        private boolean isSizeable;

        public Start() {}

        public Start(World world, ChunkGeneratorEnd chunkprovidertheend, Random random, int i, int j) {
            super(i, j);
            this.create(world, chunkprovidertheend, random, i, j);
        }

        private void create(World world, ChunkGeneratorEnd chunkprovidertheend, Random random, int i, int j) {
            Random random1 = new Random((long) (i + j * 10387313));
            Rotation enumblockrotation = Rotation.values()[random1.nextInt(Rotation.values().length)];
            int k = MapGenEndCity.getYPosForStructure(i, j, chunkprovidertheend);

            if (k < 60) {
                this.isSizeable = false;
            } else {
                BlockPos blockposition = new BlockPos(i * 16 + 8, k, j * 16 + 8);

                StructureEndCityPieces.startHouseTower(world.getSaveHandler().getStructureTemplateManager(), blockposition, enumblockrotation, this.components, random);
                this.updateBoundingBox();
                this.isSizeable = true;
            }
        }

        public boolean isSizeableStructure() {
            return this.isSizeable;
        }
    }
}
