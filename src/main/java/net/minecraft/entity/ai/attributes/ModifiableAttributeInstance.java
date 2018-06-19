package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class ModifiableAttributeInstance implements IAttributeInstance {

    private final AbstractAttributeMap field_111138_a;
    private final IAttribute field_111136_b;
    private final Map<Integer, Set<AttributeModifier>> field_111137_c = Maps.newHashMap();
    private final Map<String, Set<AttributeModifier>> field_111134_d = Maps.newHashMap();
    private final Map<UUID, AttributeModifier> field_111135_e = Maps.newHashMap();
    private double field_111132_f;
    private boolean field_111133_g = true;
    private double field_111139_h;

    public ModifiableAttributeInstance(AbstractAttributeMap attributemapbase, IAttribute iattribute) {
        this.field_111138_a = attributemapbase;
        this.field_111136_b = iattribute;
        this.field_111132_f = iattribute.func_111110_b();

        for (int i = 0; i < 3; ++i) {
            this.field_111137_c.put(Integer.valueOf(i), Sets.newHashSet());
        }

    }

    public IAttribute func_111123_a() {
        return this.field_111136_b;
    }

    public double func_111125_b() {
        return this.field_111132_f;
    }

    public void func_111128_a(double d0) {
        if (d0 != this.func_111125_b()) {
            this.field_111132_f = d0;
            this.func_111131_f();
        }
    }

    public Collection<AttributeModifier> func_111130_a(int i) {
        return (Collection) this.field_111137_c.get(Integer.valueOf(i));
    }

    public Collection<AttributeModifier> func_111122_c() {
        HashSet hashset = Sets.newHashSet();

        for (int i = 0; i < 3; ++i) {
            hashset.addAll(this.func_111130_a(i));
        }

        return hashset;
    }

    @Nullable
    public AttributeModifier func_111127_a(UUID uuid) {
        return (AttributeModifier) this.field_111135_e.get(uuid);
    }

    public boolean func_180374_a(AttributeModifier attributemodifier) {
        return this.field_111135_e.get(attributemodifier.func_111167_a()) != null;
    }

    public void func_111121_a(AttributeModifier attributemodifier) {
        if (this.func_111127_a(attributemodifier.func_111167_a()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        } else {
            Object object = (Set) this.field_111134_d.get(attributemodifier.func_111166_b());

            if (object == null) {
                object = Sets.newHashSet();
                this.field_111134_d.put(attributemodifier.func_111166_b(), object);
            }

            ((Set) this.field_111137_c.get(Integer.valueOf(attributemodifier.func_111169_c()))).add(attributemodifier);
            ((Set) object).add(attributemodifier);
            this.field_111135_e.put(attributemodifier.func_111167_a(), attributemodifier);
            this.func_111131_f();
        }
    }

    protected void func_111131_f() {
        this.field_111133_g = true;
        this.field_111138_a.func_180794_a((IAttributeInstance) this);
    }

    public void func_111124_b(AttributeModifier attributemodifier) {
        for (int i = 0; i < 3; ++i) {
            Set set = (Set) this.field_111137_c.get(Integer.valueOf(i));

            set.remove(attributemodifier);
        }

        Set set1 = (Set) this.field_111134_d.get(attributemodifier.func_111166_b());

        if (set1 != null) {
            set1.remove(attributemodifier);
            if (set1.isEmpty()) {
                this.field_111134_d.remove(attributemodifier.func_111166_b());
            }
        }

        this.field_111135_e.remove(attributemodifier.func_111167_a());
        this.func_111131_f();
    }

    public void func_188479_b(UUID uuid) {
        AttributeModifier attributemodifier = this.func_111127_a(uuid);

        if (attributemodifier != null) {
            this.func_111124_b(attributemodifier);
        }

    }

    public double func_111126_e() {
        if (this.field_111133_g) {
            this.field_111139_h = this.func_111129_g();
            this.field_111133_g = false;
        }

        return this.field_111139_h;
    }

    private double func_111129_g() {
        double d0 = this.func_111125_b();

        AttributeModifier attributemodifier;

        for (Iterator iterator = this.func_180375_b(0).iterator(); iterator.hasNext(); d0 += attributemodifier.func_111164_d()) {
            attributemodifier = (AttributeModifier) iterator.next();
        }

        double d1 = d0;

        Iterator iterator1;
        AttributeModifier attributemodifier1;

        for (iterator1 = this.func_180375_b(1).iterator(); iterator1.hasNext(); d1 += d0 * attributemodifier1.func_111164_d()) {
            attributemodifier1 = (AttributeModifier) iterator1.next();
        }

        for (iterator1 = this.func_180375_b(2).iterator(); iterator1.hasNext(); d1 *= 1.0D + attributemodifier1.func_111164_d()) {
            attributemodifier1 = (AttributeModifier) iterator1.next();
        }

        return this.field_111136_b.func_111109_a(d1);
    }

    private Collection<AttributeModifier> func_180375_b(int i) {
        HashSet hashset = Sets.newHashSet(this.func_111130_a(i));

        for (IAttribute iattribute = this.field_111136_b.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
            IAttributeInstance attributeinstance = this.field_111138_a.func_111151_a(iattribute);

            if (attributeinstance != null) {
                hashset.addAll(attributeinstance.func_111130_a(i));
            }
        }

        return hashset;
    }
}
