package net.minecraft.tileentity;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;


public interface IHopper extends IInventory {

    World func_145831_w();

    double func_96107_aA(); default double getX() { return func_96107_aA(); } // Paper - OBFHELPER

    double func_96109_aB(); default double getY() { return func_96109_aB(); } // Paper - OBFHELPER

    double func_96108_aC(); default double getZ() { return func_96108_aC(); } // Paper - OBFHELPER
}
