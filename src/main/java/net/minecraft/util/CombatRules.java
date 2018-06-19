package net.minecraft.util;
import net.minecraft.util.math.MathHelper;


public class CombatRules {

    public static float func_189427_a(float f, float f1, float f2) {
        float f3 = 2.0F + f2 / 4.0F;
        float f4 = MathHelper.func_76131_a(f1 - f / f3, f1 * 0.2F, 20.0F);

        return f * (1.0F - f4 / 25.0F);
    }

    public static float func_188401_b(float f, float f1) {
        float f2 = MathHelper.func_76131_a(f1, 0.0F, 20.0F);

        return f * (1.0F - f2 / 25.0F);
    }
}
