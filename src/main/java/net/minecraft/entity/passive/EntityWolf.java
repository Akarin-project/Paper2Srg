package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
// CraftBukkit end

public class EntityWolf extends EntityTameable {

    private static final DataParameter<Float> field_184759_bz = EntityDataManager.func_187226_a(EntityWolf.class, DataSerializers.field_187193_c);
    private static final DataParameter<Boolean> field_184760_bA = EntityDataManager.func_187226_a(EntityWolf.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> field_184758_bB = EntityDataManager.func_187226_a(EntityWolf.class, DataSerializers.field_187192_b);
    private float field_70926_e;
    private float field_70924_f;
    private boolean field_70925_g;
    private boolean field_70928_h;
    private float field_70929_i;
    private float field_70927_j;

    public EntityWolf(World world) {
        super(world);
        this.func_70105_a(0.6F, 0.85F);
        this.func_70903_f(false);
    }

    @Override
    protected void func_184651_r() {
        this.field_70911_d = new EntityAISit(this);
        this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(2, this.field_70911_d);
        this.field_70714_bg.func_75776_a(3, new EntityWolf.a(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
        this.field_70714_bg.func_75776_a(4, new EntityAILeapAtTarget(this, 0.4F));
        this.field_70714_bg.func_75776_a(5, new EntityAIAttackMelee(this, 1.0D, true));
        this.field_70714_bg.func_75776_a(6, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.field_70714_bg.func_75776_a(7, new EntityAIMate(this, 1.0D));
        this.field_70714_bg.func_75776_a(8, new EntityAIWanderAvoidWater(this, 1.0D));
        this.field_70714_bg.func_75776_a(9, new EntityAIBeg(this, 8.0F));
        this.field_70714_bg.func_75776_a(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(10, new EntityAILookIdle(this));
        this.field_70715_bh.func_75776_a(1, new EntityAIOwnerHurtByTarget(this));
        this.field_70715_bh.func_75776_a(2, new EntityAIOwnerHurtTarget(this));
        this.field_70715_bh.func_75776_a(3, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.field_70715_bh.func_75776_a(4, new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate() {
            public boolean a(@Nullable Entity entity) {
                return entity instanceof EntitySheep || entity instanceof EntityRabbit;
            }

            @Override
            public boolean apply(@Nullable Object object) {
                return this.a((Entity) object);
            }
        }));
        this.field_70715_bh.func_75776_a(5, new EntityAINearestAttackableTarget(this, AbstractSkeleton.class, false));
    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.30000001192092896D);
        if (this.func_70909_n()) {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        } else {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(8.0D);
        }

        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e).func_111128_a(2.0D);
    }

    // CraftBukkit - add overriden version
    @Override
    public boolean setGoalTarget(EntityLivingBase entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason reason, boolean fire) {
        if (!super.setGoalTarget(entityliving, reason, fire)) {
            return false;
        }
        entityliving = func_70638_az();
        if (entityliving == null) {
            this.func_70916_h(false);
        } else if (!this.func_70909_n()) {
            this.func_70916_h(true);
        }
        return true;
    }
    // CraftBukkit end

    @Override
    public void func_70624_b(@Nullable EntityLivingBase entityliving) {
        super.func_70624_b(entityliving);
        if (entityliving == null) {
            this.func_70916_h(false);
        } else if (!this.func_70909_n()) {
            this.func_70916_h(true);
        }

    }

    @Override
    protected void func_70619_bc() {
        this.field_70180_af.func_187227_b(EntityWolf.field_184759_bz, Float.valueOf(this.func_110143_aJ()));
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityWolf.field_184759_bz, Float.valueOf(this.func_110143_aJ()));
        this.field_70180_af.func_187214_a(EntityWolf.field_184760_bA, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(EntityWolf.field_184758_bB, Integer.valueOf(EnumDyeColor.RED.func_176767_b()));
    }

    @Override
    protected void func_180429_a(BlockPos blockposition, Block block) {
        this.func_184185_a(SoundEvents.field_187869_gK, 0.15F, 1.0F);
    }

    public static void func_189788_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityWolf.class);
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74757_a("Angry", this.func_70919_bu());
        nbttagcompound.func_74774_a("CollarColor", (byte) this.func_175546_cu().func_176767_b());
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_70916_h(nbttagcompound.func_74767_n("Angry"));
        if (nbttagcompound.func_150297_b("CollarColor", 99)) {
            this.func_175547_a(EnumDyeColor.func_176766_a(nbttagcompound.func_74771_c("CollarColor")));
        }

    }

    @Override
    protected SoundEvent func_184639_G() {
        return this.func_70919_bu() ? SoundEvents.field_187861_gG : (this.field_70146_Z.nextInt(3) == 0 ? (this.func_70909_n() && this.field_70180_af.func_187225_a(EntityWolf.field_184759_bz).floatValue() < 10.0F ? SoundEvents.field_187871_gL : SoundEvents.field_187865_gI) : SoundEvents.field_187857_gE);
    }

    @Override
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187863_gH;
    }

