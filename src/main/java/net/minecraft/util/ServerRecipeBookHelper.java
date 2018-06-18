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
        if (irecipe != null && entityplayer.getRecipeBook().isUnlocked(irecipe)) {
            this.field_194332_c = entityplayer;
            this.field_194333_d = irecipe;
            this.field_194334_e = flag;
            this.field_194337_h = entityplayer.openContainer.inventorySlots;
            Container container = entityplayer.openContainer;

            this.field_194335_f = null;
            this.field_194336_g = null;
            if (container instanceof ContainerWorkbench) {
                this.field_194335_f = ((ContainerWorkbench) container).craftResult;
                this.field_194336_g = ((ContainerWorkbench) container).craftMatrix;
            } else if (container instanceof ContainerPlayer) {
                this.field_194335_f = ((ContainerPlayer) container).craftResult;
                this.field_194336_g = ((ContainerPlayer) container).craftMatrix;
            }

            if (this.field_194335_f != null && this.field_194336_g != null) {
                if (this.func_194328_c() || entityplayer.isCreative()) {
                    this.field_194331_b.clear();
                    entityplayer.inventory.fillStackedContents(this.field_194331_b, false);
                    this.field_194336_g.fillStackedContents(this.field_194331_b);
                    if (this.field_194331_b.canCraft(irecipe, (IntList) null)) {
                        this.func_194329_b();
                    } else {
                        this.func_194326_a();
                        entityplayer.connection.sendPacket(new SPacketPlaceGhostRecipe(entityplayer.openContainer.windowId, irecipe));
                    }

                    entityplayer.inventory.markDirty();
                }
            }
        }
    }

    private void func_194326_a() {
        InventoryPlayer playerinventory = this.field_194332_c.inventory;

        for (int i = 0; i < this.field_194336_g.getSizeInventory(); ++i) {
            ItemStack itemstack = this.field_194336_g.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                while (itemstack.getCount() > 0) {
                    int j = playerinventory.storeItemStack(itemstack);

                    if (j == -1) {
                        j = playerinventory.getFirstEmptyStack();
                    }

                    ItemStack itemstack1 = itemstack.copy();

                    itemstack1.setCount(1);
                    playerinventory.add(j, itemstack1);
                    this.field_194336_g.decrStackSize(i, 1);
                }
            }
        }

        this.field_194336_g.clear();
        this.field_194335_f.clear();
    }

    private void func_194329_b() {
        boolean flag = this.field_194333_d.matches(this.field_194336_g, this.field_194332_c.world);
        int i = this.field_194331_b.getBiggestCraftableStack(this.field_194333_d, (IntList) null);

        if (flag) {
            boolean flag1 = true;

            for (int j = 0; j < this.field_194336_g.getSizeInventory(); ++j) {
                ItemStack itemstack = this.field_194336_g.getStackInSlot(j);

                if (!itemstack.isEmpty() && Math.min(i, itemstack.getMaxStackSize()) > itemstack.getCount()) {
                    flag1 = false;
                }
            }

            if (flag1) {
                return;
            }
        }

        int k = this.func_194324_a(i, flag);
        IntArrayList intarraylist = new IntArrayList();

        if (this.field_194331_b.canCraft(this.field_194333_d, intarraylist, k)) {
            int l = k;
            IntListIterator intlistiterator = intarraylist.iterator();

            while (intlistiterator.hasNext()) {
                int i1 = ((Integer) intlistiterator.next()).intValue();
                int j1 = RecipeItemHelper.unpack(i1).getMaxStackSize();

                if (j1 < l) {
                    l = j1;
                }
            }

            if (this.field_194331_b.canCraft(this.field_194333_d, intarraylist, l)) {
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

            for (int k = 0; k < this.field_194336_g.getSizeInventory(); ++k) {
                ItemStack itemstack = this.field_194336_g.getStackInSlot(k);

                if (!itemstack.isEmpty() && j > itemstack.getCount()) {
                    j = itemstack.getCount();
                }
            }

            if (j < 64) {
                ++j;
            }
        }

        return j;
    }

    private void func_194323_a(int i, IntList intlist) {
        int j = this.field_194336_g.getWidth();
        int k = this.field_194336_g.getHeight();

        if (this.field_194333_d instanceof ShapedRecipes) {
            ShapedRecipes shapedrecipes = (ShapedRecipes) this.field_194333_d;

            j = shapedrecipes.getWidth();
            k = shapedrecipes.getHeight();
        }

        int l = 1;
        IntListIterator intlistiterator = intlist.iterator();

        for (int i1 = 0; i1 < this.field_194336_g.getWidth() && k != i1; ++i1) {
            for (int j1 = 0; j1 < this.field_194336_g.getHeight(); ++j1) {
                if (j == j1 || !intlistiterator.hasNext()) {
                    l += this.field_194336_g.getWidth() - j1;
                    break;
                }

                Slot slot = (Slot) this.field_194337_h.get(l);
                ItemStack itemstack = RecipeItemHelper.unpack(((Integer) intlistiterator.next()).intValue());

                if (itemstack.isEmpty()) {
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
        InventoryPlayer playerinventory = this.field_194332_c.inventory;
        int i = playerinventory.findSlotMatchingUnusedItem(itemstack);

        if (i != -1) {
            ItemStack itemstack1 = playerinventory.getStackInSlot(i).copy();

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getCount() > 1) {
                    playerinventory.decrStackSize(i, 1);
                } else {
                    playerinventory.removeStackFromSlot(i);
                }

                itemstack1.setCount(1);
                if (slot.getStack().isEmpty()) {
                    slot.putStack(itemstack1);
                } else {
                    slot.getStack().grow(1);
                }

            }
        }
    }

    private boolean func_194328_c() {
        InventoryPlayer playerinventory = this.field_194332_c.inventory;

        for (int i = 0; i < this.field_194336_g.getSizeInventory(); ++i) {
            ItemStack itemstack = this.field_194336_g.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                int j = playerinventory.storeItemStack(itemstack);

                if (j == -1) {
                    j = playerinventory.getFirstEmptyStack();
                }

                if (j == -1) {
                    return false;
                }
            }
        }

        return true;
    }
}
