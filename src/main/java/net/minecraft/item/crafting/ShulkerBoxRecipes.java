package net.minecraft.item.crafting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


// CraftBukkit - decompile weirdness
public class ShulkerBoxRecipes {

    public static class ShulkerBoxColoring extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        public ShulkerBoxColoring() {
            super("", new ItemStack(Blocks.WHITE_SHULKER_BOX, 0, 0), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.DYE)));
        }
        // CraftBukkit end

        public boolean matches(InventoryCrafting inventorycrafting, World world) {
            int i = 0;
            int j = 0;

            for (int k = 0; k < inventorycrafting.getSizeInventory(); ++k) {
                ItemStack itemstack = inventorycrafting.getStackInSlot(k);

                if (!itemstack.isEmpty()) {
                    if (Block.getBlockFromItem(itemstack.getItem()) instanceof BlockShulkerBox) {
                        ++i;
                    } else {
                        if (itemstack.getItem() != Items.DYE) {
                            return false;
                        }

                        ++j;
                    }

                    if (j > 1 || i > 1) {
                        return false;
                    }
                }
            }

            return i == 1 && j == 1;
        }

        public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack2 = inventorycrafting.getStackInSlot(i);

                if (!itemstack2.isEmpty()) {
                    if (Block.getBlockFromItem(itemstack2.getItem()) instanceof BlockShulkerBox) {
                        itemstack = itemstack2;
                    } else if (itemstack2.getItem() == Items.DYE) {
                        itemstack1 = itemstack2;
                    }
                }
            }

            ItemStack itemstack3 = BlockShulkerBox.getColoredItemStack(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()));

            if (itemstack.hasTagCompound()) {
                itemstack3.setTagCompound(itemstack.getTagCompound().copy());
            }

            return itemstack3;
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
}
