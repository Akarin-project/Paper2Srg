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

    private int ghostTime;
    private final Vec3d[][] renderLocations;

    public EntityIllusionIllager(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 5;
        this.renderLocations = new Vec3d[2][4];

        for (int i = 0; i < 4; ++i) {
            this.renderLocations[0][i] = new Vec3d(0.0D, 0.0D, 0.0D);
            this.renderLocations[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
        }

    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityIllagerWizard.b());
        this.tasks.addTask(4, new EntityIllagerIllusioner.b(null));
        this.tasks.addTask(5, new EntityIllagerIllusioner.a(null));
        this.tasks.addTask(6, new EntityAIAttackRangedBow(this, 0.5D, 20, 15.0F));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityIllusionIllager.class}));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false)).setUnseenMemoryTicks(300));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(18.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(32.0D);
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, IEntityLivingData groupdataentity) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        return super.onInitialSpawn(difficultydamagescaler, groupdataentity);
    }

    protected void entityInit() {
        super.entityInit();
    }

    protected ResourceLocation getLootTable() {
        return LootTableList.EMPTY;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isRemote && this.isInvisible()) {
            --this.ghostTime;
            if (this.ghostTime < 0) {
                this.ghostTime = 0;
            }

            if (this.hurtTime != 1 && this.ticksExisted % 1200 != 0) {
                if (this.hurtTime == this.maxHurtTime - 1) {
                    this.ghostTime = 3;

                    for (int i = 0; i < 4; ++i) {
                        this.renderLocations[0][i] = this.renderLocations[1][i];
                        this.renderLocations[1][i] = new Vec3d(0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.ghostTime = 3;
                float f = -6.0F;
                boolean flag = true;

                int j;

                for (j = 0; j < 4; ++j) {
                    this.renderLocations[0][j] = this.renderLocations[1][j];
                    this.renderLocations[1][j] = new Vec3d((double) (-6.0F + (float) this.rand.nextInt(13)) * 0.5D, (double) Math.max(0, this.rand.nextInt(6) - 4), (double) (-6.0F + (float) this.rand.nextInt(13)) * 0.5D);
                }

                for (j = 0; j < 16; ++j) {
                    this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, this.getSoundCategory(), 1.0F, 1.0F, false);
            }
        }

    }

    public boolean isOnSameTeam(Entity entity) {
        return super.isOnSameTeam(entity) ? true : (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER ? this.getTeam() == null && entity.getTeam() == null : false);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ILLUSION_ILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_ILLUSION_ILLAGER_HURT;
    }

    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_ILLAGER_CAST_SPELL;
    }

    public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) {
        EntityArrow entityarrow = this.createArrowEntity(f);
        double d0 = entityliving.posX - this.posX;
        double d1 = entityliving.getEntityBoundingBox().minY + (double) (entityliving.height / 3.0F) - entityarrow.posY;
        double d2 = entityliving.posZ - this.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
    }

    protected EntityArrow createArrowEntity(float f) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);

        entitytippedarrow.setEnchantmentEffectsFromEntity((EntityLivingBase) this, f);
        return entitytippedarrow;
    }

    public void setSwingingArms(boolean flag) {
        this.setAggressive(1, flag);
    }

    class a extends EntityIllagerWizard.c {

        private int b;

        private a() {
            super();
        }

        public boolean shouldExecute() {
            return !super.shouldExecute() ? false : (EntityIllusionIllager.this.getAttackTarget() == null ? false : (EntityIllusionIllager.this.getAttackTarget().getEntityId() == this.b ? false : EntityIllusionIllager.this.world.getDifficultyForLocation(new BlockPos(EntityIllusionIllager.this)).isHarderThan((float) EnumDifficulty.NORMAL.ordinal())));
        }

        public void startExecuting() {
            super.startExecuting();
            this.b = EntityIllusionIllager.this.getAttackTarget().getEntityId();
        }

        protected int f() {
            return 20;
        }

        protected int i() {
            return 180;
        }

        protected void j() {
            EntityIllusionIllager.this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
        }

        protected SoundEvent k() {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_BLINDNESS;
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

        public boolean shouldExecute() {
            return !super.shouldExecute() ? false : !EntityIllusionIllager.this.isPotionActive(MobEffects.INVISIBILITY);
        }

        protected int f() {
            return 20;
        }

        protected int i() {
            return 340;
        }

        protected void j() {
            EntityIllusionIllager.this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 1200));
        }

        @Nullable
        protected SoundEvent k() {
            return SoundEvents.ENTITY_ILLAGER_PREPARE_MIRROR;
        }

        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.DISAPPEAR;
        }

        b(Object object) {
            this();
        }
    }
}
