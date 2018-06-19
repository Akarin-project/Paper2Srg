package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;

public class PropertyBool extends PropertyHelper<Boolean> {

    private final ImmutableSet<Boolean> field_177717_a = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

    protected PropertyBool(String s) {
        super(s, Boolean.class);
    }

    public Collection<Boolean> func_177700_c() {
        return this.field_177717_a;
    }

    public static PropertyBool func_177716_a(String s) {
        return new PropertyBool(s);
    }

    public Optional<Boolean> func_185929_b(String s) {
        return !"true".equals(s) && !"false".equals(s) ? Optional.absent() : Optional.of(Boolean.valueOf(s));
    }

    public String func_177702_a(Boolean obool) {
        return obool.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof PropertyBool && super.equals(object)) {
            PropertyBool blockstateboolean = (PropertyBool) object;

            return this.field_177717_a.equals(blockstateboolean.field_177717_a);
        } else {
            return false;
        }
    }

    // Spigot start
    private int hashCode;
    public int hashCode() {
        int hash = hashCode;
        if (hash == 0) {
            hash = 31 * super.hashCode() + this.field_177717_a.hashCode();
            hashCode = hash;
        }
        return hash;
    }
    // Spigot end
}
