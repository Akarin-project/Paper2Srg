package net.minecraft.util;

public interface IProgressUpdate {

    void displaySavingString(String s);

    void displayLoadingString(String s);

    void setLoadingProgress(int i);
}
