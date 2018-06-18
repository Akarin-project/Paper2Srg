package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.EntityEvoker.a;
import net.minecraft.server.EntityEvoker.b;
import net.minecraft.server.EntityEvoker.c;
import net.minecraft.server.EntityEvoker.d;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityEvoker extends EntitySpellcasterIllager {

    private EntitySheep wololoTarget;

    public EntityEvoker(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 10;
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityEvoker.b(null));
        this.tasks.addTask(2, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 1.0D));
        this.tasks.addTask(4, new EntityEvoker.c(null));
        this.tasks.addTask(5, new EntityEvoker.a(null));
        this.tasks.addTask(6, new EntityEvoker.d());
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityEvoker.class}));
        this.targetTasks.addTask(2, (new EntityAINearestAttackableTarget(this, EntityPlayer.class, true)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, (new EntityAINearestAttackableTarget(this, EntityVillager.class, false)).setUnseenMemoryTicks(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
    }

    protected void entityInit() {
        super.entityInit();
    }

    public static void registerFixesEvoker(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityEvoker.class);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_EVOCATION_ILLAGER;
    }

    protected void updateAITasks() {
        super.updateAITasks();
    }

    public void onUpdate() {
        super.onUpdate();
    }

    public boolean isOnSameTeam(Entity entity) {
        return entity == null ? false : (entity == this ? true : (super.isOnSameTeam(entity) ? true : (entity instanceof EntityVex ? this.isOnSameTeam(((EntityVex) entity).getOwner()) : (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER ? this.getTeam() == null && entity.getTeam() == null : false))));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOCATION_ILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_EVOCATION_ILLAGER_HURT;
    }

    private void setWololoTarget(@Nullable EntitySheep entitysheep) {
        this.wololoTarget = entitysheep;
    }

    @Nullable
    private EntitySheep getWololoTarget() {
        return this.wololoTarget;
    }

    protected SoundEvent getSpellSound() {
        return SoundEvents.EVOCATION_ILLAGER_CAST_SPELL;
    }

    public class d extends EntityIllagerWizard.c {

        final Predicate<EntitySheep> a = new Predicate() {
            public boolean a(EntitySheep entitysheep) {
                return entitysheep.getFleeceColor() == EnumDyeColor.BLUE;
            }

            public boolean apply(Object object) {
                return this.a((EntitySheep) object);
            }
        };

        public d() {
            super();
        }

        public boolean shouldExecute() {
            if (EntityEvoker.this.getAttackTarget() != null) {
                return false;
            } else if (EntityEvoker.this.isSpellcasting()) {
                return false;
            } else if (EntityEvoker.this.ticksExisted < this.d) {
                return false;
            } else if (!EntityEvoker.this.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            } else {
                List list = EntityEvoker.this.world.getEntitiesWithinAABB(EntitySheep.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0D, 4.0D, 16.0D), this.a);

                if (list.isEmpty()) {
                    return false;
                } else {
                    EntityEvoker.this.setWololoTarget((EntitySheep) list.get(EntityEvoker.this.rand.nextInt(list.size())));
                    return true;
                }
            }
        }

        public boolean shouldContinueExecuting() {
            return EntityEvoker.this.getWololoTarget() != null && this.c > 0;
        }

        public void resetTask() {
            super.resetTask();
            EntityEvoker.this.setWololoTarget((EntitySheep) null);
        }

        protected void j() {
            EntitySheep entitysheep = EntityEvoker.this.getWololoTarget();

            if (entitysheep != null && entitysheep.isEntityAlive()) {
                entitysheep.setFleeceColor(EnumDyeColor.RED);
            }

        }

        protected int m() {
            return 40;
        }

        protected int f() {
            return 60;
        }

        protected int i() {
            return 140;
        }

        protected SoundEvent k() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO;
        }

        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.WOLOLO;
        }
    }

    class c extends EntityIllagerWizard.c {

        private c() {
            super();
        }

        public boolean shouldExecute() {
            if (!super.shouldExecute()) {
                return false;
            } else {
                int i = EntityEvoker.this.world.getEntitiesWithinAABB(EntityVex.class, EntityEvoker.this.getEntityBoundingBox().grow(16.0D)).size();

                return EntityEvoker.this.rand.nextInt(8) + 1 > i;
            }
        }

        protected int f() {
            return 100;
        }

        protected int i() {
            return 340;
        }

        protected void j() {
            for (int i = 0; i < 3; ++i) {
                BlockPos blockposition = (new BlockPos(EntityEvoker.this)).add(-2 + EntityEvoker.this.rand.nextInt(5), 1, -2 + EntityEvoker.this.rand.nextInt(5));
                EntityVex entityvex = new EntityVex(EntityEvoker.this.world);

                entityvex.moveToBlockPosAndAngles(blockposition, 0.0F, 0.0F);
                entityvex.onInitialSpawn(EntityEvoker.this.world.getDifficultyForLocation(blockposition), (IEntityLivingData) null);
                entityvex.setOwner((EntityLiving) EntityEvoker.this);
                entityvex.setBoundOrigin(blockposition);
                entityvex.setLimitedLife(20 * (30 + EntityEvoker.this.rand.nextInt(90)));
                EntityEvoker.this.world.spawnEntity(entityvex);
            }

        }

        protected SoundEvent k() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_SUMMON;
        }

        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.SUMMON_VEX;
        }

        c(Object object) {
            this();
        }
    }

    class a extends EntityIllagerWizard.c {

        private a() {
            super();
        }

        protected int f() {
            return 40;
        }

        protected int i() {
            return 100;
        }

        protected void j() {
            EntityLivingBase entityliving = EntityEvoker.this.getAttackTarget();
            double d0 = Math.min(entityliving.posY, EntityEvoker.this.posY);
            double d1 = Math.max(entityliving.posY, EntityEvoker.this.posY) + 1.0D;
            float f = (float) MathHelper.atan2(entityliving.posZ - EntityEvoker.this.posZ, entityliving.posX - EntityEvoker.this.posX);
            int i;

            if (EntityEvoker.this.getDistanceSq(entityliving) < 9.0D) {
                float f1;

                for (i = 0; i < 5; ++i) {
                    f1 = f + (float) i * 3.1415927F * 0.4F;
                    this.a(EntityEvoker.this.posX + (double) MathHelper.cos(f1) * 1.5D, EntityEvoker.this.posZ + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (i = 0; i < 8; ++i) {
                    f1 = f + (float) i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                    this.a(EntityEvoker.this.posX + (double) MathHelper.cos(f1) * 2.5D, EntityEvoker.this.posZ + (double) MathHelper.sin(f1) * 2.5D, d0, d1, f1, 3);
                }
            } else {
                for (i = 0; i < 16; ++i) {
                    double d2 = 1.25D * (double) (i + 1);
                    int j = 1 * i;

                    this.a(EntityEvoker.this.posX + (double) MathHelper.cos(f) * d2, EntityEvoker.this.posZ + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }

        }

        private void a(double d0, double d1, double d2, double d3, float f, int i) {
            BlockPos blockposition = new BlockPos(d0, d3, d1);
            boolean flag = false;
            double d4 = 0.0D;

            do {
                if (!EntityEvoker.this.world.isBlockNormalCube(blockposition, true) && EntityEvoker.this.world.isBlockNormalCube(blockposition.down(), true)) {
                    if (!EntityEvoker.this.world.isAirBlock(blockposition)) {
                        IBlockState iblockdata = EntityEvoker.this.world.getBlockState(blockposition);
                        AxisAlignedBB axisalignedbb = iblockdata.getCollisionBoundingBox(EntityEvoker.this.world, blockposition);

                        if (axisalignedbb != null) {
                            d4 = axisalignedbb.maxY;
                        }
                    }

                    flag = true;
                    break;
                }

                blockposition = blockposition.down();
            } while (blockposition.getY() >= MathHelper.floor(d2) - 1);

            if (flag) {
                EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(EntityEvoker.this.world, d0, (double) blockposition.getY() + d4, d1, f, i, EntityEvoker.this);

                EntityEvoker.this.world.spawnEntity(entityevokerfangs);
            }

        }

        protected SoundEvent k() {
            return SoundEvents.EVOCATION_ILLAGER_PREPARE_ATTACK;
        }

        protected EntitySpellcasterIllager.SpellType l() {
            return EntitySpellcasterIllager.SpellType.FANGS;
        }

        a(Object object) {
            this();
        }
    }

    class b extends EntityIllagerWizard.b {

        private b() {
            super();
        }

        public void updateTask() {
            if (EntityEvoker.this.getAttackTarget() != null) {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.getAttackTarget(), (float) EntityEvoker.this.getHorizontalFaceSpeed(), (float) EntityEvoker.this.getVerticalFaceSpeed());
            } else if (EntityEvoker.this.getWololoTarget() != null) {
                EntityEvoker.this.getLookHelper().setLookPositionWithEntity(EntityEvoker.this.getWololoTarget(), (float) EntityEvoker.this.getHorizontalFaceSpeed(), (float) EntityEvoker.this.getVerticalFaceSpeed());
            }

        }

        b(Object object) {
            this();
        }
    }
}
