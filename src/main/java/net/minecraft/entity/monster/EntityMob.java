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
        this.experienceValue = 5;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        float f = this.getBrightness();

        if (f > 0.5F) {
            this.idleTime += 2;
        }

        super.onLivingUpdate();
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }

    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_HOSTILE_SWIM;
    }

    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_HOSTILE_SPLASH;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        return this.isEntityInvulnerable(damagesource) ? false : super.attackEntityFrom(damagesource, f);
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ENTITY_HOSTILE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HOSTILE_DEATH;
    }

    protected SoundEvent getFallSound(int i) {
        return i > 4 ? SoundEvents.ENTITY_HOSTILE_BIG_FALL : SoundEvents.ENTITY_HOSTILE_SMALL_FALL;
    }

    public boolean attackEntityAsMob(Entity entity) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entity instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase) this);
        }

        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0 && entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                if (!combustEvent.isCancelled()) {
                    entity.setFire(combustEvent.getDuration());
                }
                // CraftBukkit end
            }

            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityhuman.isHandActive() ? entityhuman.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem() instanceof ItemAxe && itemstack1.getItem() == Items.SHIELD) {
                    float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1) {
                        entityhuman.getCooldownTracker().setCooldown(Items.SHIELD, 100);
                        this.world.setEntityState(entityhuman, (byte) 30);
                    }
                }
            }

            this.applyEnchantments((EntityLivingBase) this, entity);
        }

        return flag;
    }

    public float getBlockPathWeight(BlockPos blockposition) {
        return 0.5F - this.world.getLightBrightness(blockposition);
    }

    protected boolean isValidLightLevel() {
        BlockPos blockposition = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        if (this.world.getLightFor(EnumSkyBlock.SKY, blockposition) > this.rand.nextInt(32)) {
            return false;
        } else {
            //int i = this.world.getLightLevel(blockposition); // Paper
            boolean passes; // Paper
            if (this.world.isThundering()) {
                int j = this.world.getSkylightSubtracted();

                this.world.setSkylightSubtracted(10);
                passes = !world.isLightLevel(blockposition, this.rand.nextInt(9)); // Paper
                this.world.setSkylightSubtracted(j);
            } else { passes = !world.isLightLevel(blockposition, this.rand.nextInt(9)); } // Paper

            return passes; // Paper
        }
    }

    public boolean getCanSpawnHere() {
        return this.world.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    protected boolean canDropLoot() {
        return true;
    }

    public boolean isPreventingPlayerRest(EntityPlayer entityhuman) {
        return true;
    }
}
