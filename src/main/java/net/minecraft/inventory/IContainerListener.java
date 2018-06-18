package net.minecraft.inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;


public interface IContainerListener {

    void sendAllContents(Container container, NonNullList<ItemStack> nonnulllist);

    void sendSlotContents(Container container, int i, ItemStack itemstack);

    void sendWindowProperty(Container container, int i, int j);

    void sendAllWindowProperties(Container container, IInventory iinventory);
}
