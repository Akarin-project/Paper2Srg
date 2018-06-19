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

    public static final EntityPredicate field_192483_a = new EntityPredicate((ResourceLocation) null, DistancePredicate.field_193423_a, LocationPredicate.field_193455_a, MobEffectsPredicate.field_193473_a, NBTPredicate.field_193479_a);
    private final ResourceLocation field_192484_b;
    private final DistancePredicate field_192485_c;
    private final LocationPredicate field_193435_d;
    private final MobEffectsPredicate field_193436_e;
    private final NBTPredicate field_193437_f;

    public EntityPredicate(@Nullable ResourceLocation minecraftkey, DistancePredicate criterionconditiondistance, LocationPredicate criterionconditionlocation, MobEffectsPredicate criterionconditionmobeffect, NBTPredicate criterionconditionnbt) {
        this.field_192484_b = minecraftkey;
        this.field_192485_c = criterionconditiondistance;
        this.field_193435_d = criterionconditionlocation;
        this.field_193436_e = criterionconditionmobeffect;
        this.field_193437_f = criterionconditionnbt;
    }

    public boolean func_192482_a(EntityPlayerMP entityplayer, @Nullable Entity entity) {
        return this == EntityPredicate.field_192483_a ? true : (entity == null ? false : (this.field_192484_b != null && !EntityList.func_180123_a(entity, this.field_192484_b) ? false : (!this.field_192485_c.func_193422_a(entityplayer.field_70165_t, entityplayer.field_70163_u, entityplayer.field_70161_v, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v) ? false : (!this.field_193435_d.func_193452_a(entityplayer.func_71121_q(), entity.field_70165_t, entity.field_70163_u, entity.field_70161_v) ? false : (!this.field_193436_e.func_193469_a(entity) ? false : this.field_193437_f.func_193475_a(entity))))));
    }

    public static EntityPredicate func_192481_a(@Nullable JsonElement jsonelement) {
        if (jsonelement != null && !jsonelement.isJsonNull()) {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "entity");
            ResourceLocation minecraftkey = null;

            if (jsonobject.has("type")) {
                minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "type"));
                if (!EntityList.func_180125_b(minecraftkey)) {
                    throw new JsonSyntaxException("Unknown entity type \'" + minecraftkey + "\', valid types are: " + EntityList.func_192840_b());
                }
            }

            DistancePredicate criterionconditiondistance = DistancePredicate.func_193421_a(jsonobject.get("distance"));
            LocationPredicate criterionconditionlocation = LocationPredicate.func_193454_a(jsonobject.get("location"));
            MobEffectsPredicate criterionconditionmobeffect = MobEffectsPredicate.func_193471_a(jsonobject.get("effects"));
            NBTPredicate criterionconditionnbt = NBTPredicate.func_193476_a(jsonobject.get("nbt"));

            return new EntityPredicate(minecraftkey, criterionconditiondistance, criterionconditionlocation, criterionconditionmobeffect, criterionconditionnbt);
        } else {
            return EntityPredicate.field_192483_a;
        }
    }
}
