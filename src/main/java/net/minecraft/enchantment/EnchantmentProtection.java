package net.minecraft.enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;


public class EnchantmentProtection extends Enchantment {

    public final EnchantmentProtection.Type field_77356_a;

    public EnchantmentProtection(Enchantment.Rarity enchantment_rarity, EnchantmentProtection.Type enchantmentprotection_damagetype, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR, aenumitemslot);
        this.field_77356_a = enchantmentprotection_damagetype;
        if (enchantmentprotection_damagetype == EnchantmentProtection.Type.FALL) {
            this.field_77351_y = EnumEnchantmentType.ARMOR_FEET;
        }

    }

    public int func_77321_a(int i) {
        return this.field_77356_a.func_185316_b() + (i - 1) * this.field_77356_a.func_185315_c();
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + this.field_77356_a.func_185315_c();
    }

    public int func_77325_b() {
        return 4;
    }

    public int func_77318_a(int i, DamageSource damagesource) {
        return damagesource.func_76357_e() ? 0 : (this.field_77356_a == EnchantmentProtection.Type.ALL ? i : (this.field_77356_a == EnchantmentProtection.Type.FIRE && damagesource.func_76347_k() ? i * 2 : (this.field_77356_a == EnchantmentProtection.Type.FALL && damagesource == DamageSource.field_76379_h ? i * 3 : (this.field_77356_a == EnchantmentProtection.Type.EXPLOSION && damagesource.func_94541_c() ? i * 2 : (this.field_77356_a == EnchantmentProtection.Type.PROJECTILE && damagesource.func_76352_a() ? i * 2 : 0)))));
    }

    public String func_77320_a() {
        return "enchantment.protect." + this.field_77356_a.func_185314_a();
    }

    public boolean func_77326_a(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentProtection) {
            EnchantmentProtection enchantmentprotection = (EnchantmentProtection) enchantment;

            return this.field_77356_a == enchantmentprotection.field_77356_a ? false : this.field_77356_a == EnchantmentProtection.Type.FALL || enchantmentprotection.field_77356_a == EnchantmentProtection.Type.FALL;
        } else {
            return super.func_77326_a(enchantment);
        }
    }

    public static int func_92093_a(EntityLivingBase entityliving, int i) {
        int j = EnchantmentHelper.func_185284_a(Enchantments.field_77329_d, entityliving);

        if (j > 0) {
            i -= MathHelper.func_76141_d((float) i * (float) j * 0.15F);
        }

        return i;
    }

    public static double func_92092_a(EntityLivingBase entityliving, double d0) {
        int i = EnchantmentHelper.func_185284_a(Enchantments.field_185297_d, entityliving);

        if (i > 0) {
            d0 -= (double) MathHelper.func_76128_c(d0 * (double) ((float) i * 0.15F));
        }

        return d0;
    }

    public static enum Type {

        ALL("all", 1, 11, 20), FIRE("fire", 10, 8, 12), FALL("fall", 5, 6, 10), EXPLOSION("explosion", 5, 8, 12), PROJECTILE("projectile", 3, 6, 15);

        private final String field_185322_f;
        private final int field_185323_g;
        private final int field_185324_h;
        private final int field_185325_i;

        private Type(String s, int i, int j, int k) {
            this.field_185322_f = s;
            this.field_185323_g = i;
            this.field_185324_h = j;
            this.field_185325_i = k;
        }

        public String func_185314_a() {
            return this.field_185322_f;
        }

        public int func_185316_b() {
            return this.field_185323_g;
        }

        public int func_185315_c() {
            return this.field_185324_h;
        }
    }
}
