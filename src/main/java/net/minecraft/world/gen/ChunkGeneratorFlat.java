package net.minecraft.world.gen;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkGeneratorFlat implements IChunkGenerator {

    private final World world;
    private final Random random;
    private final IBlockState[] cachedBlockIDs = new IBlockState[256];
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final Map<String, MapGenStructure> structureGenerators = new HashMap();
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;

    public ChunkGeneratorFlat(World world, long i, boolean flag, String s) {
        this.world = world;
        this.random = new Random(i);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(s);
        if (flag) {
            Map map = this.flatWorldGenInfo.getWorldFeatures();

            if (map.containsKey("village") && world.paperConfig.generateVillage) { // Paper
                Map map1 = (Map) map.get("village");

                if (!map1.containsKey("size")) {
                    map1.put("size", "1");
                }

                this.structureGenerators.put("Village", new MapGenVillage(map1));
            }

            if (map.containsKey("biome_1") && world.paperConfig.generateTemple) { // Paper
                this.structureGenerators.put("Temple", new MapGenScatteredFeature((Map) map.get("biome_1")));
            }

            if (map.containsKey("mineshaft") && world.paperConfig.generateMineshaft) { // Paper
                this.structureGenerators.put("Mineshaft", new MapGenMineshaft((Map) map.get("mineshaft")));
            }

            if (map.containsKey("stronghold") && world.paperConfig.generateStronghold) { // Paper
                this.structureGenerators.put("Stronghold", new MapGenStronghold((Map) map.get("stronghold")));
            }

            if (map.containsKey("oceanmonument") && world.paperConfig.generateMonument) { // Paper
                this.structureGenerators.put("Monument", new StructureOceanMonument((Map) map.get("oceanmonument")));
            }
        }

        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake")) {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.WATER);
        }

        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake")) {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.LAVA);
        }

        this.hasDungeons = world.paperConfig.generateDungeon && this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");  // Paper
        int j = 0;
        int k = 0;
        boolean flag1 = true;
        Iterator iterator = this.flatWorldGenInfo.getFlatLayers().iterator();

        while (iterator.hasNext()) {
            FlatLayerInfo worldgenflatlayerinfo = (FlatLayerInfo) iterator.next();

            for (int l = worldgenflatlayerinfo.getMinY(); l < worldgenflatlayerinfo.getMinY() + worldgenflatlayerinfo.getLayerCount(); ++l) {
                IBlockState iblockdata = worldgenflatlayerinfo.getLayerMaterial();

                if (iblockdata.getBlock() != Blocks.AIR) {
                    flag1 = false;
                    this.cachedBlockIDs[l] = iblockdata;
                }
            }

            if (worldgenflatlayerinfo.getLayerMaterial().getBlock() == Blocks.AIR) {
                k += worldgenflatlayerinfo.getLayerCount();
            } else {
                j += worldgenflatlayerinfo.getLayerCount() + k;
                k = 0;
            }
        }

        world.setSeaLevel(j);
        this.hasDecoration = flag1 && this.flatWorldGenInfo.getBiome() != Biome.getIdForBiome(Biomes.VOID) ? false : this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");
    }

    public Chunk generateChunk(int i, int j) {
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        int k;

        for (int l = 0; l < this.cachedBlockIDs.length; ++l) {
            IBlockState iblockdata = this.cachedBlockIDs[l];

            if (iblockdata != null) {
                for (int i1 = 0; i1 < 16; ++i1) {
                    for (k = 0; k < 16; ++k) {
                        chunksnapshot.setBlockState(i1, l, k, iblockdata);
                    }
                }
            }
        }

        Iterator iterator = this.structureGenerators.values().iterator();

        while (iterator.hasNext()) {
            MapGenBase worldgenbase = (MapGenBase) iterator.next();

            worldgenbase.generate(this.world, i, j, chunksnapshot);
        }

        Chunk chunk = new Chunk(this.world, chunksnapshot, i, j);
        Biome[] abiomebase = this.world.getBiomeProvider().getBiomes((Biome[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.getIdForBiome(abiomebase[k]);
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(int i, int j) {
        int k = i * 16;
        int l = j * 16;
        BlockPos blockposition = new BlockPos(k, 0, l);
        Biome biomebase = this.world.getBiome(new BlockPos(k + 16, 0, l + 16));
        boolean flag = false;

        this.random.setSeed(this.world.getSeed());
        long i1 = this.random.nextLong() / 2L * 2L + 1L;
        long j1 = this.random.nextLong() / 2L * 2L + 1L;

        this.random.setSeed((long) i * i1 + (long) j * j1 ^ this.world.getSeed());
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);
        Iterator iterator = this.structureGenerators.values().iterator();

        while (iterator.hasNext()) {
            MapGenStructure structuregenerator = (MapGenStructure) iterator.next();
            boolean flag1 = structuregenerator.generateStructure(this.world, this.random, chunkcoordintpair);

            if (structuregenerator instanceof MapGenVillage) {
                flag |= flag1;
            }
        }

        if (this.waterLakeGenerator != null && !flag && this.random.nextInt(4) == 0) {
            this.waterLakeGenerator.generate(this.world, this.random, blockposition.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
        }

        if (this.lavaLakeGenerator != null && !flag && this.random.nextInt(8) == 0) {
            BlockPos blockposition1 = blockposition.add(this.random.nextInt(16) + 8, this.random.nextInt(this.random.nextInt(248) + 8), this.random.nextInt(16) + 8);

            if (blockposition1.getY() < this.world.getSeaLevel() || this.random.nextInt(10) == 0) {
                this.lavaLakeGenerator.generate(this.world, this.random, blockposition1);
            }
        }

        if (this.hasDungeons) {
            for (int k1 = 0; k1 < 8; ++k1) {
                (new WorldGenDungeons()).generate(this.world, this.random, blockposition.add(this.random.nextInt(16) + 8, this.random.nextInt(256), this.random.nextInt(16) + 8));
            }
        }

        if (this.hasDecoration) {
            biomebase.decorate(this.world, this.random, blockposition);
        }

    }

    public boolean generateStructures(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        Biome biomebase = this.world.getBiome(blockposition);

        return biomebase.getSpawnableList(enumcreaturetype);
    }

    @Nullable
    public BlockPos getNearestStructurePos(World world, String s, BlockPos blockposition, boolean flag) {
        MapGenStructure structuregenerator = (MapGenStructure) this.structureGenerators.get(s);

        return structuregenerator != null ? structuregenerator.getNearestStructurePos(world, blockposition, flag) : null;
    }

    public boolean isInsideStructure(World world, String s, BlockPos blockposition) {
        MapGenStructure structuregenerator = (MapGenStructure) this.structureGenerators.get(s);

        return structuregenerator != null ? structuregenerator.isInsideStructure(blockposition) : false;
    }

    public void recreateStructures(Chunk chunk, int i, int j) {
        Iterator iterator = this.structureGenerators.values().iterator();

        while (iterator.hasNext()) {
            MapGenStructure structuregenerator = (MapGenStructure) iterator.next();

            structuregenerator.generate(this.world, i, j, (ChunkPrimer) null);
        }

    }
}
