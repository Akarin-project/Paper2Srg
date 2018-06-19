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

    public static final RegistryNamespaced<ResourceLocation, Enchantment> field_185264_b = new RegistryNamespaced();
    private final EntityEquipmentSlot[] field_185263_a;
    private final Enchantment.Rarity field_77333_a;
    @Nullable
    public EnumEnchantmentType field_77351_y;
    protected String field_77350_z;

    @Nullable
    public static Enchantment func_185262_c(int i) {
        return (Enchantment) Enchantment.field_185264_b.func_148754_a(i);
    }

    public static int func_185258_b(Enchantment enchantment) {
        return Enchantment.field_185264_b.func_148757_b(enchantment); // CraftBukkit - fix decompile error
    }

    @Nullable
    public static Enchantment func_180305_b(String s) {
        return (Enchantment) Enchantment.field_185264_b.func_82594_a(new ResourceLocation(s));
    }

    protected Enchantment(Enchantment.Rarity enchantment_rarity, EnumEnchantmentType enchantmentslottype, EntityEquipmentSlot[] aenumitemslot) {
        this.field_77333_a = enchantment_rarity;
        this.field_77351_y = enchantmentslottype;
        this.field_185263_a = aenumitemslot;
    }

    public List<ItemStack> func_185260_a(EntityLivingBase entityliving) {
        ArrayList arraylist = Lists.newArrayList();
        EntityEquipmentSlot[] aenumitemslot = this.field_185263_a;
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[j];
            ItemStack itemstack = entityliving.func_184582_a(enumitemslot);

            if (!itemstack.func_190926_b()) {
                arraylist.add(itemstack);
            }
        }

        return arraylist;
    }

    public Enchantment.Rarity func_77324_c() {
        return this.field_77333_a;
    }

    public int func_77319_d() {
        return 1;
    }

    public int func_77325_b() {
        return 1;
    }

    public int func_77321_a(int i) {
        return 1 + i * 10;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 5;
    }

    public int func_77318_a(int i, DamageSource damagesource) {
        return 0;
    }

    public float func_152376_a(int i, EnumCreatureAttribute enummonstertype) {
        return 0.0F;
    }

    public final boolean func_191560_c(Enchantment enchantment) {
        return this.func_77326_a(enchantment) && enchantment.func_77326_a(this);
    }

    protected boolean func_77326_a(Enchantment enchantment) {
        return this != enchantment;
    }

    public Enchantment func_77322_b(String s) {
        this.field_77350_z = s;
        return this;
    }

    public String func_77320_a() {
        return "enchantment." + this.field_77350_z;
    }

    public String func_77316_c(int i) {
        String s = I18n.func_74838_a(this.func_77320_a());

        if (this.func_190936_d()) {
            s = TextFormatting.RED + s;
        }

        return i == 1 && this.func_77325_b() == 1 ? s : s + " " + I18n.func_74838_a("enchantment.level." + i);
    }

    public boolean func_92089_a(ItemStack itemstack) {
        return this.field_77351_y.func_77557_a(itemstack.func_77973_b());
    }

    public void func_151368_a(EntityLivingBase entityliving, Entity entity, int i) {}

    public void func_151367_b(EntityLivingBase entityliving, Entity entity, int i) {}

    public boolean func_185261_e() {
        return false;
    }

    public boolean func_190936_d() {
        return false;
    }

    public static void func_185257_f() {
        EntityEquipmentSlot[] aenumitemslot = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

        Enchantment.field_185264_b.func_177775_a(0, new ResourceLocation("protection"), new EnchantmentProtection(Enchantment.Rarity.COMMON, EnchantmentProtection.Type.ALL, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(1, new ResourceLocation("fire_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FIRE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(2, new ResourceLocation("feather_falling"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.FALL, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(3, new ResourceLocation("blast_protection"), new EnchantmentProtection(Enchantment.Rarity.RARE, EnchantmentProtection.Type.EXPLOSION, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(4, new ResourceLocation("projectile_protection"), new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.Type.PROJECTILE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(5, new ResourceLocation("respiration"), new EnchantmentOxygen(Enchantment.Rarity.RARE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(6, new ResourceLocation("aqua_affinity"), new EnchantmentWaterWorker(Enchantment.Rarity.RARE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(7, new ResourceLocation("thorns"), new EnchantmentThorns(Enchantment.Rarity.VERY_RARE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(8, new ResourceLocation("depth_strider"), new EnchantmentWaterWalker(Enchantment.Rarity.RARE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(9, new ResourceLocation("frost_walker"), new EnchantmentFrostWalker(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET}));
        Enchantment.field_185264_b.func_177775_a(10, new ResourceLocation("binding_curse"), new EnchantmentBindingCurse(Enchantment.Rarity.VERY_RARE, aenumitemslot));
        Enchantment.field_185264_b.func_177775_a(16, new ResourceLocation("sharpness"), new EnchantmentDamage(Enchantment.Rarity.COMMON, 0, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(17, new ResourceLocation("smite"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 1, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(18, new ResourceLocation("bane_of_arthropods"), new EnchantmentDamage(Enchantment.Rarity.UNCOMMON, 2, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(19, new ResourceLocation("knockback"), new EnchantmentKnockback(Enchantment.Rarity.UNCOMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(20, new ResourceLocation("fire_aspect"), new EnchantmentFireAspect(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(21, new ResourceLocation("looting"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(22, new ResourceLocation("sweeping"), new EnchantmentSweepingEdge(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(32, new ResourceLocation("efficiency"), new EnchantmentDigging(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(33, new ResourceLocation("silk_touch"), new EnchantmentUntouching(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(34, new ResourceLocation("unbreaking"), new EnchantmentDurability(Enchantment.Rarity.UNCOMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(35, new ResourceLocation("fortune"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(48, new ResourceLocation("power"), new EnchantmentArrowDamage(Enchantment.Rarity.COMMON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(49, new ResourceLocation("punch"), new EnchantmentArrowKnockback(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(50, new ResourceLocation("flame"), new EnchantmentArrowFire(Enchantment.Rarity.RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(51, new ResourceLocation("infinity"), new EnchantmentArrowInfinite(Enchantment.Rarity.VERY_RARE, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(61, new ResourceLocation("luck_of_the_sea"), new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(62, new ResourceLocation("lure"), new EnchantmentFishingSpeed(Enchantment.Rarity.RARE, EnumEnchantmentType.FISHING_ROD, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND}));
        Enchantment.field_185264_b.func_177775_a(70, new ResourceLocation("mending"), new EnchantmentMending(Enchantment.Rarity.RARE, EntityEquipmentSlot.values()));
        Enchantment.field_185264_b.func_177775_a(71, new ResourceLocation("vanishing_curse"), new EnchantmentVanishingCurse(Enchantment.Rarity.VERY_RARE, EntityEquipmentSlot.values()));
        // CraftBukkit start
        for (Object enchantment : Enchantment.field_185264_b) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new org.bukkit.craftbukkit.enchantments.CraftEnchantment((Enchantment) enchantment));
        }
        // CraftBukkit end
    }

    public static enum Rarity {

        COMMON(10), UNCOMMON(5), RARE(2), VERY_RARE(1);

        private final int field_185275_e;

        private Rarity(int i) {
            this.field_185275_e = i;
        }

        public int func_185270_a() {
            return this.field_185275_e;
        }
    }
}
