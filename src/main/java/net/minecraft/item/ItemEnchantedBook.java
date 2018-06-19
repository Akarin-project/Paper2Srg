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

    public boolean func_77616_k(ItemStack itemstack) {
        return false;
    }

    public EnumRarity func_77613_e(ItemStack itemstack) {
        return func_92110_g(itemstack).func_82582_d() ? super.func_77613_e(itemstack) : EnumRarity.UNCOMMON;
    }

    public static NBTTagList func_92110_g(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        return nbttagcompound != null ? nbttagcompound.func_150295_c("StoredEnchantments", 10) : new NBTTagList();
    }

    public static void func_92115_a(ItemStack itemstack, EnchantmentData weightedrandomenchant) {
        NBTTagList nbttaglist = func_92110_g(itemstack);
        boolean flag = true;

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);

            if (Enchantment.func_185262_c(nbttagcompound.func_74765_d("id")) == weightedrandomenchant.field_76302_b) {
                if (nbttagcompound.func_74765_d("lvl") < weightedrandomenchant.field_76303_c) {
                    nbttagcompound.func_74777_a("lvl", (short) weightedrandomenchant.field_76303_c);
                }

                flag = false;
                break;
            }
        }

        if (flag) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.func_74777_a("id", (short) Enchantment.func_185258_b(weightedrandomenchant.field_76302_b));
            nbttagcompound1.func_74777_a("lvl", (short) weightedrandomenchant.field_76303_c);
            nbttaglist.func_74742_a(nbttagcompound1);
        }

        if (!itemstack.func_77942_o()) {
            itemstack.func_77982_d(new NBTTagCompound());
        }

        itemstack.func_77978_p().func_74782_a("StoredEnchantments", nbttaglist);
    }

    public static ItemStack func_92111_a(EnchantmentData weightedrandomenchant) {
        ItemStack itemstack = new ItemStack(Items.field_151134_bR);

        func_92115_a(itemstack, weightedrandomenchant);
        return itemstack;
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        Iterator iterator;
        Enchantment enchantment;

        if (creativemodetab == CreativeTabs.field_78027_g) {
            iterator = Enchantment.field_185264_b.iterator();

            while (iterator.hasNext()) {
                enchantment = (Enchantment) iterator.next();
                if (enchantment.field_77351_y != null) {
                    for (int i = enchantment.func_77319_d(); i <= enchantment.func_77325_b(); ++i) {
                        nonnulllist.add(func_92111_a(new EnchantmentData(enchantment, i)));
                    }
                }
            }
        } else if (creativemodetab.func_111225_m().length != 0) {
            iterator = Enchantment.field_185264_b.iterator();

            while (iterator.hasNext()) {
                enchantment = (Enchantment) iterator.next();
                if (creativemodetab.func_111226_a(enchantment.field_77351_y)) {
                    nonnulllist.add(func_92111_a(new EnchantmentData(enchantment, enchantment.func_77325_b())));
                }
            }
        }

    }
}
