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

    private final int field_191073_b = 80;
    private final int field_191074_d = 20;
    public static final List<Biome> field_191072_a = Arrays.asList(new Biome[] { Biomes.field_150585_R, Biomes.field_185430_ab});
    private final ChunkGeneratorOverworld field_191075_h;

    public WoodlandMansion(ChunkGeneratorOverworld chunkprovidergenerate) {
        this.field_191075_h = chunkprovidergenerate;
    }

    public String func_143025_a() {
        return "Mansion";
    }

    protected boolean func_75047_a(int i, int j) {
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
        Random random = this.field_75039_c.func_72843_D(i1, j1, 10387319);

        i1 *= 80;
        j1 *= 80;
        i1 += (random.nextInt(60) + random.nextInt(60)) / 2;
        j1 += (random.nextInt(60) + random.nextInt(60)) / 2;
        if (i == i1 && j == j1) {
            boolean flag = this.field_75039_c.func_72959_q().func_76940_a(i * 16 + 8, j * 16 + 8, 32, WoodlandMansion.field_191072_a);

            if (flag) {
                return true;
            }
        }

        return false;
    }

    public BlockPos func_180706_b(World world, BlockPos blockposition, boolean flag) {
        this.field_75039_c = world;
        BiomeProvider worldchunkmanager = world.func_72959_q();

        return worldchunkmanager.func_190944_c() && worldchunkmanager.func_190943_d() != Biomes.field_150585_R ? null : func_191069_a(world, this, blockposition, 80, 20, 10387319, true, 100, flag);
    }

    protected StructureStart func_75049_b(int i, int j) {
        return new WorldGenWoodlandMansion.a(this.field_75039_c, this.field_191075_h, this.field_75038_b, i, j);
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

            chunkprovidergenerate.func_185976_a(i, j, chunksnapshot);
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

            if (k1 < 60) {
                this.c = false;
            } else {
                BlockPos blockposition = new BlockPos(i * 16 + 8, k1 + 1, j * 16 + 8);
                LinkedList linkedlist = Lists.newLinkedList();

                WoodlandMansionPieces.func_191152_a(world.func_72860_G().func_186340_h(), blockposition, enumblockrotation, linkedlist, random);
                this.field_75075_a.addAll(linkedlist);
                this.func_75072_c();
                this.c = true;
            }
        }

        public void func_75068_a(World world, Random random, StructureBoundingBox structureboundingbox) {
            super.func_75068_a(world, random, structureboundingbox);
            int i = this.field_75074_b.field_78895_b;

            for (int j = structureboundingbox.field_78897_a; j <= structureboundingbox.field_78893_d; ++j) {
                for (int k = structureboundingbox.field_78896_c; k <= structureboundingbox.field_78892_f; ++k) {
                    BlockPos blockposition = new BlockPos(j, i, k);

                    if (!world.func_175623_d(blockposition) && this.field_75074_b.func_175898_b((Vec3i) blockposition)) {
                        boolean flag = false;
                        Iterator iterator = this.field_75075_a.iterator();

                        while (iterator.hasNext()) {
                            StructureComponent structurepiece = (StructureComponent) iterator.next();

                            if (structurepiece.field_74887_e.func_175898_b((Vec3i) blockposition)) {
                                flag = true;
                                break;
                            }
                        }

                        if (flag) {
                            for (int l = i - 1; l > 1; --l) {
                                BlockPos blockposition1 = new BlockPos(j, l, k);

                                if (!world.func_175623_d(blockposition1) && !world.func_180495_p(blockposition1).func_185904_a().func_76224_d()) {
                                    break;
                                }

                                world.func_180501_a(blockposition1, Blocks.field_150347_e.func_176223_P(), 2);
                            }
                        }
                    }
                }
            }

        }

        public boolean func_75069_d() {
            return this.c;
        }
    }
}
