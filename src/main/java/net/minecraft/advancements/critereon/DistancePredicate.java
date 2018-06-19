package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class DistancePredicate {

    public static final DistancePredicate field_193423_a = new DistancePredicate(MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a);
    private final MinMaxBounds field_193424_b;
    private final MinMaxBounds field_193425_c;
    private final MinMaxBounds field_193426_d;
    private final MinMaxBounds field_193427_e;
    private final MinMaxBounds field_193428_f;

    public DistancePredicate(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, MinMaxBounds criterionconditionvalue2, MinMaxBounds criterionconditionvalue3, MinMaxBounds criterionconditionvalue4) {
        this.field_193424_b = criterionconditionvalue;
        this.field_193425_c = criterionconditionvalue1;
        this.field_193426_d = criterionconditionvalue2;
        this.field_193427_e = criterionconditionvalue3;
        this.field_193428_f = criterionconditionvalue4;
    }

    public boolean func_193422_a(double d0, double d1, double d2, double d3, double d4, double d5) {
        float f = (float) (d0 - d3);
        float f1 = (float) (d1 - d4);
        float f2 = (float) (d2 - d5);

        return this.field_193424_b.func_192514_a(MathHelper.func_76135_e(f)) && this.field_193425_c.func_192514_a(MathHelper.func_76135_e(f1)) && this.field_193426_d.func_192514_a(MathHelper.func_76135_e(f2)) ? (!this.field_193427_e.func_192513_a((double) (f * f + f2 * f2)) ? false : this.field_193428_f.func_192513_a((double) (f * f + f1 * f1 + f2 * f2))) : false;
    }

    public static DistancePredicate func_193421_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "distance");
            MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject.get("x"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.func_192515_a(jsonobject.get("y"));
            MinMaxBounds criterionconditionvalue2 = MinMaxBounds.func_192515_a(jsonobject.get("z"));
            MinMaxBounds criterionconditionvalue3 = MinMaxBounds.func_192515_a(jsonobject.get("horizontal"));
            MinMaxBounds criterionconditionvalue4 = MinMaxBounds.func_192515_a(jsonobject.get("absolute"));

            return new DistancePredicate(criterionconditionvalue, criterionconditionvalue1, criterionconditionvalue2, criterionconditionvalue3, criterionconditionvalue4);
        } else {
            return DistancePredicate.field_193423_a;
        }
    }
}
