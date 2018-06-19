package net.minecraft.entity.ai.attributes;

import io.netty.util.internal.ThreadLocalRandom;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.Validate;

import net.minecraft.util.math.MathHelper;

public class AttributeModifier {

    private final double field_111174_a;
    private final int field_111172_b;
    private final String field_111173_c;
    private final UUID field_111170_d;
    private boolean field_111171_e;

    public AttributeModifier(String s, double d0, int i) {
        this(MathHelper.func_180182_a((Random) ThreadLocalRandom.current()), s, d0, i);
    }

    public AttributeModifier(UUID uuid, String s, double d0, int i) {
        this.field_111171_e = true;
        this.field_111170_d = uuid;
        this.field_111173_c = s;
        this.field_111174_a = d0;
        this.field_111172_b = i;
        Validate.notEmpty(s, "Modifier name cannot be empty", new Object[0]);
        Validate.inclusiveBetween(0L, 2L, (long) i, "Invalid operation");
    }

    public UUID func_111167_a() {
        return this.field_111170_d;
    }

    public String func_111166_b() {
        return this.field_111173_c;
    }

    public int func_111169_c() {
        return this.field_111172_b;
    }

    public double func_111164_d() {
        return this.field_111174_a;
    }

    public boolean func_111165_e() {
        return this.field_111171_e;
    }

    public AttributeModifier func_111168_a(boolean flag) {
        this.field_111171_e = flag;
        return this;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            AttributeModifier attributemodifier = (AttributeModifier) object;

            if (this.field_111170_d != null) {
                if (!this.field_111170_d.equals(attributemodifier.field_111170_d)) {
                    return false;
                }
            } else if (attributemodifier.field_111170_d != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.field_111170_d != null ? this.field_111170_d.hashCode() : 0;
    }

    public String toString() {
        return "AttributeModifier{amount=" + this.field_111174_a + ", operation=" + this.field_111172_b + ", name=\'" + this.field_111173_c + '\'' + ", id=" + this.field_111170_d + ", serialize=" + this.field_111171_e + '}';
    }
}
