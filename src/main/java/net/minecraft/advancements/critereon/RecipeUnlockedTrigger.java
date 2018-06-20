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
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class RecipeUnlockedTrigger implements ICriterionTrigger<RecipeUnlockedTrigger.b> {

    private static final ResourceLocation field_192227_a = new ResourceLocation("recipe_unlocked");
    private final Map<PlayerAdvancements, RecipeUnlockedTrigger.a> field_192228_b = Maps.newHashMap();

    public RecipeUnlockedTrigger() {}

    @Override
    public ResourceLocation func_192163_a() {
        return RecipeUnlockedTrigger.field_192227_a;
    }

    @Override
    public void a(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<RecipeUnlockedTrigger.b> criteriontrigger_a) {
        RecipeUnlockedTrigger.a criteriontriggerrecipeunlocked_a = this.field_192228_b.get(advancementdataplayer);

        if (criteriontriggerrecipeunlocked_a == null) {
            criteriontriggerrecipeunlocked_a = new RecipeUnlockedTrigger.a(advancementdataplayer);
            this.field_192228_b.put(advancementdataplayer, criteriontriggerrecipeunlocked_a);
        }

        criteriontriggerrecipeunlocked_a.a(criteriontrigger_a);
    }

    @Override
    public void b(PlayerAdvancements advancementdataplayer, ICriterionTrigger.a<RecipeUnlockedTrigger.b> criteriontrigger_a) {
        RecipeUnlockedTrigger.a criteriontriggerrecipeunlocked_a = this.field_192228_b.get(advancementdataplayer);

        if (criteriontriggerrecipeunlocked_a != null) {
            criteriontriggerrecipeunlocked_a.b(criteriontrigger_a);
            if (criteriontriggerrecipeunlocked_a.a()) {
                this.field_192228_b.remove(advancementdataplayer);
            }
        }

    }

    @Override
    public void func_192167_a(PlayerAdvancements advancementdataplayer) {
        this.field_192228_b.remove(advancementdataplayer);
    }

    public RecipeUnlockedTrigger.b b(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        ResourceLocation minecraftkey = new ResourceLocation(JsonUtils.func_151200_h(jsonobject, "recipe"));
        IRecipe irecipe = CraftingManager.func_193373_a(minecraftkey);

        if (irecipe == null) {
            throw new JsonSyntaxException("Unknown recipe \'" + minecraftkey + "\'");
        } else {
            return new RecipeUnlockedTrigger.b(irecipe);
        }
    }

    public void func_192225_a(EntityPlayerMP entityplayer, IRecipe irecipe) {
        RecipeUnlockedTrigger.a criteriontriggerrecipeunlocked_a = this.field_192228_b.get(entityplayer.func_192039_O());

        if (criteriontriggerrecipeunlocked_a != null) {
            criteriontriggerrecipeunlocked_a.a(irecipe);
        }

    }

    @Override
    public b func_192166_a(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
        return this.b(jsonobject, jsondeserializationcontext);
    }

    static class a {

        private final PlayerAdvancements a;
        private final Set<ICriterionTrigger.a<RecipeUnlockedTrigger.b>> b = Sets.newHashSet();

        public a(PlayerAdvancements advancementdataplayer) {
            this.a = advancementdataplayer;
        }

        public boolean a() {
            return this.b.isEmpty();
        }

        public void a(ICriterionTrigger.a<RecipeUnlockedTrigger.b> criteriontrigger_a) {
            this.b.add(criteriontrigger_a);
        }

        public void b(ICriterionTrigger.a<RecipeUnlockedTrigger.b> criteriontrigger_a) {
            this.b.remove(criteriontrigger_a);
        }

        public void a(IRecipe irecipe) {
            ArrayList arraylist = null;
            Iterator iterator = this.b.iterator();

            ICriterionTrigger.a criteriontrigger_a;

            while (iterator.hasNext()) {
                criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
                if (((RecipeUnlockedTrigger.b) criteriontrigger_a.a()).a(irecipe)) {
                    if (arraylist == null) {
                        arraylist = Lists.newArrayList();
                    }

                    arraylist.add(criteriontrigger_a);
                }
            }

            if (arraylist != null) {
                iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    criteriontrigger_a = (ICriterionTrigger.a) iterator.next();
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
