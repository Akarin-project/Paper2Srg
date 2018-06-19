package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowDamage extends Enchantment {

    public EnchantmentArrowDamage(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.func_77322_b("arrowDamage");
    }

    public int func_77321_a(int i) {
        return 1 + (i - 1) * 10;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 15;
    }

    public int func_77325_b() {
        return 5;
    }
}
