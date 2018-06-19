package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentWaterWorker extends Enchantment {

    public EnchantmentWaterWorker(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_HEAD, aenumitemslot);
        this.func_77322_b("waterWorker");
    }

    public int func_77321_a(int i) {
        return 1;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 40;
    }

    public int func_77325_b() {
        return 1;
    }
}
