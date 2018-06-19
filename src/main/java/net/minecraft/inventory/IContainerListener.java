package net.minecraft.inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;


public interface IContainerListener {

    void func_71110_a(Container container, NonNullList<ItemStack> nonnulllist);

    void func_71111_a(Container container, int i, ItemStack itemstack);

    void func_71112_a(Container container, int i, int j);

    void func_175173_a(Container container, IInventory iinventory);
}
