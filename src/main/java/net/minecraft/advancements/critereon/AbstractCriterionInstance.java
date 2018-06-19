package net.minecraft.advancements.critereon;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.util.ResourceLocation;


public class AbstractCriterionInstance implements ICriterionInstance {

    private final ResourceLocation field_192245_a;

    public AbstractCriterionInstance(ResourceLocation minecraftkey) {
        this.field_192245_a = minecraftkey;
    }

    public ResourceLocation func_192244_a() {
        return this.field_192245_a;
    }

    public String toString() {
        return "AbstractCriterionInstance{criterion=" + this.field_192245_a + '}';
    }
}
