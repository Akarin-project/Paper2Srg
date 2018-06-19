package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;

public interface IAttributeInstance {

    IAttribute func_111123_a();

    double func_111125_b();

    void func_111128_a(double d0);

    Collection<AttributeModifier> func_111130_a(int i);

    Collection<AttributeModifier> func_111122_c();

    boolean func_180374_a(AttributeModifier attributemodifier);

    @Nullable
    AttributeModifier func_111127_a(UUID uuid);

    void func_111121_a(AttributeModifier attributemodifier);

    void func_111124_b(AttributeModifier attributemodifier);

    void func_188479_b(UUID uuid);

    double func_111126_e();
}
