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

    private final AbstractAttributeMap attributeMap;
    private final IAttribute genericAttribute;
    private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
    private final Map<String, Set<AttributeModifier>> mapByName = Maps.newHashMap();
    private final Map<UUID, AttributeModifier> mapByUUID = Maps.newHashMap();
    private double baseValue;
    private boolean needsUpdate = true;
    private double cachedValue;

    public ModifiableAttributeInstance(AbstractAttributeMap attributemapbase, IAttribute iattribute) {
        this.attributeMap = attributemapbase;
        this.genericAttribute = iattribute;
        this.baseValue = iattribute.getDefaultValue();

        for (int i = 0; i < 3; ++i) {
            this.mapByOperation.put(Integer.valueOf(i), Sets.newHashSet());
        }

    }

    @Override
    public IAttribute getAttribute() {
        return this.genericAttribute;
    }

    @Override
    public double getBaseValue() {
        return this.baseValue;
    }

    @Override
    public void setBaseValue(double d0) {
        if (d0 != this.getBaseValue()) {
            this.baseValue = d0;
            this.flagForUpdate();
        }
    }

    @Override
    public Collection<AttributeModifier> getModifiersByOperation(int i) {
        return this.mapByOperation.get(Integer.valueOf(i));
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        HashSet hashset = Sets.newHashSet();

        for (int i = 0; i < 3; ++i) {
            hashset.addAll(this.getModifiersByOperation(i));
        }

        return hashset;
    }

    @Override
    @Nullable
    public AttributeModifier getModifier(UUID uuid) {
        return this.mapByUUID.get(uuid);
    }

    @Override
    public boolean hasModifier(AttributeModifier attributemodifier) {
        return this.mapByUUID.get(attributemodifier.getID()) != null;
    }

    @Override
    public void applyModifier(AttributeModifier attributemodifier) {
        if (this.getModifier(attributemodifier.getID()) != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        } else {
            Set<AttributeModifier> object = this.mapByName.get(attributemodifier.getName());

            if (object == null) {
                object = Sets.newHashSet();
                this.mapByName.put(attributemodifier.getName(), object);
            }

            ((Set) this.mapByOperation.get(Integer.valueOf(attributemodifier.getOperation()))).add(attributemodifier);
            ((Set) object).add(attributemodifier);
            this.mapByUUID.put(attributemodifier.getID(), attributemodifier);
            this.flagForUpdate();
        }
    }

    protected void flagForUpdate() {
        this.needsUpdate = true;
        this.attributeMap.onAttributeModified(this);
    }

    @Override
    public void removeModifier(AttributeModifier attributemodifier) {
        for (int i = 0; i < 3; ++i) {
            Set set = this.mapByOperation.get(Integer.valueOf(i));

            set.remove(attributemodifier);
        }

        Set set1 = this.mapByName.get(attributemodifier.getName());

        if (set1 != null) {
            set1.remove(attributemodifier);
            if (set1.isEmpty()) {
                this.mapByName.remove(attributemodifier.getName());
            }
        }

        this.mapByUUID.remove(attributemodifier.getID());
        this.flagForUpdate();
    }

    @Override
    public void removeModifier(UUID uuid) {
        AttributeModifier attributemodifier = this.getModifier(uuid);

        if (attributemodifier != null) {
            this.removeModifier(attributemodifier);
        }

    }

    @Override
    public double getAttributeValue() {
        if (this.needsUpdate) {
            this.cachedValue = this.computeValue();
            this.needsUpdate = false;
        }

        return this.cachedValue;
    }

    private double computeValue() {
        double d0 = this.getBaseValue();

        AttributeModifier attributemodifier;

        for (Iterator iterator = this.getAppliedModifiers(0).iterator(); iterator.hasNext(); d0 += attributemodifier.getAmount()) {
            attributemodifier = (AttributeModifier) iterator.next();
        }

        double d1 = d0;

        Iterator iterator1;
        AttributeModifier attributemodifier1;

        for (iterator1 = this.getAppliedModifiers(1).iterator(); iterator1.hasNext(); d1 += d0 * attributemodifier1.getAmount()) {
            attributemodifier1 = (AttributeModifier) iterator1.next();
        }

        for (iterator1 = this.getAppliedModifiers(2).iterator(); iterator1.hasNext(); d1 *= 1.0D + attributemodifier1.getAmount()) {
            attributemodifier1 = (AttributeModifier) iterator1.next();
        }

        return this.genericAttribute.clampValue(d1);
    }

    private Collection<AttributeModifier> getAppliedModifiers(int i) {
        HashSet hashset = Sets.newHashSet(this.getModifiersByOperation(i));

        for (IAttribute iattribute = this.genericAttribute.getParent(); iattribute != null; iattribute = iattribute.getParent()) {
            IAttributeInstance attributeinstance = this.attributeMap.getAttributeInstance(iattribute);

            if (attributeinstance != null) {
                hashset.addAll(attributeinstance.getModifiersByOperation(i));
            }
        }

        return hashset;
    }
}
