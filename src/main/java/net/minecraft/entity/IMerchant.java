package net.minecraft.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public interface IMerchant {

    void setCustomer(@Nullable EntityPlayer entityhuman);

    @Nullable
    EntityPlayer getCustomer();

    @Nullable
    MerchantRecipeList getRecipes(EntityPlayer entityhuman);

    void useRecipe(MerchantRecipe merchantrecipe);

    void verifySellingItem(ItemStack itemstack);

    ITextComponent getDisplayName();

    World getWorld();

    BlockPos getPos();
}
