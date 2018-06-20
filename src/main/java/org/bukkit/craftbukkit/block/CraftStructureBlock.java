package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.tileentity.TileEntityStructure;
import org.apache.commons.lang3.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

public class CraftStructureBlock extends CraftBlockEntityState<TileEntityStructure> implements Structure {

    private static final int MAX_SIZE = 32;

    public CraftStructureBlock(Block block) {
        super(block, TileEntityStructure.class);
    }

    public CraftStructureBlock(Material material, TileEntityStructure structure) {
        super(material, structure);
    }

    @Override
    public String getStructureName() {
        return getSnapshot().func_189715_d(); // PAIL: rename getStructureName
    }

    @Override
    public void setStructureName(String name) {
        Preconditions.checkArgument(name != null, "Structure Name cannot be null");
        getSnapshot().func_184404_a(name); // PAIL: rename setStructureName
    }

    @Override
    public String getAuthor() {
        return getSnapshot().field_184421_f;
    }

    @Override
    public void setAuthor(String author) {
        Preconditions.checkArgument(author != null && !author.isEmpty(), "Author name cannot be null nor empty");
        getSnapshot().field_184421_f = author; // PAIL: rename author
    }

    @Override
    public void setAuthor(LivingEntity entity) {
        Preconditions.checkArgument(entity != null, "Structure Block author entity cannot be null");
        getSnapshot().func_189720_a(((CraftLivingEntity) entity).getHandle()); // PAIL: rename setAuthor
    }

    @Override
    public BlockVector getRelativePosition() {
        return new BlockVector(getSnapshot().field_184423_h.func_177958_n(), getSnapshot().field_184423_h.func_177956_o(), getSnapshot().field_184423_h.func_177952_p()); // PAIL: rename relativePosition
    }

    @Override
    public void setRelativePosition(BlockVector vector) {
        Validate.isTrue(isBetween(vector.getBlockX(), -MAX_SIZE, MAX_SIZE), "Structure Size (X) must be between -" + MAX_SIZE + " and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockY(), -MAX_SIZE, MAX_SIZE), "Structure Size (Y) must be between -" + MAX_SIZE + " and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockZ(), -MAX_SIZE, MAX_SIZE), "Structure Size (Z) must be between -" + MAX_SIZE + " and " + MAX_SIZE);
        getSnapshot().field_184423_h = new BlockPos(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()); // PAIL: rename relativePosition
    }

    @Override
    public BlockVector getStructureSize() {
        return new BlockVector(getSnapshot().field_184424_i.func_177958_n(), getSnapshot().field_184424_i.func_177956_o(), getSnapshot().field_184424_i.func_177952_p()); // PAIL: rename size
    }

    @Override
    public void setStructureSize(BlockVector vector) {
        Validate.isTrue(isBetween(vector.getBlockX(), 0, MAX_SIZE), "Structure Size (X) must be between 0 and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockY(), 0, MAX_SIZE), "Structure Size (Y) must be between 0 and " + MAX_SIZE);
        Validate.isTrue(isBetween(vector.getBlockZ(), 0, MAX_SIZE), "Structure Size (Z) must be between 0 and " + MAX_SIZE);
        getSnapshot().field_184424_i = new BlockPos(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ()); // PAIL: rename size
    }

    @Override
    public void setMirror(org.bukkit.block.structure.Mirror mirror) {
        getSnapshot().field_184425_j = Mirror.valueOf(mirror.name()); // PAIL: rename mirror
    }

    @Override
    public org.bukkit.block.structure.Mirror getMirror() {
        return org.bukkit.block.structure.Mirror.valueOf(getSnapshot().field_184425_j.name()); // PAIL: rename mirror
    }

    @Override
    public void setRotation(StructureRotation rotation) {
        getSnapshot().field_184426_k = Rotation.valueOf(rotation.name()); // PAIL: rename rotation
    }

    @Override
    public StructureRotation getRotation() {
        return StructureRotation.valueOf(getSnapshot().field_184426_k.name()); // PAIL: rename rotation
    }

    @Override
    public void setUsageMode(UsageMode mode) {
        getSnapshot().func_184405_a(TileEntityStructure.Mode.valueOf(mode.name())); // PAIL: rename setUsageMode
    }

    @Override
    public UsageMode getUsageMode() {
        return UsageMode.valueOf(getSnapshot().func_189700_k().name()); // PAIL rename getUsageMode
    }

    @Override
    public void setIgnoreEntities(boolean flag) {
        getSnapshot().field_184428_m = flag; // PAIL: rename ignoreEntities
    }

    @Override
    public boolean isIgnoreEntities() {
        return getSnapshot().field_184428_m; // PAIL: rename ignoreEntities
    }

    @Override
    public void setShowAir(boolean showAir) {
        getSnapshot().field_189728_o = showAir; // PAIL rename showAir
    }

    @Override
    public boolean isShowAir() {
        return getSnapshot().field_189728_o; // PAIL: rename showAir
    }

    @Override
    public void setBoundingBoxVisible(boolean showBoundingBox) {
        getSnapshot().field_189729_p = showBoundingBox; // PAIL: rename boundingBoxVisible
    }

    @Override
    public boolean isBoundingBoxVisible() {
        return getSnapshot().field_189729_p; // PAIL: rename boundingBoxVisible
    }

    @Override
    public void setIntegrity(float integrity) {
        Validate.isTrue(isBetween(integrity, 0.0f, 1.0f), "Integrity must be between 0.0f and 1.0f");
        getSnapshot().field_189730_q = integrity; // PAIL: rename integrity
    }

    @Override
    public float getIntegrity() {
        return getSnapshot().field_189730_q; // PAIL: rename integrity
    }

    @Override
    public void setSeed(long seed) {
        getSnapshot().field_189731_r = seed; // PAIL: rename seed
    }

    @Override
    public long getSeed() {
        return getSnapshot().field_189731_r; // PAIL: rename seed
    }

    @Override
    public void setMetadata(String metadata) {
        Validate.notNull(metadata, "Structure metadata cannot be null");
        if (getUsageMode() == UsageMode.DATA) {
            getSnapshot().field_184422_g = metadata; // PAIL: rename metadata
        }
    }

    @Override
    public String getMetadata() {
        return getSnapshot().field_184422_g; // PAIL: rename metadata
    }

    private static boolean isBetween(int num, int min, int max) {
        return num >= min && num <= max;
    }

    private static boolean isBetween(float num, float min, float max) {
        return num >= min && num <= max;
    }
}
