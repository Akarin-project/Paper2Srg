package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public abstract class Enchantment {

    public static final RegistryNamespaced<ResourceLocation, Enchantment> REGISTRY = new RegistryNamespaced();
    private final EntityEquipmentSlot[] applicableEquipmentTypes;
    private final Enchantment.Rarity rarity;
    @Nullable
    public EnumEnchantmentType type;
    protected String name;

    @Nullable
    public static Enchantment getEnchantmentByID(int i) {
        return (Enchantment) Enchantment.REGISTRY.getObjectById(i);
    }

    public static int getEnchantmentID(Enchantment enchantment) {
        return Enchantment.REGISTRY.getIDForObject(enchantment); // CraftBukkit - fix decompile error
    }

    @Nullable
    public static Enchantment getEnchantmentByLocation(String s) {
        return (Enchantment) Enchantment.REGISTRY.getObject(new ResourceLocation(s));
    }

    protected Enchantment(Enchantment.Rarity enchantment_rarity, EnumEnchantmentType enchantmentslottype, EntityEquipmentSlot[] aenumitemslot) {
        this.rarity = enchantment_rarity;
        this.type = enchantmentslottype;
        this.applicableEquipmentTypes = aenumitemslot;
    }

    public List<ItemStack> getEntityEquipment(EntityLivingBase entityliving) {
        ArrayList arraylist = Lists.newArrayList();
        EntityEquipmentSlot[] aenumitemslot = this.applicableEquipmentTypes;
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[j];
            ItemStack itemstack = entityliving.getItemStackFromSlot(enumitemslot);

            if (!itemstack.isEmpty()) {
                arraylist.add(itemstack);
            }
        }

        return arraylist;
    }

    public Enchantment.Rarity getRarity() {
        return this.rarity;
    }

    public int getMinLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getMinEnchantability(int i) {
        return 1 + i * 10;
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + 5;
    }

    public int calcModifierDamage(int i, DamageSource damagesource) {
        return 0;
    }

    public float calcDamageByCreature(int i, EnumCreatureAttribute enummonstertype) {
        return 0.0F;
    }

    public final boolean isCompatibleWith(Enchantment enchantment) {
        return this.canApplyTogether(enchantment) && enchantment.canApplyTogether(this);
    }

    protected boolean canApplyTogether(Enchantment enchantment) {
        return this != enchantment;
    }

    public Enchantment setName(String s) {
        this.name = s;
        return this;
    }

    public String getName() {
        return "enchantment." + this.name;
    }

    public String getTranslatedName(int i) {
        String s = I18n.translateToLocal(this.getName());

        if (this.isCurse()) {
            s = TextFormatting.RED + s;
        }

        return i == 1 && this.getMaxLevel() == 1 ? s : s + " " + I18n.translateToLocal("enchantment.level." + i);
    }

    public boolean canApply(ItemStack itemstack) {
        return this.type.canEnchantItem(itemstack.getItem());
    }

    public void onEntityDamaged(EntityLivingBase entityliving, Entity entity, int i) {}

    public void onUserHurt(EntityLivingBase entityliving, Entity entity, int i) {}

    public boolean isTreasureEnchantment() {
        return false;
    }

    public boolean isCurse() {
        return false;
    }

    public static void registerEnchantments() {
        EntityEquipmentSlot[] aenumitemslot = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

        Enchantment.REGISTRY.register(0, new ResourceLocation("protection"), new EnchantmentProtection(Enchantment.Rarity.COMMON, EnchantmentProtection.Type.ALL, aenumitemslot));
        Enchantment.REGISTRY.register(1, new ResourceLocation("fire_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FIRE, aenumitemslot));
        Enchantment.REGISTRY.register(2, new ResourceLocation("feather_falling"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FALL, aenumitemslot));
        Enchantment.REGISTRY.register(3, new ResourceLocation("blast_protection"), new EnchantmentProtection(Enchantment.Rarity.RARE, EnchantmentProtection.Type.EXPLOSION, aenumitemslot));
        Enchantment.REGISTRY.register(4, new ResourceLocation("projectile_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.PROJECTILE, aenumitemslot));
        Enchantment.REGISTRY.register(5, new ResourceLocation("respiration"), new EnchantmentOxygen(Enchantment.Rarity.RARE, aenumitemslot));
        Enchantment.REGISTRY.register(6, new ResourceLocation("aqua_affinity"), new EnchantmentWaterWorker(Enchantment.Rarity.RARE, aenumitemslot));
        Enchantment.REGISTRY.register(7, new ResourceLocation("thorns"), new EnchantmentThorns(Enchantment.Rarity.VERY_RARE, aenumitemslot));
        Enchantment.REGISTRY.register(8, new ResourceLocation("depth_strider"), new EnchantmentWaterWalker(Enchantment.Rarity.RARE, aenumitemslot));
        Enchantment.REGISTRY.register(9, new ResourceLocation("frost_walker"), new EnchantmentFrostWalker(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET}));
        Enchantment.REGISTRY.register(10, new ResourceLocation("binding_curse"), new EnchantmentBindingCurse(Enchantment.Rarity.VERY_RARE, aenumitemslot));
        Enchantment.REGISTRY.register(16, new ResourceLocation("sharpness"), new EnchantmentDamage(Enchantment.Rarity.COMMON, 0, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(17, new ResourceLocation("smite"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 1, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(18, new ResourceLocation("bane_of_arthropods"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 2, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(19, new ResourceLocation("knockback"), new EnchantmentKnockback(Enchantment.Rarity.UNCOMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(20, new ResourceLocation("fire_aspect"), new EnchantmentFireAspect(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(21, new ResourceLocation("looting"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(22, new ResourceLocation("sweeping"), new EnchantmentSweepingEdge(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(32, new ResourceLocation("efficiency"), new EnchantmentDigging(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(33, new ResourceLocation("silk_touch"), new EnchantmentUntouching(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(34, new ResourceLocation("unbreaking"), new EnchantmentDurability(Enchantment.Rarity.UNCOMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(35, new ResourceLocation("fortune"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(48, new ResourceLocation("power"), new EnchantmentArrowDamage(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(49, new ResourceLocation("punch"), new EnchantmentArrowKnockback(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(50, new ResourceLocation("flame"), new EnchantmentArrowFire(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(51, new ResourceLocation("infinity"), new EnchantmentArrowInfinite(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(61, new ResourceLocation("luck_of_the_sea"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(62, new ResourceLocation("lure"), new EnchantmentFishingSpeed(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.REGISTRY.register(70, new ResourceLocation("mending"), new EnchantmentMending(Enchantment.Rarity.RARE, EntityEquipmentSlot.values()));
        Enchantment.REGISTRY.register(71, new ResourceLocation("vanishing_curse"), new EnchantmentVanishingCurse(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.values()));
        // CraftBukkit start
        for (Object enchantment : Enchantment.REGISTRY) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new org.bukkit.craftbukkit.enchantments.CraftEnchantment((Enchantment) enchantment));
        }
        // CraftBukkit end
    }

    public static enum Rarity {

        COMMON(10), UNCOMMON(5), RARE(2), VERY_RARE(1);

        private final int weight;

        private Rarity(int i) {
            this.weight = i;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
