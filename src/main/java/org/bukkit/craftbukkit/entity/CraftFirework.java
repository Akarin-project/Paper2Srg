package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;
import java.util.UUID;

public class CraftFirework extends CraftEntity implements Firework {

    private final Random random = new Random();
    private final CraftItemStack item;

    public CraftFirework(CraftServer server, EntityFireworkRocket entity) {
        super(server, entity);

        ItemStack item = getHandle().func_184212_Q().func_187225_a(EntityFireworkRocket.field_184566_a);

        if (item.func_190926_b()) {
            item = new ItemStack(Items.field_151152_bP);
            getHandle().func_184212_Q().func_187227_b(EntityFireworkRocket.field_184566_a, item);
        }

        this.item = CraftItemStack.asCraftMirror(item);

        // Ensure the item is a firework...
        if (this.item.getType() != Material.FIREWORK) {
            this.item.setType(Material.FIREWORK);
        }
    }

    @Override
    public EntityFireworkRocket getHandle() {
        return (EntityFireworkRocket) entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREWORK;
    }

    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta) item.getItemMeta();
    }

    @Override
    public void setFireworkMeta(FireworkMeta meta) {
        item.setItemMeta(meta);

        // Copied from EntityFireworks constructor, update firework lifetime/power
        getHandle().field_92055_b = 10 * (1 + meta.getPower()) + random.nextInt(6) + random.nextInt(7);

        getHandle().func_184212_Q().func_187217_b(EntityFireworkRocket.field_184566_a);
    }

    @Override
    public void detonate() {
        getHandle().field_92055_b = 0;
    }

    // Paper start

    @Override
    public UUID getSpawningEntity() {
        return getHandle().spawningEntity;
    }

    @Override
    public LivingEntity getBoostedEntity() {
        EntityLivingBase boostedEntity = getHandle().getBoostedEntity();
        return boostedEntity != null ? (LivingEntity) boostedEntity.getBukkitEntity() : null;
    }
    // Paper end
}
