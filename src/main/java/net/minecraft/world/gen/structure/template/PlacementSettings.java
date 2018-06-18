package net.minecraft.world.gen.structure.template;

import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class PlacementSettings {

    private Mirror mirror;
    private Rotation rotation;
    private boolean ignoreEntities;
    @Nullable
    private Block replacedBlock;
    @Nullable
    private ChunkPos chunk;
    @Nullable
    private StructureBoundingBox boundingBox;
    private boolean ignoreStructureBlock;
    private float integrity;
    @Nullable
    private Random random;
    @Nullable
    private Long setSeed;

    public PlacementSettings() {
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.ignoreStructureBlock = true;
        this.integrity = 1.0F;
    }

    public PlacementSettings copy() {
        PlacementSettings definedstructureinfo = new PlacementSettings();

        definedstructureinfo.mirror = this.mirror;
        definedstructureinfo.rotation = this.rotation;
        definedstructureinfo.ignoreEntities = this.ignoreEntities;
        definedstructureinfo.replacedBlock = this.replacedBlock;
        definedstructureinfo.chunk = this.chunk;
        definedstructureinfo.boundingBox = this.boundingBox;
        definedstructureinfo.ignoreStructureBlock = this.ignoreStructureBlock;
        definedstructureinfo.integrity = this.integrity;
        definedstructureinfo.random = this.random;
        definedstructureinfo.setSeed = this.setSeed;
        return definedstructureinfo;
    }

    public PlacementSettings setMirror(Mirror enumblockmirror) {
        this.mirror = enumblockmirror;
        return this;
    }

    public PlacementSettings setRotation(Rotation enumblockrotation) {
        this.rotation = enumblockrotation;
        return this;
    }

    public PlacementSettings setIgnoreEntities(boolean flag) {
        this.ignoreEntities = flag;
        return this;
    }

    public PlacementSettings setReplacedBlock(Block block) {
        this.replacedBlock = block;
        return this;
    }

    public PlacementSettings setChunk(ChunkPos chunkcoordintpair) {
        this.chunk = chunkcoordintpair;
        return this;
    }

    public PlacementSettings setBoundingBox(StructureBoundingBox structureboundingbox) {
        this.boundingBox = structureboundingbox;
        return this;
    }

    public PlacementSettings setSeed(@Nullable Long olong) {
        this.setSeed = olong;
        return this;
    }

    public PlacementSettings setRandom(@Nullable Random random) {
        this.random = random;
        return this;
    }

    public PlacementSettings setIntegrity(float f) {
        this.integrity = f;
        return this;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public PlacementSettings setIgnoreStructureBlock(boolean flag) {
        this.ignoreStructureBlock = flag;
        return this;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public Random getRandom(@Nullable BlockPos blockposition) {
        if (this.random != null) {
            return this.random;
        } else if (this.setSeed != null) {
            return this.setSeed.longValue() == 0L ? new Random(System.currentTimeMillis()) : new Random(this.setSeed.longValue());
        } else if (blockposition == null) {
            return new Random(System.currentTimeMillis());
        } else {
            int i = blockposition.getX();
            int j = blockposition.getZ();

            return new Random((long) (i * i * 4987142 + i * 5947611) + (long) (j * j) * 4392871L + (long) (j * 389711) ^ 987234911L);
        }
    }

    public float getIntegrity() {
        return this.integrity;
    }

    public boolean getIgnoreEntities() {
        return this.ignoreEntities;
    }

    @Nullable
    public Block getReplacedBlock() {
        return this.replacedBlock;
    }

    @Nullable
    public StructureBoundingBox getBoundingBox() {
        if (this.boundingBox == null && this.chunk != null) {
            this.setBoundingBoxFromChunk();
        }

        return this.boundingBox;
    }

    public boolean getIgnoreStructureBlock() {
        return this.ignoreStructureBlock;
    }

    void setBoundingBoxFromChunk() {
        this.boundingBox = this.getBoundingBoxFromChunk(this.chunk);
    }

    @Nullable
    private StructureBoundingBox getBoundingBoxFromChunk(@Nullable ChunkPos chunkcoordintpair) {
        if (chunkcoordintpair == null) {
            return null;
        } else {
            int i = chunkcoordintpair.x * 16;
            int j = chunkcoordintpair.z * 16;

            return new StructureBoundingBox(i, 0, j, i + 16 - 1, 255, j + 16 - 1);
        }
    }
}
