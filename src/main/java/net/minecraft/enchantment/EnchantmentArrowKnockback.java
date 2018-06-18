package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowKnockback extends Enchantment {

    public EnchantmentArrowKnockback(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.setName("arrowKnockback");
    }

    public int getMinEnchantability(int i) {
        return 12 + (i - 1) * 20;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 25;
    }

    public int getMaxLevel() {
        return 2;
    }
}
