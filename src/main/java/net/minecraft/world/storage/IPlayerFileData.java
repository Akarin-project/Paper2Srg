package net.minecraft.world.storage;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerFileData {

    void func_75753_a(EntityPlayer entityhuman);

    @Nullable
    NBTTagCompound func_75752_b(EntityPlayer entityhuman);

    String[] func_75754_f();
}
