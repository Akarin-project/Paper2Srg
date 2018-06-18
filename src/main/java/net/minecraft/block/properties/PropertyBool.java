package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;

public class PropertyBool extends PropertyHelper<Boolean> {

    private final ImmutableSet<Boolean> allowedValues = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

    protected PropertyBool(String s) {
        super(s, Boolean.class);
    }

    public Collection<Boolean> getAllowedValues() {
        return this.allowedValues;
    }

    public static PropertyBool create(String s) {
        return new PropertyBool(s);
    }

    public Optional<Boolean> parseValue(String s) {
        return !"true".equals(s) && !"false".equals(s) ? Optional.absent() : Optional.of(Boolean.valueOf(s));
    }

    public String getName(Boolean obool) {
        return obool.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof PropertyBool && super.equals(object)) {
            PropertyBool blockstateboolean = (PropertyBool) object;

            return this.allowedValues.equals(blockstateboolean.allowedValues);
        } else {
            return false;
        }
    }

    // Spigot start
    private int hashCode;
    public int hashCode() {
        int hash = hashCode;
        if (hash == 0) {
            hash = 31 * super.hashCode() + this.allowedValues.hashCode();
            hashCode = hash;
        }
        return hash;
    }
    // Spigot end
}
