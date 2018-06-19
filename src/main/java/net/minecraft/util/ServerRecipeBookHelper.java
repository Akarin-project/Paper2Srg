package net.minecraft.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;

public class ServerRecipeBookHelper {

    private final Logger field_194330_a = LogManager.getLogger();
    private final RecipeItemHelper field_194331_b = new RecipeItemHelper();
    private EntityPlayerMP field_194332_c;
    private IRecipe field_194333_d;
    private boolean field_194334_e;
    private InventoryCraftResult field_194335_f;
    private InventoryCrafting field_194336_g;
    private List<Slot> field_194337_h;

    public ServerRecipeBookHelper() {}

    public void func_194327_a(EntityPlayerMP entityplayer, @Nullable IRecipe irecipe, boolean flag) {
        if (irecipe != null && entityplayer.func_192037_E().func_193830_f(irecipe)) {
            this.field_194332_c = entityplayer;
            this.field_194333_d = irecipe;
            this.field_194334_e = flag;
            this.field_194337_h = entityplayer.field_71070_bA.field_75151_b;
            Container container = entityplayer.field_71070_bA;

            this.field_194335_f = null;
            this.field_194336_g = null;
            if (container instanceof ContainerWorkbench) {
                this.field_194335_f = ((ContainerWorkbench) container).field_75160_f;
                this.field_194336_g = ((ContainerWorkbench) container).field_75162_e;
            } else if (container instanceof ContainerPlayer) {
                this.field_194335_f = ((ContainerPlayer) container).field_75179_f;
                this.field_194336_g = ((ContainerPlayer) container).field_75181_e;
            }

            if (this.field_194335_f != null && this.field_194336_g != null) {
                if (this.func_194328_c() || entityplayer.func_184812_l_()) {
                    this.field_194331_b.func_194119_a();
                    entityplayer.field_71071_by.func_194016_a(this.field_194331_b, false);
                    this.field_194336_g.func_194018_a(this.field_194331_b);
                    if (this.field_194331_b.func_194116_a(irecipe, (IntList) null)) {
                        this.func_194329_b();
                    } else {
                        this.func_194326_a();
                        entityplayer.field_71135_a.func_147359_a(new SPacketPlaceGhostRecipe(entityplayer.field_71070_bA.field_75152_c, irecipe));
                    }

                    entityplayer.field_71071_by.func_70296_d();
                }
            }
        }
    }

    private void func_194326_a() {
        InventoryPlayer playerinventory = this.field_194332_c.field_71071_by;

        for (int i = 0; i < this.field_194336_g.func_70302_i_(); ++i) {
            ItemStack itemstack = this.field_194336_g.func_70301_a(i);

            if (!itemstack.func_190926_b()) {
                while (itemstack.func_190916_E() > 0) {
                    int j = playerinventory.func_70432_d(itemstack);

                    if (j == -1) {
                        j = playerinventory.func_70447_i();
                    }

                    ItemStack itemstack1 = itemstack.func_77946_l();

                    itemstack1.func_190920_e(1);
                    playerinventory.func_191971_c(j, itemstack1);
                    this.field_194336_g.func_70298_a(i, 1);
                }
            }
        }

        this.field_194336_g.func_174888_l();
        this.field_194335_f.func_174888_l();
    }

