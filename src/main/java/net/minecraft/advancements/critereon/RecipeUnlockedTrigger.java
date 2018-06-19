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

    private static final ResourceLocation field_192227_a = new ResourceLocation("recipe_unlocked");
    private final Map<PlayerAdvancements, CriterionTriggerRecipeUnlocked.a> field_192228_b = Maps.newHashMap();

    public RecipeUnlockedTrigger() {}

    public ResourceLocation func_192163_a() {
        return RecipeUnlockedTrigger.field_192227_a;
    }

    public void a(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b> criteriontrigger_a) {
        CriterionTriggerRecipeUnlocked.a criteriontriggerrecipeunlocked_a = (CriterionTriggerRecipeUnlocked.a) this.field_192228_b.get(advancementdataplayer);

        if (criteriontriggerrecipeunlocked_a == null) {
            criteriontriggerrecipeunlocked_a = new CriterionTriggerRecipeUnlocked.a(advancementdataplayer);
            this.field_192228_b.put(advancementdataplayer, criteriontriggerrecipeunlocked_a);
        }

        criteriontriggerrecipeunlocked_a.a(criteriontrigger_a);
    }

    public void b(PlayerAdvancements advancementdataplayer, CriterionTrigger.a<CriterionTriggerRecipeUnlocked.b> criteriontrigger_a) {
        CriterionTriggerRecipeUnlocked.a criteriontriggerrecipeunlocked_a = (CriterionTriggerRecipeUnlocked.a) this.field_192228_b.get(advancementdataplayer);

        if (criteriontriggerrecipeunlocked_a != null) {
            criteriontriggerrecipeunlocked_a.b(criteriontrigger_a);
            if (criteriontriggerrecipeunlocked_a.a()) {
                this.field_192228_b.remove(advancementdataplayer);
            }
        }

    }

    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192228_b.remove(advancementdataplayer);
    }

    public CriterionTriggerRecipeUnlocked.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "recipe"));
        IRecipe irecipe = CraftingManager.func_193373_a(minecraftkey);

        if (irecipe == null) {
            throw new JsonSyntaxException("Unknown recipe \'" + minecraftkey + "\'");
        } else {
            return new CriterionTriggerRecipeUnlocked.b(irecipe);
        }
    }

    public void func_192225_a(EntityPlayerMP entityplayer, IRecipe irecipe) {
        CriterionTriggerRecipeUnlocked.a criteriontriggerrecipeunlocked_a = (CriterionTriggerRecipeUnlocked.a) this.field_192228_b.get(entityplayer.func_192039_O());

        if (criteriontriggerrecipeunlocked_a != null) {
            criteriontriggerrecipeunlocked_a.a(irecipe);
        }

    }

    public ICriterionInstance func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
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
            super(RecipeUnlockedTrigger.field_192227_a);
            this.a = irecipe;
        }

        public boolean a(IRecipe irecipe) {
            return this.a == irecipe;
        }
    }
}
