package net.minecraft.entity.ai.attributes;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.LowerStringMap;

public class AttributeMap extends AbstractAttributeMap {

    private final Set<IAttributeInstance> field_111162_d = Sets.newHashSet();
    protected final Map<String, IAttributeInstance> field_111163_c = new LowerStringMap();

    public AttributeMap() {}

    @Override
    public ModifiableAttributeInstance func_111151_a(IAttribute iattribute) {
        return (ModifiableAttributeInstance) super.func_111151_a(iattribute);
    }

    @Override
    public ModifiableAttributeInstance func_111152_a(String s) {
        IAttributeInstance attributeinstance = super.func_111152_a(s);

        if (attributeinstance == null) {
            attributeinstance = this.field_111163_c.get(s);
        }

        return (ModifiableAttributeInstance) attributeinstance;
    }

    @Override
    public IAttributeInstance func_111150_b(IAttribute iattribute) {
        IAttributeInstance attributeinstance = super.func_111150_b(iattribute);

        if (iattribute instanceof RangedAttribute && ((RangedAttribute) iattribute).func_111116_f() != null) {
            this.field_111163_c.put(((RangedAttribute) iattribute).func_111116_f(), attributeinstance);
        }

        return attributeinstance;
    }

    @Override
    protected IAttributeInstance func_180376_c(IAttribute iattribute) {
        return new ModifiableAttributeInstance(this, iattribute);
    }

    @Override
    public void func_180794_a(IAttributeInstance attributeinstance) {
        if (attributeinstance.func_111123_a().func_111111_c()) {
            this.field_111162_d.add(attributeinstance);
        }

        Iterator iterator = this.field_180377_c.get(attributeinstance.func_111123_a()).iterator();

        while (iterator.hasNext()) {
            IAttribute iattribute = (IAttribute) iterator.next();
            ModifiableAttributeInstance attributemodifiable = this.func_111151_a(iattribute);

            if (attributemodifiable != null) {
                attributemodifiable.func_111131_f();
            }
        }

    }

    public Set<IAttributeInstance> func_111161_b() {
        return this.field_111162_d;
    }

    public Collection<IAttributeInstance> func_111160_c() {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = this.func_111146_a().iterator();

        while (iterator.hasNext()) {
            IAttributeInstance attributeinstance = (IAttributeInstance) iterator.next();

            if (attributeinstance.func_111123_a().func_111111_c()) {
                hashset.add(attributeinstance);
            }
        }

        return hashset;
    }
}
