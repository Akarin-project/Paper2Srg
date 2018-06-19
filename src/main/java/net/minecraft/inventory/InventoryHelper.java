package net.minecraft.inventory;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper {

    private static final Random field_180177_a = new Random();

    public static void func_180175_a(World world, BlockPos blockposition, IInventory iinventory) {
        func_180174_a(world, (double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), iinventory);
    }

    public static void func_180176_a(World world, Entity entity, IInventory iinventory) {
        func_180174_a(world, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, iinventory);
    }

    private static void func_180174_a(World world, double d0, double d1, double d2, IInventory iinventory) {
        for (int i = 0; i < iinventory.func_70302_i_(); ++i) {
            ItemStack itemstack = iinventory.func_70301_a(i);

            if (!itemstack.func_190926_b()) {
                func_180173_a(world, d0, d1, d2, itemstack);
            }
        }

    }

    public static void func_180173_a(World world, double d0, double d1, double d2, ItemStack itemstack) {
        float f = InventoryHelper.field_180177_a.nextFloat() * 0.8F + 0.1F;
        float f1 = InventoryHelper.field_180177_a.nextFloat() * 0.8F + 0.1F;
        float f2 = InventoryHelper.field_180177_a.nextFloat() * 0.8F + 0.1F;

        while (!itemstack.func_190926_b()) {
            EntityItem entityitem = new EntityItem(world, d0 + (double) f, d1 + (double) f1, d2 + (double) f2, itemstack.func_77979_a(InventoryHelper.field_180177_a.nextInt(21) + 10));
            float f3 = 0.05F;

            entityitem.field_70159_w = InventoryHelper.field_180177_a.nextGaussian() * 0.05000000074505806D;
            entityitem.field_70181_x = InventoryHelper.field_180177_a.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
            entityitem.field_70179_y = InventoryHelper.field_180177_a.nextGaussian() * 0.05000000074505806D;
            world.func_72838_d(entityitem);
        }

    }
}
