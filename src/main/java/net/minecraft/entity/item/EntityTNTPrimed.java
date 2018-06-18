package net.minecraft.entity.item;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityTNTPrimed extends Entity {

    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(EntityTNTPrimed.class, DataSerializers.VARINT);
    @Nullable
    private EntityLivingBase tntPlacedBy;
    private int fuse;
    public float yield = 4; // CraftBukkit - add field
    public boolean isIncendiary = false; // CraftBukkit - add field

    public EntityTNTPrimed(World world) {
        super(world);
        this.fuse = 80;
        this.preventEntitySpawning = true;
        this.isImmuneToFire = true;
        this.setSize(0.98F, 0.98F);
    }

    public EntityTNTPrimed(World world, double d0, double d1, double d2, EntityLivingBase entityliving) {
        this(world);
        this.setPosition(d0, d1, d2);
        float f = (float) (Math.random() * 6.2831854820251465D);

        this.motionX = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.setFuse(80);
        this.prevPosX = d0;
        this.prevPosY = d1;
        this.prevPosZ = d2;
        this.tntPlacedBy = entityliving;
    }

    protected void entityInit() {
        this.dataManager.register(EntityTNTPrimed.FUSE, Integer.valueOf(80));
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void onUpdate() {
        if (world.spigotConfig.currentPrimedTnt++ > world.spigotConfig.maxTntTicksPerTick) { return; } // Spigot
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

        // Paper start - Configurable TNT entity height nerf
        if (this.world.paperConfig.entityTNTHeightNerf != 0 && this.posY > this.world.paperConfig.entityTNTHeightNerf) {
            this.setDead();
        }
        // Paper end

        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        --this.fuse;
        if (this.fuse <= 0) {
            // CraftBukkit start - Need to reverse the order of the explosion and the entity death so we have a location for the event
            // this.die();
            if (!this.world.isRemote) {
                this.explode();
            }
            this.setDead();
            // CraftBukkit end
        } else {
            this.handleWaterMovement();
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    private void explode() {
        // CraftBukkit start
        // float f = 4.0F;

        org.bukkit.craftbukkit.CraftServer server = this.world.getServer();
        ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, this));
        server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            this.world.newExplosion(this, this.posX, this.posY + (double) (this.height / 16.0F), this.posZ, event.getRadius(), event.getFire(), true);
        }
        // CraftBukkit end
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Fuse", (short) this.getFuse());
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.setFuse(nbttagcompound.getShort("Fuse"));
        // Paper start - Try and load origin location from the old NBT tags for backwards compatibility
        if (nbttagcompound.hasKey("SourceLoc_x")) {
            int srcX = nbttagcompound.getInteger("SourceLoc_x");
            int srcY = nbttagcompound.getInteger("SourceLoc_y");
            int srcZ = nbttagcompound.getInteger("SourceLoc_z");
            origin = new org.bukkit.Location(world.getWorld(), srcX, srcY, srcZ);
        }
        // Paper end
    }

    @Nullable
    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }

    public float getEyeHeight() {
        return 0.0F;
    }

    public void setFuse(int i) {
        this.dataManager.set(EntityTNTPrimed.FUSE, Integer.valueOf(i));
        this.fuse = i;
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityTNTPrimed.FUSE.equals(datawatcherobject)) {
            this.fuse = this.getFuseDataManager();
        }

    }

    public int getFuseDataManager() {
        return ((Integer) this.dataManager.get(EntityTNTPrimed.FUSE)).intValue();
    }

    public int getFuse() {
        return this.fuse;
    }

    // Paper start - Optional prevent TNT from moving in water
    @Override
    public boolean pushedByWater() {
        return !world.paperConfig.preventTntFromMovingInWater && super.pushedByWater();
    }

    /**
     * Author: Jedediah Smith <jedediah@silencegreys.com>
     */
    @Override
    public boolean doWaterMovement() {
        if (!world.paperConfig.preventTntFromMovingInWater) return super.doWaterMovement();

        // Preserve velocity while calling the super method
        double oldMotX = this.motionX;
        double oldMotY = this.motionY;
        double oldMotZ = this.motionZ;

        super.doWaterMovement();

        this.motionX = oldMotX;
        this.motionY = oldMotY;
        this.motionZ = oldMotZ;

        if (this.inWater) {
            // Send position and velocity updates to nearby players on every tick while the TNT is in water.
            // This does pretty well at keeping their clients in sync with the server.
            EntityTrackerEntry ete = ((WorldServer) this.getEntityWorld()).getEntityTracker().trackedEntityHashTable.lookup(this.getEntityId());
            if (ete != null) {
                SPacketEntityVelocity velocityPacket = new SPacketEntityVelocity(this);
                SPacketEntityTeleport positionPacket = new SPacketEntityTeleport(this);

                ete.trackingPlayers.stream()
                        .filter(viewer -> (viewer.posX - this.posX) * (viewer.posY - this.posY) * (viewer.posZ - this.posZ) < 16 * 16)
                        .forEach(viewer -> {
                    viewer.connection.sendPacket(velocityPacket);
                    viewer.connection.sendPacket(positionPacket);
                });
            }
        }

        return this.inWater;
    }
    // Paper end
}
