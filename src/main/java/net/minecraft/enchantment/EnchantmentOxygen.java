package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentOxygen extends Enchantment {

    public EnchantmentOxygen(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_HEAD, aenumitemslot);
        this.func_77322_b("oxygen");
    }

    public int func_77321_a(int i) {
        return 10 * i;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 30;
    }

    public int func_77325_b() {
        return 3;
    }
}
