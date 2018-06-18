package net.minecraft.enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;


public class EnchantmentProtection extends Enchantment {

    public final EnchantmentProtection.Type protectionType;

    public EnchantmentProtection(Enchantment.Rarity enchantment_rarity, EnchantmentProtection.Type enchantmentprotection_damagetype, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR, aenumitemslot);
        this.protectionType = enchantmentprotection_damagetype;
        if (enchantmentprotection_damagetype == EnchantmentProtection.Type.FALL) {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }

    }

    public int getMinEnchantability(int i) {
        return this.protectionType.getMinimalEnchantability() + (i - 1) * this.protectionType.getEnchantIncreasePerLevel();
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + this.protectionType.getEnchantIncreasePerLevel();
    }

    public int getMaxLevel() {
        return 4;
    }

    public int calcModifierDamage(int i, DamageSource damagesource) {
        return damagesource.canHarmInCreative() ? 0 : (this.protectionType == EnchantmentProtection.Type.ALL ? i : (this.protectionType == EnchantmentProtection.Type.FIRE && damagesource.isFireDamage() ? i * 2 : (this.protectionType == EnchantmentProtection.Type.FALL && damagesource == DamageSource.FALL ? i * 3 : (this.protectionType == EnchantmentProtection.Type.EXPLOSION && damagesource.isExplosion() ? i * 2 : (this.protectionType == EnchantmentProtection.Type.PROJECTILE && damagesource.isProjectile() ? i * 2 : 0)))));
    }

    public String getName() {
        return "enchantment.protect." + this.protectionType.getTypeName();
    }

    public boolean canApplyTogether(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentProtection) {
            EnchantmentProtection enchantmentprotection = (EnchantmentProtection) enchantment;

            return this.protectionType == enchantmentprotection.protectionType ? false : this.protectionType == EnchantmentProtection.Type.FALL || enchantmentprotection.protectionType == EnchantmentProtection.Type.FALL;
        } else {
            return super.canApplyTogether(enchantment);
        }
    }

    public static int getFireTimeForEntity(EntityLivingBase entityliving, int i) {
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, entityliving);

        if (j > 0) {
            i -= MathHelper.floor((float) i * (float) j * 0.15F);
        }

        return i;
    }

    public static double getBlastDamageReduction(EntityLivingBase entityliving, double d0) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, entityliving);

        if (i > 0) {
            d0 -= (double) MathHelper.floor(d0 * (double) ((float) i * 0.15F));
        }

        return d0;
    }

    public static enum Type {

        ALL("all", 1, 11, 20), FIRE("fire", 10, 8, 12), FALL("fall", 5, 6, 10), EXPLOSION("explosion", 5, 8, 12), PROJECTILE("projectile", 3, 6, 15);

        private final String typeName;
        private final int minEnchantability;
        private final int levelCost;
        private final int levelCostSpan;

        private Type(String s, int i, int j, int k) {
            this.typeName = s;
            this.minEnchantability = i;
            this.levelCost = j;
            this.levelCostSpan = k;
        }

        public String getTypeName() {
            return this.typeName;
        }

        public int getMinimalEnchantability() {
            return this.minEnchantability;
        }

        public int getEnchantIncreasePerLevel() {
            return this.levelCost;
        }
    }
}
