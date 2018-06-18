package net.minecraft.world.storage;

public class WorldSavedDataCallableSave implements Runnable {

    private final WorldSavedData data;

    public WorldSavedDataCallableSave(WorldSavedData persistentbase) {
        this.data = persistentbase;
    }

    public void run() {
        this.data.markDirty();
    }
}
