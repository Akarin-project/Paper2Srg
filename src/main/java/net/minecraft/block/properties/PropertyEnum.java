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

    private final ImmutableSet<T> field_177711_a;
    private final Map<String, T> field_177710_b = Maps.newHashMap();

    // Paper start - BlockStateEnum is a singleton, so we can use our own hashCode
    private static int hashId = 0;
    private int hashCode;
    // Paper end

    protected PropertyEnum(String s, Class<T> oclass, Collection<T> collection) {
        super(s, oclass);
        this.field_177711_a = ImmutableSet.copyOf(collection);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Enum oenum = (Enum) iterator.next();
            String s1 = ((IStringSerializable) oenum).func_176610_l();

            if (this.field_177710_b.containsKey(s1)) {
                throw new IllegalArgumentException("Multiple values have the same name \'" + s1 + "\'");
            }

            this.field_177710_b.put(s1, (T) oenum);
        }

        this.hashCode = hashId++; // Paper
    }

    public Collection<T> func_177700_c() {
        return this.field_177711_a;
    }

    public Optional<T> func_185929_b(String s) {
        return Optional.fromNullable(this.field_177710_b.get(s));
    }

    public String func_177702_a(T t0) {
        return ((IStringSerializable) t0).func_176610_l();
    }

    @Override // Paper - override equals as BlockStateEnum is a singleton
    public boolean equals(Object object) {
       return this == object;
    }

    @Override // Paper - override equals as BlockStateEnum is a singleton
    public int hashCode() {
        return hashCode;
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> func_177709_a(String s, Class<T> oclass) {
        return func_177708_a(s, oclass, Predicates.alwaysTrue());
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> func_177708_a(String s, Class<T> oclass, Predicate<T> predicate) {
        return func_177707_a(s, oclass, Collections2.filter(Lists.newArrayList(oclass.getEnumConstants()), predicate));
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> func_177706_a(String s, Class<T> oclass, T... at) {
        return func_177707_a(s, oclass, (Collection) Lists.newArrayList(at));
    }

    public static <T extends Enum<T> & IStringSerializable> PropertyEnum<T> func_177707_a(String s, Class<T> oclass, Collection<T> collection) {
        return new PropertyEnum(s, oclass, collection);
    }
}
