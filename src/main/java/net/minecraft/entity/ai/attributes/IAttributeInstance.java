package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;

public interface IAttributeInstance {

    IAttribute getAttribute();

    double getBaseValue();

    void setBaseValue(double d0);

    Collection<AttributeModifier> getModifiersByOperation(int i);

    Collection<AttributeModifier> getModifiers();

    boolean hasModifier(AttributeModifier attributemodifier);

    @Nullable
    AttributeModifier getModifier(UUID uuid);

    void applyModifier(AttributeModifier attributemodifier);

    void removeModifier(AttributeModifier attributemodifier);

    void removeModifier(UUID uuid);

    double getAttributeValue();
}
