package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeFireworks extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    private ItemStack field_92102_a;

    // CraftBukkit start - Delegate to new parent class with bogus info
    public RecipeFireworks() {
        super("", new ItemStack(Items.field_151152_bP, 0, 0), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_151016_H)));
        this.field_92102_a = ItemStack.field_190927_a;
    }
    // CraftBukkit end

    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        this.field_92102_a = ItemStack.field_190927_a;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = 0; k1 < inventorycrafting.func_70302_i_(); ++k1) {
            ItemStack itemstack = inventorycrafting.func_70301_a(k1);

            if (!itemstack.func_190926_b()) {
                if (itemstack.func_77973_b() == Items.field_151016_H) {
                    ++j;
                } else if (itemstack.func_77973_b() == Items.field_151154_bQ) {
                    ++l;
                } else if (itemstack.func_77973_b() == Items.field_151100_aR) {
                    ++k;
                } else if (itemstack.func_77973_b() == Items.field_151121_aF) {
                    ++i;
                } else if (itemstack.func_77973_b() == Items.field_151114_aO) {
                    ++i1;
                } else if (itemstack.func_77973_b() == Items.field_151045_i) {
                    ++i1;
                } else if (itemstack.func_77973_b() == Items.field_151059_bz) {
                    ++j1;
                } else if (itemstack.func_77973_b() == Items.field_151008_G) {
                    ++j1;
                } else if (itemstack.func_77973_b() == Items.field_151074_bl) {
                    ++j1;
                } else {
                    if (itemstack.func_77973_b() != Items.field_151144_bL) {
                        return false;
                    }

                    ++j1;
                }
            }
        }

        i1 += k + j1;
        if (j <= 3 && i <= 1) {
            NBTTagCompound nbttagcompound;
            NBTTagCompound nbttagcompound1;
            int l1;

            if (j >= 1 && i == 1 && i1 == 0) {
                this.field_92102_a = new ItemStack(Items.field_151152_bP, 3);
                nbttagcompound = new NBTTagCompound();
                if (l > 0) {
                    NBTTagList nbttaglist = new NBTTagList();

                    for (l1 = 0; l1 < inventorycrafting.func_70302_i_(); ++l1) {
                        ItemStack itemstack1 = inventorycrafting.func_70301_a(l1);

                        if (itemstack1.func_77973_b() == Items.field_151154_bQ && itemstack1.func_77942_o() && itemstack1.func_77978_p().func_150297_b("Explosion", 10)) {
                            nbttaglist.func_74742_a(itemstack1.func_77978_p().func_74775_l("Explosion"));
                        }
                    }

                    nbttagcompound.func_74782_a("Explosions", nbttaglist);
                }

                nbttagcompound.func_74774_a("Flight", (byte) j);
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.func_74782_a("Fireworks", nbttagcompound);
                this.field_92102_a.func_77982_d(nbttagcompound1);
                return true;
            } else if (j == 1 && i == 0 && l == 0 && k > 0 && j1 <= 1) {
                this.field_92102_a = new ItemStack(Items.field_151154_bQ);
                nbttagcompound = new NBTTagCompound();
                nbttagcompound1 = new NBTTagCompound();
                byte b0 = 0;
                ArrayList arraylist = Lists.newArrayList();

                for (int i2 = 0; i2 < inventorycrafting.func_70302_i_(); ++i2) {
                    ItemStack itemstack2 = inventorycrafting.func_70301_a(i2);

                    if (!itemstack2.func_190926_b()) {
                        if (itemstack2.func_77973_b() == Items.field_151100_aR) {
                            arraylist.add(Integer.valueOf(ItemDye.field_150922_c[itemstack2.func_77960_j() & 15]));
                        } else if (itemstack2.func_77973_b() == Items.field_151114_aO) {
                            nbttagcompound1.func_74757_a("Flicker", true);
                        } else if (itemstack2.func_77973_b() == Items.field_151045_i) {
                            nbttagcompound1.func_74757_a("Trail", true);
                        } else if (itemstack2.func_77973_b() == Items.field_151059_bz) {
                            b0 = 1;
                        } else if (itemstack2.func_77973_b() == Items.field_151008_G) {
                            b0 = 4;
                        } else if (itemstack2.func_77973_b() == Items.field_151074_bl) {
                            b0 = 2;
                        } else if (itemstack2.func_77973_b() == Items.field_151144_bL) {
                            b0 = 3;
                        }
                    }
                }

                int[] aint = new int[arraylist.size()];

                for (int j2 = 0; j2 < aint.length; ++j2) {
                    aint[j2] = ((Integer) arraylist.get(j2)).intValue();
                }

                nbttagcompound1.func_74783_a("Colors", aint);
                nbttagcompound1.func_74774_a("Type", b0);
                nbttagcompound.func_74782_a("Explosion", nbttagcompound1);
                this.field_92102_a.func_77982_d(nbttagcompound);
                return true;
            } else if (j == 0 && i == 0 && l == 1 && k > 0 && k == i1) {
                ArrayList arraylist1 = Lists.newArrayList();

                for (int k2 = 0; k2 < inventorycrafting.func_70302_i_(); ++k2) {
                    ItemStack itemstack3 = inventorycrafting.func_70301_a(k2);

                    if (!itemstack3.func_190926_b()) {
                        if (itemstack3.func_77973_b() == Items.field_151100_aR) {
                            arraylist1.add(Integer.valueOf(ItemDye.field_150922_c[itemstack3.func_77960_j() & 15]));
                        } else if (itemstack3.func_77973_b() == Items.field_151154_bQ) {
                            this.field_92102_a = itemstack3.func_77946_l();
                            this.field_92102_a.func_190920_e(1);
                        }
                    }
                }

                int[] aint1 = new int[arraylist1.size()];

                for (l1 = 0; l1 < aint1.length; ++l1) {
                    aint1[l1] = ((Integer) arraylist1.get(l1)).intValue();
                }

                if (!this.field_92102_a.func_190926_b() && this.field_92102_a.func_77942_o()) {
                    NBTTagCompound nbttagcompound2 = this.field_92102_a.func_77978_p().func_74775_l("Explosion");

                    if (nbttagcompound2 == null) {
                        return false;
                    } else {
                        nbttagcompound2.func_74783_a("FadeColors", aint1);
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        return this.field_92102_a.func_77946_l();
    }

    public ItemStack func_77571_b() {
        return this.field_92102_a;
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

    public boolean func_192399_d() {
        return true;
    }
}
