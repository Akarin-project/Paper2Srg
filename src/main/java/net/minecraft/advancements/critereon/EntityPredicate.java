package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class EntityPredicate {

    public static final EntityPredicate ANY = new EntityPredicate((ResourceLocation) null, DistancePredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NBTPredicate.ANY);
    private final ResourceLocation type;
    private final DistancePredicate distance;
    private final LocationPredicate location;
    private final MobEffectsPredicate effects;
    private final NBTPredicate nbt;

    public EntityPredicate(@Nullable ResourceLocation minecraftkey, DistancePredicate criterionconditiondistance, LocationPredicate criterionconditionlocation, MobEffectsPredicate criterionconditionmobeffect, NBTPredicate criterionconditionnbt) {
        this.type = minecraftkey;
        this.distance = criterionconditiondistance;
        this.location = criterionconditionlocation;
        this.effects = criterionconditionmobeffect;
        this.nbt = criterionconditionnbt;
    }

    public boolean test(EntityPlayerMP entityplayer, @Nullable Entity entity) {
        return this == EntityPredicate.ANY ? true : (entity == null ? false : (this.type != null && !EntityList.isMatchingName(entity, this.type) ? false : (!this.distance.test(entityplayer.posX, entityplayer.posY, entityplayer.posZ, entity.posX, entity.posY, entity.posZ) ? false : (!this.location.test(entityplayer.getServerWorld(), entity.posX, entity.posY, entity.posZ) ? false : (!this.effects.test(entity) ? false : this.nbt.test(entity))))));
    }

    public static EntityPredicate deserialize(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "entity");
            ResourceLocation minecraftkey = null;

            if (jsonobject.has("type")) {
                minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "type"));
                if (!EntityList.isRegistered(minecraftkey)) {
                    throw new JsonSyntaxException("Unknown entity type \'" + minecraftkey + "\', valid types are: " + EntityList.getValidTypeNames());
                }
            }

            DistancePredicate criterionconditiondistance = DistancePredicate.deserialize(jsonobject.get("distance"));
            LocationPredicate criterionconditionlocation = LocationPredicate.deserialize(jsonobject.get("location"));
            MobEffectsPredicate criterionconditionmobeffect = MobEffectsPredicate.deserialize(jsonobject.get("effects"));
            NBTPredicate criterionconditionnbt = NBTPredicate.deserialize(jsonobject.get("nbt"));

            return new EntityPredicate(minecraftkey, criterionconditiondistance, criterionconditionlocation, criterionconditionmobeffect, criterionconditionnbt);
        } else {
            return EntityPredicate.ANY;
        }
    }
}
