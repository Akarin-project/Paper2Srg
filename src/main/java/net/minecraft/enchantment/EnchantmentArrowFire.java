package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowFire extends Enchantment {

    public EnchantmentArrowFire(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.setName("arrowFire");
    }

    public int getMinEnchantability(int i) {
        return 20;
    }

    public int getMaxEnchantability(int i) {
        return 50;
    }

    public int getMaxLevel() {
        return 1;
    }
}
