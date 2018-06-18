package net.minecraft.entity.projectile;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityLargeFireball extends EntityFireball {

    public int explosionPower = 1;

    public EntityLargeFireball(World world) {
        super(world);
        isIncendiary = this.world.getGameRules().getBoolean("mobGriefing"); // CraftBukkit
    }

    public EntityLargeFireball(World world, EntityLivingBase entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        isIncendiary = this.world.getGameRules().getBoolean("mobGriefing"); // CraftBukkit
    }

    protected void onImpact(RayTraceResult movingobjectposition) {
        if (!this.world.isRemote) {
            if (movingobjectposition.entityHit != null) {
                movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
                this.applyEnchantments(this.shootingEntity, movingobjectposition.entityHit);
            }

            boolean flag = this.world.getGameRules().getBoolean("mobGriefing");

            // CraftBukkit start - fire ExplosionPrimeEvent
            ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(this.world.getServer(), this));
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                // give 'this' instead of (Entity) null so we know what causes the damage
                this.world.newExplosion(this, this.posX, this.posY, this.posZ, event.getRadius(), event.getFire(), isIncendiary);
            }
            // CraftBukkit end
            this.setDead();
        }

    }

    public static void registerFixesLargeFireball(DataFixer dataconvertermanager) {
        EntityFireball.registerFixesFireball(dataconvertermanager, "Fireball");
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("ExplosionPower", this.explosionPower);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("ExplosionPower", 99)) {
            // CraftBukkit - set bukkitYield when setting explosionpower
            bukkitYield = this.explosionPower = nbttagcompound.getInteger("ExplosionPower");
        }

    }
}
