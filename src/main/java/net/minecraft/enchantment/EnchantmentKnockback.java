package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentKnockback extends Enchantment {

    protected EnchantmentKnockback(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
        this.setName("knockback");
    }

    public int getMinEnchantability(int i) {
        return 5 + 20 * (i - 1);
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 2;
    }
}
