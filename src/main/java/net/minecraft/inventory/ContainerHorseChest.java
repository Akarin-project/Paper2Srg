package net.minecraft.inventory;
import net.minecraft.entity.passive.AbstractHorse;


public class ContainerHorseChest extends InventoryBasic {

    // CraftBukkit start
    public ContainerHorseChest(String s, int i, AbstractHorse owner) {
        super(s, false, i, (org.bukkit.entity.AbstractHorse) owner.getBukkitEntity());
        // CraftBukkit end
    }
}
