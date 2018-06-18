package net.minecraft.entity.monster;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityWitch extends EntityMob implements IRangedAttackMob {

    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = (new AttributeModifier(EntityWitch.MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
    private static final DataParameter<Boolean> IS_DRINKING = EntityDataManager.createKey(EntityWitch.class, DataSerializers.BOOLEAN);
    private int potionUseTimer;

    public EntityWitch(World world) {
        super(world);
        this.setSize(0.6F, 1.95F);
    }

    public static void registerFixesWitch(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityWitch.class);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0D, 60, 10.0F));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityWitch.IS_DRINKING, Boolean.valueOf(false));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITCH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_WITCH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITCH_DEATH;
    }

    public void setDrinkingPotion(boolean flag) {
        this.getDataManager().set(EntityWitch.IS_DRINKING, Boolean.valueOf(flag));
    }

    public boolean isDrinkingPotion() {
        return ((Boolean) this.getDataManager().get(EntityWitch.IS_DRINKING)).booleanValue();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(26.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    public void onLivingUpdate() {
        if (!this.world.isRemote) {
            if (this.isDrinkingPotion()) {
                if (this.potionUseTimer-- <= 0) {
                    this.setDrinkingPotion(false);
                    ItemStack itemstack = this.getHeldItemMainhand();

                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    if (itemstack.getItem() == Items.POTIONITEM) {
                        // Paper start
                        com.destroystokyo.paper.event.entity.WitchConsumePotionEvent event = new com.destroystokyo.paper.event.entity.WitchConsumePotionEvent((org.bukkit.entity.Witch) this.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(itemstack));

                        List list = event.callEvent() ? PotionUtils.getEffectsFromStack(org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(event.getPotion())) : null;
                        // Paper end

                        if (list != null) {
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                PotionEffect mobeffect = (PotionEffect) iterator.next();

                                this.addPotionEffect(new PotionEffect(mobeffect));
                            }
                        }
                    }

                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(EntityWitch.MODIFIER);
                }
            } else {
                PotionType potionregistry = null;

                if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING)) {
                    potionregistry = PotionTypes.WATER_BREATHING;
                } else if (this.rand.nextFloat() < 0.15F && (this.isBurning() || this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage()) && !this.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                    potionregistry = PotionTypes.FIRE_RESISTANCE;
                } else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    potionregistry = PotionTypes.HEALING;
                } else if (this.rand.nextFloat() < 0.5F && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget().getDistanceSq(this) > 121.0D) {
                    potionregistry = PotionTypes.SWIFTNESS;
                }

                if (potionregistry != null) {
                    // Paper start
                    ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potionregistry);
                    org.bukkit.inventory.ItemStack bukkitStack = com.destroystokyo.paper.event.entity.WitchReadyPotionEvent.process((org.bukkit.entity.Witch) this.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(potion));
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(bukkitStack));
                    // Paper end
                    this.potionUseTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setDrinkingPotion(true);
                    this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
                    IAttributeInstance attributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                    attributeinstance.removeModifier(EntityWitch.MODIFIER);
                    attributeinstance.applyModifier(EntityWitch.MODIFIER);
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F) {
                this.world.setEntityState(this, (byte) 15);
            }
        }

        super.onLivingUpdate();
    }

    protected float applyPotionDamageCalculations(DamageSource damagesource, float f) {
        f = super.applyPotionDamageCalculations(damagesource, f);
        if (damagesource.getTrueSource() == this) {
            f = 0.0F;
        }

        if (damagesource.isMagicDamage()) {
            f = (float) ((double) f * 0.15D);
        }

        return f;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WITCH;
    }

    public void attackEntityWithRangedAttack(EntityLivingBase entityliving, float f) {
        if (!this.isDrinkingPotion()) {
            double d0 = entityliving.posY + (double) entityliving.getEyeHeight() - 1.100000023841858D;
            double d1 = entityliving.posX + entityliving.motionX - this.posX;
            double d2 = d0 - this.posY;
            double d3 = entityliving.posZ + entityliving.motionZ - this.posZ;
            float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3);
            PotionType potionregistry = PotionTypes.HARMING;

            if (f1 >= 8.0F && !entityliving.isPotionActive(MobEffects.SLOWNESS)) {
                potionregistry = PotionTypes.SLOWNESS;
            } else if (entityliving.getHealth() >= 8.0F && !entityliving.isPotionActive(MobEffects.POISON)) {
                potionregistry = PotionTypes.POISON;
            } else if (f1 <= 3.0F && !entityliving.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F) {
                potionregistry = PotionTypes.WEAKNESS;
            }

            // Paper start
            ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionregistry);
            com.destroystokyo.paper.event.entity.WitchThrowPotionEvent event = new com.destroystokyo.paper.event.entity.WitchThrowPotionEvent((org.bukkit.entity.Witch) this.getBukkitEntity(), (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(), org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(potion));
            if (!event.callEvent()) {
                return;
            }
            potion = org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(event.getPotion());
            EntityPotion entitypotion = new EntityPotion(this.world, this, potion);
            // Paper end

            entitypotion.rotationPitch -= -20.0F;
            entitypotion.shoot(d1, d2 + (double) (f1 * 0.2F), d3, 0.75F, 8.0F);
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
            this.world.spawnEntity(entitypotion);
        }
    }

    public float getEyeHeight() {
        return 1.62F;
    }

    public void setSwingingArms(boolean flag) {}
}
