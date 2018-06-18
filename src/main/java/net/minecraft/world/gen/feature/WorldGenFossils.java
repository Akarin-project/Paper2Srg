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

    private static final ResourceLocation STRUCTURE_SPINE_01 = new ResourceLocation("fossils/fossil_spine_01");
    private static final ResourceLocation STRUCTURE_SPINE_02 = new ResourceLocation("fossils/fossil_spine_02");
    private static final ResourceLocation STRUCTURE_SPINE_03 = new ResourceLocation("fossils/fossil_spine_03");
    private static final ResourceLocation STRUCTURE_SPINE_04 = new ResourceLocation("fossils/fossil_spine_04");
    private static final ResourceLocation STRUCTURE_SPINE_01_COAL = new ResourceLocation("fossils/fossil_spine_01_coal");
    private static final ResourceLocation STRUCTURE_SPINE_02_COAL = new ResourceLocation("fossils/fossil_spine_02_coal");
    private static final ResourceLocation STRUCTURE_SPINE_03_COAL = new ResourceLocation("fossils/fossil_spine_03_coal");
    private static final ResourceLocation STRUCTURE_SPINE_04_COAL = new ResourceLocation("fossils/fossil_spine_04_coal");
    private static final ResourceLocation STRUCTURE_SKULL_01 = new ResourceLocation("fossils/fossil_skull_01");
    private static final ResourceLocation STRUCTURE_SKULL_02 = new ResourceLocation("fossils/fossil_skull_02");
    private static final ResourceLocation STRUCTURE_SKULL_03 = new ResourceLocation("fossils/fossil_skull_03");
    private static final ResourceLocation STRUCTURE_SKULL_04 = new ResourceLocation("fossils/fossil_skull_04");
    private static final ResourceLocation STRUCTURE_SKULL_01_COAL = new ResourceLocation("fossils/fossil_skull_01_coal");
    private static final ResourceLocation STRUCTURE_SKULL_02_COAL = new ResourceLocation("fossils/fossil_skull_02_coal");
    private static final ResourceLocation STRUCTURE_SKULL_03_COAL = new ResourceLocation("fossils/fossil_skull_03_coal");
    private static final ResourceLocation STRUCTURE_SKULL_04_COAL = new ResourceLocation("fossils/fossil_skull_04_coal");
    private static final ResourceLocation[] FOSSILS = new ResourceLocation[] { WorldGenFossils.STRUCTURE_SPINE_01, WorldGenFossils.STRUCTURE_SPINE_02, WorldGenFossils.STRUCTURE_SPINE_03, WorldGenFossils.STRUCTURE_SPINE_04, WorldGenFossils.STRUCTURE_SKULL_01, WorldGenFossils.STRUCTURE_SKULL_02, WorldGenFossils.STRUCTURE_SKULL_03, WorldGenFossils.STRUCTURE_SKULL_04};
    private static final ResourceLocation[] FOSSILS_COAL = new ResourceLocation[] { WorldGenFossils.STRUCTURE_SPINE_01_COAL, WorldGenFossils.STRUCTURE_SPINE_02_COAL, WorldGenFossils.STRUCTURE_SPINE_03_COAL, WorldGenFossils.STRUCTURE_SPINE_04_COAL, WorldGenFossils.STRUCTURE_SKULL_01_COAL, WorldGenFossils.STRUCTURE_SKULL_02_COAL, WorldGenFossils.STRUCTURE_SKULL_03_COAL, WorldGenFossils.STRUCTURE_SKULL_04_COAL};

    public WorldGenFossils() {}

    public boolean generate(World world, Random random, BlockPos blockposition) {
        Random random1 = world.getChunkFromBlockCoords(blockposition).getRandomWithSeed(987234911L);
        MinecraftServer minecraftserver = world.getMinecraftServer();
        Rotation[] aenumblockrotation = Rotation.values();
        Rotation enumblockrotation = aenumblockrotation[random1.nextInt(aenumblockrotation.length)];
        int i = random1.nextInt(WorldGenFossils.FOSSILS.length);
        TemplateManager definedstructuremanager = world.getSaveHandler().getStructureTemplateManager();
        Template definedstructure = definedstructuremanager.getTemplate(minecraftserver, WorldGenFossils.FOSSILS[i]);
        Template definedstructure1 = definedstructuremanager.getTemplate(minecraftserver, WorldGenFossils.FOSSILS_COAL[i]);
        ChunkPos chunkcoordintpair = new ChunkPos(blockposition);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkcoordintpair.getXStart(), 0, chunkcoordintpair.getZStart(), chunkcoordintpair.getXEnd(), 256, chunkcoordintpair.getZEnd());
        PlacementSettings definedstructureinfo = (new PlacementSettings()).setRotation(enumblockrotation).setBoundingBox(structureboundingbox).setRandom(random1);
        BlockPos blockposition1 = definedstructure.transformedSize(enumblockrotation);
        int j = random1.nextInt(16 - blockposition1.getX());
        int k = random1.nextInt(16 - blockposition1.getZ());
        int l = 256;

        int i1;

        for (i1 = 0; i1 < blockposition1.getX(); ++i1) {
            for (int j1 = 0; j1 < blockposition1.getX(); ++j1) {
                l = Math.min(l, world.getHeight(blockposition.getX() + i1 + j, blockposition.getZ() + j1 + k));
            }
        }

        i1 = Math.max(l - 15 - random1.nextInt(10), 10);
        BlockPos blockposition2 = definedstructure.getZeroPositionWithTransform(blockposition.add(j, i1, k), Mirror.NONE, enumblockrotation);

        definedstructureinfo.setIntegrity(0.9F);
        definedstructure.addBlocksToWorld(world, blockposition2, definedstructureinfo, 20);
        definedstructureinfo.setIntegrity(0.1F);
        definedstructure1.addBlocksToWorld(world, blockposition2, definedstructureinfo, 20);
        return true;
    }
}
