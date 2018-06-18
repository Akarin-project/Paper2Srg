package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipesArmorDyes extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    // CraftBukkit start - Delegate to new parent class with bogus info
    public RecipesArmorDyes() {
        super("", new ItemStack(Items.LEATHER_HELMET, 0, 0), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.DYE)));
    }
    // CraftBukkit end

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        ItemStack itemstack = ItemStack.EMPTY;
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(i);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof ItemArmor) {
                    ItemArmor itemarmor = (ItemArmor) itemstack1.getItem();

                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.isEmpty()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.getItem() != Items.DYE) {
                        return false;
                    }

                    arraylist.add(itemstack1);
                }
            }
        }

        return !itemstack.isEmpty() && !arraylist.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ItemStack itemstack = ItemStack.EMPTY;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemArmor itemarmor = null;

        int k;
        int l;
        float f;
        float f1;
        int i1;

        for (k = 0; k < inventorycrafting.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(k);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof ItemArmor) {
                    itemarmor = (ItemArmor) itemstack1.getItem();
                    if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || !itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1.copy();
                    itemstack.setCount(1);
                    if (itemarmor.hasColor(itemstack1)) {
                        l = itemarmor.getColor(itemstack);
                        f = (float) (l >> 16 & 255) / 255.0F;
                        f1 = (float) (l >> 8 & 255) / 255.0F;
                        float f2 = (float) (l & 255) / 255.0F;

                        i = (int) ((float) i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int) ((float) aint[0] + f * 255.0F);
                        aint[1] = (int) ((float) aint[1] + f1 * 255.0F);
                        aint[2] = (int) ((float) aint[2] + f2 * 255.0F);
                        ++j;
                    }
                } else {
                    if (itemstack1.getItem() != Items.DYE) {
                        return ItemStack.EMPTY;
                    }

                    float[] afloat = EnumDyeColor.byDyeDamage(itemstack1.getMetadata()).getColorComponentValues();
                    int j1 = (int) (afloat[0] * 255.0F);
                    int k1 = (int) (afloat[1] * 255.0F);

                    i1 = (int) (afloat[2] * 255.0F);
                    i += Math.max(j1, Math.max(k1, i1));
                    aint[0] += j1;
                    aint[1] += k1;
                    aint[2] += i1;
                    ++j;
                }
            }
        }

        if (itemarmor == null) {
            return ItemStack.EMPTY;
        } else {
            k = aint[0] / j;
            int l1 = aint[1] / j;

            l = aint[2] / j;
            f = (float) i / (float) j;
            f1 = (float) Math.max(k, Math.max(l1, l));
            k = (int) ((float) k * f / f1);
            l1 = (int) ((float) l1 * f / f1);
            l = (int) ((float) l * f / f1);
            i1 = (k << 8) + l1;
            i1 = (i1 << 8) + l;
            itemarmor.setColor(itemstack, i1);
            return itemstack;
        }
    }

    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventorycrafting) {
        NonNullList nonnulllist = NonNullList.withSize(inventorycrafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inventorycrafting.getStackInSlot(i);

            if (itemstack.getItem().hasContainerItem()) {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }

        return nonnulllist;
    }

    public boolean isDynamic() {
        return true;
    }
}
