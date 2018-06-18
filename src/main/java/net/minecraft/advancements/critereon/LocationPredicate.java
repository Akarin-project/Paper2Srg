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

    public static LocationPredicate ANY = new LocationPredicate(MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, MinMaxBounds.UNBOUNDED, (Biome) null, (String) null, (DimensionType) null);
    private final MinMaxBounds x;
    private final MinMaxBounds y;
    private final MinMaxBounds z;
    @Nullable
    final Biome biome;
    @Nullable
    private final String feature;
    @Nullable
    private final DimensionType dimension;

    public LocationPredicate(MinMaxBounds criterionconditionvalue, MinMaxBounds criterionconditionvalue1, MinMaxBounds criterionconditionvalue2, @Nullable Biome biomebase, @Nullable String s, @Nullable DimensionType dimensionmanager) {
        this.x = criterionconditionvalue;
        this.y = criterionconditionvalue1;
        this.z = criterionconditionvalue2;
        this.biome = biomebase;
        this.feature = s;
        this.dimension = dimensionmanager;
    }

    public boolean test(WorldServer worldserver, double d0, double d1, double d2) {
        return this.test(worldserver, (float) d0, (float) d1, (float) d2);
    }

    public boolean test(WorldServer worldserver, float f, float f1, float f2) {
        if (!this.x.test(f)) {
            return false;
        } else if (!this.y.test(f1)) {
            return false;
        } else if (!this.z.test(f2)) {
            return false;
        } else if (this.dimension != null && this.dimension != worldserver.provider.getDimensionType()) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos((double) f, (double) f1, (double) f2);

            return this.biome != null && this.biome != worldserver.getBiome(blockposition) ? false : this.feature == null || worldserver.getChunkProvider().isInsideStructure(worldserver, this.feature, blockposition);
        }
    }

    public static LocationPredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "location");
            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "position", new JsonObject());
            MinMaxBounds criterionconditionvalue = MinMaxBounds.deserialize(jsonobject1.get("x"));
            MinMaxBounds criterionconditionvalue1 = MinMaxBounds.deserialize(jsonobject1.get("y"));
            MinMaxBounds criterionconditionvalue2 = MinMaxBounds.deserialize(jsonobject1.get("z"));
            DimensionType dimensionmanager = jsonobject.has("dimension") ? DimensionType.byName(JsonUtils.getString(jsonobject, "dimension")) : null;
            String s = jsonobject.has("feature") ? JsonUtils.getString(jsonobject, "feature") : null;
            Biome biomebase = null;

            if (jsonobject.has("biome")) {
                ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "biome"));

                biomebase = (Biome) Biome.REGISTRY.getObject(minecraftkey);
                if (biomebase == null) {
                    throw new JsonSyntaxException("Unknown biome \'" + minecraftkey + "\'");
                }
            }

            return new LocationPredicate(criterionconditionvalue, criterionconditionvalue1, criterionconditionvalue2, biomebase, s, dimensionmanager);
        } else {
            return LocationPredicate.ANY;
        }
    }
}
