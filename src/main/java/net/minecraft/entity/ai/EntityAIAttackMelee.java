package net.minecraft.entity.ai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntityAIAttackMelee extends EntityAIBase {

    World field_75443_a;
    protected EntityCreature field_75441_b;
    protected int field_75439_d;
    double field_75440_e;
    boolean field_75437_f;
    Path field_75438_g;
    private int field_75445_i;
    private double field_151497_i;
    private double field_151495_j;
    private double field_151496_k;
    protected final int field_188493_g = 20;

    public EntityAIAttackMelee(EntityCreature entitycreature, double d0, boolean flag) {
        this.field_75441_b = entitycreature;
        this.field_75443_a = entitycreature.field_70170_p;
        this.field_75440_e = d0;
        this.field_75437_f = flag;
        this.func_75248_a(3);
    }

    public boolean func_75250_a() {
        EntityLivingBase entityliving = this.field_75441_b.func_70638_az();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.func_70089_S()) {
            return false;
        } else {
            this.field_75438_g = this.field_75441_b.func_70661_as().func_75494_a((Entity) entityliving);
            return this.field_75438_g != null ? true : this.func_179512_a(entityliving) >= this.field_75441_b.func_70092_e(entityliving.field_70165_t, entityliving.func_174813_aQ().field_72338_b, entityliving.field_70161_v);
        }
    }

    public boolean func_75253_b() {
        EntityLivingBase entityliving = this.field_75441_b.func_70638_az();

        return entityliving == null ? false : (!entityliving.func_70089_S() ? false : (!this.field_75437_f ? !this.field_75441_b.func_70661_as().func_75500_f() : (!this.field_75441_b.func_180485_d(new BlockPos(entityliving)) ? false : !(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).func_175149_v() && !((EntityPlayer) entityliving).func_184812_l_())));
    }

    public void func_75249_e() {
        this.field_75441_b.func_70661_as().func_75484_a(this.field_75438_g, this.field_75440_e);
        this.field_75445_i = 0;
    }

    public void func_75251_c() {
        EntityLivingBase entityliving = this.field_75441_b.func_70638_az();

        if (entityliving instanceof EntityPlayer && (((EntityPlayer) entityliving).func_175149_v() || ((EntityPlayer) entityliving).func_184812_l_())) {
            this.field_75441_b.func_70624_b((EntityLivingBase) null);
        }

        this.field_75441_b.func_70661_as().func_75499_g();
    }

    public void func_75246_d() {
        EntityLivingBase entityliving = this.field_75441_b.func_70638_az();

        this.field_75441_b.func_70671_ap().func_75651_a(entityliving, 30.0F, 30.0F);
        double d0 = this.field_75441_b.func_70092_e(entityliving.field_70165_t, entityliving.func_174813_aQ().field_72338_b, entityliving.field_70161_v);

        --this.field_75445_i;
        if ((this.field_75437_f || this.field_75441_b.func_70635_at().func_75522_a(entityliving)) && this.field_75445_i <= 0 && (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D || entityliving.func_70092_e(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0D || this.field_75441_b.func_70681_au().nextFloat() < 0.05F)) {
            this.field_151497_i = entityliving.field_70165_t;
            this.field_151495_j = entityliving.func_174813_aQ().field_72338_b;
            this.field_151496_k = entityliving.field_70161_v;
            this.field_75445_i = 4 + this.field_75441_b.func_70681_au().nextInt(7);
            if (d0 > 1024.0D) {
                this.field_75445_i += 10;
            } else if (d0 > 256.0D) {
                this.field_75445_i += 5;
            }

            if (!this.field_75441_b.func_70661_as().func_75497_a((Entity) entityliving, this.field_75440_e)) {
                this.field_75445_i += 15;
            }
        }

        this.field_75439_d = Math.max(this.field_75439_d - 1, 0);
        this.func_190102_a(entityliving, d0);
    }

    protected void func_190102_a(EntityLivingBase entityliving, double d0) {
        double d1 = this.func_179512_a(entityliving);

        if (d0 <= d1 && this.field_75439_d <= 0) {
            this.field_75439_d = 20;
            this.field_75441_b.func_184609_a(EnumHand.MAIN_HAND);
            this.field_75441_b.func_70652_k(entityliving);
        }

    }

    protected double func_179512_a(EntityLivingBase entityliving) {
        return (double) (this.field_75441_b.field_70130_N * 2.0F * this.field_75441_b.field_70130_N * 2.0F + entityliving.field_70130_N);
    }
}
