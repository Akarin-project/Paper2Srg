package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentFireAspect extends Enchantment {

    protected EnchantmentFireAspect(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
        this.setName("fire");
    }

    public int getMinEnchantability(int i) {
        return 10 + 20 * (i - 1);
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 2;
    }
}
