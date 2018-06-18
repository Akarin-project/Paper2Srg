package net.minecraft.tileentity;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;


public interface IHopper extends IInventory {

    World getWorld();

    double getXPos(); default double getX() { return getXPos(); } // Paper - OBFHELPER

    double getYPos(); default double getY() { return getYPos(); } // Paper - OBFHELPER

    double getZPos(); default double getZ() { return getZPos(); } // Paper - OBFHELPER
}
