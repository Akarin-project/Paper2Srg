package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentWaterWorker extends Enchantment {

    public EnchantmentWaterWorker(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_HEAD, aenumitemslot);
        this.setName("waterWorker");
    }

    public int getMinEnchantability(int i) {
        return 1;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 40;
    }

    public int getMaxLevel() {
        return 1;
    }
}
