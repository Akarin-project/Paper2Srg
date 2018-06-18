package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;

public class PropertyInteger extends PropertyHelper<Integer> {

    private final ImmutableSet<Integer> allowedValues;

    protected PropertyInteger(String s, int i, int j) {
        super(s, Integer.class);
        if (i < 0) {
            throw new IllegalArgumentException("Min value of " + s + " must be 0 or greater");
        } else if (j <= i) {
            throw new IllegalArgumentException("Max value of " + s + " must be greater than min (" + i + ")");
        } else {
            HashSet hashset = Sets.newHashSet();

            for (int k = i; k <= j; ++k) {
                hashset.add(Integer.valueOf(k));
            }

            this.allowedValues = ImmutableSet.copyOf(hashset);
        }
    }

    public Collection<Integer> getAllowedValues() {
        return this.allowedValues;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof PropertyInteger && super.equals(object)) {
            PropertyInteger blockstateinteger = (PropertyInteger) object;

            return this.allowedValues.equals(blockstateinteger.allowedValues);
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

    public static PropertyInteger create(String s, int i, int j) {
        return new PropertyInteger(s, i, j);
    }

    public Optional<Integer> parseValue(String s) {
        try {
            Integer integer = Integer.valueOf(s);

            return this.allowedValues.contains(integer) ? Optional.of(integer) : Optional.absent();
        } catch (NumberFormatException numberformatexception) {
            return Optional.absent();
        }
    }

    public String getName(Integer integer) {
        return integer.toString();
    }
}
