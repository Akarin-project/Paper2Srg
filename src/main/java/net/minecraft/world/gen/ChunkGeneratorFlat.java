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

    private final World field_73163_a;
    private final Random field_73161_b;
    private final IBlockState[] field_82700_c = new IBlockState[256];
    private final FlatGeneratorInfo field_82699_e;
    private final Map<String, MapGenStructure> field_82696_f = new HashMap();
    private final boolean field_82697_g;
    private final boolean field_82702_h;
    private WorldGenLakes field_82703_i;
    private WorldGenLakes field_82701_j;

    public ChunkGeneratorFlat(World world, long i, boolean flag, String s) {
        this.field_73163_a = world;
        this.field_73161_b = new Random(i);
        this.field_82699_e = FlatGeneratorInfo.func_82651_a(s);
        if (flag) {
            Map map = this.field_82699_e.func_82644_b();

            if (map.containsKey("village") && world.paperConfig.generateVillage) { // Paper
                Map map1 = (Map) map.get("village");

                if (!map1.containsKey("size")) {
                    map1.put("size", "1");
                }

                this.field_82696_f.put("Village", new MapGenVillage(map1));
            }

            if (map.containsKey("biome_1") && world.paperConfig.generateTemple) { // Paper
                this.field_82696_f.put("Temple", new MapGenScatteredFeature((Map) map.get("biome_1")));
            }

            if (map.containsKey("mineshaft") && world.paperConfig.generateMineshaft) { // Paper
                this.field_82696_f.put("Mineshaft", new MapGenMineshaft((Map) map.get("mineshaft")));
            }

            if (map.containsKey("stronghold") && world.paperConfig.generateStronghold) { // Paper
                this.field_82696_f.put("Stronghold", new MapGenStronghold((Map) map.get("stronghold")));
            }

            if (map.containsKey("oceanmonument") && world.paperConfig.generateMonument) { // Paper
                this.field_82696_f.put("Monument", new StructureOceanMonument((Map) map.get("oceanmonument")));
            }
        }

        if (this.field_82699_e.func_82644_b().containsKey("lake")) {
            this.field_82703_i = new WorldGenLakes(Blocks.field_150355_j);
        }

        if (this.field_82699_e.func_82644_b().containsKey("lava_lake")) {
            this.field_82701_j = new WorldGenLakes(Blocks.field_150353_l);
        }

        this.field_82702_h = world.paperConfig.generateDungeon && this.field_82699_e.func_82644_b().containsKey("dungeon");  // Paper
        int j = 0;
        int k = 0;
        boolean flag1 = true;
        Iterator iterator = this.field_82699_e.func_82650_c().iterator();

        while (iterator.hasNext()) {
            FlatLayerInfo worldgenflatlayerinfo = (FlatLayerInfo) iterator.next();

            for (int l = worldgenflatlayerinfo.func_82656_d(); l < worldgenflatlayerinfo.func_82656_d() + worldgenflatlayerinfo.func_82657_a(); ++l) {
                IBlockState iblockdata = worldgenflatlayerinfo.func_175900_c();

                if (iblockdata.func_177230_c() != Blocks.field_150350_a) {
                    flag1 = false;
                    this.field_82700_c[l] = iblockdata;
                }
            }

            if (worldgenflatlayerinfo.func_175900_c().func_177230_c() == Blocks.field_150350_a) {
                k += worldgenflatlayerinfo.func_82657_a();
            } else {
                j += worldgenflatlayerinfo.func_82657_a() + k;
                k = 0;
            }
        }

        world.func_181544_b(j);
        this.field_82697_g = flag1 && this.field_82699_e.func_82648_a() != Biome.func_185362_a(Biomes.field_185440_P) ? false : this.field_82699_e.func_82644_b().containsKey("decoration");
    }

    public Chunk func_185932_a(int i, int j) {
        ChunkPrimer chunksnapshot = new ChunkPrimer();

        int k;

        for (int l = 0; l < this.field_82700_c.length; ++l) {
            IBlockState iblockdata = this.field_82700_c[l];

            if (iblockdata != null) {
                for (int i1 = 0; i1 < 16; ++i1) {
                    for (k = 0; k < 16; ++k) {
                        chunksnapshot.func_177855_a(i1, l, k, iblockdata);
                    }
                }
            }
        }

        Iterator iterator = this.field_82696_f.values().iterator();

        while (iterator.hasNext()) {
            MapGenBase worldgenbase = (MapGenBase) iterator.next();

            worldgenbase.func_186125_a(this.field_73163_a, i, j, chunksnapshot);
        }

        Chunk chunk = new Chunk(this.field_73163_a, chunksnapshot, i, j);
        Biome[] abiomebase = this.field_73163_a.func_72959_q().func_76933_b((Biome[]) null, i * 16, j * 16, 16, 16);
        byte[] abyte = chunk.func_76605_m();

        for (k = 0; k < abyte.length; ++k) {
            abyte[k] = (byte) Biome.func_185362_a(abiomebase[k]);
        }

        chunk.func_76603_b();
        return chunk;
    }

    public void func_185931_b(int i, int j) {
        int k = i * 16;
        int l = j * 16;
        BlockPos blockposition = new BlockPos(k, 0, l);
        Biome biomebase = this.field_73163_a.func_180494_b(new BlockPos(k + 16, 0, l + 16));
        boolean flag = false;

        this.field_73161_b.setSeed(this.field_73163_a.func_72905_C());
        long i1 = this.field_73161_b.nextLong() / 2L * 2L + 1L;
        long j1 = this.field_73161_b.nextLong() / 2L * 2L + 1L;

        this.field_73161_b.setSeed((long) i * i1 + (long) j * j1 ^ this.field_73163_a.func_72905_C());
        ChunkPos chunkcoordintpair = new ChunkPos(i, j);
        Iterator iterator = this.field_82696_f.values().iterator();

        while (iterator.hasNext()) {
            MapGenStructure structuregenerator = (MapGenStructure) iterator.next();
            boolean flag1 = structuregenerator.func_175794_a(this.field_73163_a, this.field_73161_b, chunkcoordintpair);

            if (structuregenerator instanceof MapGenVillage) {
                flag |= flag1;
            }
        }

        if (this.field_82703_i != null && !flag && this.field_73161_b.nextInt(4) == 0) {
            this.field_82703_i.func_180709_b(this.field_73163_a, this.field_73161_b, blockposition.func_177982_a(this.field_73161_b.nextInt(16) + 8, this.field_73161_b.nextInt(256), this.field_73161_b.nextInt(16) + 8));
        }

        if (this.field_82701_j != null && !flag && this.field_73161_b.nextInt(8) == 0) {
            BlockPos blockposition1 = blockposition.func_177982_a(this.field_73161_b.nextInt(16) + 8, this.field_73161_b.nextInt(this.field_73161_b.nextInt(248) + 8), this.field_73161_b.nextInt(16) + 8);

            if (blockposition1.func_177956_o() < this.field_73163_a.func_181545_F() || this.field_73161_b.nextInt(10) == 0) {
                this.field_82701_j.func_180709_b(this.field_73163_a, this.field_73161_b, blockposition1);
            }
        }

        if (this.field_82702_h) {
            for (int k1 = 0; k1 < 8; ++k1) {
                (new WorldGenDungeons()).func_180709_b(this.field_73163_a, this.field_73161_b, blockposition.func_177982_a(this.field_73161_b.nextInt(16) + 8, this.field_73161_b.nextInt(256), this.field_73161_b.nextInt(16) + 8));
            }
        }

        if (this.field_82697_g) {
            biomebase.func_180624_a(this.field_73163_a, this.field_73161_b, blockposition);
        }

    }

    public boolean func_185933_a(Chunk chunk, int i, int j) {
        return false;
    }

    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType enumcreaturetype, BlockPos blockposition) {
        Biome biomebase = this.field_73163_a.func_180494_b(blockposition);

        return biomebase.func_76747_a(enumcreaturetype);
    }

    @Nullable
    public BlockPos func_180513_a(World world, String s, BlockPos blockposition, boolean flag) {
        MapGenStructure structuregenerator = (MapGenStructure) this.field_82696_f.get(s);

        return structuregenerator != null ? structuregenerator.func_180706_b(world, blockposition, flag) : null;
    }

    public boolean func_193414_a(World world, String s, BlockPos blockposition) {
        MapGenStructure structuregenerator = (MapGenStructure) this.field_82696_f.get(s);

        return structuregenerator != null ? structuregenerator.func_175795_b(blockposition) : false;
    }

    public void func_180514_a(Chunk chunk, int i, int j) {
        Iterator iterator = this.field_82696_f.values().iterator();

        while (iterator.hasNext()) {
            MapGenStructure structuregenerator = (MapGenStructure) iterator.next();

            structuregenerator.func_186125_a(this.field_73163_a, i, j, (ChunkPrimer) null);
        }

    }
}
