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

    protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
    protected final Map<String, IAttributeInstance> attributesByName = new LowerStringMap();
    protected final Multimap<IAttribute, IAttribute> descendantsByParent = HashMultimap.create();

    public AbstractAttributeMap() {}

    public IAttributeInstance getAttributeInstance(IAttribute iattribute) {
        return (IAttributeInstance) this.attributes.get(iattribute);
    }

    @Nullable
    public IAttributeInstance getAttributeInstanceByName(String s) {
        return (IAttributeInstance) this.attributesByName.get(s);
    }

    public IAttributeInstance registerAttribute(IAttribute iattribute) {
        if (this.attributesByName.containsKey(iattribute.getName())) {
            throw new IllegalArgumentException("Attribute is already registered!");
        } else {
            IAttributeInstance attributeinstance = this.createInstance(iattribute);

            this.attributesByName.put(iattribute.getName(), attributeinstance);
            this.attributes.put(iattribute, attributeinstance);

            for (IAttribute iattribute1 = iattribute.getParent(); iattribute1 != null; iattribute1 = iattribute1.getParent()) {
                this.descendantsByParent.put(iattribute1, iattribute);
            }

            return attributeinstance;
        }
    }

    protected abstract IAttributeInstance createInstance(IAttribute iattribute);

    public Collection<IAttributeInstance> getAllAttributes() {
        return this.attributesByName.values();
    }

    public void onAttributeModified(IAttributeInstance attributeinstance) {}

    public void removeAttributeModifiers(Multimap<String, AttributeModifier> multimap) {
        Iterator iterator = multimap.entries().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            IAttributeInstance attributeinstance = this.getAttributeInstanceByName((String) entry.getKey());

            if (attributeinstance != null) {
                attributeinstance.removeModifier((AttributeModifier) entry.getValue());
            }
        }

    }

    public void applyAttributeModifiers(Multimap<String, AttributeModifier> multimap) {
        Iterator iterator = multimap.entries().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            IAttributeInstance attributeinstance = this.getAttributeInstanceByName((String) entry.getKey());

            if (attributeinstance != null) {
                attributeinstance.removeModifier((AttributeModifier) entry.getValue());
                attributeinstance.applyModifier((AttributeModifier) entry.getValue());
            }
        }

    }
}
