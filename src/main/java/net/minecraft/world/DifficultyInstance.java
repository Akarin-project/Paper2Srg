package net.minecraft.world;

import javax.annotation.concurrent.Immutable;

import net.minecraft.util.math.MathHelper;

@Immutable
public class DifficultyInstance {

    private final EnumDifficulty field_180172_a;
    private final float field_180171_b;

    public DifficultyInstance(EnumDifficulty enumdifficulty, long i, long j, float f) {
        this.field_180172_a = enumdifficulty;
        this.field_180171_b = this.func_180169_a(enumdifficulty, i, j, f);
    }

    public float func_180168_b() {
        return this.field_180171_b;
    }

    public boolean func_193845_a(float f) {
        return this.field_180171_b > f;
    }

    public float func_180170_c() {
        return this.field_180171_b < 2.0F ? 0.0F : (this.field_180171_b > 4.0F ? 1.0F : (this.field_180171_b - 2.0F) / 2.0F);
    }

    private float func_180169_a(EnumDifficulty enumdifficulty, long i, long j, float f) {
        if (enumdifficulty == EnumDifficulty.PEACEFUL) {
            return 0.0F;
        } else {
            boolean flag = enumdifficulty == EnumDifficulty.HARD;
            float f1 = 0.75F;
            float f2 = MathHelper.func_76131_a(((float) i + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;

            f1 += f2;
            float f3 = 0.0F;

            f3 += MathHelper.func_76131_a((float) j / 3600000.0F, 0.0F, 1.0F) * (flag ? 1.0F : 0.75F);
            f3 += MathHelper.func_76131_a(f * 0.25F, 0.0F, f2);
            if (enumdifficulty == EnumDifficulty.EASY) {
                f3 *= 0.5F;
            }

            f1 += f3;
            return (float) enumdifficulty.func_151525_a() * f1;
        }
    }
}
