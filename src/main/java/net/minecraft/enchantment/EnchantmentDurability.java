package net.minecraft.enchantment;

import java.util.Random;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class EnchantmentDurability extends Enchantment {

    protected EnchantmentDurability(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.BREAKABLE, aenumitemslot);
        this.setName("durability");
    }

    public int getMinEnchantability(int i) {
        return 5 + (i - 1) * 8;
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean canApply(ItemStack itemstack) {
        return itemstack.isItemStackDamageable() ? true : super.canApply(itemstack);
    }

    public static boolean negateDamage(ItemStack itemstack, int i, Random random) {
        return itemstack.getItem() instanceof ItemArmor && random.nextFloat() < 0.6F ? false : random.nextInt(i + 1) > 0;
    }
}
