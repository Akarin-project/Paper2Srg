package net.minecraft.enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentWaterWalker extends Enchantment {

    public EnchantmentWaterWalker(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_FEET, aenumitemslot);
        this.func_77322_b("waterWalker");
    }

    public int func_77321_a(int i) {
        return i * 10;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 15;
    }

    public int func_77325_b() {
        return 3;
    }

    public boolean func_77326_a(Enchantment enchantment) {
        return super.func_77326_a(enchantment) && enchantment != Enchantments.field_185301_j;
    }
}
