package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentVanishingCurse extends Enchantment {

    public EnchantmentVanishingCurse(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ALL, aenumitemslot);
        this.setName("vanishing_curse");
    }

    public int getMinEnchantability(int i) {
        return 25;
    }

    public int getMaxEnchantability(int i) {
        return 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean isTreasureEnchantment() {
        return true;
    }

    public boolean isCurse() {
        return true;
    }
}
