package net.minecraft.enchantment;

import java.util.Random;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentDurability extends Enchantment {

    protected EnchantmentDurability(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BREAKABLE, aenumitemslot);
        this.func_77322_b("durability");
    }

    public int func_77321_a(int i) {
        return 5 + (i - 1) * 8;
    }

    public int func_77317_b(int i) {
        return super.func_77321_a(i) + 50;
    }

    public int func_77325_b() {
        return 3;
    }

    public boolean func_92089_a(ItemStack itemstack) {
        return itemstack.func_77984_f() ? true : super.func_92089_a(itemstack);
    }

    public static boolean func_92097_a(ItemStack itemstack, int i, Random random) {
        return itemstack.func_77973_b() instanceof ItemArmor && random.nextFloat() < 0.6F ? false : random.nextInt(i + 1) > 0;
    }
}
