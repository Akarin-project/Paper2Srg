package net.minecraft.item.crafting;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


// CraftBukkit - decompile weirdness
public class ShieldRecipes {

    public static class Decoration extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        public Decoration() {
            super("", new ItemStack(Items.SHIELD, 0, 0), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.BANNER)));
        }
        // CraftBukkit end

        public boolean matches(InventoryCrafting inventorycrafting, World world) {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack2 = inventorycrafting.getStackInSlot(i);

                if (!itemstack2.isEmpty()) {
                    if (itemstack2.getItem() == Items.BANNER) {
                        if (!itemstack1.isEmpty()) {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    } else {
                        if (itemstack2.getItem() != Items.SHIELD) {
                            return false;
                        }

                        if (!itemstack.isEmpty()) {
                            return false;
                        }

                        if (itemstack2.getSubCompound("BlockEntityTag") != null) {
                            return false;
                        }

                        itemstack = itemstack2;
                    }
                }
            }

            if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack2 = inventorycrafting.getStackInSlot(i);

                if (!itemstack2.isEmpty()) {
                    if (itemstack2.getItem() == Items.BANNER) {
                        itemstack = itemstack2;
                    } else if (itemstack2.getItem() == Items.SHIELD) {
                        itemstack1 = itemstack2.copy();
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                return itemstack1;
            } else {
                NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");
                NBTTagCompound nbttagcompound1 = nbttagcompound == null ? new NBTTagCompound() : nbttagcompound.copy();

                nbttagcompound1.setInteger("Base", itemstack.getMetadata() & 15);
                itemstack1.setTagInfo("BlockEntityTag", (NBTBase) nbttagcompound1);
                return itemstack1;
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
}
