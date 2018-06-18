package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class MapGenStronghold extends MapGenStructure {

    private final List<Biome> allowedBiomes;
    private boolean ranBiomeCheck;
    private ChunkPos[] structureCoords;
    private double distance;
    private int spread;

    public MapGenStronghold() {
        this.structureCoords = new ChunkPos[128];
        this.distance = 32.0D;
        this.spread = 3;
        this.allowedBiomes = Lists.newArrayList();
        Iterator iterator = Biome.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Biome biomebase = (Biome) iterator.next();

            if (biomebase != null && biomebase.getBaseHeight() > 0.0F) {
                this.allowedBiomes.add(biomebase);
            }
        }

    }

    public MapGenStronghold(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("distance")) {
                this.distance = MathHelper.getDouble((String) entry.getValue(), this.distance, 1.0D);
            } else if (((String) entry.getKey()).equals("count")) {
                this.structureCoords = new ChunkPos[MathHelper.getInt((String) entry.getValue(), this.structureCoords.length, 1)];
            } else if (((String) entry.getKey()).equals("spread")) {
                this.spread = MathHelper.getInt((String) entry.getValue(), this.spread, 1);
            }
        }

    }

    public String getStructureName() {
        return "Stronghold";
    }

    public BlockPos getNearestStructurePos(World world, BlockPos blockposition, boolean flag) {
        this.world = world; // Paper
        if (!this.ranBiomeCheck) {
            this.generatePositions();
            this.ranBiomeCheck = true;
        }

        BlockPos blockposition1 = null;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(0, 0, 0);
        double d0 = Double.MAX_VALUE;
        ChunkPos[] achunkcoordintpair = this.structureCoords;
        int i = achunkcoordintpair.length;

        for (int j = 0; j < i; ++j) {
            ChunkPos chunkcoordintpair = achunkcoordintpair[j];

            blockposition_mutableblockposition.setPos((chunkcoordintpair.x << 4) + 8, 32, (chunkcoordintpair.z << 4) + 8);
            double d1 = blockposition_mutableblockposition.distanceSq(blockposition);

            if (blockposition1 == null) {
                blockposition1 = new BlockPos(blockposition_mutableblockposition);
                d0 = d1;
            } else if (d1 < d0) {
                blockposition1 = new BlockPos(blockposition_mutableblockposition);
                d0 = d1;
            }
        }

        return blockposition1;
    }

    protected boolean canSpawnStructureAtCoords(int i, int j) {
        if (!this.ranBiomeCheck) {
            this.generatePositions();
            this.ranBiomeCheck = true;
        }

        ChunkPos[] achunkcoordintpair = this.structureCoords;
        int k = achunkcoordintpair.length;

        for (int l = 0; l < k; ++l) {
            ChunkPos chunkcoordintpair = achunkcoordintpair[l];

            if (i == chunkcoordintpair.x && j == chunkcoordintpair.z) {
                return true;
            }
        }

        return false;
    }

    private void generatePositions() {
        this.initializeStructureData(this.world);
        int i = 0;
        ObjectIterator objectiterator = this.structureMap.values().iterator();

        while (objectiterator.hasNext()) {
            StructureStart structurestart = (StructureStart) objectiterator.next();

            if (i < this.structureCoords.length) {
                this.structureCoords[i++] = new ChunkPos(structurestart.getChunkPosX(), structurestart.getChunkPosZ());
            }
        }

        Random random = new Random();

        random.setSeed(this.world.getSeed());
        double d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
        int j = 0;
        int k = 0;
        int l = this.structureMap.size();

        if (l < this.structureCoords.length) {
            for (int i1 = 0; i1 < this.structureCoords.length; ++i1) {
                double d1 = 4.0D * this.distance + this.distance * (double) j * 6.0D + (random.nextDouble() - 0.5D) * this.distance * 2.5D;
                int j1 = (int) Math.round(Math.cos(d0) * d1);
                int k1 = (int) Math.round(Math.sin(d0) * d1);
                BlockPos blockposition = this.world.getBiomeProvider().findBiomePosition((j1 << 4) + 8, (k1 << 4) + 8, 112, this.allowedBiomes, random);

                if (blockposition != null) {
                    j1 = blockposition.getX() >> 4;
                    k1 = blockposition.getZ() >> 4;
                }

                if (i1 >= l) {
                    this.structureCoords[i1] = new ChunkPos(j1, k1);
                }

                d0 += 6.283185307179586D / (double) this.spread;
                ++k;
                if (k == this.spread) {
                    ++j;
                    k = 0;
                    this.spread += 2 * this.spread / (j + 1);
                    this.spread = Math.min(this.spread, this.structureCoords.length - i1);
                    d0 += random.nextDouble() * 3.141592653589793D * 2.0D;
                }
            }
        }

    }

    protected StructureStart getStructureStart(int i, int j) {
        MapGenStronghold.Start worldgenstronghold_worldgenstronghold2start;

        for (worldgenstronghold_worldgenstronghold2start = new MapGenStronghold.Start(this.world, this.rand, i, j); worldgenstronghold_worldgenstronghold2start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2) worldgenstronghold_worldgenstronghold2start.getComponents().get(0)).strongholdPortalRoom == null; worldgenstronghold_worldgenstronghold2start = new MapGenStronghold.Start(this.world, this.rand, i, j)) {
            ;
        }

        return worldgenstronghold_worldgenstronghold2start;
    }

    public static class Start extends StructureStart {

        public Start() {}

        public Start(World world, Random random, int i, int j) {
            super(i, j);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart = new StructureStrongholdPieces.Stairs2(0, random, (i << 4) + 2, (j << 4) + 2);

            this.components.add(worldgenstrongholdpieces_worldgenstrongholdstart);
            worldgenstrongholdpieces_worldgenstrongholdstart.buildComponent((StructureComponent) worldgenstrongholdpieces_worldgenstrongholdstart, this.components, random);
            List list = worldgenstrongholdpieces_worldgenstrongholdstart.pendingChildren;

            while (!list.isEmpty()) {
                int k = random.nextInt(list.size());
                StructureComponent structurepiece = (StructureComponent) list.remove(k);

                structurepiece.buildComponent((StructureComponent) worldgenstrongholdpieces_worldgenstrongholdstart, this.components, random);
            }

            this.updateBoundingBox();
            this.markAvailableHeight(world, random, 10);
        }
    }
}
