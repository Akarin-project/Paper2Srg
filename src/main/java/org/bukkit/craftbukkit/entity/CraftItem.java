package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.CraftServer;

public class CraftItem extends CraftEntity implements Item {
    private final EntityItem item;

    public CraftItem(CraftServer server, Entity entity, EntityItem item) {
        super(server, entity);
        this.item = item;
    }

    public CraftItem(CraftServer server, EntityItem entity) {
        this(server, entity, entity);
    }

    public ItemStack getItemStack() {
        return CraftItemStack.asCraftMirror(item.func_92059_d());
    }

    public void setItemStack(ItemStack stack) {
        item.func_92058_a(CraftItemStack.asNMSCopy(stack));
    }

    public int getPickupDelay() {
        return item.field_145804_b;
    }

    public void setPickupDelay(int delay) {
        item.field_145804_b = Math.min(delay, Short.MAX_VALUE);
    }

    // Paper Start
    public boolean canMobPickup() {
        return item.canMobPickup;
    }

    public void setCanMobPickup(boolean canMobPickup) {
        item.canMobPickup = canMobPickup;
    }
    // Paper End

    @Override
    public String toString() {
        return "CraftItem";
    }

    public EntityType getType() {
        return EntityType.DROPPED_ITEM;
    }
}
