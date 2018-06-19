package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowKnockback extends Enchantment {

    public EnchantmentArrowKnockback(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.func_77322_b("arrowKnockback");
    }

    public int func_77321_a(int i) {
        return 12 + (i - 1) * 20;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 25;
    }

    public int func_77325_b() {
        return 2;
    }
}
