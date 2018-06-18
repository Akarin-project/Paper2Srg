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

    private static final String[] DAMAGE_NAMES = new String[] { "all", "undead", "arthropods"};
    private static final int[] MIN_COST = new int[] { 1, 5, 5};
    private static final int[] LEVEL_COST = new int[] { 11, 8, 8};
    private static final int[] LEVEL_COST_SPAN = new int[] { 20, 20, 20};
    public final int damageType;

    public EnchantmentDamage(Enchantment.Rarity enchantment_rarity, int i, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.WEAPON, aenumitemslot);
        this.damageType = i;
    }

    public int getMinEnchantability(int i) {
        return EnchantmentDamage.MIN_COST[this.damageType] + (i - 1) * EnchantmentDamage.LEVEL_COST[this.damageType];
    }

    public int getMaxEnchantability(int i) {
        return this.getMinEnchantability(i) + EnchantmentDamage.LEVEL_COST_SPAN[this.damageType];
    }

    public int getMaxLevel() {
        return 5;
    }

    public float calcDamageByCreature(int i, EnumCreatureAttribute enummonstertype) {
        return this.damageType == 0 ? 1.0F + (float) Math.max(0, i - 1) * 0.5F : (this.damageType == 1 && enummonstertype == EnumCreatureAttribute.UNDEAD ? (float) i * 2.5F : (this.damageType == 2 && enummonstertype == EnumCreatureAttribute.ARTHROPOD ? (float) i * 2.5F : 0.0F));
    }

    public String getName() {
        return "enchantment.damage." + EnchantmentDamage.DAMAGE_NAMES[this.damageType];
    }

    public boolean canApplyTogether(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentDamage);
    }

    public boolean canApply(ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemAxe ? true : super.canApply(itemstack);
    }

    public void onEntityDamaged(EntityLivingBase entityliving, Entity entity, int i) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityliving1 = (EntityLivingBase) entity;

            if (this.damageType == 2 && entityliving1.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                int j = 20 + entityliving.getRNG().nextInt(10 * i);

                entityliving1.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, j, 3));
            }
        }

    }
}
