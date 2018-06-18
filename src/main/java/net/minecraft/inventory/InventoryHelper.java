package net.minecraft.inventory;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper {

    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World world, BlockPos blockposition, IInventory iinventory) {
        dropInventoryItems(world, (double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), iinventory);
    }

    public static void dropInventoryItems(World world, Entity entity, IInventory iinventory) {
        dropInventoryItems(world, entity.posX, entity.posY, entity.posZ, iinventory);
    }

    private static void dropInventoryItems(World world, double d0, double d1, double d2, IInventory iinventory) {
        for (int i = 0; i < iinventory.getSizeInventory(); ++i) {
            ItemStack itemstack = iinventory.getStackInSlot(i);

            if (!itemstack.isEmpty()) {
                spawnItemStack(world, d0, d1, d2, itemstack);
            }
        }

    }

    public static void spawnItemStack(World world, double d0, double d1, double d2, ItemStack itemstack) {
        float f = InventoryHelper.RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = InventoryHelper.RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = InventoryHelper.RANDOM.nextFloat() * 0.8F + 0.1F;

        while (!itemstack.isEmpty()) {
            EntityItem entityitem = new EntityItem(world, d0 + (double) f, d1 + (double) f1, d2 + (double) f2, itemstack.splitStack(InventoryHelper.RANDOM.nextInt(21) + 10));
            float f3 = 0.05F;

            entityitem.motionX = InventoryHelper.RANDOM.nextGaussian() * 0.05000000074505806D;
            entityitem.motionY = InventoryHelper.RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
            entityitem.motionZ = InventoryHelper.RANDOM.nextGaussian() * 0.05000000074505806D;
            world.spawnEntity(entityitem);
        }

    }
}
