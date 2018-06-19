package net.minecraft.enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;


public class EnchantmentMending extends Enchantment {

    public EnchantmentMending(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BREAKABLE, aenumitemslot);
        this.func_77322_b("mending");
    }

    public int func_77321_a(int i) {
        return i * 25;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 50;
    }

    public boolean func_185261_e() {
        return true;
    }

    public int func_77325_b() {
        return 1;
    }
}
