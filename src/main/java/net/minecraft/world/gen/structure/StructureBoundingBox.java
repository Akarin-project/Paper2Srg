package net.minecraft.world.gen.structure;

import com.google.common.base.MoreObjects;

import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;

public class StructureBoundingBox {

    public int minX; // Paper - If changes, verify low/high getters
    public int minY; // Paper - If changes, verify low/high getters
    public int minZ; // Paper - If changes, verify low/high getters
    public int maxX; // Paper - If changes, verify low/high getters
    public int maxY; // Paper - If changes, verify low/high getters
    public int maxZ; // Paper - If changes, verify low/high getters
    public Vec3i getLowPosition() { return new Vec3i(minX, minY, minZ); } // Paper
    public Vec3i getHighPosition() { return new Vec3i(maxX, maxY, maxZ); } // Paper

    public StructureBoundingBox() {}

    public StructureBoundingBox(int[] aint) {
        if (aint.length == 6) {
            this.minX = aint[0];
            this.minY = aint[1];
            this.minZ = aint[2];
            this.maxX = aint[3];
            this.maxY = aint[4];
            this.maxZ = aint[5];
        }

    }

    public static StructureBoundingBox getNewBoundingBox() {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static StructureBoundingBox getComponentToAddBoundingBox(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, EnumFacing enumdirection) {
        switch (enumdirection) {
        case NORTH:
            return new StructureBoundingBox(i + l, j + i1, k - i2 + 1 + j1, i + k1 - 1 + l, j + l1 - 1 + i1, k + j1);

        case SOUTH:
            return new StructureBoundingBox(i + l, j + i1, k + j1, i + k1 - 1 + l, j + l1 - 1 + i1, k + i2 - 1 + j1);

        case WEST:
            return new StructureBoundingBox(i - i2 + 1 + j1, j + i1, k + l, i + j1, j + l1 - 1 + i1, k + k1 - 1 + l);

        case EAST:
            return new StructureBoundingBox(i + j1, j + i1, k + l, i + i2 - 1 + j1, j + l1 - 1 + i1, k + k1 - 1 + l);

        default:
            return new StructureBoundingBox(i + l, j + i1, k + j1, i + k1 - 1 + l, j + l1 - 1 + i1, k + i2 - 1 + j1);
        }
    }

    public static StructureBoundingBox createProper(int i, int j, int k, int l, int i1, int j1) {
        return new StructureBoundingBox(Math.min(i, l), Math.min(j, i1), Math.min(k, j1), Math.max(i, l), Math.max(j, i1), Math.max(k, j1));
    }

    public StructureBoundingBox(StructureBoundingBox structureboundingbox) {
        this.minX = structureboundingbox.minX;
        this.minY = structureboundingbox.minY;
        this.minZ = structureboundingbox.minZ;
        this.maxX = structureboundingbox.maxX;
        this.maxY = structureboundingbox.maxY;
        this.maxZ = structureboundingbox.maxZ;
    }

    public StructureBoundingBox(int i, int j, int k, int l, int i1, int j1) {
        this.minX = i;
        this.minY = j;
        this.minZ = k;
        this.maxX = l;
        this.maxY = i1;
        this.maxZ = j1;
    }

    public StructureBoundingBox(Vec3i baseblockposition, Vec3i baseblockposition1) {
        this.minX = Math.min(baseblockposition.getX(), baseblockposition1.getX());
        this.minY = Math.min(baseblockposition.getY(), baseblockposition1.getY());
        this.minZ = Math.min(baseblockposition.getZ(), baseblockposition1.getZ());
        this.maxX = Math.max(baseblockposition.getX(), baseblockposition1.getX());
        this.maxY = Math.max(baseblockposition.getY(), baseblockposition1.getY());
        this.maxZ = Math.max(baseblockposition.getZ(), baseblockposition1.getZ());
    }

    public StructureBoundingBox(int i, int j, int k, int l) {
        this.minX = i;
        this.minZ = j;
        this.maxX = k;
        this.maxZ = l;
        this.minY = 1;
        this.maxY = 512;
    }

    public boolean intersectsWith(StructureBoundingBox structureboundingbox) {
        return this.maxX >= structureboundingbox.minX && this.minX <= structureboundingbox.maxX && this.maxZ >= structureboundingbox.minZ && this.minZ <= structureboundingbox.maxZ && this.maxY >= structureboundingbox.minY && this.minY <= structureboundingbox.maxY;
    }

    public boolean intersectsWith(int i, int j, int k, int l) {
        return this.maxX >= i && this.minX <= k && this.maxZ >= j && this.minZ <= l;
    }

    public void expandTo(StructureBoundingBox structureboundingbox) {
        this.minX = Math.min(this.minX, structureboundingbox.minX);
        this.minY = Math.min(this.minY, structureboundingbox.minY);
        this.minZ = Math.min(this.minZ, structureboundingbox.minZ);
        this.maxX = Math.max(this.maxX, structureboundingbox.maxX);
        this.maxY = Math.max(this.maxY, structureboundingbox.maxY);
        this.maxZ = Math.max(this.maxZ, structureboundingbox.maxZ);
    }

    public void offset(int i, int j, int k) {
        this.minX += i;
        this.minY += j;
        this.minZ += k;
        this.maxX += i;
        this.maxY += j;
        this.maxZ += k;
    }

    public boolean contains(Vec3i baseblockposition) { return isVecInside(baseblockposition); } // Paper - OBFHELPER
    public boolean isVecInside(Vec3i baseblockposition) {
        return baseblockposition.getX() >= this.minX && baseblockposition.getX() <= this.maxX && baseblockposition.getZ() >= this.minZ && baseblockposition.getZ() <= this.maxZ && baseblockposition.getY() >= this.minY && baseblockposition.getY() <= this.maxY;
    }

    public Vec3i getLength() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }

    public int getXSize() {
        return this.maxX - this.minX + 1;
    }

    public int getYSize() {
        return this.maxY - this.minY + 1;
    }

    public int getZSize() {
        return this.maxZ - this.minZ + 1;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
    }

    public NBTTagIntArray toNBTTagIntArray() {
        return new NBTTagIntArray(new int[] { this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
    }
}
