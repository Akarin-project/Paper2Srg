package net.minecraft.enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentUntouching extends Enchantment {

    protected EnchantmentUntouching(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.DIGGER, aenumitemslot);
        this.setName("untouching");
    }

    public int getMinEnchantability(int i) {
        return 15;
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && enchantment != Enchantments.FORTUNE;
    }
}
