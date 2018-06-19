package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentFireAspect extends Enchantment {

    protected EnchantmentFireAspect(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
        this.func_77322_b("fire");
    }

    public int func_77321_a(int i) {
        return 10 + 20 * (i - 1);
    }

    public int func_77317_b(int i) {
        return super.func_77321_a(i) + 50;
    }

    public int func_77325_b() {
        return 2;
    }
}
