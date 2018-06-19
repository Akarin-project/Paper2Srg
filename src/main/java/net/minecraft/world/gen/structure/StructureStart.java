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

    protected List<StructureComponent> field_75075_a = Lists.newLinkedList();
    protected StructureBoundingBox field_75074_b;
    private int field_143024_c;
    private int field_143023_d;

    public StructureStart() {}

    public StructureStart(int i, int j) {
        this.field_143024_c = i;
        this.field_143023_d = j;
    }

    public StructureBoundingBox getBoundingBox() { return func_75071_a(); } // Paper - OBFHELPER
    public StructureBoundingBox func_75071_a() {
        return this.field_75074_b;
    }

    public List<StructureComponent> getStructurePieces() { return func_186161_c(); } // Paper - OBFHELPER
    public List<StructureComponent> func_186161_c() {
        return this.field_75075_a;
    }

    public void func_75068_a(World world, Random random, StructureBoundingBox structureboundingbox) {
        Iterator iterator = this.field_75075_a.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            if (structurepiece.func_74874_b().func_78884_a(structureboundingbox) && !structurepiece.func_74875_a(world, random, structureboundingbox)) {
                iterator.remove();
            }
        }

    }

    protected void func_75072_c() {
        this.field_75074_b = StructureBoundingBox.func_78887_a();
        Iterator iterator = this.field_75075_a.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            this.field_75074_b.func_78888_b(structurepiece.func_74874_b());
        }

    }

    public NBTTagCompound func_143021_a(int i, int j) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74778_a("id", MapGenStructureIO.func_143033_a(this));
        nbttagcompound.func_74768_a("ChunkX", i);
        nbttagcompound.func_74768_a("ChunkZ", j);
        nbttagcompound.func_74782_a("BB", this.field_75074_b.func_151535_h());
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.field_75075_a.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            nbttaglist.func_74742_a(structurepiece.func_143010_b());
        }

        nbttagcompound.func_74782_a("Children", nbttaglist);
        this.func_143022_a(nbttagcompound);
        return nbttagcompound;
    }

    public void func_143022_a(NBTTagCompound nbttagcompound) {}

    public void func_143020_a(World world, NBTTagCompound nbttagcompound) {
        this.field_143024_c = nbttagcompound.func_74762_e("ChunkX");
        this.field_143023_d = nbttagcompound.func_74762_e("ChunkZ");
        if (nbttagcompound.func_74764_b("BB")) {
            this.field_75074_b = new StructureBoundingBox(nbttagcompound.func_74759_k("BB"));
        }

        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Children", 10);

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            this.field_75075_a.add(MapGenStructureIO.func_143032_b(nbttaglist.func_150305_b(i), world));
        }

        this.func_143017_b(nbttagcompound);
    }

    public void func_143017_b(NBTTagCompound nbttagcompound) {}

    protected void func_75067_a(World world, Random random, int i) {
        int j = world.func_181545_F() - i;
        int k = this.field_75074_b.func_78882_c() + 1;

        if (k < j) {
            k += random.nextInt(j - k);
        }

        int l = k - this.field_75074_b.field_78894_e;

        this.field_75074_b.func_78886_a(0, l, 0);
        Iterator iterator = this.field_75075_a.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            structurepiece.func_181138_a(0, l, 0);
        }

    }

    protected void func_75070_a(World world, Random random, int i, int j) {
        int k = j - i + 1 - this.field_75074_b.func_78882_c();
        int l;

        if (k > 1) {
            l = i + random.nextInt(k);
        } else {
            l = i;
        }

        int i1 = l - this.field_75074_b.field_78895_b;

        this.field_75074_b.func_78886_a(0, i1, 0);
        Iterator iterator = this.field_75075_a.iterator();

        while (iterator.hasNext()) {
            StructureComponent structurepiece = (StructureComponent) iterator.next();

            structurepiece.func_181138_a(0, i1, 0);
        }

    }

    public boolean isSizeable() { return func_75069_d(); } public boolean func_75069_d() { // Paper - OBFHELPER
        return true;
    }

    public boolean func_175788_a(ChunkPos chunkcoordintpair) {
        return true;
    }

    public void func_175787_b(ChunkPos chunkcoordintpair) {}

    public int func_143019_e() {
        return this.field_143024_c;
    }

    public int func_143018_f() {
        return this.field_143023_d;
    }
}
