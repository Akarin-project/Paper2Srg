package net.minecraft.entity.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
// CraftBukkit end

public class EntityEnderPearl extends EntityThrowable {

    private EntityLivingBase perlThrower;

    public EntityEnderPearl(World world) {
        super(world);
    }

    public EntityEnderPearl(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
        this.perlThrower = entityliving;
    }

    public static void registerFixesEnderPearl(DataFixer dataconvertermanager) {
        EntityThrowable.registerFixesThrowable(dataconvertermanager, "ThrownEnderpearl");
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        EntityLivingBase entityliving = this.getThrower();

        if (movingobjectposition.entityHit != null) {
            if (movingobjectposition.entityHit == this.perlThrower) {
                return;
            }

            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entityliving), 0.0F);
        }

        if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockposition = movingobjectposition.getBlockPos();
            TileEntity tileentity = this.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityEndGateway) {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway) tileentity;

                if (entityliving != null) {
                    if (entityliving instanceof EntityPlayerMP) {
                        CriteriaTriggers.ENTER_BLOCK.trigger((EntityPlayerMP) entityliving, this.world.getBlockState(blockposition));
                    }

                    tileentityendgateway.teleportEntity((Entity) entityliving);
                    this.setDead();
                    return;
                }

                tileentityendgateway.teleportEntity((Entity) this);
                return;
            }
        }

        for (int i = 0; i < 32; ++i) {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }

        if (!this.world.isRemote) {
            if (entityliving instanceof EntityPlayerMP) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) entityliving;

                if (entityplayer.connection.getNetworkManager().isChannelOpen() && entityplayer.world == this.world && !entityplayer.isPlayerSleeping()) {
                    // CraftBukkit start - Fire PlayerTeleportEvent
                    org.bukkit.craftbukkit.entity.CraftPlayer player = entityplayer.getBukkitEntity();
                    org.bukkit.Location location = getBukkitEntity().getLocation();
                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());

                    PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                    Bukkit.getPluginManager().callEvent(teleEvent);

                    if (!teleEvent.isCancelled() && !entityplayer.connection.isDisconnected()) {
                        if (this.rand.nextFloat() < 0.05F && this.world.getGameRules().getBoolean("doMobSpawning")) {
                            EntityEndermite entityendermite = new EntityEndermite(this.world);

                            entityendermite.setSpawnedByPlayer(true);
                            entityendermite.setLocationAndAngles(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
                            this.world.addEntity(entityendermite, CreatureSpawnEvent.SpawnReason.ENDER_PEARL);
                        }

                        if (entityliving.isRiding()) {
                            entityliving.dismountRidingEntity();
                        }

                        entityplayer.connection.teleport(teleEvent.getTo());
                        entityliving.fallDistance = 0.0F;
                        CraftEventFactory.entityDamage = this;
                        entityliving.attackEntityFrom(DamageSource.FALL, 5.0F);
                        CraftEventFactory.entityDamage = null;
                    }
                    // CraftBukkit end
                }
            } else if (entityliving != null) {
                entityliving.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                entityliving.fallDistance = 0.0F;
            }

            this.setDead();
        }

    }

    public void onUpdate() {
        EntityLivingBase entityliving = this.getThrower();

        if (entityliving != null && entityliving instanceof EntityPlayer && !entityliving.isEntityAlive()) {
            this.setDead();
        } else {
            super.onUpdate();
        }

    }

    @Nullable
    public Entity changeDimension(int i) {
        if (this.thrower.dimension != i) {
            this.thrower = null;
        }

        return super.changeDimension(i);
    }
}
