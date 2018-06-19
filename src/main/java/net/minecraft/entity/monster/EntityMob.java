package net.minecraft.entity.monster;


import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityCombustByEntityEvent;

public abstract class EntityMob extends EntityCreature implements IMob {

    public EntityMob(World world) {
        super(world);
        this.field_70728_aV = 5;
    }

    public SoundCategory func_184176_by() {
        return SoundCategory.HOSTILE;
    }

    public void func_70636_d() {
        this.func_82168_bl();
        float f = this.func_70013_c();

        if (f > 0.5F) {
            this.field_70708_bq += 2;
        }

        super.func_70636_d();
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K && this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
            this.func_70106_y();
        }

    }

    protected SoundEvent func_184184_Z() {
        return SoundEvents.field_187593_cC;
    }

    protected SoundEvent func_184181_aa() {
        return SoundEvents.field_187591_cB;
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        return this.func_180431_b(damagesource) ? false : super.func_70097_a(damagesource, f);
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187741_cz;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187738_cy;
    }

    protected SoundEvent func_184588_d(int i) {
        return i > 4 ? SoundEvents.field_187735_cx : SoundEvents.field_187589_cA;
    }

    public boolean func_70652_k(Entity entity) {
        float f = (float) this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
        int i = 0;

        if (entity instanceof EntityLivingBase) {
            f += EnchantmentHelper.func_152377_a(this.func_184614_ca(), ((EntityLivingBase) entity).func_70668_bt());
            i += EnchantmentHelper.func_77501_a((EntityLivingBase) this);
        }

        boolean flag = entity.func_70097_a(DamageSource.func_76358_a(this), f);

        if (flag) {
            if (i > 0 && entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).func_70653_a(this, (float) i * 0.5F, (double) MathHelper.func_76126_a(this.field_70177_z * 0.017453292F), (double) (-MathHelper.func_76134_b(this.field_70177_z * 0.017453292F)));
                this.field_70159_w *= 0.6D;
                this.field_70179_y *= 0.6D;
            }

            int j = EnchantmentHelper.func_90036_a(this);

            if (j > 0) {
                // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                if (!combustEvent.isCancelled()) {
                    entity.func_70015_d(combustEvent.getDuration());
                }
                // CraftBukkit end
            }

            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;
                ItemStack itemstack = this.func_184614_ca();
                ItemStack itemstack1 = entityhuman.func_184587_cr() ? entityhuman.func_184607_cu() : ItemStack.field_190927_a;

                if (!itemstack.func_190926_b() && !itemstack1.func_190926_b() && itemstack.func_77973_b() instanceof ItemAxe && itemstack1.func_77973_b() == Items.field_185159_cQ) {
                    float f1 = 0.25F + (float) EnchantmentHelper.func_185293_e(this) * 0.05F;

                    if (this.field_70146_Z.nextFloat() < f1) {
                        entityhuman.func_184811_cZ().func_185145_a(Items.field_185159_cQ, 100);
                        this.field_70170_p.func_72960_a(entityhuman, (byte) 30);
                    }
                }
            }

            this.func_174815_a((EntityLivingBase) this, entity);
        }

        return flag;
    }

    public float func_180484_a(BlockPos blockposition) {
        return 0.5F - this.field_70170_p.func_175724_o(blockposition);
    }

    protected boolean func_70814_o() {
        BlockPos blockposition = new BlockPos(this.field_70165_t, this.func_174813_aQ().field_72338_b, this.field_70161_v);

        if (this.field_70170_p.func_175642_b(EnumSkyBlock.SKY, blockposition) > this.field_70146_Z.nextInt(32)) {
            return false;
        } else {
            //int i = this.world.getLightLevel(blockposition); // Paper
            boolean passes; // Paper
            if (this.field_70170_p.func_72911_I()) {
                int j = this.field_70170_p.func_175657_ab();

                this.field_70170_p.func_175692_b(10);
                passes = !field_70170_p.isLightLevel(blockposition, this.field_70146_Z.nextInt(9)); // Paper
                this.field_70170_p.func_175692_b(j);
            } else { passes = !field_70170_p.isLightLevel(blockposition, this.field_70146_Z.nextInt(9)); } // Paper

            return passes; // Paper
        }
    }

    public boolean func_70601_bi() {
        return this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL && this.func_70814_o() && super.func_70601_bi();
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e);
    }

    protected boolean func_146066_aG() {
        return true;
    }

    public boolean func_191990_c(EntityPlayer entityhuman) {
        return true;
    }
}
