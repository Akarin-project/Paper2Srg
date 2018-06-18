package net.minecraft.world;

import javax.annotation.concurrent.Immutable;

import net.minecraft.util.math.MathHelper;

@Immutable
public class DifficultyInstance {

    private final EnumDifficulty worldDifficulty;
    private final float additionalDifficulty;

    public DifficultyInstance(EnumDifficulty enumdifficulty, long i, long j, float f) {
        this.worldDifficulty = enumdifficulty;
        this.additionalDifficulty = this.calculateAdditionalDifficulty(enumdifficulty, i, j, f);
    }

    public float getAdditionalDifficulty() {
        return this.additionalDifficulty;
    }

    public boolean isHarderThan(float f) {
        return this.additionalDifficulty > f;
    }

    public float getClampedAdditionalDifficulty() {
        return this.additionalDifficulty < 2.0F ? 0.0F : (this.additionalDifficulty > 4.0F ? 1.0F : (this.additionalDifficulty - 2.0F) / 2.0F);
    }

    private float calculateAdditionalDifficulty(EnumDifficulty enumdifficulty, long i, long j, float f) {
        if (enumdifficulty == EnumDifficulty.PEACEFUL) {
            return 0.0F;
        } else {
            boolean flag = enumdifficulty == EnumDifficulty.HARD;
            float f1 = 0.75F;
            float f2 = MathHelper.clamp(((float) i + -72000.0F) / 1440000.0F, 0.0F, 1.0F) * 0.25F;

            f1 += f2;
            float f3 = 0.0F;

            f3 += MathHelper.clamp((float) j / 3600000.0F, 0.0F, 1.0F) * (flag ? 1.0F : 0.75F);
            f3 += MathHelper.clamp(f * 0.25F, 0.0F, f2);
            if (enumdifficulty == EnumDifficulty.EASY) {
                f3 *= 0.5F;
            }

            f1 += f3;
            return (float) enumdifficulty.getDifficultyId() * f1;
        }
    }
}
