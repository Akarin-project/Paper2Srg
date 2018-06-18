package net.minecraft.entity.projectile;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public abstract class EntityFireball extends Entity {

    public EntityLivingBase shootingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public float bukkitYield = 1; // CraftBukkit
    public boolean isIncendiary = true; // CraftBukkit

    public EntityFireball(World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    protected void entityInit() {}

    public EntityFireball(World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world);
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(d0, d1, d2, this.rotationYaw, this.rotationPitch);
        this.setPosition(d0, d1, d2);
        double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

        this.accelerationX = d3 / d6 * 0.1D;
        this.accelerationY = d4 / d6 * 0.1D;
        this.accelerationZ = d5 / d6 * 0.1D;
    }

    public EntityFireball(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world);
        this.shootingEntity = entityliving;
        this.projectileSource = (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        // CraftBukkit start - Added setDirection method
        this.setDirection(d0, d1, d2);
    }

    public void setDirection(double d0, double d1, double d2) {
        // CraftBukkit end
        d0 += this.rand.nextGaussian() * 0.4D;
        d1 += this.rand.nextGaussian() * 0.4D;
        d2 += this.rand.nextGaussian() * 0.4D;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        this.accelerationX = d0 / d3 * 0.1D;
        this.accelerationY = d1 / d3 * 0.1D;
        this.accelerationZ = d2 / d3 * 0.1D;
    }

    public void onUpdate() {
        if (!this.world.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.world.isBlockLoaded(new BlockPos(this)))) {
            this.setDead();
        } else {
            super.onUpdate();
            if (this.isFireballFiery()) {
                this.setFire(1);
            }

            ++this.ticksInAir;
            RayTraceResult movingobjectposition = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.shootingEntity);

            // Paper start - Call ProjectileCollideEvent
            if (movingobjectposition != null && movingobjectposition.entityHit != null) {
                com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
                if (event.isCancelled()) {
                    movingobjectposition = null;
                }
            }
            // Paper end

            if (movingobjectposition != null) {
                this.onImpact(movingobjectposition);

                // CraftBukkit start - Fire ProjectileHitEvent
                if (this.isDead) {
                    CraftEventFactory.callProjectileHitEvent(this, movingobjectposition);
                }
                // CraftBukkit end
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float f = this.getMotionFactor();

            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f1 = 0.25F;

                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
                }

                f = 0.8F;
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double) f;
            this.motionY *= (double) f;
            this.motionZ *= (double) f;
            this.world.spawnParticle(this.getParticleType(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    protected boolean isFireballFiery() {
        return true;
    }

    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SMOKE_NORMAL;
    }

    protected float getMotionFactor() {
        return 0.95F;
    }

    protected abstract void onImpact(RayTraceResult movingobjectposition);

    public static void registerFixesFireball(DataFixer dataconvertermanager, String s) {}

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setTag("direction", this.newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ}));
        nbttagcompound.setTag("power", this.newDoubleNBTList(new double[] { this.accelerationX, this.accelerationY, this.accelerationZ}));
        nbttagcompound.setInteger("life", this.ticksAlive);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist;

        if (nbttagcompound.hasKey("power", 9)) {
            nbttaglist = nbttagcompound.getTagList("power", 6);
            if (nbttaglist.tagCount() == 3) {
                this.accelerationX = nbttaglist.getDoubleAt(0);
                this.accelerationY = nbttaglist.getDoubleAt(1);
                this.accelerationZ = nbttaglist.getDoubleAt(2);
            }
        }

        this.ticksAlive = nbttagcompound.getInteger("life");
        if (nbttagcompound.hasKey("direction", 9) && nbttagcompound.getTagList("direction", 6).tagCount() == 3) {
            nbttaglist = nbttagcompound.getTagList("direction", 6);
            this.motionX = nbttaglist.getDoubleAt(0);
            this.motionY = nbttaglist.getDoubleAt(1);
            this.motionZ = nbttaglist.getDoubleAt(2);
        } else {
            this.setDead();
        }

    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public float getCollisionBorderSize() {
        return 1.0F;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            this.markVelocityChanged();
            if (damagesource.getTrueSource() != null) {
                // CraftBukkit start
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                Vec3d vec3d = damagesource.getTrueSource().getLookVec();

                if (vec3d != null) {
                    this.motionX = vec3d.x;
                    this.motionY = vec3d.y;
                    this.motionZ = vec3d.z;
                    this.accelerationX = this.motionX * 0.1D;
                    this.accelerationY = this.motionY * 0.1D;
                    this.accelerationZ = this.motionZ * 0.1D;
                }

                if (damagesource.getTrueSource() instanceof EntityLivingBase) {
                    this.shootingEntity = (EntityLivingBase) damagesource.getTrueSource();
                    this.projectileSource = (org.bukkit.projectiles.ProjectileSource) this.shootingEntity.getBukkitEntity();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public float getBrightness() {
        return 1.0F;
    }
}
