package net.minecraft.enchantment;
import net.minecraft.util.WeightedRandom;


public class EnchantmentData extends WeightedRandom.Item {

    public final Enchantment enchantment;
    public final int enchantmentLevel;

    public EnchantmentData(Enchantment enchantment, int i) {
        super(enchantment.getRarity().getWeight());
        this.enchantment = enchantment;
        this.enchantmentLevel = i;
    }
}
