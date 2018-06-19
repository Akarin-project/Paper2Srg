package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentArrowInfinite extends Enchantment {

    public EnchantmentArrowInfinite(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BOW, aenumitemslot);
        this.func_77322_b("arrowInfinite");
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

    public boolean func_77326_a(Enchantment enchantment) {
        return enchantment instanceof EnchantmentMending ? false : super.func_77326_a(enchantment);
    }
}
