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
import net.minecraft.server.CriterionTrigger;
import net.minecraft.server.CriterionTriggerBrewedPotion;
import net.minecraft.server.CriterionTriggerBrewedPotion.a;
import net.minecraft.server.CriterionTriggerBrewedPotion.b;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class BrewedPotionTrigger implements ICriterionTrigger<CriterionTriggerBrewedPotion.b> {

    private static final ResourceLocation ID = new ResourceLocation("brewed_potion");
    private final Map<PlayerAdvancements, CriterionTriggerBrewedPotion.a> listeners = Maps.newHashMap();

    public BrewedPotionTrigger() {}

    @Override
    public ResourceLocation getId() {
        return BrewedPotionTrigger.ID;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerBrewedPotion.b> criteriontrigger_a) {
        CriterionTriggerBrewedPotion.a criteriontriggerbrewedpotion_a = this.listeners.get(advancementdataplayer);

        if (criteriontriggerbrewedpotion_a == null) {
            criteriontriggerbrewedpotion_a = new CriterionTriggerBrewedPotion.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerbrewedpotion_a);
        }

        criteriontriggerbrewedpotion_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerBrewedPotion.b> criteriontrigger_a) {
        CriterionTriggerBrewedPotion.a criteriontriggerbrewedpotion_a = this.listeners.get(advancementdataplayer);

        if (criteriontriggerbrewedpotion_a != null) {
            criteriontriggerbrewedpotion_a.b(criteriontrigger_a);
            if (criteriontriggerbrewedpotion_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerBrewedPotion.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        PotionType potionregistry = null;

        if (jsonobject.has("potion")) {
            ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "potion"));

            if (!PotionType.REGISTRY.containsKey(minecraftkey)) {
                throw new JsonSyntaxException("Unknown potion \'" + minecraftkey + "\'");
            }

            potionregistry = PotionType.REGISTRY.getObject(minecraftkey);
        }

        return new CriterionTriggerBrewedPotion.b(potionregistry);
    }

    public void trigger(EntityPlayerMP entityplayer, PotionType potionregistry) {
        CriterionTriggerBrewedPotion.a criteriontriggerbrewedpotion_a = this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerbrewedpotion_a != null) {
            criteriontriggerbrewedpotion_a.a(potionregistry);
        }

    }

    @Override
    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
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
            super(BrewedPotionTrigger.ID);
            this.a = potionregistry;
        }

        public boolean a(PotionType potionregistry) {
            return this.a == null || this.a == potionregistry;
        }
    }
}
