package net.minecraft.profiler;

public interface ISnooperInfo {

    void addServerStatsToSnooper(Snooper mojangstatisticsgenerator);

    void addServerTypeToSnooper(Snooper mojangstatisticsgenerator);

    boolean isSnooperEnabled();
}
