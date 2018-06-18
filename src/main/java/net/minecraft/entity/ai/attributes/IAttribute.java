package net.minecraft.entity.ai.attributes;

import javax.annotation.Nullable;

public interface IAttribute {

    String getName();

    double clampValue(double d0);

    double getDefaultValue();

    boolean getShouldWatch();

    @Nullable
    IAttribute getParent();
}
