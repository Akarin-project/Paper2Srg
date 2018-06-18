package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;

public class DistancePredicate {

    public static final DistancePredicate ANY = new DistancePredicate(MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED);
    private final MinMaxBounds x;
    private final MinMaxBounds y;
    private final MinMaxBounds z;
    private final MinMaxBounds horizontal;
    private final MinMaxBounds absolute;

    public DistancePredicate(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, MinMaxBounds criterionconditionvalue2, MinMaxBounds criterionconditionvalue3, MinMaxBounds criterionconditionvalue4) {
        this.x = criterionconditionvalue;
        this.y = criterionconditionvalue1;
        this.z = criterionconditionvalue2;
        this.horizontal = criterionconditionvalue3;
        this.absolute = criterionconditionvalue4;
    }

    public boolean test(double d0, double d1, double d2, double d3, double d4, double d5) {
        float f = (float) (d0 - d3);
        float f1 = (float) (d1 - d4);
        float f2 = (float) (d2 - d5);

        return this.x.test(MathHelper.abs(f)) && this.y.test(MathHelper.abs(f1)) && this.z.test(MathHelper.abs(f2)) ? (!this.horizontal.testSquare((double) (f * f + f2 * f2)) ? false : this.absolute.testSquare((double) (f * f + f1 * f1 + f2 * f2))) : false;
    }

    public static DistancePredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "distance");
            MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject.get("x"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject.get("y"));
            MinMaxBounds criterionconditionvalue2 = MinMaxBounds.deserialize(jsonobject.get("z"));
            MinMaxBounds criterionconditionvalue3 = MinMaxBounds.deserialize(jsonobject.get("horizontal"));
            MinMaxBounds criterionconditionvalue4 = MinMaxBounds.deserialize(jsonobject.get("absolute"));

            return new DistancePredicate(criterionconditionvalue, criterionconditionvalue1, criterionconditionvalue2, criterionconditionvalue3, criterionconditionvalue4);
        } else {
            return DistancePredicate.ANY;
        }
    }
}
