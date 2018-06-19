package org.bukkit.craftbukkit.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal {
    public CraftEnderCrystal(CraftServer server, EntityEnderCrystal entity) {
        super(server, entity);
    }

    @Override
    public boolean isShowingBottom() {
        return getHandle().func_184520_k();
    }

    @Override
    public void setShowingBottom(boolean showing) {
        getHandle().func_184517_a(showing);
    }

    @Override
    public Location getBeamTarget() {
        BlockPos pos = getHandle().func_184518_j();
        return pos == null ? null : new Location(getWorld(), pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    @Override
    public void setBeamTarget(Location location) {
        if (location == null) {
            getHandle().func_184516_a((BlockPos) null);
        } else if (location.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot set beam target location to different world");
        } else {
            getHandle().func_184516_a(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        }
    }

    @Override
    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }

    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}
