package net.minecraft.advancements.critereon;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionType;
import net.minecraft.server.CriterionTriggerBrewedPotion.a;
import net.minecraft.server.CriterionTriggerBrewedPotion.b;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class BrewedPotionTrigger implements ICriterionTrigger<CriterionTriggerBrewedPotion.b> {

    private static final ResourceLocation field_192176_a = new ResourceLocation("brewed_potion");
    private final Map<PlayerAdvancements, CriterionTriggerBrewedPotion.a> field_192177_b = Maps.newHashMap();

    public BrewedPotionTrigger() {}

    public ResourceLocation func_192163_a() {
        return BrewedPotionTrigger.field_192176_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerBrewedPotion.b> criteriontrigger_a) {
        CriterionTriggerBrewedPotion.a criteriontriggerbrewedpotion_a = (CriterionTriggerBrewedPotion.a) this.field_192177_b.get(advancementdataplayer);

        if (criteriontriggerbrewedpotion_a == null) {
            criteriontriggerbrewedpotion_a = new CriterionTriggerBrewedPotion.a(advancementdataplayer);
            this.field_192177_b.put(advancementdataplayer, criteriontriggerbrewedpotion_a);
        }

        criteriontriggerbrewedpotion_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerBrewedPotion.b> criteriontrigger_a) {
        CriterionTriggerBrewedPotion.a criteriontriggerbrewedpotion_a = (CriterionTriggerBrewedPotion.a) this.field_192177_b.get(advancementdataplayer);

        if (criteriontriggerbrewedpotion_a != null) {
            criteriontriggerbrewedpotion_a.b(criteriontrigger_a);
            if (criteriontriggerbrewedpotion_a.a()) {
                this.field_192177_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192177_b.remove(advancementdataplayer);
    }

    public CriterionTriggerBrewedPotion.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        PotionType potionregistry = null;

        if (jsonobject.has("potion")) {
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "potion"));

            if (!PotionType.field_185176_a.func_148741_d(minecraftkey)) {
                throw new JsonSyntaxException("Unknown potion \'" + minecraftkey + "\'");
            }

            potionregistry = (PotionType) PotionType.field_185176_a.func_82594_a(minecraftkey);
        }

        return new CriterionTriggerBrewedPotion.b(potionregistry);
    }

    public void func_192173_a(EntityPlayerMP entityplayer, PotionType potionregistry) {
        CriterionTriggerBrewedPotion.a criteriontriggerbrewedpotion_a = (CriterionTriggerBrewedPotion.a) this.field_192177_b.get(entityplayer.func_192039_O());

        if (criteriontriggerbrewedpotion_a != null) {
            criteriontriggerbrewedpotion_a.a(potionregistry);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerBrewedPotion.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerBrewedPotion.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerBrewedPotion.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(PotionType potionregistry) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerBrewedPotion.b) criteriontrigger_a.a()).a(potionregistry)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                    criteriontrigger_a.a(this.a);
                }
            }

        }
    }

    public static class b extends AbstractCriterionInstance {

        private final PotionType a;

        public b(@Nullable PotionType potionregistry) {
            super(BrewedPotionTrigger.field_192176_a);
            this.a = potionregistry;
        }

        public boolean a(PotionType potionregistry) {
            return this.a == null || this.a == potionregistry;
        }
    }
}