    private void func_194329_b() {
        boolean flag = this.field_194333_d.func_77569_a(this.field_194336_g, this.field_194332_c.field_70170_p);
        int i = this.field_194331_b.func_194114_b(this.field_194333_d, (IntList) null);

        if (flag) {
            boolean flag1 = true;

            for (int j = 0; j < this.field_194336_g.func_70302_i_(); ++j) {
                ItemStack itemstack = this.field_194336_g.func_70301_a(j);

                if (!itemstack.func_190926_b() && Math.min(i, itemstack.func_77976_d()) > itemstack.func_190916_E()) {
                    flag1 = false;
                }
            }

            if (flag1) {
                return;
            }
        }

        int k = this.func_194324_a(i, flag);
        IntArrayList intarraylist = new IntArrayList();

        if (this.field_194331_b.func_194118_a(this.field_194333_d, intarraylist, k)) {
            int l = k;
            IntListIterator intlistiterator = intarraylist.iterator();

            while (intlistiterator.hasNext()) {
                int i1 = ((Integer) intlistiterator.next()).intValue();
                int j1 = RecipeItemHelper.func_194115_b(i1).func_77976_d();

                if (j1 < l) {
                    l = j1;
                }
            }

            if (this.field_194331_b.func_194118_a(this.field_194333_d, intarraylist, l)) {
                this.func_194326_a();
                this.func_194323_a(l, intarraylist);
            }
        }
    }

    private int func_194324_a(int i, boolean flag) {
        int j = 1;

        if (this.field_194334_e) {
            j = i;
        } else if (flag) {
            j = 64;

            for (int k = 0; k < this.field_194336_g.func_70302_i_(); ++k) {
                ItemStack itemstack = this.field_194336_g.func_70301_a(k);

                if (!itemstack.func_190926_b() && j > itemstack.func_190916_E()) {
                    j = itemstack.func_190916_E();
                }
            }

            if (j < 64) {
                ++j;
            }
        }

        return j;
    }

    private void func_194323_a(int i, IntList intlist) {
        int j = this.field_194336_g.func_174922_i();
        int k = this.field_194336_g.func_174923_h();

        if (this.field_194333_d instanceof ShapedRecipes) {
            ShapedRecipes shapedrecipes = (ShapedRecipes) this.field_194333_d;

            j = shapedrecipes.func_192403_f();
            k = shapedrecipes.func_192404_g();
        }

        int l = 1;
        IntListIterator intlistiterator = intlist.iterator();

        for (int i1 = 0; i1 < this.field_194336_g.func_174922_i() && k != i1; ++i1) {
            for (int j1 = 0; j1 < this.field_194336_g.func_174923_h(); ++j1) {
                if (j == j1 || !intlistiterator.hasNext()) {
                    l += this.field_194336_g.func_174922_i() - j1;
                    break;
                }

                Slot slot = (Slot) this.field_194337_h.get(l);
                ItemStack itemstack = RecipeItemHelper.func_194115_b(((Integer) intlistiterator.next()).intValue());

                if (itemstack.func_190926_b()) {
                    ++l;
                } else {
                    for (int k1 = 0; k1 < i; ++k1) {
                        this.func_194325_a(slot, itemstack);
                    }

                    ++l;
                }
            }

            if (!intlistiterator.hasNext()) {
                break;
            }
        }

    }

    private void func_194325_a(Slot slot, ItemStack itemstack) {
        InventoryPlayer playerinventory = this.field_194332_c.field_71071_by;
        int i = playerinventory.func_194014_c(itemstack);

        if (i != -1) {
            ItemStack itemstack1 = playerinventory.func_70301_a(i).func_77946_l();

            if (!itemstack1.func_190926_b()) {
                if (itemstack1.func_190916_E() > 1) {
                    playerinventory.func_70298_a(i, 1);
                } else {
                    playerinventory.func_70304_b(i);
                }

                itemstack1.func_190920_e(1);
                if (slot.func_75211_c().func_190926_b()) {
                    slot.func_75215_d(itemstack1);
                } else {
                    slot.func_75211_c().func_190917_f(1);
                }

            }
        }
    }

    private boolean func_194328_c() {
        InventoryPlayer playerinventory = this.field_194332_c.field_71071_by;

        for (int i = 0; i < this.field_194336_g.func_70302_i_(); ++i) {
            ItemStack itemstack = this.field_194336_g.func_70301_a(i);

            if (!itemstack.func_190926_b()) {
                int j = playerinventory.func_70432_d(itemstack);

                if (j == -1) {
                    j = playerinventory.func_70447_i();
                }

                if (j == -1) {
                    return false;
                }
            }
        }

        return true;
    }
}
