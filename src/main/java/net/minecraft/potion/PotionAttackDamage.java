package net.minecraft.potion;
import net.minecraft.entity.ai.attributes.AttributeModifier;


public class PotionAttackDamage extends Potion {

    protected final double field_188416_a;

    protected PotionAttackDamage(boolean flag, int i, double d0) {
        super(flag, i);
        this.field_188416_a = d0;
    }

    public double func_111183_a(int i, AttributeModifier attributemodifier) {
        return this.field_188416_a * (double) (i + 1);
    }
}
