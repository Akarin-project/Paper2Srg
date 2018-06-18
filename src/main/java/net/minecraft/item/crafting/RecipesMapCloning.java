package net.minecraft.item.crafting;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class RecipesMapCloning extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    // CraftBukkit start - Delegate to new parent class
    public RecipesMapCloning() {
        super("", new ItemStack(Items.MAP, 0, -1), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.MAP)));
    }
    // CraftBukkit end

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inventorycrafting.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(j);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == Items.FILLED_MAP) {
                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.getItem() != Items.MAP) {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemstack.isEmpty() && i > 0;
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inventorycrafting.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(j);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == Items.FILLED_MAP) {
                    if (!itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.getItem() != Items.MAP) {
                        return ItemStack.EMPTY;
                    }

                    ++i;
                }
            }
        }

        if (!itemstack.isEmpty() && i >= 1) {
            ItemStack itemstack2 = new ItemStack(Items.FILLED_MAP, i + 1, itemstack.getMetadata());

            if (itemstack.hasDisplayName()) {
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
            }

            if (itemstack.hasTagCompound()) {
                itemstack2.setTagCompound(itemstack.getTagCompound());
            }

            return itemstack2;
        } else {
            return ItemStack.EMPTY;
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
