package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentOxygen extends Enchantment {

    public EnchantmentOxygen(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_HEAD, aenumitemslot);
        this.setName("oxygen");
    }

    public int getMinEnchantability(int i) {
        return 10 * i;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 30;
    }

    public int getMaxLevel() {
        return 3;
    }
}
