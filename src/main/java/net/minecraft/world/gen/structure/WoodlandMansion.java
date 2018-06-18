package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.server.WorldGenWoodlandMansion.a;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

public class WoodlandMansion extends MapGenStructure {

    private final int featureSpacing = 80;
    private final int minFeatureSeparation = 20;
    public static final List<Biome> ALLOWED_BIOMES = Arrays.asList(new Biome[] { Biomes.ROOFED_FOREST, Biomes.MUTATED_ROOFED_FOREST});
    private final ChunkGeneratorOverworld provider;

    public WoodlandMansion(ChunkGeneratorOverworld chunkprovidergenerate) {
        this.provider = chunkprovidergenerate;
    }

    public String getStructureName() {
        return "Mansion";
    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
        int k = i;
        int l = j;

        if (i < 0) {
            k = i - 79;
        }

        if (j < 0) {
            l = j - 79;
        }

        int i1 = k / 80;
        int j1 = l / 80;
        Random random = this.world.setRandomSeed(i1, j1, 10387319);

        i1 *= 80;
        j1 *= 80;
        i1 += (random.nextInt(60) + random.nextInt(60)) / 2;
        j1 += (random.nextInt(60) + random.nextInt(60)) / 2;
        if (i == i1 && j == j1) {
            boolean flag = this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 32, WoodlandMansion.ALLOWED_BIOMES);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        this.world = world;
        BiomeProvider worldchunkmanager = world.getBiomeProvider();

        return worldchunkmanager.isFixedBiome() && worldchunkmanager.getFixedBiome() != Biomes.ROOFED_FOREST ? null : findNearestStructurePosBySpacing(world, this, blockposition, 80, 20, 10387319, true, 100, flag);
    }

    protected StructureStart getStructureStart(int i, int j) {
        return new WorldGenWoodlandMansion.a(this.world, this.provider, this.rand, i, j);
    }

    public static class a extends StructureStart {

        private boolean c;

        public a() {}

        public a(World world, ChunkGeneratorOverworld chunkprovidergenerate, Random random, int i, int j) {
            super(i, j);
            this.a(world, chunkprovidergenerate, random, i, j);
        }

        private void a(World world, ChunkGeneratorOverworld chunkprovidergenerate, Random random, int i, int j) {
            Rotation enumblockrotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            ChunkPrimer chunksnapshot = new ChunkPrimer();

            chunkprovidergenerate.setBlocksInChunk(i, j, chunksnapshot);
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

            if (k1 < 60) {
                this.c = false;
            } else {
                BlockPos blockposition = new BlockPos(i * 16 + 8, k1 + 1, j * 16 + 8);
                LinkedList linkedlist = Lists.newLinkedList();

                WoodlandMansionPieces.generateMansion(world.getSaveHandler().getStructureTemplateManager(), blockposition, enumblockrotation, linkedlist, random);
                this.components.addAll(linkedlist);
                this.updateBoundingBox();
                this.c = true;
            }
        }

        public void generateStructure(World world, Random random, StructureBoundingBox structureboundingbox) {
            super.generateStructure(world, random, structureboundingbox);
            int i = this.boundingBox.minY;

            for (int j = structureboundingbox.minX; j <= structureboundingbox.maxX; ++j) {
                for (int k = structureboundingbox.minZ; k <= structureboundingbox.maxZ; ++k) {
                    BlockPos blockposition = new BlockPos(j, i, k);

                    if (!world.isAirBlock(blockposition) && this.boundingBox.isVecInside((Vec3i) blockposition)) {
                        boolean flag = false;
                        Iterator iterator = this.components.iterator();

                        while (iterator.hasNext()) {
                            StructureComponent structurepiece = (StructureComponent) iterator.next();

                            if (structurepiece.boundingBox.isVecInside((Vec3i) blockposition)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            for (int l = i - 1; l > 1; --l) {
                                BlockPos blockposition1 = new BlockPos(j, l, k);

                                if (!world.isAirBlock(blockposition1) && !world.getBlockState(blockposition1).getMaterial().isLiquid()) {
                                    break;
                                }

                                world.setBlockState(blockposition1, Blocks.COBBLESTONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

        }

        public boolean isSizeableStructure() {
            return this.c;
        }
    }
}
