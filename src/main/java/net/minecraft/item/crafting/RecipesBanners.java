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
            super("", new ItemStack(Items.field_179564_cE, 0, 0), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_179564_cE)));
        }
        // CraftBukkit end

        public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
            boolean flag = false;

            for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
                ItemStack itemstack = inventorycrafting.func_70301_a(i);

                if (itemstack.func_77973_b() == Items.field_179564_cE) {
                    if (flag) {
                        return false;
                    }

                    if (TileEntityBanner.func_175113_c(itemstack) >= 6) {
                        return false;
                    }

                    flag = true;
                }
            }

            if (!flag) {
                return false;
            } else {
                return this.func_190933_c(inventorycrafting) != null;
            }
        }

        public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = ItemStack.field_190927_a;

            for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
                ItemStack itemstack1 = inventorycrafting.func_70301_a(i);

                if (!itemstack1.func_190926_b() && itemstack1.func_77973_b() == Items.field_179564_cE) {
                    itemstack = itemstack1.func_77946_l();
                    itemstack.func_190920_e(1);
                    break;
                }
            }

            BannerPattern enumbannerpatterntype = this.func_190933_c(inventorycrafting);

            if (enumbannerpatterntype != null) {
                int j = 0;

                for (int k = 0; k < inventorycrafting.func_70302_i_(); ++k) {
                    ItemStack itemstack2 = inventorycrafting.func_70301_a(k);

                    if (itemstack2.func_77973_b() == Items.field_151100_aR) {
                        j = itemstack2.func_77960_j();
                        break;
                    }
                }

                NBTTagCompound nbttagcompound = itemstack.func_190925_c("BlockEntityTag");
                NBTTagList nbttaglist;

                if (nbttagcompound.func_150297_b("Patterns", 9)) {
                    nbttaglist = nbttagcompound.func_150295_c("Patterns", 10);
                } else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound.func_74782_a("Patterns", nbttaglist);
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.func_74778_a("Pattern", enumbannerpatterntype.func_190993_b());
                nbttagcompound1.func_74768_a("Color", j);
                nbttaglist.func_74742_a(nbttagcompound1);
            }

            return itemstack;
        }

        public ItemStack func_77571_b() {
            return ItemStack.field_190927_a;
        }

        public NonNullList<ItemStack> func_179532_b(InventoryCrafting inventorycrafting) {
            NonNullList nonnulllist = NonNullList.func_191197_a(inventorycrafting.func_70302_i_(), ItemStack.field_190927_a);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inventorycrafting.func_70301_a(i);

                if (itemstack.func_77973_b().func_77634_r()) {
                    nonnulllist.set(i, new ItemStack(itemstack.func_77973_b().func_77668_q()));
                }
            }

            return nonnulllist;
        }

        @Nullable
        private BannerPattern func_190933_c(InventoryCrafting inventorycrafting) {
            BannerPattern[] aenumbannerpatterntype = BannerPattern.values();
            int i = aenumbannerpatterntype.length;

            for (int j = 0; j < i; ++j) {
                BannerPattern enumbannerpatterntype = aenumbannerpatterntype[j];

                if (enumbannerpatterntype.func_191000_d()) {
                    boolean flag = true;
                    int k;

                    if (enumbannerpatterntype.func_190999_e()) {
                        boolean flag1 = false;
                        boolean flag2 = false;

                        for (k = 0; k < inventorycrafting.func_70302_i_() && flag; ++k) {
                            ItemStack itemstack = inventorycrafting.func_70301_a(k);

                            if (!itemstack.func_190926_b() && itemstack.func_77973_b() != Items.field_179564_cE) {
                                if (itemstack.func_77973_b() == Items.field_151100_aR) {
                                    if (flag2) {
                                        flag = false;
                                        break;
                                    }

                                    flag2 = true;
                                } else {
                                    if (flag1 || !itemstack.func_77969_a(enumbannerpatterntype.func_190998_f())) {
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
                    } else if (inventorycrafting.func_70302_i_() != enumbannerpatterntype.func_190996_c().length * enumbannerpatterntype.func_190996_c()[0].length()) {
                        flag = false;
                    } else {
                        int l = -1;

                        for (int i1 = 0; i1 < inventorycrafting.func_70302_i_() && flag; ++i1) {
                            k = i1 / 3;
                            int j1 = i1 % 3;
                            ItemStack itemstack1 = inventorycrafting.func_70301_a(i1);

                            if (!itemstack1.func_190926_b() && itemstack1.func_77973_b() != Items.field_179564_cE) {
                                if (itemstack1.func_77973_b() != Items.field_151100_aR) {
                                    flag = false;
                                    break;
                                }

                                if (l != -1 && l != itemstack1.func_77960_j()) {
                                    flag = false;
                                    break;
                                }

                                if (enumbannerpatterntype.func_190996_c()[k].charAt(j1) == 32) {
                                    flag = false;
                                    break;
                                }

                                l = itemstack1.func_77960_j();
                            } else if (enumbannerpatterntype.func_190996_c()[k].charAt(j1) != 32) {
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

        public boolean func_192399_d() {
            return true;
        }
    }

    public static class RecipeDuplicatePattern extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

        // CraftBukkit start - Delegate to new parent class with bogus info
        public RecipeDuplicatePattern() {
            super("", new ItemStack(Items.field_179564_cE, 0, 0), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_151100_aR)));
        }
        // CraftBukkit end

        public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack1 = ItemStack.field_190927_a;

            for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
                ItemStack itemstack2 = inventorycrafting.func_70301_a(i);

                if (!itemstack2.func_190926_b()) {
                    if (itemstack2.func_77973_b() != Items.field_179564_cE) {
                        return false;
                    }

                    if (!itemstack.func_190926_b() && !itemstack1.func_190926_b()) {
                        return false;
                    }

                    EnumDyeColor enumcolor = ItemBanner.func_179225_h(itemstack2);
                    boolean flag = TileEntityBanner.func_175113_c(itemstack2) > 0;

                    if (!itemstack.func_190926_b()) {
                        if (flag) {
                            return false;
                        }

                        if (enumcolor != ItemBanner.func_179225_h(itemstack)) {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    } else if (!itemstack1.func_190926_b()) {
                        if (!flag) {
                            return false;
                        }

                        if (enumcolor != ItemBanner.func_179225_h(itemstack1)) {
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

            return !itemstack.func_190926_b() && !itemstack1.func_190926_b();
        }

        public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
            for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
                ItemStack itemstack = inventorycrafting.func_70301_a(i);

                if (!itemstack.func_190926_b() && TileEntityBanner.func_175113_c(itemstack) > 0) {
                    ItemStack itemstack1 = itemstack.func_77946_l();

                    itemstack1.func_190920_e(1);
                    return itemstack1;
                }
            }

            return ItemStack.field_190927_a;
        }

        public ItemStack func_77571_b() {
            return ItemStack.field_190927_a;
        }

        public NonNullList<ItemStack> func_179532_b(InventoryCrafting inventorycrafting) {
            NonNullList nonnulllist = NonNullList.func_191197_a(inventorycrafting.func_70302_i_(), ItemStack.field_190927_a);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inventorycrafting.func_70301_a(i);

                if (!itemstack.func_190926_b()) {
                    if (itemstack.func_77973_b().func_77634_r()) {
                        nonnulllist.set(i, new ItemStack(itemstack.func_77973_b().func_77668_q()));
                    } else if (itemstack.func_77942_o() && TileEntityBanner.func_175113_c(itemstack) > 0) {
                        ItemStack itemstack1 = itemstack.func_77946_l();

                        itemstack1.func_190920_e(1);
                        nonnulllist.set(i, itemstack1);
                    }
                }
            }

            return nonnulllist;
        }

        public boolean func_192399_d() {
            return true;
        }
    }
}
