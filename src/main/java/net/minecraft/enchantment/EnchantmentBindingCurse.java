package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentBindingCurse extends Enchantment {

    public EnchantmentBindingCurse(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEARABLE, aenumitemslot);
        this.setName("binding_curse");
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
