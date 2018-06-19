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

    void func_70932_a_(@Nullable EntityPlayer entityhuman);

    @Nullable
    EntityPlayer func_70931_l_();

    @Nullable
    MerchantRecipeList func_70934_b(EntityPlayer entityhuman);

    void func_70933_a(MerchantRecipe merchantrecipe);

    void func_110297_a_(ItemStack itemstack);

    ITextComponent func_145748_c_();

    World func_190670_t_();

    BlockPos func_190671_u_();
}
