package net.minecraft.item.crafting;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;


public class RecipeBookCloning extends ShapelessRecipes implements IRecipe { // CraftBukkit - added extends

    public RecipeBookCloning() {
        super("", new ItemStack(Items.field_151164_bB, 0, -1), NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193367_a(Items.field_151099_bA)));
    }
    // CraftBukkit end

    public boolean func_77569_a(InventoryCrafting inventorycrafting, World world) {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;

        for (int j = 0; j < inventorycrafting.func_70302_i_(); ++j) {
            ItemStack itemstack1 = inventorycrafting.func_70301_a(j);

            if (!itemstack1.func_190926_b()) {
                if (itemstack1.func_77973_b() == Items.field_151164_bB) {
                    if (!itemstack.func_190926_b()) {
                        return false;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.func_77973_b() != Items.field_151099_bA) {
                        return false;
                    }

                    ++i;
                }
            }
        }

        return !itemstack.func_190926_b() && itemstack.func_77942_o() && i > 0;
    }

    public ItemStack func_77572_b(InventoryCrafting inventorycrafting) {
        int i = 0;
        ItemStack itemstack = ItemStack.field_190927_a;

        for (int j = 0; j < inventorycrafting.func_70302_i_(); ++j) {
            ItemStack itemstack1 = inventorycrafting.func_70301_a(j);

            if (!itemstack1.func_190926_b()) {
                if (itemstack1.func_77973_b() == Items.field_151164_bB) {
                    if (!itemstack.func_190926_b()) {
                        return ItemStack.field_190927_a;
                    }

                    itemstack = itemstack1;
                } else {
                    if (itemstack1.func_77973_b() != Items.field_151099_bA) {
                        return ItemStack.field_190927_a;
                    }

                    ++i;
                }
            }
        }

        if (!itemstack.func_190926_b() && itemstack.func_77942_o() && i >= 1 && ItemWrittenBook.func_179230_h(itemstack) < 2) {
            ItemStack itemstack2 = new ItemStack(Items.field_151164_bB, i);

            itemstack2.func_77982_d(itemstack.func_77978_p().func_74737_b());
            itemstack2.func_77978_p().func_74768_a("generation", ItemWrittenBook.func_179230_h(itemstack) + 1);
            if (itemstack.func_82837_s()) {
                itemstack2.func_151001_c(itemstack.func_82833_r());
            }

            return itemstack2;
        } else {
            return ItemStack.field_190927_a;
        }
    }

    public ItemStack func_77571_b() {
        return ItemStack.field_190927_a;
    }

    public NonNullList<ItemStack> func_179532_b(InventoryCrafting inventorycrafting) {
        NonNullList nonnulllist = NonNullList.func_191197_a(inventorycrafting.func_70302_i_(), ItemStack.field_190927_a);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inventorycrafting.func_70301_a(i);

            if (itemstack.func_77973_b() instanceof ItemWrittenBook) {
                ItemStack itemstack1 = itemstack.func_77946_l();

                itemstack1.func_190920_e(1);
                nonnulllist.set(i, itemstack1);
                break;
            }
        }

        return nonnulllist;
    }

    public boolean func_192399_d() {
        return true;
    }
}
