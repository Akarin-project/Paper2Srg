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

    private static final EnchantmentHelper.ModifierDamage ENCHANTMENT_MODIFIER_DAMAGE = new EnchantmentHelper.ModifierDamage(null);
    private static final EnchantmentHelper.ModifierLiving ENCHANTMENT_MODIFIER_LIVING = new EnchantmentHelper.ModifierLiving(null);
    private static final EnchantmentHelper.HurtIterator ENCHANTMENT_ITERATOR_HURT = new EnchantmentHelper.HurtIterator(null);
    private static final EnchantmentHelper.DamageIterator ENCHANTMENT_ITERATOR_DAMAGE = new EnchantmentHelper.DamageIterator(null);

    public static int getEnchantmentLevel(Enchantment enchantment, ItemStack itemstack) {
        if (itemstack.isEmpty()) {
            return 0;
        } else {
            NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                Enchantment enchantment1 = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
                short short0 = nbttagcompound.getShort("lvl");

                if (enchantment1 == enchantment) {
                    return short0;
                }
            }

            return 0;
        }
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack itemstack) {
        LinkedHashMap linkedhashmap = Maps.newLinkedHashMap();
        NBTTagList nbttaglist = itemstack.getItem() == Items.ENCHANTED_BOOK ? ItemEnchantedBook.getEnchantments(itemstack) : itemstack.getEnchantmentTagList();

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            Enchantment enchantment = Enchantment.getEnchantmentByID(nbttagcompound.getShort("id"));
            short short0 = nbttagcompound.getShort("lvl");

            linkedhashmap.put(enchantment, Integer.valueOf(short0));
        }

        return linkedhashmap;
    }

    public static void setEnchantments(Map<Enchantment, Integer> map, ItemStack itemstack) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            Enchantment enchantment = (Enchantment) entry.getKey();

            if (enchantment != null) {
                int i = ((Integer) entry.getValue()).intValue();
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setShort("id", (short) Enchantment.getEnchantmentID(enchantment));
                nbttagcompound.setShort("lvl", (short) i);
                nbttaglist.appendTag(nbttagcompound);
                if (itemstack.getItem() == Items.ENCHANTED_BOOK) {
                    ItemEnchantedBook.addEnchantment(itemstack, new EnchantmentData(enchantment, i));
                }
            }
        }

        if (nbttaglist.hasNoTags()) {
            if (itemstack.hasTagCompound()) {
                itemstack.getTagCompound().removeTag("ench");
            }
        } else if (itemstack.getItem() != Items.ENCHANTED_BOOK) {
            itemstack.setTagInfo("ench", (NBTBase) nbttaglist);
        }

    }

    private static void applyEnchantmentModifier(EnchantmentHelper.IModifier enchantmentmanager_enchantmentmodifier, ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                short short0 = nbttaglist.getCompoundTagAt(i).getShort("id");
                short short1 = nbttaglist.getCompoundTagAt(i).getShort("lvl");

                if (Enchantment.getEnchantmentByID(short0) != null) {
                    enchantmentmanager_enchantmentmodifier.calculateModifier(Enchantment.getEnchantmentByID(short0), short1);
                }
            }

        }
    }

    private static void applyEnchantmentModifierArray(EnchantmentHelper.IModifier enchantmentmanager_enchantmentmodifier, Iterable<ItemStack> iterable) {
        Iterator iterator = iterable.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            applyEnchantmentModifier(enchantmentmanager_enchantmentmodifier, itemstack);
        }

    }

    public static int getEnchantmentModifierDamage(Iterable<ItemStack> iterable, DamageSource damagesource) {
        EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE.damageModifier = 0;
        EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE.source = damagesource;
        applyEnchantmentModifierArray((EnchantmentHelper.IModifier) EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE, iterable);
        return EnchantmentHelper.ENCHANTMENT_MODIFIER_DAMAGE.damageModifier;
    }

    public static float getModifierForCreature(ItemStack itemstack, EnumCreatureAttribute enummonstertype) {
        EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING.livingModifier = 0.0F;
        EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING.entityLiving = enummonstertype;
        applyEnchantmentModifier((EnchantmentHelper.IModifier) EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING, itemstack);
        return EnchantmentHelper.ENCHANTMENT_MODIFIER_LIVING.livingModifier;
    }

    public static float getSweepingDamageRatio(EntityLivingBase entityliving) {
        int i = getMaxEnchantmentLevel(Enchantments.SWEEPING, entityliving);

        return i > 0 ? EnchantmentSweepingEdge.getSweepingDamageRatio(i) : 0.0F;
    }

    public static void applyThornEnchantments(EntityLivingBase entityliving, Entity entity) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.attacker = entity;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT.user = entityliving;
        if (entityliving != null) {
            applyEnchantmentModifierArray((EnchantmentHelper.IModifier) EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT, entityliving.getEquipmentAndArmor());
        }

        if (entity instanceof EntityPlayer) {
            applyEnchantmentModifier((EnchantmentHelper.IModifier) EnchantmentHelper.ENCHANTMENT_ITERATOR_HURT, entityliving.getHeldItemMainhand());
        }

    }

    public static void applyArthropodEnchantments(EntityLivingBase entityliving, Entity entity) {
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.user = entityliving;
        EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE.target = entity;
        if (entityliving != null) {
            applyEnchantmentModifierArray((EnchantmentHelper.IModifier) EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE, entityliving.getEquipmentAndArmor());
        }

        if (entityliving instanceof EntityPlayer) {
            applyEnchantmentModifier((EnchantmentHelper.IModifier) EnchantmentHelper.ENCHANTMENT_ITERATOR_DAMAGE, entityliving.getHeldItemMainhand());
        }

    }

    public static int getMaxEnchantmentLevel(Enchantment enchantment, EntityLivingBase entityliving) {
        List list = enchantment.getEntityEquipment(entityliving);

        if (list == null) {
            return 0;
        } else {
            int i = 0;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();
                int j = getEnchantmentLevel(enchantment, itemstack);

                if (j > i) {
                    i = j;
                }
            }

            return i;
        }
    }

    public static int getKnockbackModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.KNOCKBACK, entityliving);
    }

    public static int getFireAspectModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.FIRE_ASPECT, entityliving);
    }

    public static int getRespirationModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.RESPIRATION, entityliving);
    }

    public static int getDepthStriderModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.DEPTH_STRIDER, entityliving);
    }

    public static int getEfficiencyModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.EFFICIENCY, entityliving);
    }

    public static int getFishingLuckBonus(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, itemstack);
    }

    public static int getFishingSpeedBonus(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.LURE, itemstack);
    }

    public static int getLootingModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.LOOTING, entityliving);
    }

    public static boolean getAquaAffinityModifier(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.AQUA_AFFINITY, entityliving) > 0;
    }

    public static boolean hasFrostWalkerEnchantment(EntityLivingBase entityliving) {
        return getMaxEnchantmentLevel(Enchantments.FROST_WALKER, entityliving) > 0;
    }

    public static boolean hasBindingCurse(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.BINDING_CURSE, itemstack) > 0;
    }

    public static boolean hasVanishingCurse(ItemStack itemstack) {
        return getEnchantmentLevel(Enchantments.VANISHING_CURSE, itemstack) > 0;
    }

    public static ItemStack getRandomEquippedItemWithEnchant(Enchantment enchantment, EntityLivingBase entityliving) { return getEnchantedItem(enchantment, entityliving); } // Paper - OBFHELPER
    public static ItemStack getEnchantedItem(Enchantment enchantment, EntityLivingBase entityliving) {
        List list = enchantment.getEntityEquipment(entityliving);

        if (list.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ArrayList arraylist = Lists.newArrayList();
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                if (!itemstack.isEmpty() && getEnchantmentLevel(enchantment, itemstack) > 0) {
                    arraylist.add(itemstack);
                }
            }

            return arraylist.isEmpty() ? ItemStack.EMPTY : (ItemStack) arraylist.get(entityliving.getRNG().nextInt(arraylist.size()));
        }
    }

    public static int calcItemStackEnchantability(Random random, int i, int j, ItemStack itemstack) {
        Item item = itemstack.getItem();
        int k = item.getItemEnchantability();

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

    public static ItemStack addRandomEnchantment(Random random, ItemStack itemstack, int i, boolean flag) {
        List list = buildEnchantmentList(random, itemstack, i, flag);
        boolean flag1 = itemstack.getItem() == Items.BOOK;

        if (flag1) {
            itemstack = new ItemStack(Items.ENCHANTED_BOOK);
        }

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EnchantmentData weightedrandomenchant = (EnchantmentData) iterator.next();

            if (flag1) {
                ItemEnchantedBook.addEnchantment(itemstack, weightedrandomenchant);
            } else {
                itemstack.addEnchantment(weightedrandomenchant.enchantment, weightedrandomenchant.enchantmentLevel);
            }
        }

        return itemstack;
    }

    public static List<EnchantmentData> buildEnchantmentList(Random random, ItemStack itemstack, int i, boolean flag) {
        ArrayList arraylist = Lists.newArrayList();
        Item item = itemstack.getItem();
        int j = item.getItemEnchantability();

        if (j <= 0) {
            return arraylist;
        } else {
            i += 1 + random.nextInt(j / 4 + 1) + random.nextInt(j / 4 + 1);
            float f = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;

            i = MathHelper.clamp(Math.round((float) i + (float) i * f), 1, Integer.MAX_VALUE);
            List list = getEnchantmentDatas(i, itemstack, flag);

            if (!list.isEmpty()) {
                arraylist.add(WeightedRandom.getRandomItem(random, list));

                while (random.nextInt(50) <= i) {
                    removeIncompatible(list, (EnchantmentData) Util.getLastElement(arraylist));
                    if (list.isEmpty()) {
                        break;
                    }

                    arraylist.add(WeightedRandom.getRandomItem(random, list));
                    i /= 2;
                }
            }

            return arraylist;
        }
    }

    public static void removeIncompatible(List<EnchantmentData> list, EnchantmentData weightedrandomenchant) {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            if (!weightedrandomenchant.enchantment.isCompatibleWith(((EnchantmentData) iterator.next()).enchantment)) {
                iterator.remove();
            }
        }

    }

    public static List<EnchantmentData> getEnchantmentDatas(int i, ItemStack itemstack, boolean flag) {
        ArrayList arraylist = Lists.newArrayList();
        Item item = itemstack.getItem();
        boolean flag1 = itemstack.getItem() == Items.BOOK;
        Iterator iterator = Enchantment.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Enchantment enchantment = (Enchantment) iterator.next();

            if ((!enchantment.isTreasureEnchantment() || flag) && (enchantment.type.canEnchantItem(item) || flag1)) {
                for (int j = enchantment.getMaxLevel(); j > enchantment.getMinLevel() - 1; --j) {
                    if (i >= enchantment.getMinEnchantability(j) && i <= enchantment.getMaxEnchantability(j)) {
                        arraylist.add(new EnchantmentData(enchantment, j));
                        break;
                    }
                }
            }
        }

        return arraylist;
    }

    static final class DamageIterator implements EnchantmentHelper.IModifier {

        public EntityLivingBase user;
        public Entity target;

        private DamageIterator() {}

        public void calculateModifier(Enchantment enchantment, int i) {
            enchantment.onEntityDamaged(this.user, this.target, i);
        }

        DamageIterator(Object object) {
            this();
        }
    }

    static final class HurtIterator implements EnchantmentHelper.IModifier {

        public EntityLivingBase user;
        public Entity attacker;

        private HurtIterator() {}

        public void calculateModifier(Enchantment enchantment, int i) {
            enchantment.onUserHurt(this.user, this.attacker, i);
        }

        HurtIterator(Object object) {
            this();
        }
    }

    static final class ModifierLiving implements EnchantmentHelper.IModifier {

        public float livingModifier;
        public EnumCreatureAttribute entityLiving;

        private ModifierLiving() {}

        public void calculateModifier(Enchantment enchantment, int i) {
            this.livingModifier += enchantment.calcDamageByCreature(i, this.entityLiving);
        }

        ModifierLiving(Object object) {
            this();
        }
    }

    static final class ModifierDamage implements EnchantmentHelper.IModifier {

        public int damageModifier;
        public DamageSource source;

        private ModifierDamage() {}

        public void calculateModifier(Enchantment enchantment, int i) {
            this.damageModifier += enchantment.calcModifierDamage(i, this.source);
        }

        ModifierDamage(Object object) {
            this();
        }
    }

    interface IModifier {

        void calculateModifier(Enchantment enchantment, int i);
    }
}
