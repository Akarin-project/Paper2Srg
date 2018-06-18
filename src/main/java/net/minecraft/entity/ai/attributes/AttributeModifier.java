package net.minecraft.entity.ai.attributes;

import io.netty.util.internal.ThreadLocalRandom;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.Validate;

import net.minecraft.util.math.MathHelper;

public class AttributeModifier {

    private final double amount;
    private final int operation;
    private final String name;
    private final UUID id;
    private boolean isSaved;

    public AttributeModifier(String s, double d0, int i) {
        this(MathHelper.getRandomUUID((Random) ThreadLocalRandom.current()), s, d0, i);
    }

    public AttributeModifier(UUID uuid, String s, double d0, int i) {
        this.isSaved = true;
        this.id = uuid;
        this.name = s;
        this.amount = d0;
        this.operation = i;
        Validate.notEmpty(s, "Modifier name cannot be empty", new Object[0]);
        Validate.inclusiveBetween(0L, 2L, (long) i, "Invalid operation");
    }

    public UUID getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOperation() {
        return this.operation;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean isSaved() {
        return this.isSaved;
    }

    public AttributeModifier setSaved(boolean flag) {
        this.isSaved = flag;
        return this;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            AttributeModifier attributemodifier = (AttributeModifier) object;

            if (this.id != null) {
                if (!this.id.equals(attributemodifier.id)) {
                    return false;
                }
            } else if (attributemodifier.id != null) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }

    public String toString() {
        return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name=\'" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
    }
}
