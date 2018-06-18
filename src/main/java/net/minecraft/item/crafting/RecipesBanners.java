package net.minecraft.item.crafting;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

// CraftBukkit - decompile weirdness
public class RecipesBanners {

    public static class RecipeAddPattern extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        public RecipeAddPattern() {
            super("", new ItemStack(Items.BANNER, 0, 0), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.BANNER)));
        }
        // CraftBukkit end

        public boolean matches(InventoryCrafting inventorycrafting, World world) {
            boolean flag = false;

            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack = inventorycrafting.getStackInSlot(i);

                if (itemstack.getItem() == Items.BANNER) {
                    if (flag) {
                        return false;
                    }

                    if (TileEntityBanner.getPatterns(itemstack) >= 6) {
                        return false;
                    }

                    flag = true;
                }
            }

            if (!flag) {
                return false;
            } else {
                return this.matchPatterns(inventorycrafting) != null;
            }
        }

        public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = ItemStack.EMPTY;

            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack1 = inventorycrafting.getStackInSlot(i);

                if (!itemstack1.isEmpty() && itemstack1.getItem() == Items.BANNER) {
                    itemstack = itemstack1.copy();
                    itemstack.setCount(1);
                    break;
                }
            }

            BannerPattern enumbannerpatterntype = this.matchPatterns(inventorycrafting);

            if (enumbannerpatterntype != null) {
                int j = 0;

                for (int k = 0; k < inventorycrafting.getSizeInventory(); ++k) {
                    ItemStack itemstack2 = inventorycrafting.getStackInSlot(k);

                    if (itemstack2.getItem() == Items.DYE) {
                        j = itemstack2.getMetadata();
                        break;
                    }
                }

                NBTTagCompound nbttagcompound = itemstack.getOrCreateSubCompound("BlockEntityTag");
                NBTTagList nbttaglist;

                if (nbttagcompound.hasKey("Patterns", 9)) {
                    nbttaglist = nbttagcompound.getTagList("Patterns", 10);
                } else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound.setTag("Patterns", nbttaglist);
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setString("Pattern", enumbannerpatterntype.getHashname());
                nbttagcompound1.setInteger("Color", j);
                nbttaglist.appendTag(nbttagcompound1);
            }

            return itemstack;
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

        @Nullable
        private BannerPattern matchPatterns(InventoryCrafting inventorycrafting) {
            BannerPattern[] aenumbannerpatterntype = BannerPattern.values();
            int i = aenumbannerpatterntype.length;

            for (int j = 0; j < i; ++j) {
                BannerPattern enumbannerpatterntype = aenumbannerpatterntype[j];

                if (enumbannerpatterntype.hasPattern()) {
                    boolean flag = true;
                    int k;

                    if (enumbannerpatterntype.hasPatternItem()) {
                        boolean flag1 = false;
                        boolean flag2 = false;

                        for (k = 0; k < inventorycrafting.getSizeInventory() && flag; ++k) {
                            ItemStack itemstack = inventorycrafting.getStackInSlot(k);

                            if (!itemstack.isEmpty() && itemstack.getItem() != Items.BANNER) {
                                if (itemstack.getItem() == Items.DYE) {
                                    if (flag2) {
                                        flag = false;
                                        break;
                                    }

                                    flag2 = true;
                                } else {
                                    if (flag1 || !itemstack.isItemEqual(enumbannerpatterntype.getPatternItem())) {
                                        flag = false;
                                        break;
                                    }

                                    flag1 = true;
                                }
                            }
                        }

                        if (!flag1 || !flag2) {
                            flag = false;
                        }
                    } else if (inventorycrafting.getSizeInventory() != enumbannerpatterntype.getPatterns().length * enumbannerpatterntype.getPatterns()[0].length()) {
                        flag = false;
                    } else {
                        int l = -1;

                        for (int i1 = 0; i1 < inventorycrafting.getSizeInventory() && flag; ++i1) {
                            k = i1 / 3;
                            int j1 = i1 % 3;
                            ItemStack itemstack1 = inventorycrafting.getStackInSlot(i1);

                            if (!itemstack1.isEmpty() && itemstack1.getItem() != Items.BANNER) {
                                if (itemstack1.getItem() != Items.DYE) {
                                    flag = false;
                                    break;
                                }

                                if (l != -1 && l != itemstack1.getMetadata()) {
                                    flag = false;
                                    break;
                                }

                                if (enumbannerpatterntype.getPatterns()[k].charAt(j1) == 32) {
                                    flag = false;
                                    break;
                                }

                                l = itemstack1.getMetadata();
                            } else if (enumbannerpatterntype.getPatterns()[k].charAt(j1) != 32) {
                                flag = false;
                                break;
                            }
                        }
                    }

                    if (flag) {
                        return enumbannerpatterntype;
                    }
                }
            }

            return null;
        }

        public boolean isDynamic() {
            return true;
        }
    }

    public static class RecipeDuplicatePattern extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        public RecipeDuplicatePattern() {
            super("", new ItemStack(Items.BANNER, 0, 0), NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(Items.DYE)));
        }
        // CraftBukkit end

        public boolean matches(InventoryCrafting inventorycrafting, World world) {
            ItemStack itemstack = ItemStack.EMPTY;
            ItemStack itemstack1 = ItemStack.EMPTY;

            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack2 = inventorycrafting.getStackInSlot(i);

                if (!itemstack2.isEmpty()) {
                    if (itemstack2.getItem() != Items.BANNER) {
                        return false;
                    }

                    if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
                        return false;
                    }

                    EnumDyeColor enumcolor = ItemBanner.getBaseColor(itemstack2);
                    boolean flag = TileEntityBanner.getPatterns(itemstack2) > 0;

                    if (!itemstack.isEmpty()) {
                        if (flag) {
                            return false;
                        }

                        if (enumcolor != ItemBanner.getBaseColor(itemstack)) {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    } else if (!itemstack1.isEmpty()) {
                        if (!flag) {
                            return false;
                        }

                        if (enumcolor != ItemBanner.getBaseColor(itemstack1)) {
                            return false;
                        }

                        itemstack = itemstack2;
                    } else if (flag) {
                        itemstack = itemstack2;
                    } else {
                        itemstack1 = itemstack2;
                    }
                }
            }

            return !itemstack.isEmpty() && !itemstack1.isEmpty();
        }

        public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
            for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
                ItemStack itemstack = inventorycrafting.getStackInSlot(i);

                if (!itemstack.isEmpty() && TileEntityBanner.getPatterns(itemstack) > 0) {
                    ItemStack itemstack1 = itemstack.copy();

                    itemstack1.setCount(1);
                    return itemstack1;
                }
            }

            return ItemStack.EMPTY;
        }

        public ItemStack getRecipeOutput() {
            return ItemStack.EMPTY;
        }

        public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventorycrafting) {
            NonNullList nonnulllist = NonNullList.withSize(inventorycrafting.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inventorycrafting.getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    if (itemstack.getItem().hasContainerItem()) {
                        nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                    } else if (itemstack.hasTagCompound() && TileEntityBanner.getPatterns(itemstack) > 0) {
                        ItemStack itemstack1 = itemstack.copy();

                        itemstack1.setCount(1);
                        nonnulllist.set(i, itemstack1);
                    }
                }
            }

            return nonnulllist;
        }

        public boolean isDynamic() {
            return true;
        }
    }
}
