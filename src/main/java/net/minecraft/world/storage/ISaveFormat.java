package net.minecraft.world.storage;

import java.io.File;

import net.minecraft.util.IProgressUpdate;

public interface ISaveFormat {

    ISaveHandler getSaveLoader(String s, boolean flag);

    boolean isOldMapFormat(String s);

    boolean convertMapFormat(String s, IProgressUpdate iprogressupdate);

    File getFile(String s, String s1);
}
