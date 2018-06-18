package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowDamage extends Enchantment {

    public EnchantmentArrowDamage(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.setName("arrowDamage");
    }

    public int getMinEnchantability(int i) {
        return 1 + (i - 1) * 10;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 15;
    }

    public int getMaxLevel() {
        return 5;
    }
}
