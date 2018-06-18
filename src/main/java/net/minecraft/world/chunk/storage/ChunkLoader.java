package net.minecraft.world.chunk.storage;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.NibbleArray;


public class ChunkLoader {

    public static ChunkLoader.AnvilConverterData load(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInteger("xPos");
        int j = nbttagcompound.getInteger("zPos");
        ChunkLoader.AnvilConverterData oldchunkloader_oldchunk = new ChunkLoader.AnvilConverterData(i, j);

        oldchunkloader_oldchunk.blocks = nbttagcompound.getByteArray("Blocks");
        oldchunkloader_oldchunk.data = new NibbleArrayReader(nbttagcompound.getByteArray("Data"), 7);
        oldchunkloader_oldchunk.skyLight = new NibbleArrayReader(nbttagcompound.getByteArray("SkyLight"), 7);
        oldchunkloader_oldchunk.blockLight = new NibbleArrayReader(nbttagcompound.getByteArray("BlockLight"), 7);
        oldchunkloader_oldchunk.heightmap = nbttagcompound.getByteArray("HeightMap");
        oldchunkloader_oldchunk.terrainPopulated = nbttagcompound.getBoolean("TerrainPopulated");
        oldchunkloader_oldchunk.entities = nbttagcompound.getTagList("Entities", 10);
        oldchunkloader_oldchunk.tileEntities = nbttagcompound.getTagList("TileEntities", 10);
        oldchunkloader_oldchunk.tileTicks = nbttagcompound.getTagList("TileTicks", 10);

        try {
            oldchunkloader_oldchunk.lastUpdated = nbttagcompound.getLong("LastUpdate");
        } catch (ClassCastException classcastexception) {
            oldchunkloader_oldchunk.lastUpdated = (long) nbttagcompound.getInteger("LastUpdate");
        }

        return oldchunkloader_oldchunk;
    }

    public static void convertToAnvilFormat(ChunkLoader.AnvilConverterData oldchunkloader_oldchunk, NBTTagCompound nbttagcompound, BiomeProvider worldchunkmanager) {
        nbttagcompound.setInteger("xPos", oldchunkloader_oldchunk.x);
        nbttagcompound.setInteger("zPos", oldchunkloader_oldchunk.z);
        nbttagcompound.setLong("LastUpdate", oldchunkloader_oldchunk.lastUpdated);
        int[] aint = new int[oldchunkloader_oldchunk.heightmap.length];

        for (int i = 0; i < oldchunkloader_oldchunk.heightmap.length; ++i) {
            aint[i] = oldchunkloader_oldchunk.heightmap[i];
        }

        nbttagcompound.setIntArray("HeightMap", aint);
        nbttagcompound.setBoolean("TerrainPopulated", oldchunkloader_oldchunk.terrainPopulated);
        NBTTagList nbttaglist = new NBTTagList();

        int j;
        int k;

        for (int l = 0; l < 8; ++l) {
            boolean flag = true;

            for (j = 0; j < 16 && flag; ++j) {
                k = 0;

                while (k < 16 && flag) {
                    int i1 = 0;

                    while (true) {
                        if (i1 < 16) {
                            int j1 = j << 11 | i1 << 7 | k + (l << 4);
                            byte b0 = oldchunkloader_oldchunk.blocks[j1];

                            if (b0 == 0) {
                                ++i1;
                                continue;
                            }

                            flag = false;
                        }

                        ++k;
                        break;
                    }
                }
            }

            if (!flag) {
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = new NibbleArray();
                NibbleArray nibblearray2 = new NibbleArray();

                for (int k1 = 0; k1 < 16; ++k1) {
                    for (int l1 = 0; l1 < 16; ++l1) {
                        for (int i2 = 0; i2 < 16; ++i2) {
                            int j2 = k1 << 11 | i2 << 7 | l1 + (l << 4);
                            byte b1 = oldchunkloader_oldchunk.blocks[j2];

                            abyte[l1 << 8 | i2 << 4 | k1] = (byte) (b1 & 255);
                            nibblearray.set(k1, l1, i2, oldchunkloader_oldchunk.data.get(k1, l1 + (l << 4), i2));
                            nibblearray1.set(k1, l1, i2, oldchunkloader_oldchunk.skyLight.get(k1, l1 + (l << 4), i2));
                            nibblearray2.set(k1, l1, i2, oldchunkloader_oldchunk.blockLight.get(k1, l1 + (l << 4), i2));
                        }
                    }
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setByte("Y", (byte) (l & 255));
                nbttagcompound1.setByteArray("Blocks", abyte);
                nbttagcompound1.setByteArray("Data", nibblearray.getData());
                nbttagcompound1.setByteArray("SkyLight", nibblearray1.getData());
                nbttagcompound1.setByteArray("BlockLight", nibblearray2.getData());
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Sections", nbttaglist);
        byte[] abyte1 = new byte[256];
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                blockposition_mutableblockposition.setPos(oldchunkloader_oldchunk.x << 4 | j, 0, oldchunkloader_oldchunk.z << 4 | k);
                abyte1[k << 4 | j] = (byte) (Biome.getIdForBiome(worldchunkmanager.getBiome(blockposition_mutableblockposition, Biomes.DEFAULT)) & 255);
            }
        }

        nbttagcompound.setByteArray("Biomes", abyte1);
        nbttagcompound.setTag("Entities", oldchunkloader_oldchunk.entities);
        nbttagcompound.setTag("TileEntities", oldchunkloader_oldchunk.tileEntities);
        if (oldchunkloader_oldchunk.tileTicks != null) {
            nbttagcompound.setTag("TileTicks", oldchunkloader_oldchunk.tileTicks);
        }

    }

    public static class AnvilConverterData {

        public long lastUpdated;
        public boolean terrainPopulated;
        public byte[] heightmap;
        public NibbleArrayReader blockLight;
        public NibbleArrayReader skyLight;
        public NibbleArrayReader data;
        public byte[] blocks;
        public NBTTagList entities;
        public NBTTagList tileEntities;
        public NBTTagList tileTicks;
        public final int x;
        public final int z;

        public AnvilConverterData(int i, int j) {
            this.x = i;
            this.z = j;
        }
    }
}
