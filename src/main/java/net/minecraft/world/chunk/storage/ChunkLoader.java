package net.minecraft.world.chunk.storage;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.NibbleArray;


public class ChunkLoader {

    public static ChunkLoader.AnvilConverterData func_76691_a(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.func_74762_e("xPos");
        int j = nbttagcompound.func_74762_e("zPos");
        ChunkLoader.AnvilConverterData oldchunkloader_oldchunk = new ChunkLoader.AnvilConverterData(i, j);

        oldchunkloader_oldchunk.field_76693_g = nbttagcompound.func_74770_j("Blocks");
        oldchunkloader_oldchunk.field_76692_f = new NibbleArrayReader(nbttagcompound.func_74770_j("Data"), 7);
        oldchunkloader_oldchunk.field_76695_e = new NibbleArrayReader(nbttagcompound.func_74770_j("SkyLight"), 7);
        oldchunkloader_oldchunk.field_76694_d = new NibbleArrayReader(nbttagcompound.func_74770_j("BlockLight"), 7);
        oldchunkloader_oldchunk.field_76697_c = nbttagcompound.func_74770_j("HeightMap");
        oldchunkloader_oldchunk.field_76696_b = nbttagcompound.func_74767_n("TerrainPopulated");
        oldchunkloader_oldchunk.field_76702_h = nbttagcompound.func_150295_c("Entities", 10);
        oldchunkloader_oldchunk.field_151564_i = nbttagcompound.func_150295_c("TileEntities", 10);
        oldchunkloader_oldchunk.field_151563_j = nbttagcompound.func_150295_c("TileTicks", 10);

        try {
            oldchunkloader_oldchunk.field_76698_a = nbttagcompound.func_74763_f("LastUpdate");
        } catch (ClassCastException classcastexception) {
            oldchunkloader_oldchunk.field_76698_a = (long) nbttagcompound.func_74762_e("LastUpdate");
        }

        return oldchunkloader_oldchunk;
    }

    public static void func_76690_a(ChunkLoader.AnvilConverterData oldchunkloader_oldchunk, NBTTagCompound nbttagcompound, BiomeProvider worldchunkmanager) {
        nbttagcompound.func_74768_a("xPos", oldchunkloader_oldchunk.field_76701_k);
        nbttagcompound.func_74768_a("zPos", oldchunkloader_oldchunk.field_76699_l);
        nbttagcompound.func_74772_a("LastUpdate", oldchunkloader_oldchunk.field_76698_a);
        int[] aint = new int[oldchunkloader_oldchunk.field_76697_c.length];

        for (int i = 0; i < oldchunkloader_oldchunk.field_76697_c.length; ++i) {
            aint[i] = oldchunkloader_oldchunk.field_76697_c[i];
        }

        nbttagcompound.func_74783_a("HeightMap", aint);
        nbttagcompound.func_74757_a("TerrainPopulated", oldchunkloader_oldchunk.field_76696_b);
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
                            byte b0 = oldchunkloader_oldchunk.field_76693_g[j1];

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
                            byte b1 = oldchunkloader_oldchunk.field_76693_g[j2];

                            abyte[l1 << 8 | i2 << 4 | k1] = (byte) (b1 & 255);
                            nibblearray.func_76581_a(k1, l1, i2, oldchunkloader_oldchunk.field_76692_f.func_76686_a(k1, l1 + (l << 4), i2));
                            nibblearray1.func_76581_a(k1, l1, i2, oldchunkloader_oldchunk.field_76695_e.func_76686_a(k1, l1 + (l << 4), i2));
                            nibblearray2.func_76581_a(k1, l1, i2, oldchunkloader_oldchunk.field_76694_d.func_76686_a(k1, l1 + (l << 4), i2));
                        }
                    }
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.func_74774_a("Y", (byte) (l & 255));
                nbttagcompound1.func_74773_a("Blocks", abyte);
                nbttagcompound1.func_74773_a("Data", nibblearray.func_177481_a());
                nbttagcompound1.func_74773_a("SkyLight", nibblearray1.func_177481_a());
                nbttagcompound1.func_74773_a("BlockLight", nibblearray2.func_177481_a());
                nbttaglist.func_74742_a(nbttagcompound1);
            }
        }

        nbttagcompound.func_74782_a("Sections", nbttaglist);
        byte[] abyte1 = new byte[256];
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        for (j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                blockposition_mutableblockposition.func_181079_c(oldchunkloader_oldchunk.field_76701_k << 4 | j, 0, oldchunkloader_oldchunk.field_76699_l << 4 | k);
                abyte1[k << 4 | j] = (byte) (Biome.func_185362_a(worldchunkmanager.func_180300_a(blockposition_mutableblockposition, Biomes.field_180279_ad)) & 255);
            }
        }

        nbttagcompound.func_74773_a("Biomes", abyte1);
        nbttagcompound.func_74782_a("Entities", oldchunkloader_oldchunk.field_76702_h);
        nbttagcompound.func_74782_a("TileEntities", oldchunkloader_oldchunk.field_151564_i);
        if (oldchunkloader_oldchunk.field_151563_j != null) {
            nbttagcompound.func_74782_a("TileTicks", oldchunkloader_oldchunk.field_151563_j);
        }

    }

    public static class AnvilConverterData {

        public long field_76698_a;
        public boolean field_76696_b;
        public byte[] field_76697_c;
        public NibbleArrayReader field_76694_d;
        public NibbleArrayReader field_76695_e;
        public NibbleArrayReader field_76692_f;
        public byte[] field_76693_g;
        public NBTTagList field_76702_h;
        public NBTTagList field_151564_i;
        public NBTTagList field_151563_j;
        public final int field_76701_k;
        public final int field_76699_l;

        public AnvilConverterData(int i, int j) {
            this.field_76701_k = i;
            this.field_76699_l = j;
        }
    }
}
