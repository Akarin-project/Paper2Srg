package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;



public class CraftAttributeInstance implements AttributeInstance {

    private final net.minecraft.entity.ai.attributes.IAttributeInstance handle;
    private final Attribute attribute;

    public CraftAttributeInstance(net.minecraft.entity.ai.attributes.IAttributeInstance handle, Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }

    @Override
    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public double getBaseValue() {
        return handle.func_111125_b();
    }

    @Override
    public void setBaseValue(double d) {
        handle.func_111128_a(d);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        List<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (net.minecraft.entity.ai.attributes.AttributeModifier nms : handle.func_111122_c()) {
            result.add(convert(nms));
        }

        return result;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.func_111121_a(convert(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.func_111124_b(convert(modifier));
    }

    @Override
    public double getValue() {
        return handle.func_111126_e();
    }

    @Override
    public double getDefaultValue() {
       return handle.func_111123_a().func_111110_b();
    }

    private static net.minecraft.entity.ai.attributes.AttributeModifier convert(AttributeModifier bukkit) {
        return new net.minecraft.entity.ai.attributes.AttributeModifier(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), bukkit.getOperation().ordinal());
    }

    private static AttributeModifier convert(net.minecraft.entity.ai.attributes.AttributeModifier nms) {
        return new AttributeModifier(nms.func_111167_a(), nms.func_111166_b(), nms.func_111164_d(), AttributeModifier.Operation.values()[nms.func_111169_c()]);
    }
}
