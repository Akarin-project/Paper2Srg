package net.minecraft.enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentLootBonus extends Enchantment {

    protected EnchantmentLootBonus(Enchantment.Rarity enchantment_rarity, EnumEnchantmentType enchantmentslottype, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, enchantmentslottype, aenumitemslot);
        if (enchantmentslottype == EnumEnchantmentType.DIGGER) {
            this.func_77322_b("lootBonusDigger");
        } else if (enchantmentslottype == EnumEnchantmentType.FISHING_ROD) {
            this.func_77322_b("lootBonusFishing");
        } else {
            this.func_77322_b("lootBonus");
        }

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

    public boolean func_77326_a(Enchantment enchantment) {
        return super.func_77326_a(enchantment) && enchantment != Enchantments.field_185306_r;
    }
}
