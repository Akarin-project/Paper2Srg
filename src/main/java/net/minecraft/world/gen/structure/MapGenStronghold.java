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

    private final List<Biome> field_151546_e;
    private boolean field_75056_f;
    private ChunkPos[] field_75057_g;
    private double field_82671_h;
    private int field_82672_i;

    public MapGenStronghold() {
        this.field_75057_g = new ChunkPos[128];
        this.field_82671_h = 32.0D;
        this.field_82672_i = 3;
        this.field_151546_e = Lists.newArrayList();
        Iterator iterator = Biome.field_185377_q.iterator();

        while (iterator.hasNext()) {
            Biome biomebase = (Biome) iterator.next();

            if (biomebase != null && biomebase.func_185355_j() > 0.0F) {
                this.field_151546_e.add(biomebase);
            }
        }

    }

    public MapGenStronghold(Map<String, String> map) {
        this();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (((String) entry.getKey()).equals("distance")) {
                this.field_82671_h = MathHelper.func_82713_a((String) entry.getValue(), this.field_82671_h, 1.0D);
            } else if (((String) entry.getKey()).equals("count")) {
                this.field_75057_g = new ChunkPos[MathHelper.func_82714_a((String) entry.getValue(), this.field_75057_g.length, 1)];
            } else if (((String) entry.getKey()).equals("spread")) {
                this.field_82672_i = MathHelper.func_82714_a((String) entry.getValue(), this.field_82672_i, 1);
            }
        }

    }

    public String func_143025_a() {
        return "Stronghold";
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        this.field_75039_c = world; // Paper
        if (!this.field_75056_f) {
            this.func_189104_c();
            this.field_75056_f = true;
        }

        BlockPos blockposition1 = null;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(0, 0, 0);
        double d0 = Double.MAX_VALUE;
        ChunkPos[] achunkcoordintpair = this.field_75057_g;
        int i = achunkcoordintpair.length;

        for (int j = 0; j < i; ++j) {
            ChunkPos chunkcoordintpair = achunkcoordintpair[j];

            blockposition_mutableblockposition.func_181079_c((chunkcoordintpair.field_77276_a << 4) + 8, 32, (chunkcoordintpair.field_77275_b << 4) + 8);
            double d1 = blockposition_mutableblockposition.func_177951_i(blockposition);

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

    protected boolean func_75047_a(int i, int j) {
        if (!this.field_75056_f) {
            this.func_189104_c();
            this.field_75056_f = true;
        }

        ChunkPos[] achunkcoordintpair = this.field_75057_g;
        int k = achunkcoordintpair.length;

        for (int l = 0; l < k; ++l) {
            ChunkPos chunkcoordintpair = achunkcoordintpair[l];

            if (i == chunkcoordintpair.field_77276_a && j == chunkcoordintpair.field_77275_b) {
                return true;
            }
        }

        return false;
    }

    private void func_189104_c() {
        this.func_143027_a(this.field_75039_c);
        int i = 0;
        ObjectIterator objectiterator = this.field_75053_d.values().iterator();

        while (objectiterator.hasNext()) {
            StructureStart structurestart = (StructureStart) objectiterator.next();

            if (i < this.field_75057_g.length) {
                this.field_75057_g[i++] = new ChunkPos(structurestart.func_143019_e(), structurestart.func_143018_f());
            }
        }

        Random random = new Random();

        random.setSeed(this.field_75039_c.func_72905_C());
        double d0 = random.nextDouble() * 3.141592653589793D * 2.0D;
        int j = 0;
        int k = 0;
        int l = this.field_75053_d.size();

        if (l < this.field_75057_g.length) {
            for (int i1 = 0; i1 < this.field_75057_g.length; ++i1) {
                double d1 = 4.0D * this.field_82671_h + this.field_82671_h * (double) j * 6.0D + (random.nextDouble() - 0.5D) * this.field_82671_h * 2.5D;
                int j1 = (int) Math.round(Math.cos(d0) * d1);
                int k1 = (int) Math.round(Math.sin(d0) * d1);
                BlockPos blockposition = this.field_75039_c.func_72959_q().func_180630_a((j1 << 4) + 8, (k1 << 4) + 8, 112, this.field_151546_e, random);

                if (blockposition != null) {
                    j1 = blockposition.func_177958_n() >> 4;
                    k1 = blockposition.func_177952_p() >> 4;
                }

                if (i1 >= l) {
                    this.field_75057_g[i1] = new ChunkPos(j1, k1);
                }

                d0 += 6.283185307179586D / (double) this.field_82672_i;
                ++k;
                if (k == this.field_82672_i) {
                    ++j;
                    k = 0;
                    this.field_82672_i += 2 * this.field_82672_i / (j + 1);
                    this.field_82672_i = Math.min(this.field_82672_i, this.field_75057_g.length - i1);
                    d0 += random.nextDouble() * 3.141592653589793D * 2.0D;
                }
            }
        }

    }

    protected StructureStart func_75049_b(int i, int j) {
        MapGenStronghold.Start worldgenstronghold_worldgenstronghold2start;

        for (worldgenstronghold_worldgenstronghold2start = new MapGenStronghold.Start(this.field_75039_c, this.field_75038_b, i, j); worldgenstronghold_worldgenstronghold2start.func_186161_c().isEmpty() || ((StructureStrongholdPieces.Stairs2) worldgenstronghold_worldgenstronghold2start.func_186161_c().get(0)).field_75025_b == null; worldgenstronghold_worldgenstronghold2start = new MapGenStronghold.Start(this.field_75039_c, this.field_75038_b, i, j)) {
            ;
        }

        return worldgenstronghold_worldgenstronghold2start;
    }

    public static class Start extends StructureStart {

        public Start() {}

        public Start(World world, Random random, int i, int j) {
            super(i, j);
            StructureStrongholdPieces.func_75198_a();
            StructureStrongholdPieces.Stairs2 worldgenstrongholdpieces_worldgenstrongholdstart = new StructureStrongholdPieces.Stairs2(0, random, (i << 4) + 2, (j << 4) + 2);

            this.field_75075_a.add(worldgenstrongholdpieces_worldgenstrongholdstart);
            worldgenstrongholdpieces_worldgenstrongholdstart.func_74861_a((StructureComponent) worldgenstrongholdpieces_worldgenstrongholdstart, this.field_75075_a, random);
            List list = worldgenstrongholdpieces_worldgenstrongholdstart.field_75026_c;

            while (!list.isEmpty()) {
                int k = random.nextInt(list.size());
                StructureComponent structurepiece = (StructureComponent) list.remove(k);

                structurepiece.func_74861_a((StructureComponent) worldgenstrongholdpieces_worldgenstrongholdstart, this.field_75075_a, random);
            }

            this.func_75072_c();
            this.func_75067_a(world, random, 10);
        }
    }
}
