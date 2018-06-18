package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

import net.minecraft.util.math.MathHelper;

public class RangedAttribute extends BaseAttribute {

    private final double minimumValue;
    public double maximumValue; // Spigot
    private String description;

    public RangedAttribute(@Nullable IAttribute iattribute, String s, double d0, double d1, double d2) {
        super(iattribute, s, d0);
        this.minimumValue = d1;
        this.maximumValue = d2;
        if (d1 > d2) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        } else if (d0 < d1) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        } else if (d0 > d2) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }

    public RangedAttribute setDescription(String s) {
        this.description = s;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public double clampValue(double d0) {
        d0 = MathHelper.clamp(d0, this.minimumValue, this.maximumValue);
        return d0;
    }
}
