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
            super("", new ItemStack(Items.field_185159_cQ, 0, 0), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_179564_cE)));
        }
        // CraftBukkit end

        public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack1 = ItemStack.field_190927_a;

            for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
                ItemStack itemstack2 = inventorycrafting.func_70301_a(i);

                if (!itemstack2.func_190926_b()) {
                    if (itemstack2.func_77973_b() == Items.field_179564_cE) {
                        if (!itemstack1.func_190926_b()) {
                            return false;
                        }

                        itemstack1 = itemstack2;
                    } else {
                        if (itemstack2.func_77973_b() != Items.field_185159_cQ) {
                            return false;
                        }

                        if (!itemstack.func_190926_b()) {
                            return false;
                        }

                        if (itemstack2.func_179543_a("BlockEntityTag") != null) {
                            return false;
                        }

                        itemstack = itemstack2;
                    }
                }
            }

            if (!itemstack.func_190926_b() && !itemstack1.func_190926_b()) {
                return true;
            } else {
                return false;
            }
        }

        public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack1 = ItemStack.field_190927_a;

            for (int i = 0; i < inventorycrafting.func_70302_i_(); ++i) {
                ItemStack itemstack2 = inventorycrafting.func_70301_a(i);

                if (!itemstack2.func_190926_b()) {
                    if (itemstack2.func_77973_b() == Items.field_179564_cE) {
                        itemstack = itemstack2;
                    } else if (itemstack2.func_77973_b() == Items.field_185159_cQ) {
                        itemstack1 = itemstack2.func_77946_l();
                    }
                }
            }

            if (itemstack1.func_190926_b()) {
                return itemstack1;
            } else {
                NBTTagCompound nbttagcompound = itemstack.func_179543_a("BlockEntityTag");
                NBTTagCompound nbttagcompound1 = nbttagcompound == null ? new NBTTagCompound() : nbttagcompound.func_74737_b();

                nbttagcompound1.func_74768_a("Base", itemstack.func_77960_j() & 15);
                itemstack1.func_77983_a("BlockEntityTag", (NBTBase) nbttagcompound1);
                return itemstack1;
            }
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

        public boolean func_192399_d() {
            return true;
        }
    }
}
