package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentMending extends Enchantment {

    public EnchantmentMending(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BREAKABLE, aenumitemslot);
        this.setName("mending");
    }

    public int getMinEnchantability(int i) {
        return i * 25;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 50;
    }

    public boolean isTreasureEnchantment() {
        return true;
    }

    public int getMaxLevel() {
        return 1;
    }
}
