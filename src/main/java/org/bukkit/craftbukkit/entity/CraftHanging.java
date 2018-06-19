package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EntityHanging;
import net.minecraft.util.EnumFacing;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;

public class CraftHanging extends CraftEntity implements Hanging {
    public CraftHanging(CraftServer server, EntityHanging entity) {
        super(server, entity);
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        EntityHanging hanging = getHandle();
        EnumFacing dir = hanging.field_174860_b;
        switch (face) {
            case SOUTH:
            default:
                getHandle().func_174859_a(EnumFacing.SOUTH);
                break;
            case WEST:
                getHandle().func_174859_a(EnumFacing.WEST);
                break;
            case NORTH:
                getHandle().func_174859_a(EnumFacing.NORTH);
                break;
            case EAST:
                getHandle().func_174859_a(EnumFacing.EAST);
                break;
        }
        if (!force && !hanging.func_70518_d()) {
            // Revert since it doesn't fit
            hanging.func_174859_a(dir);
            return false;
        }
        return true;
    }

    public BlockFace getFacing() {
        EnumFacing direction = this.getHandle().field_174860_b;
        if (direction == null) return BlockFace.SELF;
        switch (direction) {
            case SOUTH:
            default:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
            case NORTH:
                return BlockFace.NORTH;
            case EAST:
                return BlockFace.EAST;
        }
    }

    @Override
    public EntityHanging getHandle() {
        return (EntityHanging) entity;
    }

    @Override
    public String toString() {
        return "CraftHanging";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
