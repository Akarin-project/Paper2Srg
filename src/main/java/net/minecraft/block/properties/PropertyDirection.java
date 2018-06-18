package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;

import net.minecraft.util.EnumFacing;

public class PropertyDirection extends PropertyEnum<EnumFacing> {

    protected PropertyDirection(String s, Collection<EnumFacing> collection) {
        super(s, EnumFacing.class, collection);
    }

    public static PropertyDirection create(String s) {
        return create(s, Predicates.alwaysTrue());
    }

    public static PropertyDirection create(String s, Predicate<EnumFacing> predicate) {
        return create(s, Collections2.filter(Lists.newArrayList(EnumFacing.values()), predicate));
    }

    public static PropertyDirection create(String s, Collection<EnumFacing> collection) {
        return new PropertyDirection(s, collection);
    }
}
