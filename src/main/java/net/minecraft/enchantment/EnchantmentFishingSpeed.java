package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentFishingSpeed extends Enchantment {

    protected EnchantmentFishingSpeed(Enchantment.Rarity enchantment_rarity, EnumEnchantmentType enchantmentslottype, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, enchantmentslottype, aenumitemslot);
        this.setName("fishingSpeed");
    }

    public int getMinEnchantability(int i) {
        return 15 + (i - 1) * 9;
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }
}
