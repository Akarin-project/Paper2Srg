package net.minecraft.enchantment;
import net.minecraft.util.WeightedRandom;


public class EnchantmentData extends WeightedRandom.Item {

    public final Enchantment field_76302_b;
    public final int field_76303_c;

    public EnchantmentData(Enchantment enchantment, int i) {
        super(enchantment.func_77324_c().func_185270_a());
        this.field_76302_b = enchantment;
        this.field_76303_c = i;
    }
}
