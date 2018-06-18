package net.minecraft.item;

import java.util.Iterator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class ItemEnchantedBook extends Item {

    public ItemEnchantedBook() {}

    public boolean isEnchantable(ItemStack itemstack) {
        return false;
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return getEnchantments(itemstack).hasNoTags() ? super.getRarity(itemstack) : EnumRarity.UNCOMMON;
    }

    public static NBTTagList getEnchantments(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        return nbttagcompound != null ? nbttagcompound.getTagList("StoredEnchantments", 10) : new NBTTagList();
    }

    public static void addEnchantment(ItemStack itemstack, EnchantmentData weightedrandomenchant) {
        NBTTagList nbttaglist = getEnchantments(itemstack);
        boolean flag = true;

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);

            if (Enchantment.getEnchantmentByID(nbttagcompound.getShort("id")) == weightedrandomenchant.enchantment) {
                if (nbttagcompound.getShort("lvl") < weightedrandomenchant.enchantmentLevel) {
                    nbttagcompound.setShort("lvl", (short) weightedrandomenchant.enchantmentLevel);
                }

                flag = false;
                break;
            }
        }

        if (flag) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setShort("id", (short) Enchantment.getEnchantmentID(weightedrandomenchant.enchantment));
            nbttagcompound1.setShort("lvl", (short) weightedrandomenchant.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound1);
        }

        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }

        itemstack.getTagCompound().setTag("StoredEnchantments", nbttaglist);
    }

    public static ItemStack getEnchantedItemStack(EnchantmentData weightedrandomenchant) {
        ItemStack itemstack = new ItemStack(Items.ENCHANTED_BOOK);

        addEnchantment(itemstack, weightedrandomenchant);
        return itemstack;
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        Iterator iterator;
        Enchantment enchantment;

        if (creativemodetab == CreativeTabs.SEARCH) {
            iterator = Enchantment.REGISTRY.iterator();

            while (iterator.hasNext()) {
                enchantment = (Enchantment) iterator.next();
                if (enchantment.type != null) {
                    for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                        nonnulllist.add(getEnchantedItemStack(new EnchantmentData(enchantment, i)));
                    }
                }
            }
        } else if (creativemodetab.getRelevantEnchantmentTypes().length != 0) {
            iterator = Enchantment.REGISTRY.iterator();

            while (iterator.hasNext()) {
                enchantment = (Enchantment) iterator.next();
                if (creativemodetab.hasRelevantEnchantmentType(enchantment.type)) {
                    nonnulllist.add(getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
                }
            }
        }

    }
}
