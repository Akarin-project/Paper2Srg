package net.minecraft.entity.item;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class EntityMinecartTNT extends EntityMinecart {

    private int field_94106_a = -1;

    public EntityMinecartTNT(World world) {
        super(world);
    }

    public EntityMinecartTNT(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189674_a(DataFixer dataconvertermanager) {
        EntityMinecart.func_189669_a(dataconvertermanager, EntityMinecartTNT.class);
    }

    public EntityMinecart.Type func_184264_v() {
        return EntityMinecart.Type.TNT;
    }

    public IBlockState func_180457_u() {
        return Blocks.field_150335_W.func_176223_P();
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_94106_a > 0) {
            --this.field_94106_a;
            this.field_70170_p.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
        } else if (this.field_94106_a == 0) {
            this.func_94103_c(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        }

        if (this.field_70123_F) {
            double d0 = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;

            if (d0 >= 0.009999999776482582D) {
                this.func_94103_c(d0);
            }
        }

    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        Entity entity = damagesource.func_76364_f();

        if (entity instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entity;

            if (entityarrow.func_70027_ad()) {
                this.func_94103_c(entityarrow.field_70159_w * entityarrow.field_70159_w + entityarrow.field_70181_x * entityarrow.field_70181_x + entityarrow.field_70179_y * entityarrow.field_70179_y);
            }
        }

        return super.func_70097_a(damagesource, f);
    }

    public void func_94095_a(DamageSource damagesource) {
        double d0 = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;

        if (!damagesource.func_76347_k() && !damagesource.func_94541_c() && d0 < 0.009999999776482582D) {
            super.func_94095_a(damagesource);
            if (!damagesource.func_94541_c() && this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
                this.func_70099_a(new ItemStack(Blocks.field_150335_W, 1), 0.0F);
            }

        } else {
            if (this.field_94106_a < 0) {
                this.func_94105_c();
                this.field_94106_a = this.field_70146_Z.nextInt(20) + this.field_70146_Z.nextInt(20);
            }

        }
    }

    protected void func_94103_c(double d0) {
        if (!this.field_70170_p.field_72995_K) {
            double d1 = Math.sqrt(d0);

            if (d1 > 5.0D) {
                d1 = 5.0D;
            }

            this.field_70170_p.func_72876_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, (float) (4.0D + this.field_70146_Z.nextDouble() * 1.5D * d1), true);
            this.func_70106_y();
        }

    }

    public void func_180430_e(float f, float f1) {
        if (f >= 3.0F) {
            float f2 = f / 10.0F;

            this.func_94103_c((double) (f2 * f2));
        }

        super.func_180430_e(f, f1);
    }

    public void func_96095_a(int i, int j, int k, boolean flag) {
        if (flag && this.field_94106_a < 0) {
            this.func_94105_c();
        }

    }

    public void func_94105_c() {
        this.field_94106_a = 80;
        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72960_a(this, (byte) 10);
            if (!this.func_174814_R()) {
                this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187904_gd, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

    }

    public boolean func_96096_ay() {
        return this.field_94106_a > -1;
    }

    public float func_180428_a(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.func_96096_ay() && (BlockRailBase.func_176563_d(iblockdata) || BlockRailBase.func_176562_d(world, blockposition.func_177984_a())) ? 0.0F : super.func_180428_a(explosion, world, blockposition, iblockdata);
    }

    public boolean func_174816_a(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata, float f) {
        return this.func_96096_ay() && (BlockRailBase.func_176563_d(iblockdata) || BlockRailBase.func_176562_d(world, blockposition.func_177984_a())) ? false : super.func_174816_a(explosion, world, blockposition, iblockdata, f);
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_150297_b("TNTFuse", 99)) {
            this.field_94106_a = nbttagcompound.func_74762_e("TNTFuse");
        }

    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("TNTFuse", this.field_94106_a);
    }
}
