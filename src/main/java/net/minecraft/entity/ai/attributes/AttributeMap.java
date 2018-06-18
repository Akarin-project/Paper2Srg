package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.LowerStringMap;

public class AttributeMap extends AbstractAttributeMap {

    private final Set<IAttributeInstance> dirtyInstances = Sets.newHashSet();
    protected final Map<String, IAttributeInstance> instancesByName = new LowerStringMap();

    public AttributeMap() {}

    public ModifiableAttributeInstance getAttributeInstance(IAttribute iattribute) {
        return (ModifiableAttributeInstance) super.getAttributeInstance(iattribute);
    }

    public ModifiableAttributeInstance getAttributeInstanceByName(String s) {
        IAttributeInstance attributeinstance = super.getAttributeInstanceByName(s);

        if (attributeinstance == null) {
            attributeinstance = (IAttributeInstance) this.instancesByName.get(s);
        }

        return (ModifiableAttributeInstance) attributeinstance;
    }

    public IAttributeInstance registerAttribute(IAttribute iattribute) {
        IAttributeInstance attributeinstance = super.registerAttribute(iattribute);

        if (iattribute instanceof RangedAttribute && ((RangedAttribute) iattribute).getDescription() != null) {
            this.instancesByName.put(((RangedAttribute) iattribute).getDescription(), attributeinstance);
        }

        return attributeinstance;
    }

    protected IAttributeInstance createInstance(IAttribute iattribute) {
        return new ModifiableAttributeInstance(this, iattribute);
    }

    public void onAttributeModified(IAttributeInstance attributeinstance) {
        if (attributeinstance.getAttribute().getShouldWatch()) {
            this.dirtyInstances.add(attributeinstance);
        }

        Iterator iterator = this.descendantsByParent.get(attributeinstance.getAttribute()).iterator();

        while (iterator.hasNext()) {
            IAttribute iattribute = (IAttribute) iterator.next();
            ModifiableAttributeInstance attributemodifiable = this.getAttributeInstance(iattribute);

            if (attributemodifiable != null) {
                attributemodifiable.flagForUpdate();
            }
        }

    }

    public Set<IAttributeInstance> getDirtyInstances() {
        return this.dirtyInstances;
    }

    public Collection<IAttributeInstance> getWatchedAttributes() {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = this.getAllAttributes().iterator();

        while (iterator.hasNext()) {
            IAttributeInstance attributeinstance = (IAttributeInstance) iterator.next();

            if (attributeinstance.getAttribute().getShouldWatch()) {
                hashset.add(attributeinstance);
            }
        }

        return hashset;
    }

    public IAttributeInstance getAttributeInstanceByName(String s) {
        return this.getAttributeInstanceByName(s);
    }

    public IAttributeInstance getAttributeInstance(IAttribute iattribute) {
        return this.getAttributeInstance(iattribute);
    }
}
