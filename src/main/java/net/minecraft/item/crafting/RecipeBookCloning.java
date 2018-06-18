package net.minecraft.item.crafting;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class RecipeBookCloning extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    public RecipeBookCloning() {
        super("", new ItemStack(Items.WRITTEN_BOOK, 0, -1), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.WRITABLE_BOOK)));
    }
    // CraftBukkit end

    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inventorycrafting.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(j);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == Items.WRITTEN_BOOK) {
                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.getItem() != Items.WRITABLE_BOOK) {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemstack.isEmpty() && itemstack.hasTagCompound() && i > 0;
    }

    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        int i = 0;
        ItemStack itemstack = ItemStack.EMPTY;

        for (int j = 0; j < inventorycrafting.getSizeInventory(); ++j) {
            ItemStack itemstack1 = inventorycrafting.getStackInSlot(j);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() == Items.WRITTEN_BOOK) {
                    if (!itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.getItem() != Items.WRITABLE_BOOK) {
                        return ItemStack.EMPTY;
                    }

                    ++i;
                }
            }
        }

        if (!itemstack.isEmpty() && itemstack.hasTagCompound() && i >= 1 && ItemWrittenBook.getGeneration(itemstack) < 2) {
            ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK, i);

            itemstack2.setTagCompound(itemstack.getTagCompound().copy());
            itemstack2.getTagCompound().setInteger("generation", ItemWrittenBook.getGeneration(itemstack) + 1);
            if (itemstack.hasDisplayName()) {
                itemstack2.setStackDisplayName(itemstack.getDisplayName());
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

            if (itemstack.getItem() instanceof ItemWrittenBook) {
                ItemStack itemstack1 = itemstack.copy();

                itemstack1.setCount(1);
                nonnulllist.set(i, itemstack1);
                break;
            }
        }

        return nonnulllist;
    }

    public boolean isDynamic() {
        return true;
    }
}
