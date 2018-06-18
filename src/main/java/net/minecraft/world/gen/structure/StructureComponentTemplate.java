package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponentTemplate extends StructureComponent {

    private static final PlacementSettings DEFAULT_PLACE_SETTINGS = new PlacementSettings();
    protected Template template;
    protected PlacementSettings placeSettings;
    protected BlockPos templatePosition;

    public StructureComponentTemplate() {
        this.placeSettings = StructureComponentTemplate.DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    }

    public StructureComponentTemplate(int i) {
        super(i);
        this.placeSettings = StructureComponentTemplate.DEFAULT_PLACE_SETTINGS.setIgnoreEntities(true).setReplacedBlock(Blocks.AIR);
    }

    protected void setup(Template definedstructure, BlockPos blockposition, PlacementSettings definedstructureinfo) {
        this.template = definedstructure;
        this.setCoordBaseMode(EnumFacing.NORTH);
        this.templatePosition = blockposition;
        this.placeSettings = definedstructureinfo;
        this.setBoundingBoxFromTemplate();
    }

    protected void writeStructureToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("TPX", this.templatePosition.getX());
        nbttagcompound.setInteger("TPY", this.templatePosition.getY());
        nbttagcompound.setInteger("TPZ", this.templatePosition.getZ());
    }

    protected void readStructureFromNBT(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager) {
        this.templatePosition = new BlockPos(nbttagcompound.getInteger("TPX"), nbttagcompound.getInteger("TPY"), nbttagcompound.getInteger("TPZ"));
    }

    public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
        this.placeSettings.setBoundingBox(structureboundingbox);
        this.template.addBlocksToWorld(world, this.templatePosition, this.placeSettings, 18);
        Map map = this.template.getDataBlocks(this.templatePosition, this.placeSettings);
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            String s = (String) entry.getValue();

            this.handleDataMarker(s, (BlockPos) entry.getKey(), world, random, structureboundingbox);
        }

        return true;
    }

    protected abstract void handleDataMarker(String s, BlockPos blockposition, World world, Random random, StructureBoundingBox structureboundingbox);

    private void setBoundingBoxFromTemplate() {
        Rotation enumblockrotation = this.placeSettings.getRotation();
        BlockPos blockposition = this.template.transformedSize(enumblockrotation);
        Mirror enumblockmirror = this.placeSettings.getMirror();

        this.boundingBox = new StructureBoundingBox(0, 0, 0, blockposition.getX(), blockposition.getY() - 1, blockposition.getZ());
        switch (enumblockrotation) {
        case NONE:
        default:
            break;

        case CLOCKWISE_90:
            this.boundingBox.offset(-blockposition.getX(), 0, 0);
            break;

        case COUNTERCLOCKWISE_90:
            this.boundingBox.offset(0, 0, -blockposition.getZ());
            break;

        case CLOCKWISE_180:
            this.boundingBox.offset(-blockposition.getX(), 0, -blockposition.getZ());
        }

        BlockPos blockposition1;

        switch (enumblockmirror) {
        case NONE:
        default:
            break;

        case FRONT_BACK:
            blockposition1 = BlockPos.ORIGIN;
            if (enumblockrotation != Rotation.CLOCKWISE_90 && enumblockrotation != Rotation.COUNTERCLOCKWISE_90) {
                if (enumblockrotation == Rotation.CLOCKWISE_180) {
                    blockposition1 = blockposition1.offset(EnumFacing.EAST, blockposition.getX());
                } else {
                    blockposition1 = blockposition1.offset(EnumFacing.WEST, blockposition.getX());
                }
            } else {
                blockposition1 = blockposition1.offset(enumblockrotation.rotate(EnumFacing.WEST), blockposition.getZ());
            }

            this.boundingBox.offset(blockposition1.getX(), 0, blockposition1.getZ());
            break;

        case LEFT_RIGHT:
            blockposition1 = BlockPos.ORIGIN;
            if (enumblockrotation != Rotation.CLOCKWISE_90 && enumblockrotation != Rotation.COUNTERCLOCKWISE_90) {
                if (enumblockrotation == Rotation.CLOCKWISE_180) {
                    blockposition1 = blockposition1.offset(EnumFacing.SOUTH, blockposition.getZ());
                } else {
                    blockposition1 = blockposition1.offset(EnumFacing.NORTH, blockposition.getZ());
                }
            } else {
                blockposition1 = blockposition1.offset(enumblockrotation.rotate(EnumFacing.NORTH), blockposition.getX());
            }

            this.boundingBox.offset(blockposition1.getX(), 0, blockposition1.getZ());
        }

        this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
    }

    public void offset(int i, int j, int k) {
        super.offset(i, j, k);
        this.templatePosition = this.templatePosition.add(i, j, k);
    }
}
