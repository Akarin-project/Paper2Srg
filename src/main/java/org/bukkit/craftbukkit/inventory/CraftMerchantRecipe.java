package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import net;

public class CraftMerchantRecipe extends MerchantRecipe {

    private final net.minecraft.village.MerchantRecipe handle;

    public CraftMerchantRecipe(net.minecraft.village.MerchantRecipe merchantRecipe) {
        super(CraftItemStack.asBukkitCopy(merchantRecipe.field_77402_c), 0);
        this.handle = merchantRecipe;
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.field_77403_a));
        addIngredient(CraftItemStack.asBukkitCopy(merchantRecipe.field_77401_b));
    }

    public CraftMerchantRecipe(ItemStack result, int uses, int maxUses, boolean experienceReward) {
        super(result, uses, maxUses, experienceReward);
        this.handle = new net.minecraft.village.MerchantRecipe(
                net.minecraft.item.ItemStack.field_190927_a,
                net.minecraft.item.ItemStack.field_190927_a,
                CraftItemStack.asNMSCopy(result),
                uses,
                maxUses,
                this
        );
    }

    @Override
    public int getUses() {
        return handle.field_77400_d;
    }

    @Override
    public void setUses(int uses) {
        handle.field_77400_d = uses;
    }

    @Override
    public int getMaxUses() {
        return handle.field_82786_e;
    }

    @Override
    public void setMaxUses(int maxUses) {
        handle.field_82786_e = maxUses;
    }

    @Override
    public boolean hasExperienceReward() {
        return handle.field_180323_f;
    }

    @Override
    public void setExperienceReward(boolean flag) {
        handle.field_180323_f = flag;
    }

    public net.minecraft.village.MerchantRecipe toMinecraft() {
        List<ItemStack> ingredients = getIngredients();
        Preconditions.checkState(!ingredients.isEmpty(), "No offered ingredients");
        handle.field_77403_a = CraftItemStack.asNMSCopy(ingredients.get(0));
        if (ingredients.size() > 1) {
            handle.field_77401_b = CraftItemStack.asNMSCopy(ingredients.get(1));
        }
        return handle;
    }

    public static CraftMerchantRecipe fromBukkit(MerchantRecipe recipe) {
        if (recipe instanceof CraftMerchantRecipe) {
            return (CraftMerchantRecipe) recipe;
        } else {
            CraftMerchantRecipe craft = new CraftMerchantRecipe(recipe.getResult(), recipe.getUses(), recipe.getMaxUses(), recipe.hasExperienceReward());
            craft.setIngredients(recipe.getIngredients());

            return craft;
        }
    }
}
