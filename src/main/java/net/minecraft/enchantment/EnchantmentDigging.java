package net.minecraft.enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;


public class EnchantmentDigging extends Enchantment {

    protected EnchantmentDigging(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.DIGGER, aenumitemslot);
        this.setName("digging");
    }

    public int getMinEnchantability(int i) {
        return 1 + 10 * (i - 1);
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean canApply(ItemStack itemstack) {
        return itemstack.getItem() == Items.SHEARS ? true : super.canApply(itemstack);
    }
}
