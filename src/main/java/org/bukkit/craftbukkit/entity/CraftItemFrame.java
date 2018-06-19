package org.bukkit.craftbukkit.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;

import org.apache.commons.lang.Validate;

import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {
    public CraftItemFrame(CraftServer server, EntityItemFrame entity) {
        super(server, entity);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        if (!super.setFacingDirection(face, force)) {
            return false;
        }

        update();

        return true;
    }

    private void update() {
        EntityItemFrame old = this.getHandle();

        WorldServer world = ((CraftWorld) getWorld()).getHandle();
        BlockPos position = old.func_174857_n();
        EnumFacing direction = old.func_174811_aO();
        ItemStack item = old.func_82335_i() != null ? old.func_82335_i().func_77946_l() : null;

        old.func_70106_y();

        EntityItemFrame frame = new EntityItemFrame(world,position,direction);
        frame.func_82334_a(item);
        world.func_72838_d(frame);
        this.entity = frame;
    }

    public void setItem(org.bukkit.inventory.ItemStack item) {
        getHandle().func_82334_a(CraftItemStack.asNMSCopy(item));
    }

    public org.bukkit.inventory.ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(getHandle().func_82335_i());
    }

    public Rotation getRotation() {
        return toBukkitRotation(getHandle().func_82333_j());
    }

    Rotation toBukkitRotation(int value) {
        // Translate NMS rotation integer to Bukkit API
        switch (value) {
        case 0:
            return Rotation.NONE;
        case 1:
            return Rotation.CLOCKWISE_45;
        case 2:
            return Rotation.CLOCKWISE;
        case 3:
            return Rotation.CLOCKWISE_135;
        case 4:
            return Rotation.FLIPPED;
        case 5:
            return Rotation.FLIPPED_45;
        case 6:
            return Rotation.COUNTER_CLOCKWISE;
        case 7:
            return Rotation.COUNTER_CLOCKWISE_45;
        default:
            throw new AssertionError("Unknown rotation " + value + " for " + getHandle());
        }
    }

    public void setRotation(Rotation rotation) {
        Validate.notNull(rotation, "Rotation cannot be null");
        getHandle().func_82336_g(toInteger(rotation));
    }

    static int toInteger(Rotation rotation) {
        // Translate Bukkit API rotation to NMS integer
        switch (rotation) {
        case NONE:
            return 0;
        case CLOCKWISE_45:
            return 1;
        case CLOCKWISE:
            return 2;
        case CLOCKWISE_135:
            return 3;
        case FLIPPED:
            return 4;
        case FLIPPED_45:
            return 5;
        case COUNTER_CLOCKWISE:
            return 6;
        case COUNTER_CLOCKWISE_45:
            return 7;
        default:
            throw new IllegalArgumentException(rotation + " is not applicable to an ItemFrame");
        }
    }

    @Override
    public EntityItemFrame getHandle() {
        return (EntityItemFrame) entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
    }

    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }
}
