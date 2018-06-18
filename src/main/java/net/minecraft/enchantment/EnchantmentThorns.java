package net.minecraft.enchantment;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EnchantmentThorns extends Enchantment {

    public EnchantmentThorns(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_CHEST, aenumitemslot);
        this.setName("thorns");
    }

    public int getMinEnchantability(int i) {
        return 10 + 20 * (i - 1);
    }

    public int getMaxEnchantability(int i) {
        return super.getMinEnchantability(i) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean canApply(ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemArmor ? true : super.canApply(itemstack);
    }

    public void onUserHurt(EntityLivingBase entityliving, Entity entity, int i) {
        Random random = entityliving.getRNG();
        ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.THORNS, entityliving);

        if (entity != null && shouldHit(i, random)) { // CraftBukkit
            if (entity != null) {
                entity.attackEntityFrom(DamageSource.causeThornsDamage(entityliving), (float) getDamage(i, random));
            }

            if (!itemstack.isEmpty()) {
                itemstack.damageItem(3, entityliving);
            }
        } else if (!itemstack.isEmpty()) {
            itemstack.damageItem(1, entityliving);
        }

    }

    public static boolean shouldHit(int i, Random random) {
        return i <= 0 ? false : random.nextFloat() < 0.15F * (float) i;
    }

    public static int getDamage(int i, Random random) {
        return i > 10 ? i - 10 : 1 + random.nextInt(4);
    }
}
