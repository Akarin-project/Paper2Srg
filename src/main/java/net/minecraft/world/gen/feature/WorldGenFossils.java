package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenFossils extends WorldGenerator {

    private static final ResourceLocation field_189890_a = new ResourceLocation("fossils/fossil_spine_01");
    private static final ResourceLocation field_189891_b = new ResourceLocation("fossils/fossil_spine_02");
    private static final ResourceLocation field_189892_c = new ResourceLocation("fossils/fossil_spine_03");
    private static final ResourceLocation field_189893_d = new ResourceLocation("fossils/fossil_spine_04");
    private static final ResourceLocation field_189894_e = new ResourceLocation("fossils/fossil_spine_01_coal");
    private static final ResourceLocation field_189895_f = new ResourceLocation("fossils/fossil_spine_02_coal");
    private static final ResourceLocation field_189896_g = new ResourceLocation("fossils/fossil_spine_03_coal");
    private static final ResourceLocation field_189897_h = new ResourceLocation("fossils/fossil_spine_04_coal");
    private static final ResourceLocation field_189898_i = new ResourceLocation("fossils/fossil_skull_01");
    private static final ResourceLocation field_189899_j = new ResourceLocation("fossils/fossil_skull_02");
    private static final ResourceLocation field_189900_k = new ResourceLocation("fossils/fossil_skull_03");
    private static final ResourceLocation field_189901_l = new ResourceLocation("fossils/fossil_skull_04");
    private static final ResourceLocation field_189902_m = new ResourceLocation("fossils/fossil_skull_01_coal");
    private static final ResourceLocation field_189903_n = new ResourceLocation("fossils/fossil_skull_02_coal");
    private static final ResourceLocation field_189904_o = new ResourceLocation("fossils/fossil_skull_03_coal");
    private static final ResourceLocation field_189905_p = new ResourceLocation("fossils/fossil_skull_04_coal");
    private static final ResourceLocation[] field_189906_q = new ResourceLocation[] { WorldGenFossils.field_189890_a, WorldGenFossils.field_189891_b, WorldGenFossils.field_189892_c, WorldGenFossils.field_189893_d, WorldGenFossils.field_189898_i, WorldGenFossils.field_189899_j, WorldGenFossils.field_189900_k, WorldGenFossils.field_189901_l};
    private static final ResourceLocation[] field_189907_r = new ResourceLocation[] { WorldGenFossils.field_189894_e, WorldGenFossils.field_189895_f, WorldGenFossils.field_189896_g, WorldGenFossils.field_189897_h, WorldGenFossils.field_189902_m, WorldGenFossils.field_189903_n, WorldGenFossils.field_189904_o, WorldGenFossils.field_189905_p};

    public WorldGenFossils() {}

    public boolean func_180709_b(World world, Random random, BlockPos blockposition) {
        Random random1 = world.func_175726_f(blockposition).func_76617_a(987234911L);
        MinecraftServer minecraftserver = world.func_73046_m();
        Rotation[] aenumblockrotation = Rotation.values();
        Rotation enumblockrotation = aenumblockrotation[random1.nextInt(aenumblockrotation.length)];
        int i = random1.nextInt(WorldGenFossils.field_189906_q.length);
        TemplateManager definedstructuremanager = world.func_72860_G().func_186340_h();
        Template definedstructure = definedstructuremanager.func_186237_a(minecraftserver, WorldGenFossils.field_189906_q[i]);
        Template definedstructure1 = definedstructuremanager.func_186237_a(minecraftserver, WorldGenFossils.field_189907_r[i]);
        ChunkPos chunkcoordintpair = new ChunkPos(blockposition);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkcoordintpair.func_180334_c(), 0, chunkcoordintpair.func_180333_d(), chunkcoordintpair.func_180332_e(), 256, chunkcoordintpair.func_180330_f());
        PlacementSettings definedstructureinfo = (new PlacementSettings()).func_186220_a(enumblockrotation).func_186223_a(structureboundingbox).func_189950_a(random1);
        BlockPos blockposition1 = definedstructure.func_186257_a(enumblockrotation);
        int j = random1.nextInt(16 - blockposition1.func_177958_n());
        int k = random1.nextInt(16 - blockposition1.func_177952_p());
        int l = 256;

        int i1;

        for (i1 = 0; i1 < blockposition1.func_177958_n(); ++i1) {
            for (int j1 = 0; j1 < blockposition1.func_177958_n(); ++j1) {
                l = Math.min(l, world.func_189649_b(blockposition.func_177958_n() + i1 + j, blockposition.func_177952_p() + j1 + k));
            }
        }

        i1 = Math.max(l - 15 - random1.nextInt(10), 10);
        BlockPos blockposition2 = definedstructure.func_189961_a(blockposition.func_177982_a(j, i1, k), Mirror.NONE, enumblockrotation);

        definedstructureinfo.func_189946_a(0.9F);
        definedstructure.func_189962_a(world, blockposition2, definedstructureinfo, 20);
        definedstructureinfo.func_189946_a(0.1F);
        definedstructure1.func_189962_a(world, blockposition2, definedstructureinfo, 20);
        return true;
    }
}
