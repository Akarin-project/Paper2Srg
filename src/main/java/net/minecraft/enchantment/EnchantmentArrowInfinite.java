package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowInfinite extends Enchantment {

    public EnchantmentArrowInfinite(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.setName("arrowInfinite");
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

    public boolean canApplyTogether(Enchantment enchantment) {
        return enchantment instanceof EnchantmentMending ? false : super.canApplyTogether(enchantment);
    }
}
