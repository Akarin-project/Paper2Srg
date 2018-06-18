package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.EntityVindicator.a;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityVindicator extends AbstractIllager {

    private boolean johnny;
    private static final Predicate<Entity> JOHNNY_SELECTOR = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityLivingBase && ((EntityLivingBase) entity).attackable();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };

    public EntityVindicator(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
    }

    public static void registerFixesVindicator(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityVindicator.class);
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] { EntityVindicator.class}));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
        this.targetTasks.addTask(4, new EntityVindicator.a(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3499999940395355D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(12.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(24.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    protected void entityInit() {
        super.entityInit();
    }

    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_VINDICATION_ILLAGER;
    }

    public void setAggressive(boolean flag) {
        this.setAggressive(1, flag);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        if (this.johnny) {
            nbttagcompound.setBoolean("Johnny", true);
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("Johnny", 99)) {
            this.johnny = nbttagcompound.getBoolean("Johnny");
        }

    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        IEntityLivingData groupdataentity1 = super.onInitialSpawn(difficultydamagescaler, groupdataentity);

        this.setEquipmentBasedOnDifficulty(difficultydamagescaler);
        this.setEnchantmentBasedOnDifficulty(difficultydamagescaler);
        return groupdataentity1;
    }

    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficultydamagescaler) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    protected void updateAITasks() {
        super.updateAITasks();
        this.setAggressive(this.getAttackTarget() != null);
    }

    public boolean isOnSameTeam(Entity entity) {
        return super.isOnSameTeam(entity) ? true : (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER ? this.getTeam() == null && entity.getTeam() == null : false);
    }

    public void setCustomNameTag(String s) {
        super.setCustomNameTag(s);
        if (!this.johnny && "Johnny".equals(s)) {
            this.johnny = true;
        }

    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATION_ILLAGER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATION_ILLAGER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_VINDICATION_ILLAGER_HURT;
    }

    static class a extends EntityAINearestAttackableTarget<EntityLivingBase> {

        public a(EntityVindicator entityvindicator) {
            super(entityvindicator, EntityLivingBase.class, 0, true, true, EntityVindicator.JOHNNY_SELECTOR);
        }

        public boolean shouldExecute() {
            return ((EntityVindicator) this.taskOwner).johnny && super.shouldExecute();
        }
    }
}
