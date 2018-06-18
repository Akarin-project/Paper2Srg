package net.minecraft.entity.projectile;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityWitherSkull extends EntityFireball {

    private static final DataParameter<Boolean> INVULNERABLE = EntityDataManager.createKey(EntityWitherSkull.class, DataSerializers.BOOLEAN);

    public EntityWitherSkull(World world) {
        super(world);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntityWitherSkull(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.setSize(0.3125F, 0.3125F);
    }

    public static void registerFixesWitherSkull(DataFixer dataconvertermanager) {
        EntityFireball.registerFixesFireball(dataconvertermanager, "WitherSkull");
    }

    protected float getMotionFactor() {
        return this.isInvulnerable() ? 0.73F : super.getMotionFactor();
    }

    public boolean isBurning() {
        return false;
    }

    public float getExplosionResistance(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata) {
        float f = super.getExplosionResistance(explosion, world, blockposition, iblockdata);
        Block block = iblockdata.getBlock();

        if (this.isInvulnerable() && EntityWither.canDestroyBlock(block)) {
            f = Math.min(0.8F, f);
        }

        return f;
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        if (!this.world.isRemote) {
            if (movingobjectposition.entityHit != null) {
                // Spigot start
                boolean didDamage = false;
                if (this.shootingEntity != null) {
                    didDamage = movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, shootingEntity), 8.0F);
                    if (didDamage) { // CraftBukkit
                        if (movingobjectposition.entityHit.isEntityAlive()) {
                            this.applyEnchantments(this.shootingEntity, movingobjectposition.entityHit);
                        } else {
                            this.shootingEntity.heal(5.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.WITHER); // CraftBukkit
                        }
                    }
                } else {
                    didDamage = movingobjectposition.entityHit.attackEntityFrom(DamageSource.MAGIC, 5.0F);
                }

                if (didDamage && movingobjectposition.entityHit instanceof EntityLivingBase) {
                // Spigot end
                    byte b0 = 0;

                    if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                        b0 = 10;
                    } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                        b0 = 40;
                    }

                    if (b0 > 0) {
                        ((EntityLivingBase) movingobjectposition.entityHit).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * b0, 1));
                    }
                }
            }

            // CraftBukkit start
            // this.world.createExplosion(this, this.locX, this.locY, this.locZ, 1.0F, false, this.world.getGameRules().getBoolean("mobGriefing"));
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 1.0F, false);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.world.newExplosion(this, this.posX, this.posY, this.posZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
            }
            // CraftBukkit end
            this.setDead();
        }

    }

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        return false;
    }

    protected void entityInit() {
        this.dataManager.register(EntityWitherSkull.INVULNERABLE, Boolean.valueOf(false));
    }

    public boolean isInvulnerable() {
        return ((Boolean) this.dataManager.get(EntityWitherSkull.INVULNERABLE)).booleanValue();
    }

    public void setInvulnerable(boolean flag) {
        this.dataManager.set(EntityWitherSkull.INVULNERABLE, Boolean.valueOf(flag));
    }

    protected boolean isFireballFiery() {
        return false;
    }
}
