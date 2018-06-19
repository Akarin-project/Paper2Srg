package net.minecraft.enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentUntouching extends Enchantment {

    protected EnchantmentUntouching(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.DIGGER, aenumitemslot);
        this.func_77322_b("untouching");
    }

    public int func_77321_a(int i) {
        return 15;
    }

    public int func_77317_b(int i) {
        return super.func_77321_a(i) + 50;
    }

    public int func_77325_b() {
        return 1;
    }

    public boolean func_77326_a(Enchantment enchantment) {
        return super.func_77326_a(enchantment) && enchantment != Enchantments.field_185308_t;
    }
}
