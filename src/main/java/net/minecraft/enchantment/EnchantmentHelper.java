package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;

public class EnchantmentHelper {

    private static final EnchantmentHelper.ModifierDamage field_77520_b = new EnchantmentHelper.ModifierDamage(null);
    private static final EnchantmentHelper.ModifierLiving field_77521_c = new EnchantmentHelper.ModifierLiving(null);
    private static final EnchantmentHelper.HurtIterator field_151388_d = new EnchantmentHelper.HurtIterator(null);
    private static final EnchantmentHelper.DamageIterator field_151389_e = new EnchantmentHelper.DamageIterator(null);

    public static int func_77506_a(Enchantment enchantment, ItemStack itemstack) {
        if (itemstack.func_190926_b()) {
            return 0;
        } else {
            NBTTagList nbttaglist = itemstack.func_77986_q();

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
                Enchantment enchantment1 = Enchantment.func_185262_c(nbttagcompound.func_74765_d("id"));
                short short0 = nbttagcompound.func_74765_d("lvl");

                if (enchantment1 == enchantment) {
                    return short0;
                }
            }

            return 0;
        }
    }

    public static Map<Enchantment, Integer> func_82781_a(ItemStack itemstack) {
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
        NBTTagList nbttaglist = itemstack.func_77973_b() == Items.field_151134_bR ? ItemEnchantedBook.func_92110_g(itemstack) : itemstack.func_77986_q();

        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            Enchantment enchantment = Enchantment.func_185262_c(nbttagcompound.func_74765_d("id"));
            short short0 = nbttagcompound.func_74765_d("lvl");

            linkedhashmap.put(enchantment, Integer.valueOf(short0));
        }

        return linkedhashmap;
    }

    public static void func_82782_a(Map<Enchantment, Integer> map, ItemStack itemstack) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            Enchantment enchantment = (Enchantment) entry.getKey();

            if (enchantment != null) {
                int i = ((Integer) entry.getValue()).intValue();
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.func_74777_a("id", (short) Enchantment.func_185258_b(enchantment));
                nbttagcompound.func_74777_a("lvl", (short) i);
                nbttaglist.func_74742_a(nbttagcompound);
                if (itemstack.func_77973_b() == Items.field_151134_bR) {
                    ItemEnchantedBook.func_92115_a(itemstack, new EnchantmentData(enchantment, i));
                }
            }
        }

        if (nbttaglist.func_82582_d()) {
            if (itemstack.func_77942_o()) {
                itemstack.func_77978_p().func_82580_o("ench");
            }
        } else if (itemstack.func_77973_b() != Items.field_151134_bR) {
            itemstack.func_77983_a("ench", (NBTBase) nbttaglist);
        }

    }

    private static void func_77518_a(EnchantmentHelper.IModifier enchantmentmanager_enchantmentmodifier, ItemStack itemstack) {
        if (!itemstack.func_190926_b()) {
            NBTTagList nbttaglist = itemstack.func_77986_q();

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                short short0 = nbttaglist.func_150305_b(i).func_74765_d("id");
                short short1 = nbttaglist.func_150305_b(i).func_74765_d("lvl");

                if (Enchantment.func_185262_c(short0) != null) {
                    enchantmentmanager_enchantmentmodifier.func_77493_a(Enchantment.func_185262_c(short0), short1);
                }
            }

        }
    }

    private static void func_77516_a(EnchantmentHelper.IModifier enchantmentmanager_enchantmentmodifier, Iterable<ItemStack> iterable) {
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            func_77518_a(enchantmentmanager_enchantmentmodifier, itemstack);
        }

    }

    public static int func_77508_a(Iterable<ItemStack> iterable, DamageSource damagesource) {
        EnchantmentHelper.field_77520_b.field_77497_a = 0;
        EnchantmentHelper.field_77520_b.field_77496_b = damagesource;
        func_77516_a((EnchantmentHelper.IModifier) EnchantmentHelper.field_77520_b, iterable);
        return EnchantmentHelper.field_77520_b.field_77497_a;
    }

    public static float func_152377_a(ItemStack itemstack, EnumCreatureAttribute enummonstertype) {
        EnchantmentHelper.field_77521_c.field_77495_a = 0.0F;
        EnchantmentHelper.field_77521_c.field_77494_b = enummonstertype;
        func_77518_a((EnchantmentHelper.IModifier) EnchantmentHelper.field_77521_c, itemstack);
        return EnchantmentHelper.field_77521_c.field_77495_a;
    }

    public static float func_191527_a(EntityLivingBase entityliving) {
        int i = func_185284_a(Enchantments.field_191530_r, entityliving);

        return i > 0 ? EnchantmentSweepingEdge.func_191526_e(i) : 0.0F;
    }

    public static void func_151384_a(EntityLivingBase entityliving, Entity entity) {
        EnchantmentHelper.field_151388_d.field_151363_b = entity;
        EnchantmentHelper.field_151388_d.field_151364_a = entityliving;
        if (entityliving != null) {
            func_77516_a((EnchantmentHelper.IModifier) EnchantmentHelper.field_151388_d, entityliving.func_184209_aF());
        }

        if (entity instanceof EntityPlayer) {
            func_77518_a((EnchantmentHelper.IModifier) EnchantmentHelper.field_151388_d, entityliving.func_184614_ca());
        }

    }

    public static void func_151385_b(EntityLivingBase entityliving, Entity entity) {
        EnchantmentHelper.field_151389_e.field_151366_a = entityliving;
        EnchantmentHelper.field_151389_e.field_151365_b = entity;
        if (entityliving != null) {
            func_77516_a((EnchantmentHelper.IModifier) EnchantmentHelper.field_151389_e, entityliving.func_184209_aF());
        }

        if (entityliving instanceof EntityPlayer) {
            func_77518_a((EnchantmentHelper.IModifier) EnchantmentHelper.field_151389_e, entityliving.func_184614_ca());
        }

    }

    public static int func_185284_a(Enchantment enchantment, EntityLivingBase entityliving) {
        List list = enchantment.func_185260_a(entityliving);

        if (list == null) {
            return 0;
        } else {
            int i = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();
                int j = func_77506_a(enchantment, itemstack);

                if (j > i) {
                    i = j;
                }
            }

            return i;
        }
    }

    public static int func_77501_a(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_180313_o, entityliving);
    }

    public static int func_90036_a(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_77334_n, entityliving);
    }

    public static int func_185292_c(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_185298_f, entityliving);
    }

    public static int func_185294_d(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_185300_i, entityliving);
    }

    public static int func_185293_e(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_185305_q, entityliving);
    }

    public static int func_191529_b(ItemStack itemstack) {
        return func_77506_a(Enchantments.field_151370_z, itemstack);
    }

    public static int func_191528_c(ItemStack itemstack) {
        return func_77506_a(Enchantments.field_151369_A, itemstack);
    }

    public static int func_185283_h(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_185304_p, entityliving);
    }

    public static boolean func_185287_i(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_185299_g, entityliving) > 0;
    }

    public static boolean func_189869_j(EntityLivingBase entityliving) {
        return func_185284_a(Enchantments.field_185301_j, entityliving) > 0;
    }

    public static boolean func_190938_b(ItemStack itemstack) {
        return func_77506_a(Enchantments.field_190941_k, itemstack) > 0;
    }

    public static boolean func_190939_c(ItemStack itemstack) {
        return func_77506_a(Enchantments.field_190940_C, itemstack) > 0;
    }

    public static ItemStack getRandomEquippedItemWithEnchant(Enchantment enchantment, EntityLivingBase entityliving) { return func_92099_a(enchantment, entityliving); } // Paper - OBFHELPER
    public static ItemStack func_92099_a(Enchantment enchantment, EntityLivingBase entityliving) {
        List list = enchantment.func_185260_a(entityliving);

        if (list.isEmpty()) {
            return ItemStack.field_190927_a;
        } else {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                if (!itemstack.func_190926_b() && func_77506_a(enchantment, itemstack) > 0) {
                    arraylist.add(itemstack);
                }
            }

            return arraylist.isEmpty() ? ItemStack.field_190927_a : (ItemStack) arraylist.get(entityliving.func_70681_au().nextInt(arraylist.size()));
        }
    }

    public static int func_77514_a(Random random, int i, int j, ItemStack itemstack) {
        Item item = itemstack.func_77973_b();
        int k = item.func_77619_b();

        if (k <= 0) {
            return 0;
        } else {
            if (j > 15) {
                j = 15;
            }

            int l = random.nextInt(8) + 1 + (j >> 1) + random.nextInt(j + 1);

            return i == 0 ? Math.max(l / 3, 1) : (i == 1 ? l * 2 / 3 + 1 : Math.max(l, j * 2));
        }
    }

    public static ItemStack func_77504_a(Random random, ItemStack itemstack, int i, boolean flag) {
        List list = func_77513_b(random, itemstack, i, flag);
        boolean flag1 = itemstack.func_77973_b() == Items.field_151122_aG;

        if (flag1) {
            itemstack = new ItemStack(Items.field_151134_bR);
        }

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EnchantmentData weightedrandomenchant = (EnchantmentData) iterator.next();

            if (flag1) {
                ItemEnchantedBook.func_92115_a(itemstack, weightedrandomenchant);
            } else {
                itemstack.func_77966_a(weightedrandomenchant.field_76302_b, weightedrandomenchant.field_76303_c);
            }
        }

        return itemstack;
    }

    public static List<EnchantmentData> func_77513_b(Random random, ItemStack itemstack, int i, boolean flag) {
        ArrayList arraylist = Lists.newArrayList();
        Item item = itemstack.func_77973_b();
        int j = item.func_77619_b();

        if (j <= 0) {
            return arraylist;
        } else {
            i += 1 + random.nextInt(j / 4 + 1) + random.nextInt(j / 4 + 1);
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;

            i = MathHelper.func_76125_a(Math.round((float) i + (float) i * f), 1, Integer.MAX_VALUE);
            List list = func_185291_a(i, itemstack, flag);

            if (!list.isEmpty()) {
                arraylist.add(WeightedRandom.func_76271_a(random, list));

                while (random.nextInt(50) <= i) {
                    func_185282_a(list, (EnchantmentData) Util.func_184878_a(arraylist));
                    if (list.isEmpty()) {
                        break;
                    }

                    arraylist.add(WeightedRandom.func_76271_a(random, list));
                    i /= 2;
                }
            }

            return arraylist;
        }
    }

    public static void func_185282_a(List<EnchantmentData> list, EnchantmentData weightedrandomenchant) {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            if (!weightedrandomenchant.field_76302_b.func_191560_c(((EnchantmentData) iterator.next()).field_76302_b)) {
                iterator.remove();
            }
        }

    }

    public static List<EnchantmentData> func_185291_a(int i, ItemStack itemstack, boolean flag) {
        ArrayList arraylist = Lists.newArrayList();
        Item item = itemstack.func_77973_b();
        boolean flag1 = itemstack.func_77973_b() == Items.field_151122_aG;
        Iterator iterator = Enchantment.field_185264_b.iterator();

        while (iterator.hasNext()) {
            Enchantment enchantment = (Enchantment) iterator.next();

            if ((!enchantment.func_185261_e() || flag) && (enchantment.field_77351_y.func_77557_a(item) || flag1)) {
                for (int j = enchantment.func_77325_b(); j > enchantment.func_77319_d() - 1; --j) {
                    if (i >= enchantment.func_77321_a(j) && i <= enchantment.func_77317_b(j)) {
                        arraylist.add(new EnchantmentData(enchantment, j));
                        break;
                    }
                }
            }
        }

        return arraylist;
    }

    static final class DamageIterator implements EnchantmentHelper.IModifier {

        public EntityLivingBase field_151366_a;
        public Entity field_151365_b;

        private DamageIterator() {}

        public void func_77493_a(Enchantment enchantment, int i) {
            enchantment.func_151368_a(this.field_151366_a, this.field_151365_b, i);
        }

        DamageIterator(Object object) {
            this();
        }
    }

    static final class HurtIterator implements EnchantmentHelper.IModifier {

        public EntityLivingBase field_151364_a;
        public Entity field_151363_b;

        private HurtIterator() {}

        public void func_77493_a(Enchantment enchantment, int i) {
            enchantment.func_151367_b(this.field_151364_a, this.field_151363_b, i);
        }

        HurtIterator(Object object) {
            this();
        }
    }

    static final class ModifierLiving implements EnchantmentHelper.IModifier {

        public float field_77495_a;
        public EnumCreatureAttribute field_77494_b;

        private ModifierLiving() {}

        public void func_77493_a(Enchantment enchantment, int i) {
            this.field_77495_a += enchantment.func_152376_a(i, this.field_77494_b);
        }

        ModifierLiving(Object object) {
            this();
        }
    }

    static final class ModifierDamage implements EnchantmentHelper.IModifier {

        public int field_77497_a;
        public DamageSource field_77496_b;

        private ModifierDamage() {}

        public void func_77493_a(Enchantment enchantment, int i) {
            this.field_77497_a += enchantment.func_77318_a(i, this.field_77496_b);
        }

        ModifierDamage(Object object) {
            this();
        }
    }

    interface IModifier {

        void func_77493_a(Enchantment enchantment, int i);
    }
}
