package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentSweepingEdge extends Enchantment {

    public EnchantmentSweepingEdge(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
    }

    public int func_77321_a(int i) {
        return 5 + (i - 1) * 9;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 15;
    }

    public int func_77325_b() {
        return 3;
    }

    public static float func_191526_e(int i) {
        return 1.0F - 1.0F / (float) (i + 1);
    }

    public String func_77320_a() {
        return "enchantment.sweeping";
    }
}
