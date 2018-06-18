package net.minecraft.item;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item {

    protected ItemMapBase() {}

    public boolean isMap() {
        return true;
    }

    @Nullable
    public Packet<?> createMapDataPacket(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        return null;
    }
}
