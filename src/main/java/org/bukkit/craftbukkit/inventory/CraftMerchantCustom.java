package org.bukkit.craftbukkit.inventory;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.IMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class CraftMerchantCustom extends CraftMerchant {

    public CraftMerchantCustom(String title) {
        super(new MinecraftMerchant(title));
    }

    @Override
    public String toString() {
        return "CraftMerchantCustom";
    }

    private static class MinecraftMerchant implements IMerchant {

        private final String title;
        private final MerchantRecipeList trades = new MerchantRecipeList();
        private EntityPlayer tradingPlayer;

        public MinecraftMerchant(String title) {
            this.title = title;
        }

        @Override
        public void func_70932_a_(EntityPlayer entityhuman) {
            this.tradingPlayer = entityhuman;
        }

        @Override
        public EntityPlayer func_70931_l_() {
            return this.tradingPlayer;
        }

        @Override
        public MerchantRecipeList func_70934_b(EntityPlayer entityhuman) {
            return this.trades;
        }

        @Override
        public void func_70933_a(MerchantRecipe merchantrecipe) {
            // increase recipe's uses
            merchantrecipe.func_77399_f();
        }

        @Override
        public void func_110297_a_(ItemStack itemstack) {
        }

        @Override
        public ITextComponent func_145748_c_() {
            return new TextComponentString(title);
        }

        @Override
        public World func_190670_t_() {
            return null;
        }

        @Override
        public BlockPos func_190671_u_() {
            return null;
        }
    }
}
