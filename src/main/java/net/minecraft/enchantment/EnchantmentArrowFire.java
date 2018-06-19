package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowFire extends Enchantment {

    public EnchantmentArrowFire(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.func_77322_b("arrowFire");
    }

    public int func_77321_a(int i) {
        return 20;
    }

    public int func_77317_b(int i) {
        return 50;
    }

    public int func_77325_b() {
        return 1;
    }
}
