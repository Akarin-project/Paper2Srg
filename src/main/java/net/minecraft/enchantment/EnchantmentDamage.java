package net.minecraft.enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;


public class EnchantmentDamage extends Enchantment {

    private static final String[] field_77359_A = new String[] { "all", "undead", "arthropods"};
    private static final int[] field_77360_B = new int[] { 1, 5, 5};
    private static final int[] field_77362_C = new int[] { 11, 8, 8};
    private static final int[] field_77358_D = new int[] { 20, 20, 20};
    public final int field_77361_a;

    public EnchantmentDamage(Enchantment.Rarity enchantment_rarity, int i, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
        this.field_77361_a = i;
    }

    public int func_77321_a(int i) {
        return EnchantmentDamage.field_77360_B[this.field_77361_a] + (i - 1) * EnchantmentDamage.field_77362_C[this.field_77361_a];
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + EnchantmentDamage.field_77358_D[this.field_77361_a];
    }

    public int func_77325_b() {
        return 5;
    }

    public float func_152376_a(int i, EnumCreatureAttribute enummonstertype) {
        return this.field_77361_a == 0 ? 1.0F + (float) Math.max(0, i - 1) * 0.5F : (this.field_77361_a == 1 && enummonstertype == EnumCreatureAttribute.UNDEAD ? (float) i * 2.5F : (this.field_77361_a == 2 && enummonstertype == EnumCreatureAttribute.ARTHROPOD ? (float) i * 2.5F : 0.0F));
    }

    public String func_77320_a() {
        return "enchantment.damage." + EnchantmentDamage.field_77359_A[this.field_77361_a];
    }

    public boolean func_77326_a(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentDamage);
    }

    public boolean func_92089_a(ItemStack itemstack) {
        return itemstack.func_77973_b() instanceof ItemAxe ? true : super.func_92089_a(itemstack);
    }

    public void func_151368_a(EntityLivingBase entityliving, Entity entity, int i) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityliving1 = (EntityLivingBase) entity;

            if (this.field_77361_a == 2 && entityliving1.func_70668_bt() == EnumCreatureAttribute.ARTHROPOD) {
                int j = 20 + entityliving.func_70681_au().nextInt(10 * i);

                entityliving1.func_70690_d(new PotionEffect(MobEffects.field_76421_d, j, 3));
            }
        }

    }
}
