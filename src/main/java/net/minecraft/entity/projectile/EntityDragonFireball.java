package net.minecraft.entity.projectile;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDragonFireball extends EntityFireball {

    public EntityDragonFireball(World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    public EntityDragonFireball(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.setSize(1.0F, 1.0F);
    }

    public static void registerFixesDragonFireball(DataFixer dataconvertermanager) {
        EntityFireball.registerFixesFireball(dataconvertermanager, "DragonFireball");
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        if (movingobjectposition.entityHit == null || !movingobjectposition.entityHit.isEntityEqual(this.shootingEntity)) {
            if (!this.world.isRemote) {
                List list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(4.0D, 2.0D, 4.0D));
                EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);

                entityareaeffectcloud.setOwner(this.shootingEntity);
                entityareaeffectcloud.setParticle(EnumParticleTypes.DRAGON_BREATH);
                entityareaeffectcloud.setRadius(3.0F);
                entityareaeffectcloud.setDuration(600);
                entityareaeffectcloud.setRadiusPerTick((7.0F - entityareaeffectcloud.getRadius()) / (float) entityareaeffectcloud.getDuration());
                entityareaeffectcloud.addEffect(new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 1));
                if (!list.isEmpty()) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        EntityLivingBase entityliving = (EntityLivingBase) iterator.next();
                        double d0 = this.getDistanceSq(entityliving);

                        if (d0 < 16.0D) {
                            entityareaeffectcloud.setPosition(entityliving.posX, entityliving.posY, entityliving.posZ);
                            break;
                        }
                    }
                }

                this.world.playEvent(2006, new BlockPos(this.posX, this.posY, this.posZ), 0);
                this.world.spawnEntity(entityareaeffectcloud);
                this.setDead();
            }

        }
    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        return false;
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.DRAGON_BREATH;
    }

    protected boolean isFireballFiery() {
        return false;
    }
}
