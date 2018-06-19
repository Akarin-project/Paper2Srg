package net.minecraft.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;

public interface IInventory extends IWorldNameable {

    int func_70302_i_();

    boolean func_191420_l();

    ItemStack func_70301_a(int i);

    ItemStack func_70298_a(int i, int j);

    ItemStack func_70304_b(int i);

    void func_70299_a(int i, ItemStack itemstack);

    int func_70297_j_();

    void func_70296_d();

    boolean func_70300_a(EntityPlayer entityhuman);

    void func_174889_b(EntityPlayer entityhuman);

    void func_174886_c(EntityPlayer entityhuman);

    boolean func_94041_b(int i, ItemStack itemstack);

    int func_174887_a_(int i);

    void func_174885_b(int i, int j);

    int func_174890_g();

    void func_174888_l();

    // CraftBukkit start
    java.util.List<ItemStack> getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    org.bukkit.Location getLocation();

    int MAX_STACK = 64;
    // CraftBukkit end
}
