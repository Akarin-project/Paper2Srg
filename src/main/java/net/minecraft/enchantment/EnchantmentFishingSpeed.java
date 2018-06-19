package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentFishingSpeed extends Enchantment {

    protected EnchantmentFishingSpeed(Enchantment.Rarity enchantment_rarity, EnumEnchantmentType enchantmentslottype, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, enchantmentslottype, aenumitemslot);
        this.func_77322_b("fishingSpeed");
    }

    public int func_77321_a(int i) {
        return 15 + (i - 1) * 9;
    }

    public int func_77317_b(int i) {
        return super.func_77321_a(i) + 50;
    }

    public int func_77325_b() {
        return 3;
    }
}
