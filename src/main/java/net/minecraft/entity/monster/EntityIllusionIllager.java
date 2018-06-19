package net.minecraft.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.EntityIllagerIllusioner.a;
import net.minecraft.server.EntityIllagerIllusioner.b;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityIllusionIllager extends EntitySpellcasterIllager implements IRangedAttackMob {

    private int field_193099_c;
    private final Vec3d[][] field_193100_bx;

    public EntityIllusionIllager(World world) {
        super(world);
        this.func_70105_a(0.6F, 1.95F);
        this.field_70728_aV = 5;
        this.field_193100_bx = new Vec3d[2][4];

        for (int i = 0; i < 4; ++i) {
            this.field_193100_bx[0][i] = new Vec3d(0.0D, 0.0D, 0.0D);
            this.field_193100_bx[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
        }

    }

    protected void func_184651_r() {
        super.func_184651_r();
        this.field_70714_bg.func_75776_a(0, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityIllagerWizard.b());
        this.field_70714_bg.func_75776_a(4, new EntityIllagerIllusioner.b(null));
        this.field_70714_bg.func_75776_a(5, new EntityIllagerIllusioner.a(null));
        this.field_70714_bg.func_75776_a(6, new EntityAIAttackRangedBow(this, 0.5D, 20, 15.0F));
        this.field_70714_bg.func_75776_a(8, new EntityAIWander(this, 0.6D));
        this.field_70714_bg.func_75776_a(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.field_70714_bg.func_75776_a(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityIllusionIllager.class}));
        this.field_70715_bh.func_75776_a(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).func_190882_b(300));
        this.field_70715_bh.func_75776_a(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).func_190882_b(300));
        this.field_70715_bh.func_75776_a(3, (new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false)).func_190882_b(300));
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.5D);
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(18.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(32.0D);
    }

    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, IEntityLivingData groupdataentity) {
        this.func_184201_a(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.field_151031_f));
        return super.func_180482_a(difficultydamagescaler, groupdataentity);
    }

    protected void func_70088_a() {
        super.func_70088_a();
    }

    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186419_a;
    }

    public void func_70636_d() {
        super.func_70636_d();
        if (this.field_70170_p.field_72995_K && this.func_82150_aj()) {
            --this.field_193099_c;
            if (this.field_193099_c < 0) {
                this.field_193099_c = 0;
            }

            if (this.field_70737_aN != 1 && this.field_70173_aa % 1200 != 0) {
                if (this.field_70737_aN == this.field_70738_aO - 1) {
                    this.field_193099_c = 3;

                    for (int i = 0; i < 4; ++i) {
                        this.field_193100_bx[0][i] = this.field_193100_bx[1][i];
                        this.field_193100_bx[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.field_193099_c = 3;
                float f = -6.0F;
                boolean flag = true;

                int j;

                for (j = 0; j < 4; ++j) {
                    this.field_193100_bx[0][j] = this.field_193100_bx[1][j];
                    this.field_193100_bx[1][j] = new Vec3d((double) (-6.0F + (float) this.field_70146_Z.nextInt(13)) * 0.5D, (double) Math.max(0, this.field_70146_Z.nextInt(6) - 4), (double) (-6.0F + (float) this.field_70146_Z.nextInt(13)) * 0.5D);
                }

                for (j = 0; j < 16; ++j) {
                    this.field_70170_p.func_175688_a(EnumParticleTypes.CLOUD, this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, this.field_70163_u + this.field_70146_Z.nextDouble() * (double) this.field_70131_O, this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                this.field_70170_p.func_184134_a(this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_193788_dg, this.func_184176_by(), 1.0F, 1.0F, false);
            }
        }

    }

    public boolean func_184191_r(Entity entity) {
        return super.func_184191_r(entity) ? true : (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).func_70668_bt() == EnumCreatureAttribute.ILLAGER ? this.func_96124_cp() == null && entity.func_96124_cp() == null : false);
    }

    protected SoundEvent func_184639_G() {
        return SoundEvents.field_193783_dc;
    }

    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_193786_de;
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_193787_df;
    }

    protected SoundEvent func_193086_dk() {
        return SoundEvents.field_193784_dd;
    }

    public void func_82196_d(EntityLivingBase entityliving, float f) {
        EntityArrow entityarrow = this.func_193097_t(f);
        double d0 = entityliving.field_70165_t - this.field_70165_t;
        double d1 = entityliving.func_174813_aQ().field_72338_b + (double) (entityliving.field_70131_O / 3.0F) - entityarrow.field_70163_u;
        double d2 = entityliving.field_70161_v - this.field_70161_v;
        double d3 = (double) MathHelper.func_76133_a(d0 * d0 + d2 * d2);

        entityarrow.func_70186_c(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.field_70170_p.func_175659_aa().func_151525_a() * 4));
        this.func_184185_a(SoundEvents.field_187866_fi, 1.0F, 1.0F / (this.func_70681_au().nextFloat() * 0.4F + 0.8F));
        this.field_70170_p.func_72838_d(entityarrow);
    }

    protected EntityArrow func_193097_t(float f) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.field_70170_p, this);

        entitytippedarrow.func_190547_a((EntityLivingBase) this, f);
        return entitytippedarrow;
    }

    public void func_184724_a(boolean flag) {
        this.func_193079_a(1, flag);
    }

    class a extends EntityIllagerWizard.c {

        private int b;

        private a() {
            super();
        }

        public boolean func_75250_a() {
            return !super.func_75250_a() ? false : (EntityIllusionIllager.this.func_70638_az() == null ? false : (EntityIllusionIllager.this.func_70638_az().func_145782_y() == this.b ? false : EntityIllusionIllager.this.field_70170_p.func_175649_E(new BlockPos(EntityIllusionIllager.this)).func_193845_a((float) EnumDifficulty.NORMAL.ordinal())));
        }

        public void func_75249_e() {
            super.func_75249_e();
            this.b = EntityIllusionIllager.this.func_70638_az().func_145782_y();
        }

        protected int f() {
            return 20;
        }

        protected int i() {
            return 180;
        }

        protected void j() {
            EntityIllusionIllager.this.func_70638_az().func_70690_d(new PotionEffect(MobEffects.field_76440_q, 400));
        }

        protected SoundEvent k() {
            return SoundEvents.field_193789_dh;
        }

        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.BLINDNESS;
        }

        a(Object object) {
            this();
        }
    }

    class b extends EntityIllagerWizard.c {

        private b() {
            super();
        }

        public boolean func_75250_a() {
            return !super.func_75250_a() ? false : !EntityIllusionIllager.this.func_70644_a(MobEffects.field_76441_p);
        }

        protected int f() {
            return 20;
        }

        protected int i() {
            return 340;
        }

        protected void j() {
            EntityIllusionIllager.this.func_70690_d(new PotionEffect(MobEffects.field_76441_p, 1200));
        }

        @Nullable
        protected SoundEvent k() {
            return SoundEvents.field_193790_di;
        }

        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.DISAPPEAR;
        }

        b(Object object) {
            this();
        }
    }
}
