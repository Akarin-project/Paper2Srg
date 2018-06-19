package net.minecraft.entity.passive;

import java.util.Calendar;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityBat extends EntityAmbientCreature {

    private static final DataParameter<Byte> field_184660_a = EntityDataManager.func_187226_a(EntityBat.class, DataSerializers.field_187191_a);
    private BlockPos field_82237_a;

    public EntityBat(World world) {
        super(world);
        this.func_70105_a(0.5F, 0.9F);
        this.func_82236_f(true);
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityBat.field_184660_a, Byte.valueOf((byte) 0));
    }

    protected float func_70599_aP() {
        return 0.1F;
    }

    protected float func_70647_i() {
        return super.func_70647_i() * 0.95F;
    }

    @Nullable
    public SoundEvent func_184639_G() {
        return this.func_82235_h() && this.field_70146_Z.nextInt(4) != 0 ? null : SoundEvents.field_187740_w;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187743_y;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187742_x;
    }

    public boolean func_70104_M() {
        return false;
    }

    protected void func_82167_n(Entity entity) {}

    protected void func_85033_bc() {}

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(6.0D);
    }

    public boolean func_82235_h() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityBat.field_184660_a)).byteValue() & 1) != 0;
    }

    public void func_82236_f(boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityBat.field_184660_a)).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(EntityBat.field_184660_a, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.field_70180_af.func_187227_b(EntityBat.field_184660_a, Byte.valueOf((byte) (b0 & -2)));
        }

    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.func_82235_h()) {
            this.field_70159_w = 0.0D;
            this.field_70181_x = 0.0D;
            this.field_70179_y = 0.0D;
            this.field_70163_u = (double) MathHelper.func_76128_c(this.field_70163_u) + 1.0D - (double) this.field_70131_O;
        } else {
            this.field_70181_x *= 0.6000000238418579D;
        }

    }

    protected void func_70619_bc() {
        super.func_70619_bc();
        BlockPos blockposition = new BlockPos(this);
        BlockPos blockposition1 = blockposition.func_177984_a();

        if (this.func_82235_h()) {
            if (this.field_70170_p.func_180495_p(blockposition1).func_185915_l()) {
                if (this.field_70146_Z.nextInt(200) == 0) {
                    this.field_70759_as = (float) this.field_70146_Z.nextInt(360);
                }

                if (this.field_70170_p.func_184136_b(this, 4.0D) != null) {
                    this.func_82236_f(false);
                    this.field_70170_p.func_180498_a((EntityPlayer) null, 1025, blockposition, 0);
                }
            } else {
                this.func_82236_f(false);
                this.field_70170_p.func_180498_a((EntityPlayer) null, 1025, blockposition, 0);
            }
        } else {
            if (this.field_82237_a != null && (!this.field_70170_p.func_175623_d(this.field_82237_a) || this.field_82237_a.func_177956_o() < 1)) {
                this.field_82237_a = null;
            }

            if (this.field_82237_a == null || this.field_70146_Z.nextInt(30) == 0 || this.field_82237_a.func_177954_c((double) ((int) this.field_70165_t), (double) ((int) this.field_70163_u), (double) ((int) this.field_70161_v)) < 4.0D) {
                this.field_82237_a = new BlockPos((int) this.field_70165_t + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7), (int) this.field_70163_u + this.field_70146_Z.nextInt(6) - 2, (int) this.field_70161_v + this.field_70146_Z.nextInt(7) - this.field_70146_Z.nextInt(7));
            }

            double d0 = (double) this.field_82237_a.func_177958_n() + 0.5D - this.field_70165_t;
            double d1 = (double) this.field_82237_a.func_177956_o() + 0.1D - this.field_70163_u;
            double d2 = (double) this.field_82237_a.func_177952_p() + 0.5D - this.field_70161_v;

            this.field_70159_w += (Math.signum(d0) * 0.5D - this.field_70159_w) * 0.10000000149011612D;
            this.field_70181_x += (Math.signum(d1) * 0.699999988079071D - this.field_70181_x) * 0.10000000149011612D;
            this.field_70179_y += (Math.signum(d2) * 0.5D - this.field_70179_y) * 0.10000000149011612D;
            float f = (float) (MathHelper.func_181159_b(this.field_70179_y, this.field_70159_w) * 57.2957763671875D) - 90.0F;
            float f1 = MathHelper.func_76142_g(f - this.field_70177_z);

            this.field_191988_bg = 0.5F;
            this.field_70177_z += f1;
            if (this.field_70146_Z.nextInt(100) == 0 && this.field_70170_p.func_180495_p(blockposition1).func_185915_l()) {
                this.func_82236_f(true);
            }
        }

    }

    protected boolean func_70041_e_() {
        return false;
    }

    public void func_180430_e(float f, float f1) {}

    protected void func_184231_a(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {}

    public boolean func_145773_az() {
        return true;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            if (!this.field_70170_p.field_72995_K && this.func_82235_h()) {
                this.func_82236_f(false);
            }

            return super.func_70097_a(damagesource, f);
        }
    }

    public static void func_189754_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityBat.class);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_70180_af.func_187227_b(EntityBat.field_184660_a, Byte.valueOf(nbttagcompound.func_74771_c("BatFlags")));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74774_a("BatFlags", ((Byte) this.field_70180_af.func_187225_a(EntityBat.field_184660_a)).byteValue());
    }

    public boolean func_70601_bi() {
        BlockPos blockposition = new BlockPos(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v);

        if (blockposition.func_177956_o() >= this.field_70170_p.func_181545_F()) {
            return false;
        } else {
            int i = this.field_70170_p.func_175671_l(blockposition);
            byte b0 = 4;

            if (this.func_175569_a(this.field_70170_p.func_83015_S())) {
                b0 = 7;
            } else if (this.field_70146_Z.nextBoolean()) {
                return false;
            }

            return i > this.field_70146_Z.nextInt(b0) ? false : super.func_70601_bi();
        }
    }

    private boolean func_175569_a(Calendar calendar) {
        return calendar.get(2) + 1 == 10 && calendar.get(5) >= 20 || calendar.get(2) + 1 == 11 && calendar.get(5) <= 3;
    }

    public float func_70047_e() {
        return this.field_70131_O / 2.0F;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186377_ab;
    }
}
