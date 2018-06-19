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

    private Mirror field_186228_a;
    private Rotation field_186229_b;
    private boolean field_186230_c;
    @Nullable
    private Block field_186231_d;
    @Nullable
    private ChunkPos field_186232_e;
    @Nullable
    private StructureBoundingBox field_186233_f;
    private boolean field_186234_g;
    private float field_189951_h;
    @Nullable
    private Random field_189952_i;
    @Nullable
    private Long field_189953_j;

    public PlacementSettings() {
        this.field_186228_a = Mirror.NONE;
        this.field_186229_b = Rotation.NONE;
        this.field_186234_g = true;
        this.field_189951_h = 1.0F;
    }

    public PlacementSettings func_186217_a() {
        PlacementSettings definedstructureinfo = new PlacementSettings();

        definedstructureinfo.field_186228_a = this.field_186228_a;
        definedstructureinfo.field_186229_b = this.field_186229_b;
        definedstructureinfo.field_186230_c = this.field_186230_c;
        definedstructureinfo.field_186231_d = this.field_186231_d;
        definedstructureinfo.field_186232_e = this.field_186232_e;
        definedstructureinfo.field_186233_f = this.field_186233_f;
        definedstructureinfo.field_186234_g = this.field_186234_g;
        definedstructureinfo.field_189951_h = this.field_189951_h;
        definedstructureinfo.field_189952_i = this.field_189952_i;
        definedstructureinfo.field_189953_j = this.field_189953_j;
        return definedstructureinfo;
    }

    public PlacementSettings func_186214_a(Mirror enumblockmirror) {
        this.field_186228_a = enumblockmirror;
        return this;
    }

    public PlacementSettings func_186220_a(Rotation enumblockrotation) {
        this.field_186229_b = enumblockrotation;
        return this;
    }

    public PlacementSettings func_186222_a(boolean flag) {
        this.field_186230_c = flag;
        return this;
    }

    public PlacementSettings func_186225_a(Block block) {
        this.field_186231_d = block;
        return this;
    }

    public PlacementSettings func_186218_a(ChunkPos chunkcoordintpair) {
        this.field_186232_e = chunkcoordintpair;
        return this;
    }

    public PlacementSettings func_186223_a(StructureBoundingBox structureboundingbox) {
        this.field_186233_f = structureboundingbox;
        return this;
    }

    public PlacementSettings func_189949_a(@Nullable Long olong) {
        this.field_189953_j = olong;
        return this;
    }

    public PlacementSettings func_189950_a(@Nullable Random random) {
        this.field_189952_i = random;
        return this;
    }

    public PlacementSettings func_189946_a(float f) {
        this.field_189951_h = f;
        return this;
    }

    public Mirror func_186212_b() {
        return this.field_186228_a;
    }

    public PlacementSettings func_186226_b(boolean flag) {
        this.field_186234_g = flag;
        return this;
    }

    public Rotation func_186215_c() {
        return this.field_186229_b;
    }

    public Random func_189947_a(@Nullable BlockPos blockposition) {
        if (this.field_189952_i != null) {
            return this.field_189952_i;
        } else if (this.field_189953_j != null) {
            return this.field_189953_j.longValue() == 0L ? new Random(System.currentTimeMillis()) : new Random(this.field_189953_j.longValue());
        } else if (blockposition == null) {
            return new Random(System.currentTimeMillis());
        } else {
            int i = blockposition.func_177958_n();
            int j = blockposition.func_177952_p();

            return new Random((long) (i * i * 4987142 + i * 5947611) + (long) (j * j) * 4392871L + (long) (j * 389711) ^ 987234911L);
        }
    }

    public float func_189948_f() {
        return this.field_189951_h;
    }

    public boolean func_186221_e() {
        return this.field_186230_c;
    }

    @Nullable
    public Block func_186219_f() {
        return this.field_186231_d;
    }

    @Nullable
    public StructureBoundingBox func_186213_g() {
        if (this.field_186233_f == null && this.field_186232_e != null) {
            this.func_186224_i();
        }

        return this.field_186233_f;
    }

    public boolean func_186227_h() {
        return this.field_186234_g;
    }

    void func_186224_i() {
        this.field_186233_f = this.func_186216_b(this.field_186232_e);
    }

    @Nullable
    private StructureBoundingBox func_186216_b(@Nullable ChunkPos chunkcoordintpair) {
        if (chunkcoordintpair == null) {
            return null;
        } else {
            int i = chunkcoordintpair.field_77276_a * 16;
            int j = chunkcoordintpair.field_77275_b * 16;

            return new StructureBoundingBox(i, 0, j, i + 16 - 1, 255, j + 16 - 1);
        }
    }
}
