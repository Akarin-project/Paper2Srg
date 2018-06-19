package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentVanishingCurse extends Enchantment {

    public EnchantmentVanishingCurse(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ALL, aenumitemslot);
        this.func_77322_b("vanishing_curse");
    }

    public int func_77321_a(int i) {
        return 25;
    }

    public int func_77317_b(int i) {
        return 50;
    }

    public int func_77325_b() {
        return 1;
    }

    public boolean func_185261_e() {
        return true;
    }

    public boolean func_190936_d() {
        return true;
    }
}
