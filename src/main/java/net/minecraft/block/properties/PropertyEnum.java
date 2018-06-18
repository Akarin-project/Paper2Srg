package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.IStringSerializable;

public class PropertyEnum<T extends Enum<T> & IStringSerializable> extends PropertyHelper<T> {

    private final ImmutableSet<T> allowedValues;
    private final Map<String, T> nameToValue = Maps.newHashMap();

    // Paper start - BlockStateEnum is a singleton, so we can use our own hashCode
    private static int hashId = 0;
    private int hashCode;
    // Paper end

    protected PropertyEnum(String s, Class<T> oclass, Collection<T> collection) {
        super(s, oclass);
        this.allowedValues = ImmutableSet.copyOf(collection);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Enum oenum = (Enum) iterator.next();
            String s1 = ((IStringSerializable) oenum).getName();

            if (this.nameToValue.containsKey(s1)) {
                throw new IllegalArgumentException("Multiple values have the same name \'" + s1 + "\'");
            }

            this.nameToValue.put(s1, (T) oenum);
        }

        this.hashCode = hashId++; // Paper
    }

    public Collection<T> getAllowedValues() {
        return this.allowedValues;
    }

    public Optional<T> parseValue(String s) {
        return Optional.fromNullable(this.nameToValue.get(s));
    }

    public String getName(T t0) {
        return ((IStringSerializable) t0).getName();
    }

    @Override // Paper - override equals as BlockStateEnum is a singleton
    public boolean equals(Object object) {
       return this == object;
    }

    @Override // Paper - override equals as BlockStateEnum is a singleton
    public int hashCode() {
        return hashCode;
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String s, Class<T> oclass) {
        return create(s, oclass, Predicates.alwaysTrue());
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String s, Class<T> oclass, Predicate<T> predicate) {
        return create(s, oclass, Collections2.filter(Lists.newArrayList(oclass.getEnumConstants()), predicate));
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String s, Class<T> oclass, T... at) {
        return create(s, oclass, (Collection) Lists.newArrayList(at));
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> create(String s, Class<T> oclass, Collection<T> collection) {
        return new PropertyEnum(s, oclass, collection);
    }
}
