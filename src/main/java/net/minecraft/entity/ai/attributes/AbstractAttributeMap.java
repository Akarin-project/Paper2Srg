package net.minecraft.entity.ai.attributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

import net.minecraft.util.LowerStringMap;

public abstract class AbstractAttributeMap {

    protected final Map<IAttribute, IAttributeInstance> field_111154_a = Maps.newHashMap();
    protected final Map<String, IAttributeInstance> field_111153_b = new LowerStringMap();
    protected final Multimap<IAttribute, IAttribute> field_180377_c = HashMultimap.create();

    public AbstractAttributeMap() {}

    public IAttributeInstance func_111151_a(IAttribute iattribute) {
        return (IAttributeInstance) this.field_111154_a.get(iattribute);
    }

    @Nullable
    public IAttributeInstance func_111152_a(String s) {
        return (IAttributeInstance) this.field_111153_b.get(s);
    }

    public IAttributeInstance func_111150_b(IAttribute iattribute) {
        if (this.field_111153_b.containsKey(iattribute.func_111108_a())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        } else {
            IAttributeInstance attributeinstance = this.func_180376_c(iattribute);

            this.field_111153_b.put(iattribute.func_111108_a(), attributeinstance);
            this.field_111154_a.put(iattribute, attributeinstance);

            for (IAttribute iattribute1 = iattribute.func_180372_d(); iattribute1 != null; iattribute1 = iattribute1.func_180372_d()) {
                this.field_180377_c.put(iattribute1, iattribute);
            }

            return attributeinstance;
        }
    }

    protected abstract IAttributeInstance func_180376_c(IAttribute iattribute);

    public Collection<IAttributeInstance> func_111146_a() {
        return this.field_111153_b.values();
    }

    public void func_180794_a(IAttributeInstance attributeinstance) {}

    public void func_111148_a(Multimap<String, AttributeModifier> multimap) {
        Iterator iterator = multimap.entries().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            IAttributeInstance attributeinstance = this.func_111152_a((String) entry.getKey());

            if (attributeinstance != null) {
                attributeinstance.func_111124_b((AttributeModifier) entry.getValue());
            }
        }

    }

    public void func_111147_b(Multimap<String, AttributeModifier> multimap) {
        Iterator iterator = multimap.entries().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            IAttributeInstance attributeinstance = this.func_111152_a((String) entry.getKey());

            if (attributeinstance != null) {
                attributeinstance.func_111124_b((AttributeModifier) entry.getValue());
                attributeinstance.func_111121_a((AttributeModifier) entry.getValue());
            }
        }

    }
}
