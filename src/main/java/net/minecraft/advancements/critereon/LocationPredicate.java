package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;

public class LocationPredicate {

    public static LocationPredicate field_193455_a = new LocationPredicate(MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, MinMaxBounds.field_192516_a, (Biome) null, (String) null, (DimensionType) null);
    private final MinMaxBounds field_193457_c;
    private final MinMaxBounds field_193458_d;
    private final MinMaxBounds field_193459_e;
    @Nullable
    final Biome field_193456_b;
    @Nullable
    private final String field_193460_f;
    @Nullable
    private final DimensionType field_193461_g;

    public LocationPredicate(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, MinMaxBounds criterionconditionvalue2, @Nullable Biome biomebase, @Nullable String s, @Nullable DimensionType dimensionmanager) {
        this.field_193457_c = criterionconditionvalue;
        this.field_193458_d = criterionconditionvalue1;
        this.field_193459_e = criterionconditionvalue2;
        this.field_193456_b = biomebase;
        this.field_193460_f = s;
        this.field_193461_g = dimensionmanager;
    }

    public boolean func_193452_a(WorldServer worldserver, double d0, double d1, double d2) {
        return this.func_193453_a(worldserver, (float) d0, (float) d1, (float) d2);
    }

    public boolean func_193453_a(WorldServer worldserver, float f, float f1, float f2) {
        if (!this.field_193457_c.func_192514_a(f)) {
            return false;
        } else if (!this.field_193458_d.func_192514_a(f1)) {
            return false;
        } else if (!this.field_193459_e.func_192514_a(f2)) {
            return false;
        } else if (this.field_193461_g != null && this.field_193461_g != worldserver.field_73011_w.func_186058_p()) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos((double) f, (double) f1, (double) f2);

            return this.field_193456_b != null && this.field_193456_b != worldserver.func_180494_b(blockposition) ? false : this.field_193460_f == null || worldserver.func_72863_F().func_193413_a(worldserver, this.field_193460_f, blockposition);
        }
    }

    public static LocationPredicate func_193454_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "location");
            JsonObject jsonobject1 = JsonUtils.func_151218_a(jsonobject, "position", new JsonObject());
            MinMaxBounds criterionconditionvalue = MinMaxBounds.func_192515_a(jsonobject1.get("x"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.func_192515_a(jsonobject1.get("y"));
            MinMaxBounds criterionconditionvalue2 = MinMaxBounds.func_192515_a(jsonobject1.get("z"));
            DimensionType dimensionmanager = jsonobject.has("dimension") ? DimensionType.func_193417_a(JsonUtils.func_151200_h(jsonobject, "dimension")) : null;
            String s = jsonobject.has("feature") ? JsonUtils.func_151200_h(jsonobject, "feature") : null;
            Biome biomebase = null;

            if (jsonobject.has("biome")) {
                ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "biome"));

                biomebase = (Biome) Biome.field_185377_q.func_82594_a(minecraftkey);
                if (biomebase == null) {
                    throw new JsonSyntaxException("Unknown biome \'" + minecraftkey + "\'");
                }
            }

            return new LocationPredicate(criterionconditionvalue, criterionconditionvalue1, criterionconditionvalue2, biomebase, s, dimensionmanager);
        } else {
            return LocationPredicate.field_193455_a;
        }
    }
}
