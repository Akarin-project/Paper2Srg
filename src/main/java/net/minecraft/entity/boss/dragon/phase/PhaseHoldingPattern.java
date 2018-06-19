package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseHoldingPattern extends PhaseBase {

    private Path field_188677_b;
    private Vec3d field_188678_c;
    private boolean field_188679_d;

    public PhaseHoldingPattern(EntityDragon entityenderdragon) {
        super(entityenderdragon);
    }

    public PhaseList<PhaseHoldingPattern> func_188652_i() {
        return PhaseList.field_188741_a;
    }

    public void func_188659_c() {
        double d0 = this.field_188678_c == null ? 0.0D : this.field_188678_c.func_186679_c(this.field_188661_a.field_70165_t, this.field_188661_a.field_70163_u, this.field_188661_a.field_70161_v);

        if (d0 < 100.0D || d0 > 22500.0D || this.field_188661_a.field_70123_F || this.field_188661_a.field_70124_G) {
            this.func_188675_j();
        }

    }

    public void func_188660_d() {
        this.field_188677_b = null;
        this.field_188678_c = null;
    }

    @Nullable
    public Vec3d func_188650_g() {
        return this.field_188678_c;
    }

    private void func_188675_j() {
        int i;

        if (this.field_188677_b != null && this.field_188677_b.func_75879_b()) {
            BlockPos blockposition = this.field_188661_a.field_70170_p.func_175672_r(new BlockPos(WorldGenEndPodium.field_186139_a));

            i = this.field_188661_a.func_184664_cU() == null ? 0 : this.field_188661_a.func_184664_cU().func_186092_c();
            if (this.field_188661_a.func_70681_au().nextInt(i + 3) == 0) {
                this.field_188661_a.func_184670_cT().func_188758_a(PhaseList.field_188743_c);
                return;
            }

            double d0 = 64.0D;
            EntityPlayer entityhuman = this.field_188661_a.field_70170_p.func_184139_a(blockposition, d0, d0);

            if (entityhuman != null) {
                d0 = entityhuman.func_174831_c(blockposition) / 512.0D;
            }

            if (entityhuman != null && (this.field_188661_a.func_70681_au().nextInt(MathHelper.func_76130_a((int) d0) + 2) == 0 || this.field_188661_a.func_70681_au().nextInt(i + 2) == 0)) {
                this.func_188674_a(entityhuman);
                return;
            }
        }

        if (this.field_188677_b == null || this.field_188677_b.func_75879_b()) {
            int j = this.field_188661_a.func_184671_o();

            i = j;
            if (this.field_188661_a.func_70681_au().nextInt(8) == 0) {
                this.field_188679_d = !this.field_188679_d;
                i = j + 6;
            }

            if (this.field_188679_d) {
                ++i;
            } else {
                --i;
            }

            if (this.field_188661_a.func_184664_cU() != null && this.field_188661_a.func_184664_cU().func_186092_c() >= 0) {
                i %= 12;
                if (i < 0) {
                    i += 12;
                }
            } else {
                i -= 12;
                i &= 7;
                i += 12;
            }

            this.field_188677_b = this.field_188661_a.func_184666_a(j, i, (PathPoint) null);
            if (this.field_188677_b != null) {
                this.field_188677_b.func_75875_a();
            }
        }

        this.func_188676_k();
    }

    private void func_188674_a(EntityPlayer entityhuman) {
        this.field_188661_a.func_184670_cT().func_188758_a(PhaseList.field_188742_b);
        ((PhaseStrafePlayer) this.field_188661_a.func_184670_cT().func_188757_b(PhaseList.field_188742_b)).func_188686_a(entityhuman);
    }

    private void func_188676_k() {
        if (this.field_188677_b != null && !this.field_188677_b.func_75879_b()) {
            Vec3d vec3d = this.field_188677_b.func_186310_f();

            this.field_188677_b.func_75875_a();
            double d0 = vec3d.field_72450_a;
            double d1 = vec3d.field_72449_c;

            double d2;

            do {
                d2 = vec3d.field_72448_b + (double) (this.field_188661_a.func_70681_au().nextFloat() * 20.0F);
            } while (d2 < vec3d.field_72448_b);

            this.field_188678_c = new Vec3d(d0, d2, d1);
        }

    }

    public void func_188655_a(EntityEnderCrystal entityendercrystal, BlockPos blockposition, DamageSource damagesource, @Nullable EntityPlayer entityhuman) {
        if (entityhuman != null && !entityhuman.field_71075_bZ.field_75102_a) {
            this.func_188674_a(entityhuman);
        }

    }
}
