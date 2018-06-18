package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase {

    private final EntityAnimal animal;
    private final Class<? extends EntityAnimal> mateClass;
    World world;
    private EntityAnimal targetMate;
    int spawnBabyDelay;
    double moveSpeed;

    public EntityAIMate(EntityAnimal entityanimal, double d0) {
        this(entityanimal, d0, entityanimal.getClass());
    }

    public EntityAIMate(EntityAnimal entityanimal, double d0, Class<? extends EntityAnimal> oclass) {
        this.animal = entityanimal;
        this.world = entityanimal.world;
        this.mateClass = oclass;
        this.moveSpeed = d0;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    public void updateTask() {
        this.animal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.animal.getVerticalFaceSpeed());
        this.animal.getNavigator().tryMoveToEntityLiving((Entity) this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;
        if (this.spawnBabyDelay >= 60 && this.animal.getDistanceSq(this.targetMate) < 9.0D) {
            this.spawnBaby();
        }

    }

    private EntityAnimal getNearbyMate() {
        List list = this.world.getEntitiesWithinAABB(this.mateClass, this.animal.getEntityBoundingBox().grow(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityAnimal entityanimal1 = (EntityAnimal) iterator.next();

            if (this.animal.canMateWith(entityanimal1) && this.animal.getDistanceSq(entityanimal1) < d0) {
                entityanimal = entityanimal1;
                d0 = this.animal.getDistanceSq(entityanimal1);
            }
        }

        return entityanimal;
    }

    private void spawnBaby() {
        EntityAgeable entityageable = this.animal.createChild(this.targetMate);

        if (entityageable != null) {
            // CraftBukkit start - set persistence for tame animals
            if (entityageable instanceof EntityTameable && ((EntityTameable) entityageable).isTamed()) {
                entityageable.persistenceRequired = true;
            }
            // CraftBukkit end
            EntityPlayerMP entityplayer = this.animal.getLoveCause();

            if (entityplayer == null && this.targetMate.getLoveCause() != null) {
                entityplayer = this.targetMate.getLoveCause();
            }
            // CraftBukkit start - call EntityBreedEvent
            int experience = this.animal.getRNG().nextInt(7) + 1;
            org.bukkit.event.entity.EntityBreedEvent entityBreedEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityBreedEvent(entityageable, animal, targetMate, entityplayer, this.animal.breedItem, experience);
            if (entityBreedEvent.isCancelled()) {
                return;
            }
            experience = entityBreedEvent.getExperience();
            // CraftBukkit end

            if (entityplayer != null) {
                entityplayer.addStat(StatList.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(entityplayer, this.animal, this.targetMate, entityageable);
            }

            this.animal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.animal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.animal.posX, this.animal.posY, this.animal.posZ, 0.0F, 0.0F);
            this.world.addEntity(entityageable, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.BREEDING); // CraftBukkit - added SpawnReason
            Random random = this.animal.getRNG();

            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double) this.animal.width * 2.0D - (double) this.animal.width;
                double d4 = 0.5D + random.nextDouble() * (double) this.animal.height;
                double d5 = random.nextDouble() * (double) this.animal.width * 2.0D - (double) this.animal.width;

                this.world.spawnParticle(EnumParticleTypes.HEART, this.animal.posX + d3, this.animal.posY + d4, this.animal.posZ + d5, d0, d1, d2, new int[0]);
            }

            if (this.world.getGameRules().getBoolean("doMobLoot")) {
                // CraftBukkit start - use event experience
                if (experience > 0) {
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.animal.posX, this.animal.posY, this.animal.posZ, experience, org.bukkit.entity.ExperienceOrb.SpawnReason.BREED, entityplayer, entityageable)); // Paper
                }
                // CraftBukkit end
            }

        }
    }
}
