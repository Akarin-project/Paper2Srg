package org.bukkit.craftbukkit.generator;

import java.util.List;
import java.util.Random;


import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.block.CraftBlock;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.structure.MapGenStronghold;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class CustomChunkGenerator extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final WorldServer world;
    private final Random random;
    private final MapGenStronghold strongholdGen = new MapGenStronghold();

    private static class CustomBiomeGrid implements BiomeGrid {
        Biome[] biome;

        @Override
        public Biome getBiome(int x, int z) {
            return CraftBlock.biomeBaseToBiome(biome[(z << 4) | x]);
        }

        @Override
        public void setBiome(int x, int z, Biome bio) {
           biome[(z << 4) | x] = CraftBlock.biomeToBiomeBase(bio);
        }
    }

    public CustomChunkGenerator(World world, long seed, ChunkGenerator generator) {
        this.world = (WorldServer) world;
        this.generator = generator;

        this.random = new Random(seed);
    }

    @Override
    public Chunk func_185932_a(int x, int z) {
        random.setSeed((long) x * 341873128712L + (long) z * 132897987541L);

        Chunk chunk;

        // Get default biome data for chunk
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = new Biome[256];
        world.func_72959_q().func_76933_b(biomegrid.biome, x << 4, z << 4, 16, 16);

        // Try ChunkData method (1.8+)
        CraftChunkData data = (CraftChunkData) generator.generateChunkData(this.world.getWorld(), random, x, z, biomegrid);
        if (data != null) {
            char[][] sections = data.getRawChunkData();
            chunk = new Chunk(this.world, x, z);
            
            ExtendedBlockStorage[] csect = chunk.func_76587_i();
            int scnt = Math.min(csect.length, sections.length);
            
            // Loop through returned sections
            for (int sec = 0; sec < scnt; sec++) {
                if(sections[sec] == null) {
                    continue;
                }
                char[] section = sections[sec];
                char emptyTest = 0;
                for (int i = 0; i < 4096; i++) {
                    // Filter invalid block id & data values.
                    if (Block.field_176229_d.func_148745_a(section[i]) == null) {
                        section[i] = 0;
                    }
                    emptyTest |= section[i];
                }
                // Build chunk section
                if (emptyTest != 0) {
                    csect[sec] = new ExtendedBlockStorage(sec << 4, true, section, this.world.chunkPacketBlockController.getPredefinedBlockData(chunk, sec)); // Paper - Anti-Xray - Add predefined block data
                }
            }
        }
        else {
            // Try extended block method (1.2+)
            short[][] xbtypes = generator.generateExtBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
            if (xbtypes != null) {
                chunk = new Chunk(this.world, x, z);
                
                ExtendedBlockStorage[] csect = chunk.func_76587_i();
                int scnt = Math.min(csect.length, xbtypes.length);
                
                // Loop through returned sections
                for (int sec = 0; sec < scnt; sec++) {
                    if (xbtypes[sec] == null) {
                        continue;
                    }
                    char[] secBlkID = new char[4096]; // Allocate blk ID bytes
                    short[] bdata = xbtypes[sec];
                    for (int i = 0; i < bdata.length; i++) {
                        Block b = Block.func_149729_e(bdata[i]);
                        secBlkID[i] = (char) Block.field_176229_d.func_148747_b(b.func_176223_P());
                    }
                    // Build chunk section
                    csect[sec] = new ExtendedBlockStorage(sec << 4, true, secBlkID, this.world.chunkPacketBlockController.getPredefinedBlockData(chunk, sec)); // Paper - Anti-Xray - Add predefined block data
                }
            }
            else { // Else check for byte-per-block section data
                byte[][] btypes = generator.generateBlockSections(this.world.getWorld(), this.random, x, z, biomegrid);
                
                if (btypes != null) {
                    chunk = new Chunk(this.world, x, z);
                    
                    ExtendedBlockStorage[] csect = chunk.func_76587_i();
                    int scnt = Math.min(csect.length, btypes.length);
                    
                    for (int sec = 0; sec < scnt; sec++) {
                        if (btypes[sec] == null) {
                            continue;
                        }
                        
                        char[] secBlkID = new char[4096]; // Allocate block ID bytes
                        for (int i = 0; i < secBlkID.length; i++) {
                            Block b = Block.func_149729_e(btypes[sec][i] & 0xFF);
                            secBlkID[i] = (char) Block.field_176229_d.func_148747_b(b.func_176223_P());
                        }
                        csect[sec] = new ExtendedBlockStorage(sec << 4, true, secBlkID, this.world.chunkPacketBlockController.getPredefinedBlockData(chunk, sec)); // Paper - Anti-Xray - Add predefined block data
                    }
                }
                else { // Else, fall back to pre 1.2 method
                    @SuppressWarnings("deprecation")
                            byte[] types = generator.generate(this.world.getWorld(), this.random, x, z);
                    int ydim = types.length / 256;
                    int scnt = ydim / 16;
                    
                    chunk = new Chunk(this.world, x, z); // Create empty chunk
                    
                    ExtendedBlockStorage[] csect = chunk.func_76587_i();
                    
                    scnt = Math.min(scnt, csect.length);
                    // Loop through sections
                    for (int sec = 0; sec < scnt; sec++) {
                        char[] csbytes = null; // Add sections when needed
                        
                        for (int cy = 0; cy < 16; cy++) {
                            int cyoff = cy | (sec << 4);
                            
                            for (int cx = 0; cx < 16; cx++) {
                                int cxyoff = (cx * ydim * 16) + cyoff;
                                
                                for (int cz = 0; cz < 16; cz++) {
                                    byte blk = types[cxyoff + (cz * ydim)];
                                    
                                    if (blk != 0) { // If non-empty
                                        if (csbytes == null) { // If no section yet, get one
                                            csbytes = new char[16*16*16];
                                        }
                                        
                                        Block b = Block.func_149729_e(blk & 0xFF);
                                        csbytes[(cy << 8) | (cz << 4) | cx] = (char) Block.field_176229_d.func_148747_b(b.func_176223_P());
                                    }
                                }
                            }
                        }
                        // If section built, finish prepping its state
                        if (csbytes != null) {
                            ExtendedBlockStorage cs = csect[sec] = new ExtendedBlockStorage(sec << 4, true, csbytes, this.world.chunkPacketBlockController.getPredefinedBlockData(chunk, sec)); // Paper - Anti-Xray - Add predefined block data
                            cs.func_76672_e();
                        }
                    }
                }
            }
        }
        // Set biome grid
        byte[] biomeIndex = chunk.func_76605_m();
        for (int i = 0; i < biomeIndex.length; i++) {
            biomeIndex[i] = (byte) (Biome.field_185377_q.func_148757_b(biomegrid.biome[i]) & 0xFF); // PAIL : rename
        }
        // Initialize lighting
        chunk.func_76603_b();

        return chunk;
    }

    @Override
    public boolean func_185933_a(Chunk chunk, int i, int i1) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        return generator.generate(world, random, x, z);
    }

    @Override
    public byte[][] generateBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateBlockSections(world, random, x, z, biomes);
    }

    @Override
    public short[][] generateExtBlockSections(org.bukkit.World world, Random random, int x, int z, BiomeGrid biomes) {
        return generator.generateExtBlockSections(world, random, x, z, biomes);
    }

    public Chunk getChunkAt(int x, int z) {
        return func_185932_a(x, z);
    }

    @Override
    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return generator.canSpawn(world, x, z);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return generator.getDefaultPopulators(world);
    }

    @Override
    public List<Biome.SpawnListEntry> func_177458_a(EnumCreatureType type, BlockPos position) {
        Biome biomebase = world.func_180494_b(position);

        return biomebase == null ? null : biomebase.func_76747_a(type);
    }

    @Override
    public boolean func_193414_a(World world, String type, BlockPos position) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.func_175795_b(position) : false;
    }

    @Override
    public BlockPos func_180513_a(World world, String type, BlockPos position, boolean flag) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.func_180706_b(world, position, flag) : null;
    }

    @Override
    public void func_185931_b(int i, int j) {}

    @Override
    public void func_180514_a(Chunk chunk, int i, int j) {
        strongholdGen.func_186125_a(this.world, i, j, (ChunkPrimer) null);
    }
}
