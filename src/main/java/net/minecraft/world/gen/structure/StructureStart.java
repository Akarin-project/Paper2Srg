package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public abstract class StructureStart {

    protected List<StructureComponent> components = Lists.newLinkedList();
    protected StructureBoundingBox boundingBox;
    private int chunkPosX;
    private int chunkPosZ;

    public StructureStart() {}

    public StructureStart(int i, int j) {
        this.chunkPosX = i;
        this.chunkPosZ = j;
    }

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public List<StructureComponent> getStructurePieces() { return getComponents(); } // Paper - OBFHELPER
    public List<StructureComponent> getComponents() {
        return this.components;
    }

    public void generateStructure(World world, Random random, StructureBoundingBox structureboundingbox) {
        Iterator iterator = this.components.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            if (structurepiece.getBoundingBox().intersectsWith(structureboundingbox) && !structurepiece.addComponentParts(world, random, structureboundingbox)) {
                iterator.remove();
            }
        }

    }

    protected void updateBoundingBox() {
        this.boundingBox = StructureBoundingBox.getNewBoundingBox();
        Iterator iterator = this.components.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            this.boundingBox.expandTo(structurepiece.getBoundingBox());
        }

    }

    public NBTTagCompound writeStructureComponentsToNBT(int i, int j) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("id", MapGenStructureIO.getStructureStartName(this));
        nbttagcompound.setInteger("ChunkX", i);
        nbttagcompound.setInteger("ChunkZ", j);
        nbttagcompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.components.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            nbttaglist.appendTag(structurepiece.createStructureBaseNBT());
        }

        nbttagcompound.setTag("Children", nbttaglist);
        this.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {}

    public void readStructureComponentsFromNBT(World world, NBTTagCompound nbttagcompound) {
        this.chunkPosX = nbttagcompound.getInteger("ChunkX");
        this.chunkPosZ = nbttagcompound.getInteger("ChunkZ");
        if (nbttagcompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(nbttagcompound.getIntArray("BB"));
        }

        NBTTagList nbttaglist = nbttagcompound.getTagList("Children", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            this.components.add(MapGenStructureIO.getStructureComponent(nbttaglist.getCompoundTagAt(i), world));
        }

        this.readFromNBT(nbttagcompound);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {}

    protected void markAvailableHeight(World world, Random random, int i) {
        int j = world.getSeaLevel() - i;
        int k = this.boundingBox.getYSize() + 1;

        if (k < j) {
            k += random.nextInt(j - k);
        }

        int l = k - this.boundingBox.maxY;

        this.boundingBox.offset(0, l, 0);
        Iterator iterator = this.components.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            structurepiece.offset(0, l, 0);
        }

    }

    protected void setRandomHeight(World world, Random random, int i, int j) {
        int k = j - i + 1 - this.boundingBox.getYSize();
        int l;

        if (k > 1) {
            l = i + random.nextInt(k);
        } else {
            l = i;
        }

        int i1 = l - this.boundingBox.minY;

        this.boundingBox.offset(0, i1, 0);
        Iterator iterator = this.components.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            structurepiece.offset(0, i1, 0);
        }

    }

    public boolean isSizeable() { return isSizeableStructure(); } public boolean isSizeableStructure() { // Paper - OBFHELPER
        return true;
    }

    public boolean isValidForPostProcess(ChunkPos chunkcoordintpair) {
        return true;
    }

    public void notifyPostProcessAt(ChunkPos chunkcoordintpair) {}

    public int getChunkPosX() {
        return this.chunkPosX;
    }

    public int getChunkPosZ() {
        return this.chunkPosZ;
    }
}
