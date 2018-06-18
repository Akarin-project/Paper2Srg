package net.minecraft.enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentWaterWalker extends Enchantment {

    public EnchantmentWaterWalker(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_FEET, aenumitemslot);
        this.setName("waterWalker");
    }

    public int getMinEnchantability(int i) {
        return i * 10;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 15;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && enchantment != Enchantments.FROST_WALKER;
    }
}
