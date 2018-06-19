package net.minecraft.world.storage;

import java.io.File;

import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {

    ISaveHandler func_75804_a(String s, boolean flag);

    boolean func_75801_b(String s);

    boolean func_75805_a(String s, IProgressUpdate iprogressupdate);

    File func_186352_b(String s, String s1);
}
