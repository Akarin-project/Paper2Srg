package net.minecraft.world.border;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldServer;

public class WorldBorder {

    private final List<IBorderListener> field_177758_a = Lists.newArrayList();
    private double field_177756_b;
    private double field_177757_c;
    private double field_177754_d = 6.0E7D;
    private double field_177755_e;
    private long field_177752_f;
    private long field_177753_g;
    private int field_177762_h;
    private double field_177763_i;
    private double field_177760_j;
    private int field_177761_k;
    private int field_177759_l;
    public WorldServer world; // CraftBukkit

    public WorldBorder() {
        this.field_177755_e = this.field_177754_d;
        this.field_177762_h = 29999984;
        this.field_177763_i = 0.2D;
        this.field_177760_j = 5.0D;
        this.field_177761_k = 15;
        this.field_177759_l = 5;
    }

    public boolean isInBounds(BlockPos blockposition) { return func_177746_a(blockposition); }public boolean func_177746_a(BlockPos blockposition) { // Paper - OBFHELPER
        return (double) (blockposition.func_177958_n() + 1) > this.func_177726_b() && (double) blockposition.func_177958_n() < this.func_177728_d() && (double) (blockposition.func_177952_p() + 1) > this.func_177736_c() && (double) blockposition.func_177952_p() < this.func_177733_e();
    }

    // Paper start
    private final BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
    public boolean isBlockInBounds(int chunkX, int chunkZ) {
        mutPos.setValues(chunkX, 64, chunkZ);
        return isInBounds(mutPos);
    }
    public boolean isChunkInBounds(int chunkX, int chunkZ) {
        mutPos.setValues(((chunkX << 4) + 15), 64, (chunkZ << 4) + 15);
        return isInBounds(mutPos);
    }
    // Paper end

    public boolean func_177730_a(ChunkPos chunkcoordintpair) {
        return (double) chunkcoordintpair.func_180332_e() > this.func_177726_b() && (double) chunkcoordintpair.func_180334_c() < this.func_177728_d() && (double) chunkcoordintpair.func_180330_f() > this.func_177736_c() && (double) chunkcoordintpair.func_180333_d() < this.func_177733_e();
    }

    public boolean func_177743_a(AxisAlignedBB axisalignedbb) {
        return axisalignedbb.field_72336_d > this.func_177726_b() && axisalignedbb.field_72340_a < this.func_177728_d() && axisalignedbb.field_72334_f > this.func_177736_c() && axisalignedbb.field_72339_c < this.func_177733_e();
    }

    public double func_177745_a(Entity entity) {
        return this.func_177729_b(entity.field_70165_t, entity.field_70161_v);
    }

    public double func_177729_b(double d0, double d1) {
        double d2 = d1 - this.func_177736_c();
        double d3 = this.func_177733_e() - d1;
        double d4 = d0 - this.func_177726_b();
        double d5 = this.func_177728_d() - d0;
        double d6 = Math.min(d4, d5);

        d6 = Math.min(d6, d2);
        return Math.min(d6, d3);
    }

    public EnumBorderStatus func_177734_a() {
        return this.field_177755_e < this.field_177754_d ? EnumBorderStatus.SHRINKING : (this.field_177755_e > this.field_177754_d ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
    }

    public double func_177726_b() {
        double d0 = this.func_177731_f() - this.func_177741_h() / 2.0D;

        if (d0 < (double) (-this.field_177762_h)) {
            d0 = (double) (-this.field_177762_h);
        }

        return d0;
    }

    public double func_177736_c() {
        double d0 = this.func_177721_g() - this.func_177741_h() / 2.0D;

        if (d0 < (double) (-this.field_177762_h)) {
            d0 = (double) (-this.field_177762_h);
        }

        return d0;
    }

    public double func_177728_d() {
        double d0 = this.func_177731_f() + this.func_177741_h() / 2.0D;

        if (d0 > (double) this.field_177762_h) {
            d0 = (double) this.field_177762_h;
        }

        return d0;
    }

    public double func_177733_e() {
        double d0 = this.func_177721_g() + this.func_177741_h() / 2.0D;

        if (d0 > (double) this.field_177762_h) {
            d0 = (double) this.field_177762_h;
        }

        return d0;
    }

    public double func_177731_f() {
        return this.field_177756_b;
    }

    public double func_177721_g() {
        return this.field_177757_c;
    }

    public void func_177739_c(double d0, double d1) {
        this.field_177756_b = d0;
        this.field_177757_c = d1;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177693_a(this, d0, d1);
        }

    }

    public double func_177741_h() {
        if (this.func_177734_a() != EnumBorderStatus.STATIONARY) {
            double d0 = (double) ((float) (System.currentTimeMillis() - this.field_177753_g) / (float) (this.field_177752_f - this.field_177753_g));

            if (d0 < 1.0D) {
                return this.field_177754_d + (this.field_177755_e - this.field_177754_d) * d0;
            }

            this.func_177750_a(this.field_177755_e);
        }

        return this.field_177754_d;
    }

    public long func_177732_i() {
        return this.func_177734_a() == EnumBorderStatus.STATIONARY ? 0L : this.field_177752_f - System.currentTimeMillis();
    }

    public double func_177751_j() {
        return this.field_177755_e;
    }

    public void func_177750_a(double d0) {
        this.field_177754_d = d0;
        this.field_177755_e = d0;
        this.field_177752_f = System.currentTimeMillis();
        this.field_177753_g = this.field_177752_f;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177694_a(this, d0);
        }

    }

    public void func_177738_a(double d0, double d1, long i) {
        this.field_177754_d = d0;
        this.field_177755_e = d1;
        this.field_177753_g = System.currentTimeMillis();
        this.field_177752_f = this.field_177753_g + i;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177692_a(this, d0, d1, i);
        }

    }

    protected List<IBorderListener> func_177735_k() {
        return Lists.newArrayList(this.field_177758_a);
    }

    public void func_177737_a(IBorderListener iworldborderlistener) {
        if (field_177758_a.contains(iworldborderlistener)) return; // CraftBukkit
        this.field_177758_a.add(iworldborderlistener);
    }

    public void func_177725_a(int i) {
        this.field_177762_h = i;
    }

    public int func_177722_l() {
        return this.field_177762_h;
    }

    public double func_177742_m() {
        return this.field_177760_j;
    }

    public void func_177724_b(double d0) {
        this.field_177760_j = d0;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177695_c(this, d0);
        }

    }

    public double func_177727_n() {
        return this.field_177763_i;
    }

    public void func_177744_c(double d0) {
        this.field_177763_i = d0;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177696_b(this, d0);
        }

    }

    public int func_177740_p() {
        return this.field_177761_k;
    }

    public void func_177723_b(int i) {
        this.field_177761_k = i;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177691_a(this, i);
        }

    }

    public int func_177748_q() {
        return this.field_177759_l;
    }

    public void func_177747_c(int i) {
        this.field_177759_l = i;
        Iterator iterator = this.func_177735_k().iterator();

        while (iterator.hasNext()) {
            IBorderListener iworldborderlistener = (IBorderListener) iterator.next();

            iworldborderlistener.func_177690_b(this, i);
        }

    }
}
