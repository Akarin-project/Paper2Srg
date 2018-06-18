package net.minecraft.enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentLootBonus extends Enchantment {

    protected EnchantmentLootBonus(Enchantment.Rarity enchantment_rarity, EnumEnchantmentType enchantmentslottype, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, enchantmentslottype, aenumitemslot);
        if (enchantmentslottype == EnumEnchantmentType.DIGGER) {
            this.setName("lootBonusDigger");
        } else if (enchantmentslottype == EnumEnchantmentType.FISHING_ROD) {
            this.setName("lootBonusFishing");
        } else {
            this.setName("lootBonus");
        }

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

    public boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) && enchantment != Enchantments.SILK_TOUCH;
    }
}
