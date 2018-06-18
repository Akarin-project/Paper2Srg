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

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.CriterionTriggerRecipeUnlocked.a;
import net.minecraft.server.CriterionTriggerRecipeUnlocked.b;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class RecipeUnlockedTrigger implements ICriterionTrigger<CriterionTriggerRecipeUnlocked.b> {

    private static final ResourceLocation ID = new ResourceLocation("recipe_unlocked");
    private final Map<PlayerAdvancements, CriterionTriggerRecipeUnlocked.a> listeners = Maps.newHashMap();

    public RecipeUnlockedTrigger() {}

    public ResourceLocation getId() {
        return RecipeUnlockedTrigger.ID;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b> criteriontrigger_a) {
        CriterionTriggerRecipeUnlocked.a criteriontriggerrecipeunlocked_a = (CriterionTriggerRecipeUnlocked.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerrecipeunlocked_a == null) {
            criteriontriggerrecipeunlocked_a = new CriterionTriggerRecipeUnlocked.a(advancementdataplayer);
            this.listeners.put(advancementdataplayer, criteriontriggerrecipeunlocked_a);
        }

        criteriontriggerrecipeunlocked_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b> criteriontrigger_a) {
        CriterionTriggerRecipeUnlocked.a criteriontriggerrecipeunlocked_a = (CriterionTriggerRecipeUnlocked.a) this.listeners.get(advancementdataplayer);

        if (criteriontriggerrecipeunlocked_a != null) {
            criteriontriggerrecipeunlocked_a.b(criteriontrigger_a);
            if (criteriontriggerrecipeunlocked_a.a()) {
                this.listeners.remove(advancementdataplayer);
            }
        }

    }

    public void removeAllListeners(PlayerAdvancements advancementdataplayer) {
        this.listeners.remove(advancementdataplayer);
    }

    public CriterionTriggerRecipeUnlocked.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.getString(jsonobject, "recipe"));
        IRecipe irecipe = CraftingManager.getRecipe(minecraftkey);

        if (irecipe == null) {
            throw new JsonSyntaxException("Unknown recipe \'" + minecraftkey + "\'");
        } else {
            return new CriterionTriggerRecipeUnlocked.b(irecipe);
        }
    }

    public void trigger(EntityPlayerMP entityplayer, IRecipe irecipe) {
        CriterionTriggerRecipeUnlocked.a criteriontriggerrecipeunlocked_a = (CriterionTriggerRecipeUnlocked.a) this.listeners.get(entityplayer.getAdvancements());

        if (criteriontriggerrecipeunlocked_a != null) {
            criteriontriggerrecipeunlocked_a.a(irecipe);
        }

    }

    public ICriterionInstance deserializeInstance(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(IRecipe irecipe) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            CriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (CriterionTrigger.a) iterator.next();
                if (((CriterionTriggerRecipeUnlocked.b) criteriontrigger_a.a()).a(irecipe)) {
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

        private final IRecipe a;

        public b(IRecipe irecipe) {
            super(RecipeUnlockedTrigger.ID);
            this.a = irecipe;
        }

        public boolean a(IRecipe irecipe) {
            return this.a == irecipe;
        }
    }
}
