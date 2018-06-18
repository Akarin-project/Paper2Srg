package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentSweepingEdge extends Enchantment {

    public EnchantmentSweepingEdge(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
    }

    public int getMinEnchantability(int i) {
        return 5 + (i - 1) * 9;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 15;
    }

    public int getMaxLevel() {
        return 3;
    }

    public static float getSweepingDamageRatio(int i) {
        return 1.0F - 1.0F / (float) (i + 1);
    }

    public String getName() {
        return "enchantment.sweeping";
    }
}