    @Override
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187859_gF;
    }

    @Override
    protected float func_70599_aP() {
        return 0.4F;
    }

    @Override
    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186401_I;
    }

    @Override
    public void func_70636_d() {
        super.func_70636_d();
        if (!this.field_70170_p.field_72995_K && this.field_70925_g && !this.field_70928_h && !this.func_70781_l() && this.field_70122_E) {
            this.field_70928_h = true;
            this.field_70929_i = 0.0F;
            this.field_70927_j = 0.0F;
            this.field_70170_p.func_72960_a(this, (byte) 8);
        }

        if (!this.field_70170_p.field_72995_K && this.func_70638_az() == null && this.func_70919_bu()) {
            this.func_70916_h(false);
        }

    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        this.field_70924_f = this.field_70926_e;
        if (this.func_70922_bv()) {
            this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
        } else {
            this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
        }

        if (this.func_70026_G()) {
            this.field_70925_g = true;
            this.field_70928_h = false;
            this.field_70929_i = 0.0F;
            this.field_70927_j = 0.0F;
        } else if ((this.field_70925_g || this.field_70928_h) && this.field_70928_h) {
            if (this.field_70929_i == 0.0F) {
                this.func_184185_a(SoundEvents.field_187867_gJ, this.func_70599_aP(), (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
            }

            this.field_70927_j = this.field_70929_i;
            this.field_70929_i += 0.05F;
            if (this.field_70927_j >= 2.0F) {
                this.field_70925_g = false;
                this.field_70928_h = false;
                this.field_70927_j = 0.0F;
                this.field_70929_i = 0.0F;
            }

            if (this.field_70929_i > 0.4F) {
                float f = (float) this.func_174813_aQ().field_72338_b;
                int i = (int) (MathHelper.func_76126_a((this.field_70929_i - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * this.field_70130_N * 0.5F;
                    float f2 = (this.field_70146_Z.nextFloat() * 2.0F - 1.0F) * this.field_70130_N * 0.5F;

                    this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_SPLASH, this.field_70165_t + f1, f + 0.8F, this.field_70161_v + f2, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
                }
            }
        }

    }

    @Override
    public float func_70047_e() {
        return this.field_70131_O * 0.8F;
    }

    @Override
    public int func_70646_bf() {
        return this.func_70906_o() ? 20 : super.func_70646_bf();
    }

    @Override
    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else {
            Entity entity = damagesource.func_76346_g();

            if (this.field_70911_d != null) {
                // CraftBukkit - moved into EntityLiving.d(DamageSource, float)
                // this.goalSit.setSitting(false);
            }
            if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
                f = (f + 1.0F) / 2.0F;
            }

            return super.func_70097_a(damagesource, f);
        }
    }

    @Override
    public boolean func_70652_k(Entity entity) {
        boolean flag = entity.func_70097_a(DamageSource.func_76358_a(this), ((int) this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e()));

        if (flag) {
            this.func_174815_a(this, entity);
        }

        return flag;
    }

    @Override
    public void func_70903_f(boolean flag) {
        super.func_70903_f(flag);
        if (flag) {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
        } else {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(8.0D);
        }

        this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111128_a(4.0D);
    }

    @Override
    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (this.func_70909_n()) {
            if (!itemstack.func_190926_b()) {
                if (itemstack.func_77973_b() instanceof ItemFood) {
                    ItemFood itemfood = (ItemFood) itemstack.func_77973_b();

                    if (itemfood.func_77845_h() && this.field_70180_af.func_187225_a(EntityWolf.field_184759_bz).floatValue() < 20.0F) {
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack.func_190918_g(1);
                        }

                        this.heal(itemfood.func_150905_g(itemstack), org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.EATING); // CraftBukkit
                        return true;
                    }
                } else if (itemstack.func_77973_b() == Items.field_151100_aR) {
                    EnumDyeColor enumcolor = EnumDyeColor.func_176766_a(itemstack.func_77960_j());

                    if (enumcolor != this.func_175546_cu()) {
                        this.func_175547_a(enumcolor);
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack.func_190918_g(1);
                        }

                        return true;
                    }
                }
            }

            if (this.func_152114_e(entityhuman) && !this.field_70170_p.field_72995_K && !this.func_70877_b(itemstack)) {
                this.field_70911_d.func_75270_a(!this.func_70906_o());
                this.field_70703_bu = false;
                this.field_70699_by.func_75499_g();
                this.setGoalTarget((EntityLivingBase) null, TargetReason.FORGOT_TARGET, true); // CraftBukkit - reason
            }
        } else if (itemstack.func_77973_b() == Items.field_151103_aS && !this.func_70919_bu()) {
            if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }

            if (!this.field_70170_p.field_72995_K) {
                // CraftBukkit - added event call and isCancelled check.
                if (this.field_70146_Z.nextInt(3) == 0 && !CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    this.func_193101_c(entityhuman);
                    this.field_70699_by.func_75499_g();
                    this.func_70624_b((EntityLivingBase) null);
                    this.field_70911_d.func_75270_a(true);
                    this.func_70606_j(this.func_110138_aP()); // CraftBukkit - 20.0 -> getMaxHealth()
                    this.func_70908_e(true);
                    this.field_70170_p.func_72960_a(this, (byte) 7);
                } else {
                    this.func_70908_e(false);
                    this.field_70170_p.func_72960_a(this, (byte) 6);
                }
            }

            return true;
        }

        return super.func_184645_a(entityhuman, enumhand);
    }

    @Override
    public boolean func_70877_b(ItemStack itemstack) {
        return itemstack.func_77973_b() instanceof ItemFood && ((ItemFood) itemstack.func_77973_b()).func_77845_h();
    }

    @Override
    public int func_70641_bl() {
        return 8;
    }

    public boolean func_70919_bu() {
        return (this.field_70180_af.func_187225_a(EntityWolf.field_184755_bv).byteValue() & 2) != 0;
    }

    public void func_70916_h(boolean flag) {
        byte b0 = this.field_70180_af.func_187225_a(EntityWolf.field_184755_bv).byteValue();

        if (flag) {
            this.field_70180_af.func_187227_b(EntityWolf.field_184755_bv, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.field_70180_af.func_187227_b(EntityWolf.field_184755_bv, Byte.valueOf((byte) (b0 & -3)));
        }

    }

    public EnumDyeColor func_175546_cu() {
        return EnumDyeColor.func_176766_a(this.field_70180_af.func_187225_a(EntityWolf.field_184758_bB).intValue() & 15);
    }

    public void func_175547_a(EnumDyeColor enumcolor) {
        this.field_70180_af.func_187227_b(EntityWolf.field_184758_bB, Integer.valueOf(enumcolor.func_176767_b()));
    }

    @Override
    public EntityWolf func_90011_a(EntityAgeable entityageable) {
        EntityWolf entitywolf = new EntityWolf(this.field_70170_p);
        UUID uuid = this.func_184753_b();

        if (uuid != null) {
            entitywolf.func_184754_b(uuid);
            entitywolf.func_70903_f(true);
        }

        return entitywolf;
    }

    public void func_70918_i(boolean flag) {
        this.field_70180_af.func_187227_b(EntityWolf.field_184760_bA, Boolean.valueOf(flag));
    }

    @Override
    public boolean func_70878_b(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.func_70909_n()) {
            return false;
        } else if (!(entityanimal instanceof EntityWolf)) {
            return false;
        } else {
            EntityWolf entitywolf = (EntityWolf) entityanimal;

            return !entitywolf.func_70909_n() ? false : (entitywolf.func_70906_o() ? false : this.func_70880_s() && entitywolf.func_70880_s());
        }
    }

    public boolean func_70922_bv() {
        return this.field_70180_af.func_187225_a(EntityWolf.field_184760_bA).booleanValue();
    }

    @Override
    public boolean func_142018_a(EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) entityliving;

                if (entitywolf.func_70909_n() && entitywolf.func_70902_q() == entityliving1) {
                    return false;
                }
            }

            return entityliving instanceof EntityPlayer && entityliving1 instanceof EntityPlayer && !((EntityPlayer) entityliving1).func_96122_a((EntityPlayer) entityliving) ? false : !(entityliving instanceof AbstractHorse) || !((AbstractHorse) entityliving).func_110248_bS();
        } else {
            return false;
        }
    }

    @Override
    public boolean func_184652_a(EntityPlayer entityhuman) {
        return !this.func_70919_bu() && super.func_184652_a(entityhuman);
    }

    class a<T extends Entity> extends EntityAIAvoidEntity<T> {

        private final EntityWolf d;

        public a(EntityWolf entitywolf, Class oclass, float f, double d0, double d1) {
            super(entitywolf, oclass, f, d0, d1);
            this.d = entitywolf;
        }

        @Override
        public boolean func_75250_a() {
            return super.func_75250_a() && this.field_75376_d instanceof EntityLlama ? !this.d.func_70909_n() && this.a((EntityLlama) this.field_75376_d) : false;
        }

        private boolean a(EntityLlama entityllama) {
            return entityllama.func_190707_dL() >= EntityWolf.this.field_70146_Z.nextInt(5);
        }

        @Override
        public void func_75249_e() {
            EntityWolf.this.func_70624_b((EntityLivingBase) null);
            super.func_75249_e();
        }

        @Override
        public void func_75246_d() {
            EntityWolf.this.func_70624_b((EntityLivingBase) null);
            super.func_75246_d();
        }
    }
}
