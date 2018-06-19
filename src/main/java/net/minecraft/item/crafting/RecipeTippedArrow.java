package net.minecraft.item.crafting;

import java.util.Collection;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeTippedArrow extends ShapedRecipes implements IRecipe { // CraftBukkit

    // CraftBukkit start
    public RecipeTippedArrow() {
        super("", 3, 3, NonNullList.func_193580_a(Ingredient.field_193370_a,
                Ingredient.func_193367_a(Items.field_151032_g), Ingredient.func_193367_a(Items.field_151032_g), Ingredient.func_193367_a(Items.field_151032_g),
                Ingredient.func_193367_a(Items.field_151032_g), Ingredient.func_193367_a(Items.field_185156_bI), Ingredient.func_193367_a(Items.field_151032_g),
                Ingredient.func_193367_a(Items.field_151032_g), Ingredient.func_193367_a(Items.field_151032_g), Ingredient.func_193367_a(Items.field_151032_g)),
                new ItemStack(Items.field_185167_i, 8));
    }
    // CraftBukkit end

    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        if (inventorycrafting.func_174922_i() == 3 && inventorycrafting.func_174923_h() == 3) {
            for (int i = 0; i < inventorycrafting.func_174922_i(); ++i) {
                for (int j = 0; j < inventorycrafting.func_174923_h(); ++j) {
                    ItemStack itemstack = inventorycrafting.func_70463_b(i, j);

                    if (itemstack.func_190926_b()) {
                        return false;
                    }

                    Item item = itemstack.func_77973_b();

                    if (i == 1 && j == 1) {
                        if (item != Items.field_185156_bI) {
                            return false;
                        }
                    } else if (item != Items.field_151032_g) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        ItemStack itemstack = inventorycrafting.func_70463_b(1, 1);

        if (itemstack.func_77973_b() != Items.field_185156_bI) {
            return ItemStack.field_190927_a;
        } else {
            ItemStack itemstack1 = new ItemStack(Items.field_185167_i, 8);

            PotionUtils.func_185188_a(itemstack1, PotionUtils.func_185191_c(itemstack));
            PotionUtils.func_185184_a(itemstack1, (Collection) PotionUtils.func_185190_b(itemstack));
            return itemstack1;
        }
    }

    public ItemStack func_77571_b() {
        return ItemStack.field_190927_a;
    }

    public NonNullList<ItemStack> func_179532_b(InventoryCrafting inventorycrafting) {
        return NonNullList.func_191197_a(inventorycrafting.func_70302_i_(), ItemStack.field_190927_a);
    }

    public boolean func_192399_d() {
        return true;
    }
}
