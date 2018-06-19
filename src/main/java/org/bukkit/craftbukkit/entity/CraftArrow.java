package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.entity.projectile.EntityArrow;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

import org.bukkit.entity.Arrow.PickupStatus;

public class CraftArrow extends AbstractProjectile implements Arrow {

    public CraftArrow(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }

    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        getHandle().func_70240_a(knockbackStrength);
    }

    public int getKnockbackStrength() {
        return getHandle().field_70256_ap;
    }

    public boolean isCritical() {
        return getHandle().func_70241_g();
    }

    public void setCritical(boolean critical) {
        getHandle().func_70243_d(critical);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            getHandle().field_70250_c = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().field_70250_c = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public boolean isInBlock() {
        return getHandle().field_70254_i;
    }

    @Override
    public Block getAttachedBlock() {
        if (!isInBlock()) {
            return null;
        }

        EntityArrow handle = getHandle();
        return getWorld().getBlockAt(handle.field_145791_d, handle.field_145792_e, handle.field_145789_f); // PAIL: rename tileX, tileY, tileZ
    }

    @Override
    public PickupStatus getPickupStatus() {
        return PickupStatus.values()[getHandle().field_70251_a.ordinal()];
    }

    @Override
    public void setPickupStatus(PickupStatus status) {
        Preconditions.checkNotNull(status, "status");
        getHandle().field_70251_a = EntityArrow.PickupStatus.func_188795_a(status.ordinal());
    }

    @Override
    public EntityArrow getHandle() {
        return (EntityArrow) entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }

    public EntityType getType() {
        return EntityType.ARROW;
    }

    // Spigot start
    private final Arrow.Spigot spigot = new Arrow.Spigot()
    {
        @Override
        public double getDamage()
        {
            return getHandle().func_70242_d();
        }

        @Override
        public void setDamage(double damage)
        {
            getHandle().func_70239_b( damage );
        }
    };

    public Arrow.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
